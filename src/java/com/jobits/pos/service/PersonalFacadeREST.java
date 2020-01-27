/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jobits.pos.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.jobits.pos.persistence.Orden;
import com.jobits.pos.persistence.Personal;
import com.jobits.pos.persistence.Venta;
import com.jobits.pos.authentication.Credentials;
import com.jobits.pos.authentication.Secured;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.security.auth.login.CredentialException;
import javax.security.auth.login.CredentialExpiredException;
import javax.security.auth.login.CredentialNotFoundException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * FirstDream
 *
 * @author Jorge
 *
 */
@Path("login/")
public class PersonalFacadeREST extends AbstractFacade<Personal> {

    @PersistenceContext(unitName = "Restaurant_Manager_Web_ServicePU")
    private EntityManager em;

    public static HashMap<String, Credentials> tokens = new HashMap<>();

    public PersonalFacadeREST() {
        super(Personal.class);
    }

    @RolesAllowed("1")
    @GET
    @Secured
    @Path("MOSTRAR-PERSONAL-TRABAJANDO")
    public Response findActiveUsers() {
        ArrayList<String> aux = new ArrayList<>();

        for (Orden x : super.em1.find(Venta.class, findVenta().getFecha()).getOrdenList()) {
            String nombre = x.getPersonalusuario().getUsuario();
            if (!aux.contains(nombre)) {
                aux.add(nombre);
            }
        }
        Collections.sort(aux);
        return toJsonString(Response.Status.OK, aux);
    }

    @POST
    @Path("AUTH")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response authenticateUser(String input) {

        try {

            ObjectMapper mapper = new JsonMapper();
            Credentials credentials = mapper.readValue(input, Credentials.class);

            String username = credentials.getUsername();
            String password = credentials.getPassword();

            // Authenticate the user using the credentials provided
            Personal p = authenticate(username, password);
            credentials.setAccessLevel(p.getPuestoTrabajonombrePuesto().getNivelAcceso());

            // Issue a token for the user
            String token = issueToken(credentials);

            // Return the token on the response
            return Response.ok(token).build();

        } catch (CredentialException ex) {
            return Response.status(Response.Status.FORBIDDEN).entity(ex.getMessage()).build();
        } catch (JsonProcessingException ex) {
            return Response.status(Response.Status.UNSUPPORTED_MEDIA_TYPE).entity(ex.getMessage()).build();
        } catch (InternalServerErrorException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    private Personal authenticate(String username, String password) throws CredentialException, InternalServerErrorException {
        List<Personal> list = super.findAll();

        for (Personal x : list) {
            if (x.getUsuario().equals(username)) {
                if (getSHA256(x.getContrasenna()).equals(password)) {
                    if (!x.getOnline()) {
                        return x;
                    } else {
                        throw new CredentialExpiredException("Usuario en linea");
                    }
                }
                throw new CredentialException("Credenciales incorrectas");
            }
        }
        throw new CredentialNotFoundException("Credenciales no encontradas");
    }

    private String issueToken(Credentials credentials) {
        // Issue a token (can be a random String persisted to a database or a JWT token)
        // The issued token must be associated to a user
        // Return the issued token
        Random random = new SecureRandom();
        String token = new BigInteger(121, random).toString(32);
        for (String s : tokens.keySet()) {
            if (tokens.get(s).getUsername().equals(credentials.getUsername())) {
                return s;
            }
        }
        tokens.put(token, credentials);
        return token;
    }

    private String getSHA256(String stringToConvert) throws InternalServerErrorException {
        try {
            byte[] bytes = MessageDigest.getInstance("SHA-256").digest(stringToConvert.getBytes());
            return String.format("%064x", new BigInteger(1, bytes));
        } catch (NoSuchAlgorithmException ex) {
            throw new InternalServerErrorException("Error con algoritmo Hash");
        }
    }

}
