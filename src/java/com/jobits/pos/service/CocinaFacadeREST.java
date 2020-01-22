/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jobits.pos.service;

import com.jobits.pos.persistence.Cocina;
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
 * @author Jorge
 * 
 */
 
@Path("cocina")
public class CocinaFacadeREST extends AbstractFacade<Cocina> {

    @PersistenceContext(unitName = "Restaurant_Manager_Web_ServicePU")
    private EntityManager em;

    public CocinaFacadeREST() {
        super(Cocina.class);
    }

  @GET
    @Path("NAMES")
    @Produces(MediaType.TEXT_PLAIN)
    public String getCocinasNombres() {
        String ret = "";
        for (Cocina cocina : super.findAll()) {
          ret += cocina.getCodCocina()+ ",";
      }
        return ret.substring(0, ret.length()-1);
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
