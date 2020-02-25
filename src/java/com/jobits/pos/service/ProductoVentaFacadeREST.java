/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jobits.pos.service;

import com.jobits.pos.authentication.Secured;
import com.jobits.pos.controllers.IPVController;
import com.jobits.pos.persistence.Carta;
import com.jobits.pos.persistence.Mesa;
import com.jobits.pos.persistence.ProductoVenta;
import com.jobits.pos.persistence.Seccion;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
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

    @RolesAllowed("0")
    @Secured
    @GET
    @Path("RESTANTES")
    public Response getRestantesDeProducto(@QueryParam("codProducto") String codProducto) {
        return toJsonString(Response.Status.OK, new IPVController(getEntityManager()).getRestantes(codProducto,findVenta().getFecha()));
    }

    @Override
    protected EntityManager getEntityManager() {
        return em1;
    }

}
