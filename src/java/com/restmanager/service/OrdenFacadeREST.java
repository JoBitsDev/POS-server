/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.restmanager.service;

import com.restmanager.*;
import com.restmanager.XMLservice.ProductovOrdenXMLexport;
import com.restmanager.printservice.Impresion;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.print.PrintException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Jorge
 */
@Path("com.restmanager.orden")
public class OrdenFacadeREST extends AbstractFacade<Orden> {

    SimpleDateFormat FormatDate = new SimpleDateFormat("MM'/'dd'/'yy");
    SimpleDateFormat FormatTime = new SimpleDateFormat(" hh ':' mm ' ' a ");
    private Date today = new Date();

    public OrdenFacadeREST() {
        super(Orden.class);

    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Orden entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") String id, Orden entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") String id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML})
    public Orden find(@PathParam("id") String id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Orden> findAll() {
        return super.findAll();
    }

    /**
     *
     *
     * @param codMesa
     * @param usuarioTrabajador
     * @return
     */
    @GET
    @Path("CREATE_{codMesa}_{usuarioTrabajador}")
    @Produces(MediaType.TEXT_PLAIN)
    public String create(@PathParam("codMesa") String codMesa,
            @PathParam("usuarioTrabajador") String usuarioTrabajador) {
        Orden o = new Orden(ajustarNoOrden());
        Mesa m = getEntityManager().find(Mesa.class, codMesa);
        Personal p = getEntityManager().find(Personal.class, usuarioTrabajador);

        Venta v = getEntityManager().find(Venta.class, today);

        if (v == null) {
            v = new Venta(today);
        }

        m.setEstado("ocupada");
        o.setMesacodMesa(m);
        o.setPersonalusuario(p);
        o.setVentafecha(v);
        o.setDeLaCasa(false);
        o.setHoraComenzada(new Date());
        o.setPorciento(Float.valueOf("10"));
       
        
        super.create(o);
        
        
        return "1";
    }

    @GET
    @Path("FIND_{codMesa}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Orden get(@PathParam("codMesa") String codMesa) {
        Mesa m = getEntityManager().find(Mesa.class, codMesa);
        if (m.getEstado().equals("vacia")) {
            return null;
        }

        List<Orden> ord = super.findAll();
        Venta v = getEntityManager().find(Venta.class, today);
        for (int i = ord.size() - 1; i >= 0; i--) {
            if (ord.get(i).getMesacodMesa().equals(m) && ord.get(i).getHoraTerminada() == null && 
                   ord.get(i).getVentafecha().getFecha().compareTo(v.getFecha()) >= 0 ) {
                return ord.get(i);
            }
        }

        return null;
    }

    @GET
    @Path("FIND2_{codMesa}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getNoOrden(@PathParam("codMesa") String codMesa) {
        Mesa m = getEntityManager().find(Mesa.class, codMesa);
        if (m.getEstado().equals("vacia")) {
            return null;
        }

        List<Orden> ord = super.findAll();
        Venta v = getEntityManager().find(Venta.class, today);
        for (int i = ord.size() - 1; i >= 0; i--) {
            if (ord.get(i).getMesacodMesa().getCodMesa().equals(codMesa) &&
                    ord.get(i).getHoraTerminada() == null) {
                return ord.get(i).getCodOrden();
            }
        }

        return null;
    }
    
    @GET
    @Path("GETCAMARERO_{codOrden}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getCamarero (@PathParam("codOrden") String codOrden) {
        return super.find(codOrden).getPersonalusuario().getUsuario();
    }
    

    @GET
    @Path("LISTPRODUCTS_{codOrden}")
    @Produces({MediaType.TEXT_XML})
    public String getPDV(@PathParam("codOrden") String codOrden) {
        Orden o = super.find(codOrden);

        return ProductovOrdenXMLexport.exportToXML(o.getProductovOrdenList());
    }

    @GET
    @Path("ADD_{codOrden}_{codProductoVenta}")
    @Produces(MediaType.TEXT_PLAIN)
    public String addProducto(@PathParam("codOrden") String codOrden,
            @PathParam("codProductoVenta") String codProducto) {

        Orden o = super.find(codOrden);
        ProductoVenta producto = getEntityManager().find(ProductoVenta.class, codProducto);
        ArrayList<ProductovOrden> po = new ArrayList<>(o.getProductovOrdenList());
        ProductovOrden aux = new ProductovOrden(codProducto, codOrden);
        aux.setOrden(o);
        aux.setProductoVenta(producto);
        int contains = -1;
        if (!po.isEmpty()) {
            for (int i = 0; contains == -1 && i < po.size(); i++) {
                if (po.get(i).getOrden().getCodOrden().equals(codOrden)
                        && po.get(i).getProductoVenta().getPCod().equals(codProducto)) {
                    contains = i;
                }
            }
        }

        if (contains != -1) {
            ProductovOrden p = po.get(contains);
            int cant = p.getCantidad();
            p.setCantidad(++cant);

        } else {
            aux.setCantidad(1);
            aux.setEnviadosacocina(0);

            //em.persist(aux);
            po.add(aux);

        }
        o.setProductovOrdenList(po);

        super.edit(o);
        return "1";
    }//TODO: METODoS ARCAICOS

    @GET
    @Path("REMOVE_{codOrden}_{codProductoVenta}")
    @Produces(MediaType.TEXT_PLAIN)
    public String removeProducto(@PathParam("codOrden") String codOrden,
            @PathParam("codProductoVenta") String codProducto) {
        Orden o = super.find(codOrden);

        ArrayList<ProductovOrden> po = new ArrayList<>(o.getProductovOrdenList());
        int contains = -1;

        for (int i = 0; contains == -1 && i < po.size(); i++) {
            if (po.get(i).getOrden().getCodOrden().equals(codOrden)
                    && po.get(i).getProductoVenta().getPCod().equals(codProducto)) {
                contains = i;
            }
        }

        if (contains != -1) {
            ProductovOrden p = po.get(contains);
            int cant = p.getCantidad();
            if (cant > 1) {
                p.setCantidad(cant - 1);
                o.setProductovOrdenList(po);
                super.edit(o);

            } else {
                po.remove(contains);
                getEntityManager().getTransaction().begin();
                p = getEntityManager().find(ProductovOrden.class, p.getProductovOrdenPK());
                getEntityManager().remove(p);
                getEntityManager().getTransaction().commit();
                o.setProductovOrdenList(po);
                super.edit(o);
            }

        }//TODO: aqui hay que disminuir tambien los contadores para enviado a cocina junto con los contadores de cantidad

        return "1";
    }//TODO: METODoS ARCAICOS'

    @GET
    @Path("REMOVEALL_{codOrden}")
    @Produces(MediaType.TEXT_PLAIN)
    public String removeAllProducto(@PathParam("codOrden") String codOrden,
            @PathParam("codProductoVenta") String codProducto) {
        Orden o = super.find(codOrden);

        ArrayList<ProductovOrden> po = new ArrayList<>(o.getProductovOrdenList());
        getEntityManager().getTransaction().begin();
        while (!po.isEmpty()) {
            ProductovOrden p = po.remove(0);
            p = getEntityManager().find(ProductovOrden.class, p.getProductovOrdenPK());
            getEntityManager().remove(p);
        }
        o.setProductovOrdenList(po);
        getEntityManager().getTransaction().commit();
        
        super.edit(o);
        return "1";
    }//TODO: METODoS ARCAICOS

    @GET
    @Path("FINISH_{codOrden}")
    @Produces(MediaType.TEXT_PLAIN)
    public String finish(@PathParam("codOrden") String codOrden) {
        float ordenGastosEnInsumos = 0,
                ordenValorMonetario = 0;
        
        Orden o = super.find(codOrden);
        Mesa m = getEntityManager().find(Mesa.class, o.getMesacodMesa().getCodMesa());
        
        m.setEstado("vacia");
        o.setHoraTerminada(new Date());
        
        for (ProductovOrden x : o.getProductovOrdenList()) {
            ordenValorMonetario += x.getProductoVenta().getPrecioVenta()*x.getCantidad();
            ordenGastosEnInsumos += x.getProductoVenta().getGasto()*x.getCantidad();
        }
        
        if(o.getPorciento() != 0){
            ordenValorMonetario += o.getPorciento()*ordenValorMonetario;
        }
        
        o.setOrdengastoEninsumos(ordenGastosEnInsumos);
        o.setOrdenvalorMonetario(ordenValorMonetario);
        
        
        Impresion i = new Impresion(getEntityManager().find(Carta.class, "Mnu-1"), false, 24);
        try {
            i.print(o,false);
        } catch (PrintException | NullPointerException ex) {
            Logger.getLogger(OrdenFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
        }

        em1.getTransaction().begin();
        em1.merge(m);
        em1.getTransaction().commit();
        super.edit(o);

        return "1";
    }//TODO: METODoS ARCAICOS

    @GET
    @Path("ENVIARCOCINA_{codOrden}")
    @Produces(MediaType.TEXT_PLAIN)
    public String enviarACocina(@PathParam("codOrden") String codOrden) {

        Orden o = super.find(codOrden);
        Mesa m = getEntityManager().find(Mesa.class, o.getMesacodMesa().getCodMesa());
        Impresion i = new Impresion(getEntityManager().find(Carta.class, "Mnu-1"), false, 24);
        try {

            i.printKitchen(o);

        } catch (PrintException | NullPointerException ex) {
            Logger.getLogger(OrdenFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
        }
        super.edit(o);

        return "1";
    }//TODO: METODoS ARCAICOS
    
    
    @GET
    @Path("PRINT_{codOrden}")
    @Produces(MediaType.TEXT_PLAIN)
    public String print(@PathParam("codOrden") String codOrden) {

        Orden o = super.find(codOrden);
        Mesa m = getEntityManager().find(Mesa.class, o.getMesacodMesa().getCodMesa());
        Impresion i = new Impresion(getEntityManager().find(Carta.class, "Mnu-1"), false, 24);
        try {
            
            i.print(o,false);

        } catch (PrintException | NullPointerException ex) {
            Logger.getLogger(OrdenFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
        }
        super.edit(o);

        return "1";
    }//TODO: METODoS ARCAICOS

    @GET
    @Path("SETDELACASA_{codOrden}_{deLaCasa}")
    @Produces(MediaType.TEXT_PLAIN)
    public String setDeLaCasa(@PathParam("codOrden") String codOrden,
            @PathParam("deLaCasa") String deLaCasa) {

        Orden o = super.find(codOrden);
        o.setDeLaCasa(deLaCasa.equals("true"));
        super.edit(o);
        return "1";
    }//TODO: METODoS ARCAICOS

    @GET
    @Path("MOVERAMESA_{codOrden}_{codMesa}")
    @Produces(MediaType.TEXT_PLAIN)
    public String setMesa(@PathParam("codOrden") String codOrden,
            @PathParam("codMesa") String codMesa) {
        getEntityManager().getTransaction().begin();
        Orden o = super.find(codOrden);
        Mesa mesaDestino = getEntityManager().find(Mesa.class, codMesa);
        Mesa mesaOrigen = o.getMesacodMesa();
        if(mesaDestino.getEstado().equals("ocupada")){
            getEntityManager().getTransaction().rollback();
            return "La mesa de destino esta ocupada";
        }
        mesaDestino.setEstado("ocupada");
        getEntityManager().merge(mesaDestino);
        o.setMesacodMesa(mesaDestino);
        mesaOrigen.setEstado("vacia");
        getEntityManager().merge(mesaOrigen);
        super.edit(o);
        getEntityManager().getTransaction().commit();
        return "1";
    }//TODO: METODoS ARCAICOS

    
    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    @GET
    @Path("fetch")
    @Produces(MediaType.TEXT_PLAIN)
    public String siguientNoOrden() {
        Configuracion c = getEntityManager().find(Configuracion.class, "O");
        int ret = c.getValor();
        c.setValor(ret+1);
        getEntityManager().persist(c);
        return "O-" + (ret);
    }
    

    @Override
    protected EntityManager getEntityManager() {
        return em1;
    }
    
    private String ajustarNoOrden(){
        String numeroOrdenNuevo = siguientNoOrden() ;
        boolean existe = super.find(numeroOrdenNuevo) != null;
        
        while(existe){
              numeroOrdenNuevo = siguientNoOrden();
              existe = super.find(numeroOrdenNuevo) != null;
            
        }
        return numeroOrdenNuevo;
        
    }

}
