/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jobits.pos.service;

import com.jobits.pos.authentication.Secured;
import com.jobits.pos.persistence.Area;
import com.jobits.pos.persistence.Mesa;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
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
@Path("area/")
public class AreaFacadeREST extends AbstractFacade<Area> {

    @PersistenceContext(unitName = "Restaurant_Manager_Web_ServicePU")
    private EntityManager em;

    public AreaFacadeREST() {
        super(Area.class);
    }

    @RolesAllowed("0")
    @Secured
    @GET
    @Path("FIND-VACIAS")
    public Response findEmptyTables(@QueryParam("codMesa") String codMesa) {
        List<String> ret = new ArrayList<>();
        Mesa mesa = em1.find(Mesa.class, codMesa);
        Area a = mesa.getAreacodArea();
        ArrayList<Mesa> mesas = new ArrayList<>(a.getMesaList());
        Collections.sort(mesas);

        for (Mesa m : mesas) {
            if (m.getEstado().equals("vacia")) {
                ret.add(m.getCodMesa());
            }
        }
        return toJsonString(Response.Status.OK, ret);
    }

    @RolesAllowed("0")
    @GET
    @Secured
    @Path("FIND-ALL-MESAS-AREA")
    public Response getAreaMesas(@QueryParam("selectedArea") String id) {
        List<Mesa> ret = new ArrayList<>();
        for (Mesa mesa : new ArrayList<Mesa>(super.findAll(Mesa.class))) {
            if (mesa.getAreacodArea().getCodArea().equals(id)) {
                ret.add(mesa);
            }
        }
        return toJsonString(Response.Status.OK, ret);
    }

    @RolesAllowed("0")
    @GET
    @Secured
    @Path("FIND-ALL")
    public Response getAreaMesas() {
        return toJsonString(Response.Status.OK, new ArrayList<>(super.findAll(Mesa.class)));
    }

    @RolesAllowed("1")
    @Secured
    @GET
    public Response getAreas() {
        ArrayList<String> ret = new ArrayList<>();
        for (Area a : new ArrayList<>(super.findAll())) {
            ret.add(a.getCodArea());
        }
        return toJsonString(Response.Status.OK, ret);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}
