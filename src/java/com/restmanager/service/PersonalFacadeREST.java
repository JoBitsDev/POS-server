/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.restmanager.service;

import com.restmanager.Orden;
import com.restmanager.Personal;
import com.restmanager.Venta;
import java.util.ArrayList;
import java.util.Date;
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
 *
 * @author Jorge
 */

@Path("com.restmanager.personal")
public class PersonalFacadeREST extends AbstractFacade<Personal> {

    @PersistenceContext(unitName = "Restaurant_Manager_Web_ServicePU")
    private EntityManager em;

    public PersonalFacadeREST() {
        super(Personal.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Personal entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") String id, Personal entity) {
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
    public Personal find(@PathParam("id") String id) {
        return super.find(id);
    }

    @GET
    @Path("{action}_{user}_{pass}")
    @Produces(MediaType.TEXT_PLAIN)
    public String find(@PathParam("action") String action,
            @PathParam("user") String user , @PathParam("pass") String pass){
        List<Personal> list = super.findAll();
        
        for (Personal x : list) {
            if(x.getUsuario().equals(user)){
                if (x.getContrasenna().equals(pass)) {
                    if (!x.getOnline()) {
                        return "1";
                    }
                }
                return "2";
            }
        }
        return "0";
    }
    
    @GET
    @Path("MOSTRAR_PERSONAL_TRABAJANDO")
    @Produces({MediaType.TEXT_PLAIN})
    public String findActiveUsers() {
        ArrayList<String> aux = new ArrayList<>();
        
        
        
        for (Orden x : super.em1.find(Venta.class, new Date()).getOrdenList()) {
            String nombre = x.getPersonalusuario().getUsuario();
            if(!aux.contains(nombre)){
                aux.add(nombre);
            }
        }
        
        String ret = "";
        
        
        for (int i = 0; i < aux.size(); i++) {
            ret += aux.get(i)+ ",";
        }
        return ret;
    }
    
    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Personal> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Personal> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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
