/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jobits.pos.service;

import com.jobits.pos.authentication.Secured;
import com.jobits.pos.persistence.Seccion;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * FirstDream
 * @author Jorge
 * 
 */
 
@Path("seccion/")
public class SeccionFacadeREST extends AbstractFacade<Seccion> {

    @PersistenceContext(unitName = "Restaurant_Manager_Web_ServicePU")
    private EntityManager em;

    public SeccionFacadeREST() {
        super(Seccion.class);
    }
    @RolesAllowed("0")
    @Secured
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getAlll() {
        ArrayList <Seccion> ret = new ArrayList<>(super.findAll());
        Collections.sort(ret);
        return toJsonString(Response.Status.OK, ret);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}
