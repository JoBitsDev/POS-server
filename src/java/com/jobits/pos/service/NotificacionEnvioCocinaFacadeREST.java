/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jobits.pos.service;

import com.jobits.pos.persistence.Cocina;
import com.jobits.pos.persistence.Impresora;
import com.jobits.pos.persistence.NotificacionEnvioCocina;
import com.jobits.pos.persistence.NotificacionEnvioCocinaPK;
import com.jobits.pos.persistence.ProductovOrden;
import com.restmanager.XMLservice.ProductovOrdenXMLexport;
import com.jobits.pos.notificationdelivery.Notificable;
import com.jobits.pos.notificationdelivery.Notificador;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.PathSegment;

/**
 * FirstDream
 *
 * @author Jorge
 *
 */
@Path("notificacion/")
public class NotificacionEnvioCocinaFacadeREST extends AbstractFacade<NotificacionEnvioCocina> {

    @PersistenceContext(unitName = "Restaurant_Manager_Web_ServicePU")
    private EntityManager em;

    private NotificacionEnvioCocinaPK getPrimaryKey(PathSegment pathSegment) {
        /*
         * pathSemgent represents a URI path segment and any associated matrix parameters.
         * URI path part is supposed to be in form of 'somePath;cocinacodCocina=
        cocinacodCocinaValue;productovOrdenproductoVentapCod=productovOrdenproductoVentapCodValue;
        productovOrdenordencodOrden=productovOrdenordencodOrdenValue'.
         * Here 'somePath' is a result of getPath() method invocation and
         * it is ignored in the following code.
         * Matrix parameters are used as field names to build a primary key instance.
         */
        com.jobits.pos.persistence.NotificacionEnvioCocinaPK key = new com.jobits.pos.persistence.NotificacionEnvioCocinaPK();
        javax.ws.rs.core.MultivaluedMap<String, String> map = pathSegment.getMatrixParameters();
        java.util.List<String> cocinacodCocina = map.get("cocinacodCocina");
        if (cocinacodCocina != null && !cocinacodCocina.isEmpty()) {
            key.setCocinacodCocina(cocinacodCocina.get(0));
        }
        java.util.List<String> productovOrdenproductoVentapCod = map.get("productovOrdenproductoVentapCod");
        if (productovOrdenproductoVentapCod != null && !productovOrdenproductoVentapCod.isEmpty()) {
            key.setProductovOrdenproductoVentapCod(productovOrdenproductoVentapCod.get(0));
        }
        java.util.List<String> productovOrdenordencodOrden = map.get("productovOrdenordencodOrden");
        if (productovOrdenordencodOrden != null && !productovOrdenordencodOrden.isEmpty()) {
            key.setProductovOrdenordencodOrden(productovOrdenordencodOrden.get(0));
        }
        return key;
    }

    public NotificacionEnvioCocinaFacadeREST() {
        super(NotificacionEnvioCocina.class);
    }

    @GET
    @Path("PENDING_{codCocina}")
    @Produces({MediaType.TEXT_PLAIN})
    public String showPending(@PathParam("codCocina") String codCocina, @Context HttpServletRequest inRequest) {
        registerDevice(inRequest, codCocina);
        List<ProductovOrden> ret = new ArrayList<>();
        List<NotificacionEnvioCocina> all = findAll();
        for (NotificacionEnvioCocina x : all) {
            if (x.getProductovOrden().getOrden().getHoraTerminada() != null) {
                em1.getTransaction().begin();
                super.remove(x);
                em1.getTransaction().commit();
                continue;
            }
            if (x.getCocina().getCodCocina().equals(codCocina)) {
                x.getProductovOrden().setCantidad(x.getCantidad());
                ret.add(x.getProductovOrden());
            }
        }
        return ProductovOrdenXMLexport.exportToXML(ret);
    }

    @GET
    @Path("NOTIFY_{codOrden}_{codProducto}")
    @Produces({MediaType.TEXT_PLAIN})
    public String notify(@PathParam("codOrden") String codOrden, @PathParam("codProducto") String codProducto) {
        for (NotificacionEnvioCocina x : findAll()) {
            if (x.getNotificacionEnvioCocinaPK().getProductovOrdenordencodOrden().equals(codOrden)
                    && x.getNotificacionEnvioCocinaPK().getProductovOrdenproductoVentapCod().equals(codProducto)) {
                x.getProductovOrden().setListoParaRecoger(Boolean.TRUE);
                x.getProductovOrden().setEnviadosacocina(x.getProductovOrden().getCantidad());
                super.em1.getTransaction().begin();
                super.em1.merge(x.getProductovOrden());
                super.em1.getTransaction().commit();
                super.remove(x);
                new Notificador(x.getIpDependiente(), new Notificable() {
                    @Override
                    public String getMensajeNotificacion() {
                        return "Productos a recoger en " + x.getProductovOrden().getProductoVenta().getCocinacodCocina().getNombreCocina();
                    }

                    @Override
                    public String getTituloNotificacion() {
                        return "Restaurant Manager";
                    }

                    @Override
                    public String getDescripcionNotificacion() {
                        return x.getCantidad() + " de " + x.getProductovOrden().getProductoVenta().getNombre();
                    }
                }).notificar();
                return "Notificacion Exitosa";

            }
        }
        return "Los parámetros no son válidos o la notificación ya fue enviada \n "
                + "o el receptor no esta conectado";
    }

    @GET
    @Path("ip")
    @Produces(MediaType.TEXT_PLAIN)
    public String ip(@Context HttpServletRequest inRequest) {
        return inRequest.getRemoteHost();
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") PathSegment id) {
        com.jobits.pos.persistence.NotificacionEnvioCocinaPK key = getPrimaryKey(id);
        super.remove(super.find(key));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public NotificacionEnvioCocina find(@PathParam("id") PathSegment id) {
        com.jobits.pos.persistence.NotificacionEnvioCocinaPK key = getPrimaryKey(id);
        return super.find(key);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<NotificacionEnvioCocina> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<NotificacionEnvioCocina> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    private void registerDevice(HttpServletRequest inRequest, String cod_cocina) {
        boolean founded = false;
        Cocina c = em1.find(Cocina.class, cod_cocina);
        Impresora imp = null;
        String host = inRequest.getRemoteHost();
        for (Impresora i : c.getImpresoraList()) {
            if (!i.getIpImpresora().equals(host)) {
                i.setIpImpresora(host);
            }
            imp = i;
            founded = true;
        }
        if (!founded) {
            Impresora i = new Impresora();
            i.setCocinacodCocina(c);
            i.setEstaactiva(true);
            i.setIpImpresora(host);
            i.setNombreImpresora("Device");
            i.setCodImpresora("D-" + c.getCodCocina());
            em1.getTransaction().begin();
            em1.persist(i);
            em1.getTransaction().commit();
        } else {
            em1.getTransaction().begin();
            em1.merge(imp);
            em1.getTransaction().commit();
        }
    }

    private void logOutDevice(HttpServletRequest inRequest, String cod_cocina){
        //TODO : finish
    }
    
}
