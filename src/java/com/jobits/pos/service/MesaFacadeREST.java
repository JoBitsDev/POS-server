/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jobits.pos.service;

import com.jobits.pos.persistence.Area;
import com.jobits.pos.persistence.Mesa;
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
@Path("com.jobits.pos.mesa")
public class MesaFacadeREST extends AbstractFacade<Mesa> {

    @PersistenceContext(unitName = "Restaurant_Manager_Web_ServicePU")
    private EntityManager em;

    public MesaFacadeREST() {
        super(Mesa.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Mesa entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") String id, Mesa entity) {
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
    public Mesa find(@PathParam("id") String id) {
        return super.find(id);
    }

    @GET
    @Path("AREA_{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Mesa> getAreaMesas(@PathParam("id") String id) {
        List<Mesa> ret = new ArrayList<>();
        for (Mesa mesa : super.findAll()) {
            if (mesa.getAreacodArea().getCodArea().equals(id)) {
             ret.add(mesa);
            }
        }
        return ret;
    }
    
       @GET
    @Path("MOSTRARVACIAS")
    @Produces(MediaType.APPLICATION_XML)
    public List<Mesa> findEmptyTables() {
       // em1.getEntityManagerFactory().getCache().evict(Mesa.class);
        List<Mesa> mesas = findAll();
        List<Mesa> ret = new ArrayList<>();
        
        for (Mesa m : mesas) {
            if(m.getEstado().equals("vacia")){
                ret.add(m);
            }
        }
        return ret;
    }
    

    @GET
    @Path("AREAS")
    @Produces({MediaType.TEXT_PLAIN})
    public String getAreas() {
        String ret = "";
        for (Area cocina : (List<Area>) super.findAll(Area.class)) {
            ret += cocina.getCodArea() + ",";
        }
        return ret.substring(0, ret.length() - 1);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Mesa> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Mesa> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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
