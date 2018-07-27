/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.restmanager.service;

import com.restmanager.*;
import com.restmanager.XMLservice.OrdenXMLExport;
import com.restmanager.XMLservice.ProductovOrdenXMLexport;
import com.restmanager.printservice.Impresion;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
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
    private static LinkedList<Orden> ordenesActivas = new LinkedList<>();
    
    public static final String
            ESTADO_MESA_VACIA = "vacia",
            ESTADO_MESA_ESPERANDO_CONFIRMACION = "esperando confirmacion";

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
       
        m.setEstado(usuarioTrabajador);
        o.setMesacodMesa(m);
        o.setPersonalusuario(p);
        o.setVentafecha(v);
        o.setDeLaCasa(false);
        o.setHoraComenzada(new Date());
        o.setOrdenvalorMonetario(Float.valueOf("0"));
        o.setPorciento(Float.valueOf("10"));
       
        
        super.create(o);
        
        ordenesActivas.add(o);
        
        
        return "1";
    }

    @GET
    @Path("FIND_{codMesa}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public String get(@PathParam("codMesa") String codMesa) {
        Mesa m = getEntityManager().find(Mesa.class, codMesa);
        if (m.getEstado().equals(ESTADO_MESA_VACIA)) {
            return null;
        }
        if(ordenesActivas.isEmpty()){
            buscarOrdenes();
        }
        for (Orden x : ordenesActivas) {
            if(x.getMesacodMesa().getCodMesa().equals(codMesa)){
                x = super.find(x.getCodOrden());
                if(x.getHoraTerminada() == null){
                return x.getCodOrden();
                }
                else{
                    ordenesActivas.remove(x);
                    break;
                }
            }
        }
       
        m.setEstado(ESTADO_MESA_VACIA);
        
        em1.getTransaction().begin();
        em1.merge(m);
        em1.flush();
        if(em1.getTransaction().isActive()){
        em1.getTransaction().commit();}
        
        return null;
    }

    @GET
    @Path("FIND2_{codMesa}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getNoOrden(@PathParam("codMesa") String codMesa) {
        Mesa m = getEntityManager().find(Mesa.class, codMesa);
        if (m.getEstado().equals(ESTADO_MESA_VACIA)) {
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

        m.setEstado(ESTADO_MESA_VACIA);
        
        em1.getTransaction().begin();
        em1.merge(m);
        em1.flush();
        if(em1.getTransaction().isActive())
        em1.getTransaction().commit();
        
        return null;
    }
    
    @GET
    @Path("FINDALL_{codMesa}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getAll(@PathParam("codMesa") String codMesa) {
        Mesa m = getEntityManager().find(Mesa.class, codMesa);
        if (m.getEstado().equals(ESTADO_MESA_VACIA)) {
            return null;
        }

        String ordenes = "";
        
        List<Orden> ord = super.findAll();
        Venta v = getEntityManager().find(Venta.class, today);
        for (int i = ord.size() - 1; i >= 0; i--) {
            if (ord.get(i).getMesacodMesa().getCodMesa().equals(codMesa) &&
                    ord.get(i).getHoraTerminada() == null) {
                ordenes += (ord.get(i).getCodOrden());
                ordenes += ",";
            }
        }
        
        return ordenes.substring(0, ordenes.length()-1);
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
            aux.setNumeroComensal(0);

            //em.persist(aux);
            po.add(aux);

        }
        o.setProductovOrdenList(po);
        o.setOrdenvalorMonetario(calcularValorTotal(o));
       
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
                o.setOrdenvalorMonetario(calcularValorTotal(o));
                super.edit(o);

            } else {
                po.remove(contains);
                getEntityManager().getTransaction().begin();
                p = getEntityManager().find(ProductovOrden.class, p.getProductovOrdenPK());
                getEntityManager().remove(p);
                getEntityManager().getTransaction().commit();
                o.setProductovOrdenList(po);
                o.setOrdenvalorMonetario(calcularValorTotal(o));
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
        o.setOrdenvalorMonetario(calcularValorTotal(o));
        getEntityManager().getTransaction().commit();
        
        super.edit(o);
        return "1";
    }//TODO: METODoS ARCAICOS

    @GET
    @Path("FINISH_{codOrden}")
    @Produces(MediaType.TEXT_PLAIN)
    public String finish(@PathParam("codOrden") String codOrden) {
      
        
        Orden o = super.find(codOrden);
        Mesa m = getEntityManager().find(Mesa.class, o.getMesacodMesa().getCodMesa());
        
        for (ProductovOrden x : o.getProductovOrdenList()) {
            if(x.getCantidad() > x.getEnviadosacocina()){
                return "2";
            }
        }
        
       
        //o.setHoraTerminada(new Date());
        
        Impresion i = new Impresion(getEntityManager().find(Carta.class, "Mnu-1"));
        try {
            i.print(o,false);
        } catch (PrintException | NullPointerException ex) {
            Logger.getLogger(OrdenFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        m.setEstado(ESTADO_MESA_ESPERANDO_CONFIRMACION);
        
        
        o.setOrdengastoEninsumos(calcularGastoTotal(o));
        o.setOrdenvalorMonetario(calcularValorTotal(o));

        
        em1.getTransaction().begin();
        em1.merge(m);
        em1.flush();
        if(em1.getTransaction().isActive()){
        em1.getTransaction().commit();}

        
        
        super.edit(o);
        ordenesActivas.remove(o);

        return "1";
    }//TODO: METODoS ARCAICOS

    @GET
    @Path("ENVIARCOCINA_{codOrden}")
    @Produces(MediaType.TEXT_PLAIN)
    public String enviarACocina(@PathParam("codOrden") String codOrden) {

        Orden o = super.find(codOrden);
        Mesa m = getEntityManager().find(Mesa.class, o.getMesacodMesa().getCodMesa());
        Impresion i = new Impresion(getEntityManager().find(Carta.class, "Mnu-1"));
        try {

            i.printKitchen(o);
            

        } catch (PrintException | NullPointerException ex) {
            Logger.getLogger(OrdenFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        super.edit(o);

        return "1";
    }//TODO: METODoS ARCAICOS
    
    @GET
    @Path("ADDNOTA_{codOrden}_{pcod}_{nota}")
    @Produces(MediaType.TEXT_PLAIN)
    public String addNota(
            @PathParam("codOrden") String codOrden,
            @PathParam("pcod") String pCod,
            @PathParam("nota") String nota) {

        Orden o = super.find(codOrden);
        ProductovOrden pv = null;
        nota = nota.replace('%', ' ');
        
        for (ProductovOrden x : o.getProductovOrdenList()) {
            if(x.getProductoVenta().getPCod().equals(pCod)){
                if(x.getNota() == null){
                    NotaPK notaPk = new NotaPK(pCod, codOrden);
                    Nota newNota = new Nota(notaPk);
                    newNota.setDescripcion(nota);
                    x.setNota(newNota);
                    pv = x;
                }
                else {
                    x.getNota().setDescripcion(nota);
                    pv = x;}
            }
            
        }
        em1.getTransaction().begin();
        em1.merge(pv.getNota());
        em1.merge(pv);
        em1.getTransaction().commit();
        super.edit(o);

        return "1";
    }//TODO: METODoS ARCAICOS
    
     @GET
    @Path("GETNOTA_{codOrden}_{pcod}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getNota(
            @PathParam("codOrden") String codOrden,
            @PathParam("pcod") String pCod){

        Orden o = super.find(codOrden);
        
        for (ProductovOrden x : o.getProductovOrdenList()) {
            if(x.getProductoVenta().getPCod().equals(pCod)){
                if(x.getNota() == null){
                    return "0";
                }
               return x.getNota().getDescripcion().replace('-', ' ');
            }
            
        }
        return "0";
                
                

    }//TODO: METODoS ARCAICOS
    
     @GET
    @Path("GETCOMENSAL_{codOrden}_{pcod}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getComensal(
            @PathParam("codOrden") String codOrden,
            @PathParam("pcod") String pCod){

        Orden o = super.find(codOrden);
        
        for (ProductovOrden x : o.getProductovOrdenList()) {
            if(x.getProductoVenta().getPCod().equals(pCod)){
                if(x.getNumeroComensal() == null){
                    return "0";
                }
               return ""+x.getNumeroComensal();
            }
            
        }
        return "0";
                
                

    }//TODO: METODoS ARCAICOS
    
     @GET
    @Path("ADDCOMENSAL_{codOrden}_{pcod}_{numeroComensal}")
    @Produces(MediaType.TEXT_PLAIN)
    public String addComensal(
            @PathParam("codOrden") String codOrden,
            @PathParam("pcod") String pCod,
            @PathParam("numeroComensal") String numero){

        Orden o = super.find(codOrden);
        ProductovOrden pv = null;
        for (ProductovOrden x : o.getProductovOrdenList()) {
            if(x.getProductoVenta().getPCod().equals(pCod)){
                x.setNumeroComensal(Integer.parseInt(numero));
                pv = x;
                
            }
            
        }
        
        em1.getTransaction().begin();
        em1.merge(pv);
        em1.getTransaction().commit();
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
        mesaOrigen.setEstado(ESTADO_MESA_VACIA);
        getEntityManager().merge(mesaOrigen);
        super.edit(o);
        getEntityManager().getTransaction().commit();
        return "1";
    }//TODO: METODoS ARCAICOS
    
     @GET
    @Path("CEDERORDEN_{codOrden}_{usuario}")
    @Produces(MediaType.TEXT_PLAIN)
    public String setUsuario(@PathParam("codOrden") String codOrden,
            @PathParam("usuario") String usuario) {
        getEntityManager().getTransaction().begin();
        Orden o = super.find(codOrden);
        Personal personalDestino = getEntityManager().find(Personal.class, usuario);
       
        o.setPersonalusuario(personalDestino);
        super.edit(o);
        
        getEntityManager().getTransaction().commit();
        return "1";
    }//TODO: METODoS ARCAICOS
    
    @GET
    @Path("ISMINE_{codMesa}_{user}")
    @Produces(MediaType.TEXT_PLAIN)
    public String isMine(@PathParam("codMesa") String codMesa, @PathParam("user") String user) {
        
        String noOrden = get(codMesa);
        
        Orden o = super.find(noOrden);
        
        if(o != null){
            boolean mine = o.getPersonalusuario().getUsuario().equals(user);
            if(mine){
                return "1";
            }
            else{
                return "0";
            }
        }
        return "0";
        
    }


    
     @GET
    @Path("MENUINFANTIL_{codOrden}_{entrante}_{platoFuerte}_{postre}_{liquido}_{nota}")
    @Produces(MediaType.TEXT_PLAIN)
    public String menuInfantil(@PathParam("entrante") int entrante,
            @PathParam("platoFuerte") int platoFuerte,
            @PathParam("postre") int postre,
            @PathParam("liquido") int liquido,
            @PathParam("nota") String nota,
            @PathParam("codOrden") String codOrden){
        
        String [] entrantes = {"Albondiguillas entomatadas",
        "Burritos Surtidos",
        "Coronitas de salchichas",
        "Crema de Queso, Jamon",
        "Croquetas mixtas",
        "Ensaladillas Frias",
        "Huevitos de codornis primavera",
        "Moldes de gelatinas con queso",
        "Papas Fritas",
        "Papitas rellenas con queso y carne",
        "Pure Africano",
        "Pure Bretonam",
        "Rollitos de Jamon con Piña",
        "Sopa Campesina con vianda y arroz"};
        
        String [] platos_Principales = {
        "Bistec de res al bambino",
        "Canoitas de cordero en su jugo",
        "Espaguetis Matrichana",
        "Espaguetis napolitano",
        "Espaguetis Pulpetin",
        "Fileticos de pescado al nido",
        "Hamburguesa",
        "Mini Pizza 3 quesos",
        "Mini Pizza Napolitana ",
        "Muslitos de pollo rellenos con jamon",
        "Pechuguitas de pollo gratinadas",
        "Picadillos a la criolla",
        "Pinchitos de carne al erizo",
        "Tres delicias a la plancha"
                };
        
        String [] postres  = {
        "Flan de caramelo",
        "Gelatina de varios sabores",
        "Helados caprichosos del sodero",
        "Helados sorpresa del sodero"
        };
        
        String [] liquidos = {
        "Refresco Tu Kola",
        "Refresco Naranja",
        "Refresco Limon",
        "Malta",
        "Jugo Natural",
        "Agua natural"
        };

        Orden o = super.find(codOrden);
        Impresion i = new Impresion(getEntityManager().find(Carta.class, "Mnu-1"));
        try {

            i.printMenuInfantil(o,entrantes[entrante],
                    platos_Principales[platoFuerte],postres[postre],
                    liquidos[liquido],nota);

        } catch (NullPointerException ex) {
            Logger.getLogger(OrdenFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        super.edit(o);

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
    private float calcularValorTotal(Orden o){
        float ordenValorMonetario = 0;
        
         for (ProductovOrden x : o.getProductovOrdenList()) {
            ordenValorMonetario += x.getProductoVenta().getPrecioVenta()*x.getCantidad();
        }
        
        if(o.getPorciento() != 0){
            if(!o.getDeLaCasa()){
            ordenValorMonetario += (o.getPorciento()/100)*ordenValorMonetario;}
        }
        
        return ordenValorMonetario;
        
    }
    
    private float calcularGastoTotal(Orden o){
        float ordenGastosEnInsumos = 0;
        
         for (ProductovOrden x : o.getProductovOrdenList()) {
            ordenGastosEnInsumos += x.getProductoVenta().getGasto()*x.getCantidad();
        }
         return ordenGastosEnInsumos;
    }

    private void buscarOrdenes() {
        List<Orden> ord = super.findAll();
        Venta v = getEntityManager().find(Venta.class, today);
        for (int i = ord.size() - 1; i >= 0; i--) {
            if (ord.get(i).getHoraTerminada() == null && ord.get(i).getVentafecha().equals(v)) {
               ordenesActivas.add(ord.get(i));
            }
        }
     
    }

}