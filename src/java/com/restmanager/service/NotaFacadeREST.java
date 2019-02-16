/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.restmanager.service;

import com.restmanager.Nota;
import com.restmanager.NotaPK;
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
 * @author Jorge
 * 
 */
 
@Path("com.restmanager.nota")
public class NotaFacadeREST extends AbstractFacade<Nota> {

    @PersistenceContext(unitName = "Restaurant_Manager_Web_ServicePU")
    private EntityManager em;

    private NotaPK getPrimaryKey(PathSegment pathSegment) {
        /*
         * pathSemgent represents a URI path segment and any associated matrix parameters.
         * URI path part is supposed to be in form of 'somePath;productovOrdenproductoVentapCod=productovOrdenproductoVentapCodValue;productovOrdenordencodOrden=productovOrdenordencodOrdenValue'.
         * Here 'somePath' is a result of getPath() method invocation and
         * it is ignored in the following code.
         * Matrix parameters are used as field names to build a primary key instance.
         */
        com.restmanager.NotaPK key = new com.restmanager.NotaPK();
        javax.ws.rs.core.MultivaluedMap<String, String> map = pathSegment.getMatrixParameters();
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

    public NotaFacadeREST() {
        super(Nota.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Nota entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") PathSegment id, Nota entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") PathSegment id) {
        com.restmanager.NotaPK key = getPrimaryKey(id);
        super.remove(super.find(key));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Nota find(@PathParam("id") PathSegment id) {
        com.restmanager.NotaPK key = getPrimaryKey(id);
        return super.find(key);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Nota> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Nota> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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
