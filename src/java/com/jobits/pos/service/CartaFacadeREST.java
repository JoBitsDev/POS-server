/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jobits.pos.service;

import com.jobits.pos.authentication.Secured;
import com.jobits.pos.persistence.Carta;
import com.jobits.pos.persistence.Negocio;
import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import com.jobits.utils.R;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.core.Response;

/**
 * FirstDream
 *
 * @author Jorge
 *
 */
@Path("carta/")
public class CartaFacadeREST extends AbstractFacade<Carta> {

    public CartaFacadeREST() {
        super(Carta.class);
    }

    @RolesAllowed("0")
    @Secured
    @GET
    @Path("NOMBRE-REST")
    public Response getNombreRest() {
        return toJsonString(Response.Status.OK, R.em1.find(Negocio.class, 1).getNombre());
    }

    @Override
    protected EntityManager getEntityManager() {
        return em1;
    }

}
