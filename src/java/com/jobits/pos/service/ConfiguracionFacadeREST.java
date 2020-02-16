/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jobits.pos.service;

import com.jobits.pos.authentication.Secured;
import com.jobits.pos.persistence.Configuracion;
import com.jobits.utils.utils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 * FirstDream
 *
 * @author Jorge
 *
 */
@Path("configuracion/")
public class ConfiguracionFacadeREST extends AbstractFacade<Configuracion> {

    @PersistenceContext(unitName = "Restaurant_Manager_Web_ServicePU")
    private EntityManager em;

    public ConfiguracionFacadeREST() {
        super(Configuracion.class);
    }

    @GET
    @Path("CHECK-SHA")
    public String countREST(@QueryParam("url") String path, @QueryParam("sha") String sha, @Context HttpServletRequest header) {
        try {
            String resp = "";
            //path = path.replace("_", "/");
            HttpURLConnection con;

            URL url = new URL(path);
            con = (HttpURLConnection) url.openConnection();
            con.setDoInput(true);
            con.setDoOutput(false);
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "text/plain");
            con.setRequestProperty("Authorization", "Bearer " + getToken(header));

            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {//si esta ok lee el JSON
                BufferedReader input = new BufferedReader(
                        new InputStreamReader(con.getInputStream()),
                        8192);
                resp = "";
                String linea;
                while ((linea = input.readLine()) != null) {
                    resp += linea;
                }
                con.disconnect();
                input.close();
                //os.close();
                return sha.equals(utils.getSHA256(resp)) ? "" : resp;
            } else {//Si no, lee el error y lo propaga
                return "";
            }
        } catch (Exception ex) {
            return "";
        }
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}
