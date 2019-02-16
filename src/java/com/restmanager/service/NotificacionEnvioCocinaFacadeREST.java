/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.restmanager.service;

import com.restmanager.NotificacionEnvioCocina;
import com.restmanager.NotificacionEnvioCocinaPK;
import com.restmanager.ProductovOrden;
import com.restmanager.XMLservice.ProductovOrdenXMLexport;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.PathSegment;

/**
 * FirstDream
 *
 * @author Jorge
 *
 */
@Path("com.restmanager.notificacion")
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
        com.restmanager.NotificacionEnvioCocinaPK key = new com.restmanager.NotificacionEnvioCocinaPK();
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
    public String showPending(@PathParam("codCocina") String codCocina) {
        List<ProductovOrden> ret = new ArrayList<>();
        for (NotificacionEnvioCocina x : findAll()) {
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
                super.em1.getTransaction().begin();
                super.em1.merge(x.getProductovOrden());
                super.em1.getTransaction().commit();
                super.remove(x);
                return "Notificacion Exitosa";

            }
        }
        return "Los parámetros no son válidos o la notificación ya fue enviada";
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") PathSegment id) {
        com.restmanager.NotificacionEnvioCocinaPK key = getPrimaryKey(id);
        super.remove(super.find(key));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public NotificacionEnvioCocina find(@PathParam("id") PathSegment id) {
        com.restmanager.NotificacionEnvioCocinaPK key = getPrimaryKey(id);
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

}
