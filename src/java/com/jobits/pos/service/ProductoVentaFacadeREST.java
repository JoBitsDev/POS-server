/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jobits.pos.service;

import com.jobits.pos.authentication.Secured;
import com.jobits.pos.persistence.Carta;
import com.jobits.pos.persistence.Mesa;
import com.jobits.pos.persistence.ProductoVenta;
import com.jobits.pos.persistence.Seccion;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.security.RolesAllowed;
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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * FirstDream
 *
 * @author Jorge
 *
 */
@Path("productoventa/")
public class ProductoVentaFacadeREST extends AbstractFacade<ProductoVenta> {

    @PersistenceContext(unitName = "Restaurant_Manager_Web_ServicePU")
    private EntityManager em;

    public ProductoVentaFacadeREST() {
        super(ProductoVenta.class);
    }

    @RolesAllowed("0")
    @Secured
    @GET
    @Path("PRODUCTS")
    public Response getProductsFromArea(@QueryParam("codMesa") String id) {
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
        Collections.sort(ret);
        return toJsonString(Response.Status.OK, ret);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}
