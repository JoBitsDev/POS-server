/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jobits.pos.service;

import com.jobits.pos.persistence.Carta;
import com.jobits.pos.persistence.Mesa;
import com.jobits.pos.persistence.ProductoVenta;
import com.jobits.pos.persistence.Seccion;
import java.util.AbstractList;
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

/**
 * FirstDream
 *
 * @author Jorge
 *
 */
@Path("com.restmanager.productoventa")
public class ProductoVentaFacadeREST extends AbstractFacade<ProductoVenta> {

    @PersistenceContext(unitName = "Restaurant_Manager_Web_ServicePU")
    private EntityManager em;

    public ProductoVentaFacadeREST() {
        super(ProductoVenta.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(ProductoVenta entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") String id, ProductoVenta entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") String id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public ProductoVenta find(@PathParam("id") String id) {
        return super.find(id);
    }

    @GET
    @Path("PRODUCTS_{cod_mesa}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<ProductoVenta> getProductsFromArea(@PathParam("cod_mesa") String id) {
        Mesa m = em1.find(Mesa.class, id);
        List<ProductoVenta> ret = new ArrayList<>();
        for (Carta carta : new ArrayList<>(m.getAreacodArea().getCartaList())) {
            for (Seccion seccion : new ArrayList<>(carta.getSeccionList())) {
                for (ProductoVenta p : seccion.getProductoVentaList()) {
                    if (p.getVisible()) {
                        ret.add(p);
                    }
                }
            }
        }
        return ret;
    }

@GET
        @Override
        @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
        public List<ProductoVenta> findAll() {
        return super.findAll();
    }

    @GET
        @Path("{from}/{to}")
        @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
        public List<ProductoVenta> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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
