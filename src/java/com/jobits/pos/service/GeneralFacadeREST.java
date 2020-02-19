/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jobits.pos.service;

import com.jobits.pos.persistence.Carta;
import com.jobits.pos.persistence.Negocio;
import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import com.jobits.utils.R;
import java.util.HashMap;
import javax.ws.rs.core.Response;

/**
 * FirstDream
 *
 * @author Jorge
 *
 */
@Path("general/")
public class GeneralFacadeREST extends AbstractFacade<Carta> {

    public GeneralFacadeREST() {
        super(Carta.class);
    }

    @GET
    @Path("INFO")
    public Response getMonedas() {
        HashMap<String, Object> ret = new HashMap<>();
        Negocio n = R.em1.find(Negocio.class, 1);
        ret.put("nombre", n.getNombre());
        ret.put("monedaPrincipal", " " + n.getMonedaPrincipal());
        String secundaria = n.getMonedaPrincipal().equals("CUC") ? " MN" : " CUC";
        ret.put("monedaSecundaria", secundaria);
        ret.put("cambio", R.COINCHANGE);
        ret.put("majorVersion", R.MAJOR_VERSION);
        ret.put("minorVersion", R.MINOR_VERSION);
        return toJsonString(Response.Status.OK, ret);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em1;
    }

}
