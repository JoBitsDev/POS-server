/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jobits.pos.service;

import com.jobits.pos.persistence.ProductovOrden;
import com.jobits.pos.persistence.ProductovOrdenPK;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.DispatcherType;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.PathSegment;

/**
 * FirstDream
 * @author Jorge
 * 
 */
 
@Path("productovorden")
public class ProductovOrdenFacadeREST extends AbstractFacade<ProductovOrden> {

    @PersistenceContext(unitName = "Restaurant_Manager_Web_ServicePU")
    private EntityManager em;

    private ProductovOrdenPK getPrimaryKey(PathSegment pathSegment) {
        /*
         * pathSemgent represents a URI path segment and any associated matrix parameters.
         * URI path part is supposed to be in form of 'somePath;productoVentapCod=productoVentapCodValue;ordencodOrden=ordencodOrdenValue'.
         * Here 'somePath' is a result of getPath() method invocation and
         * it is ignored in the following code.
         * Matrix parameters are used as field names to build a primary key instance.
         */
        com.jobits.pos.persistence.ProductovOrdenPK key = new com.jobits.pos.persistence.ProductovOrdenPK();
        javax.ws.rs.core.MultivaluedMap<String, String> map = pathSegment.getMatrixParameters();
        java.util.List<String> productoVentapCod = map.get("productoVentapCod");
        if (productoVentapCod != null && !productoVentapCod.isEmpty()) {
            key.setProductoVentapCod(productoVentapCod.get(0));
        }
        java.util.List<String> ordencodOrden = map.get("ordencodOrden");
        if (ordencodOrden != null && !ordencodOrden.isEmpty()) {
            key.setOrdencodOrden(ordencodOrden.get(0));
        }
        return key;
    }

    public ProductovOrdenFacadeREST() {
        super(ProductovOrden.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(ProductovOrden entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") PathSegment id, ProductovOrden entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") PathSegment id) {
        com.jobits.pos.persistence.ProductovOrdenPK key = getPrimaryKey(id);
        super.remove(super.find(key));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public ProductovOrden find(@PathParam("id") PathSegment id) {
        com.jobits.pos.persistence.ProductovOrdenPK key = getPrimaryKey(id);
        return super.find(key);
    }

    @GET
    @Path("FIND_{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<ProductovOrden> findList(@PathParam("id") String id) {
        List<ProductovOrden> l= super.findAll();
        List<ProductovOrden> ret = new ArrayList();
        l.stream().filter((x) -> 
                (x.getOrden().getCodOrden().equals(id))).forEachOrdered((x) -> {
            ret.add(x);
        });
       return ret; 
        
       
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<ProductovOrden> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<ProductovOrden> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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
