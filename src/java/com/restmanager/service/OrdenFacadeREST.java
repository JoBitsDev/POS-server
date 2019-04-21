/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.restmanager.service;

import com.restmanager.*;
import com.restmanager.XMLservice.ProductovOrdenXMLexport;
import com.restmanager.notificationdelivery.Notificable;
import com.restmanager.notificationdelivery.Notificador;
import com.restmanager.printservice.Impresion;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import restmanager.resources.R;

/**
 *
 * @author Jorge
 */
@Path("com.restmanager.orden")
public class OrdenFacadeREST extends AbstractFacade<Orden> {

    SimpleDateFormat FormatDate = new SimpleDateFormat("MM'/'dd'/'yy");
    SimpleDateFormat FormatTime = new SimpleDateFormat(" hh ':' mm ' ' a ");
    private Date today = new Date();

    public static final String ESTADO_MESA_VACIA = "vacia",
            ESTADO_MESA_ESPERANDO_CONFIRMACION = "esperando confirmacion";

    public OrdenFacadeREST() {
        super(Orden.class);

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

        Venta v = findVenta();

        if (v == null) {
            return "2";
        }

        m.setEstado(o.getCodOrden() + " " + usuarioTrabajador);
        o.setMesacodMesa(m);
        o.setPersonalusuario(p);
        o.setVentafecha(v);
        o.setDeLaCasa(false);
        o.setHoraComenzada(new Date());
        o.setOrdenvalorMonetario(Float.valueOf("0"));
        o.setPorciento(m.getAreacodArea().getPorcientoPorServicio().floatValue());

        super.create(o);

        return "1";
    }

    @GET
    @Path("GETCAMARERO_{codOrden}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getCamarero(@PathParam("codOrden") String codOrden) {
        return super.find(codOrden).getPersonalusuario().getUsuario();
    }

    @GET
    @Path("LISTPRODUCTS_{codOrden}")
    @Produces({MediaType.TEXT_XML})
    public String getPDV(@PathParam("codOrden") String codOrden) {
        Orden o = super.find(codOrden);

        if (o != null && o.getHoraTerminada() != null) {
            Mesa m = o.getMesacodMesa();
            m.setEstado(ESTADO_MESA_VACIA);

            em1.getTransaction().begin();
            em1.merge(m);
            em1.flush();
            if (em1.getTransaction().isActive()) {
                em1.getTransaction().commit();
            }

            return null;
        }

        return ProductovOrdenXMLexport.exportToXML(o.getProductovOrdenList());
    }

    @GET
    @Path("ISVALID_{codOrden}")
    @Produces({MediaType.TEXT_XML})
    public String isValid(@PathParam("codOrden") String codOrden) {
        Orden o = super.find(codOrden);

        if (o != null) {
            if (o.getHoraTerminada() != null) {
                Mesa m = o.getMesacodMesa();
                m.setEstado(ESTADO_MESA_VACIA);

                em1.getTransaction().begin();
                em1.merge(m);
                em1.flush();
                if (em1.getTransaction().isActive()) {
                    em1.getTransaction().commit();
                }
                return "0";
            } else {
                return "1";
            }
        } else {
            return "0";
        }
    }

    @GET
    @Path("ADD_{codOrden}_{codProductoVenta}")
    @Produces(MediaType.TEXT_PLAIN)
    public String addProducto(@PathParam("codOrden") String codOrden,
            @PathParam("codProductoVenta") String codProducto) {

        Orden o = super.find(codOrden);
        ProductoVenta producto = getEntityManager().find(ProductoVenta.class, codProducto);
        ArrayList<ProductovOrden> po = new ArrayList<>(o.getProductovOrdenList());
        int contains = -1;
        if (!po.isEmpty()) {
            for (int i = 0; contains == -1 && i < po.size(); i++) {
                if (po.get(i).getProductoVenta().getPCod().equals(codProducto)) {
                    contains = i;
                }
            }
        }

        if (contains != -1) {
            ProductovOrden p = po.get(contains);
            float cant = p.getCantidad();
            p.setCantidad(++cant);

        } else {
            ProductovOrden aux = new ProductovOrden(codProducto, codOrden);
            aux.setOrden(o);
            aux.setProductoVenta(producto);
            aux.setCantidad(1);
            aux.setEnviadosacocina((float) 0);
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
    @Path("ADD_{codOrden}_{codProductoVenta}_{cantidad}")
    @Produces(MediaType.TEXT_PLAIN)
    public String addProducto(@PathParam("codOrden") String codOrden,
            @PathParam("codProductoVenta") String codProducto, @PathParam("cantidad") Float cantidad) {

        Orden o = super.find(codOrden);
        ProductoVenta producto = getEntityManager().find(ProductoVenta.class, codProducto);
        ArrayList<ProductovOrden> po = new ArrayList<>(o.getProductovOrdenList());
        int contains = -1;
        if (!po.isEmpty()) {
            for (int i = 0; contains == -1 && i < po.size(); i++) {
                if (po.get(i).getProductoVenta().getPCod().equals(codProducto)) {
                    contains = i;
                }
            }
        }

        if (contains != -1) {
            ProductovOrden p = po.get(contains);
            float cant = p.getCantidad();
            p.setCantidad(cant + cantidad);

        } else {
            ProductovOrden aux = new ProductovOrden(codProducto, codOrden);
            aux.setOrden(o);
            aux.setProductoVenta(producto);
            aux.setCantidad(cantidad);
            aux.setEnviadosacocina((float) 0);
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
            if (po.get(i).getProductoVenta().getPCod().equals(codProducto)) {
                contains = i;
            }
        }

        if (contains != -1) {
            ProductovOrden p = po.get(contains);
            float cant = p.getCantidad();
            if (cant > 1) {
                p.setCantidad(cant - 1);

            } else {
                po.get(contains).setCantidad(0);
                Impresion i = new Impresion();
                i.printCancelationTicket(o);
                po.remove(contains);
                getEntityManager().getTransaction().begin();
                p = getEntityManager().find(ProductovOrden.class, p.getProductovOrdenPK());
                getEntityManager().remove(p);
                getEntityManager().getTransaction().commit();

            }
            o.setProductovOrdenList(po);
            o.setOrdenvalorMonetario(calcularValorTotal(o));
            super.edit(o);
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

            if (R.TABLETS_EN_COCINA) {
                if (!x.getNotificacionEnvioCocinaList().isEmpty()) {
                    return "2";
                }
            } else {
                if (x.getCantidad() > x.getEnviadosacocina()) {
                    return "2";
                }

            }
        }

        o.setHoraTerminada(new Date());
        Impresion i = new Impresion();
        i.print(o, false);

        m.setEstado(ESTADO_MESA_VACIA);

        o.setOrdengastoEninsumos(calcularGastoTotal(o));
        o.setOrdenvalorMonetario(calcularValorTotal(o));

        em1.getTransaction().begin();
        em1.merge(m);
        em1.flush();
        if (em1.getTransaction().isActive()) {
            em1.getTransaction().commit();
        }

        super.edit(o);

        return "1";
    }//TODO: METODoS ARCAICOS

    @GET
    @Path("ENVIARCOCINA_{codOrden}")
    @Produces(MediaType.TEXT_PLAIN)
    public String enviarACocina(@PathParam("codOrden") String codOrden, @Context HttpServletRequest inRequest) {

        Orden o = super.find(codOrden);
        Mesa m = getEntityManager().find(Mesa.class, o.getMesacodMesa().getCodMesa());
        if (!R.TABLETS_EN_COCINA) {
            Impresion i = new Impresion();
            i.printKitchen(o);
        } else {
            for (ProductovOrden x : o.getProductovOrdenList()) {
                if (x.getEnviadosacocina() < x.getCantidad()) {
                    NotificacionEnvioCocinaPK notPK = new NotificacionEnvioCocinaPK();
                    notPK.setCocinacodCocina(x.getProductoVenta().getCocinacodCocina().getCodCocina());
                    notPK.setProductovOrdenordencodOrden(o.getCodOrden());
                    notPK.setProductovOrdenproductoVentapCod(x.getProductoVenta().getPCod());
                    NotificacionEnvioCocina not = super.em1.find(NotificacionEnvioCocina.class, notPK);
                    boolean exist = true;
                    if (not == null) {
                        not = new NotificacionEnvioCocina(notPK);
                        exist = false;
                    }
                    not.setCocina(x.getProductoVenta().getCocinacodCocina());
                    not.setHoraNotificacion(new Date());
                    not.setProductovOrden(x);
                    not.setIp_dependiente(inRequest.getRemoteHost());
                    super.em1.getTransaction().begin();
                    if (exist) {
                        super.em1.merge(not);
                        not.setCantidad(not.getCantidad() + (x.getCantidad() - x.getEnviadosacocina()));
                    } else {
                        super.em1.persist(not);
                        not.setCantidad(x.getCantidad() - x.getEnviadosacocina());
                    }

                    enviarNotificacion(not);
                    super.em1.getTransaction().commit();
                    x.setEnviadosacocina(x.getCantidad());
                    x.setListoParaRecoger(false);
                }
            }
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
            if (x.getProductoVenta().getPCod().equals(pCod)) {
                if (x.getNota() == null) {
                    NotaPK notaPk = new NotaPK(pCod, codOrden);
                    Nota newNota = new Nota(notaPk);
                    newNota.setDescripcion(nota);
                    x.setNota(newNota);
                    pv = x;
                } else {
                    x.getNota().setDescripcion(nota);
                    pv = x;
                }
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
            @PathParam("pcod") String pCod) {

        Orden o = super.find(codOrden);

        for (ProductovOrden x : o.getProductovOrdenList()) {
            if (x.getProductoVenta().getPCod().equals(pCod)) {
                if (x.getNota() == null) {
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
            @PathParam("pcod") String pCod) {

        Orden o = super.find(codOrden);

        for (ProductovOrden x : o.getProductovOrdenList()) {
            if (x.getProductoVenta().getPCod().equals(pCod)) {
                if (x.getNumeroComensal() == null) {
                    return "0";
                }
                return "" + x.getNumeroComensal();
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
            @PathParam("numeroComensal") String numero) {

        Orden o = super.find(codOrden);
        ProductovOrden pv = null;
        for (ProductovOrden x : o.getProductovOrdenList()) {
            if (x.getProductoVenta().getPCod().equals(pCod)) {
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
        Impresion i = new Impresion();
        i.print(o, false);

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
        if (mesaDestino.getEstado().equals("ocupada")) {
            getEntityManager().getTransaction().rollback();
            return "La mesa de destino esta ocupada";
        }
        mesaDestino.setEstado(o.getCodOrden() + " " + o.getPersonalusuario().getUsuario());
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
        o.getMesacodMesa().setEstado(o.getCodOrden() + " " + personalDestino.getUsuario());
        getEntityManager().merge(o.getMesacodMesa());
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
        c.setValor(ret + 1);
        getEntityManager().persist(c);
        return "O-" + (ret);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em1;
    }

    private String ajustarNoOrden() {
        String numeroOrdenNuevo = siguientNoOrden();
        boolean existe = super.find(numeroOrdenNuevo) != null;

        while (existe) {
            numeroOrdenNuevo = siguientNoOrden();
            existe = super.find(numeroOrdenNuevo) != null;

        }
        return numeroOrdenNuevo;

    }

    private float calcularValorTotal(Orden o) {
        float ordenValorMonetario = 0;

        for (ProductovOrden x : o.getProductovOrdenList()) {
            ordenValorMonetario += x.getProductoVenta().getPrecioVenta() * x.getCantidad();
        }

        if (o.getPorciento() != 0) {
            if (!o.getDeLaCasa()) {
                ordenValorMonetario += (o.getPorciento() / 100) * ordenValorMonetario;
            }
        }

        return ordenValorMonetario;

    }

    private float calcularGastoTotal(Orden o) {
        float ordenGastosEnInsumos = 0;

        for (ProductovOrden x : o.getProductovOrdenList()) {
            ordenGastosEnInsumos += x.getProductoVenta().getGasto() * x.getCantidad();
        }
        return ordenGastosEnInsumos;
    }

    private String enviarNotificacion(NotificacionEnvioCocina c) {
        for (Impresora i : c.getCocina().getImpresoraList()) {
            if (i.getIpImpresora() != null) {
                new Notificador(i.getIpImpresora(), new Notificable() {
                    @Override
                    public String getMensajeNotificacion() {
                        return "Productos a elaborar " + c.getProductovOrden().getProductoVenta().getNombre();
                    }

                    @Override
                    public String getTituloNotificacion() {
                        return "Restaurant Manager";
                    }

                    @Override
                    public String getDescripcionNotificacion() {
                        return c.getCantidad() + " de " + c.getProductovOrden().getProductoVenta().getNombre();
                    }
                }).notificar();
            }
        }
        return "Notificacion Exitosa";

    }

    private Venta findVenta() {
        Venta ret;
        e.getCache().evictAll();
        em1.close();
        em1 = e.createEntityManager();
        javax.persistence.criteria.CriteriaQuery cq = em1.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Venta.class));
        List<Venta> ventas = em1.createQuery(cq).getResultList();
        for (int i = ventas.size() - 1; i >= 0; i--) {
            if (ventas.get(i).getVentaTotal() == null) {
                return ventas.get(i);
            }
        }

        return null;
    }

}
