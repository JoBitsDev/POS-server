/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jobits.pos.authentication;

import com.jobits.pos.service.PersonalFacadeREST;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Priority;
import javax.annotation.security.RolesAllowed;
import javax.security.auth.login.CredentialException;
import javax.security.auth.login.CredentialNotFoundException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

/**
 * Esta clase en un filtro que cualquier metodo que tenga @secured pasa por este
 * filtro
 *
 * @author Jorge
 *
 */
@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    @Context
    private ResourceInfo resourceInfo;

    private static final String AUTHENTICATION_SCHEME = "Bearer";

    /**
     * esto es lo que se ejecuta antes de entrar en cualquier metodo que este
     * anotado con secure
     *
     * @param requestContext
     * @throws IOException
     */
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        // Obtiene el header para parsear el token
        String authorizationHeader
                = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        // Chequea que el header para ver si no esta vacio
        if (!isTokenBasedAuthentication(authorizationHeader)) {
            abortWithUnauthorized(requestContext, "Token no válido");
            return;
        }

        // Obtiene el token
        String token = authorizationHeader
                .substring(AUTHENTICATION_SCHEME.length()).trim();

        try {

            //obtiene el rol que se puede anotar a un metodo
            List<String> rolesSet = Arrays.asList(resourceInfo.getResourceMethod().getAnnotation(RolesAllowed.class).value());

            // Valida el token con un rol
            validateToken(token, rolesSet);

        } catch (CredentialException e) {
            abortWithUnauthorized(requestContext, e.getMessage());
        }
    }

    /**
     * Chequea si el header es valido. Esto se cumple si: - No debe ser nulo -
     * Debe tener en el encabezado "Bearer" mas un espacio en blanco. - el token
     * es case-insentivie.(da lo mismo que en mayuscula que en minuscula).
     *
     * @param authorizationHeader
     * @return
     */
    private boolean isTokenBasedAuthentication(String authorizationHeader) {
        // 
        return authorizationHeader != null && authorizationHeader.toLowerCase()
                .startsWith(AUTHENTICATION_SCHEME.toLowerCase() + " ");
    }

    /**
     * Este metodo para la ejecucion y devuelve un error 401 (UNAUTHORIZED)
     *
     * @param requestContext
     */
    private void abortWithUnauthorized(ContainerRequestContext requestContext, String message) {
        requestContext.abortWith(
                Response.status(Response.Status.UNAUTHORIZED)
                        .entity(message)
                        .build());
    }

    /**
     * Metodo para validar el token.si el metodo se ejecuta correctamente todo
     * ok. de lo contrario lanza excepcion
     *
     * @param token el token por parametro
     * @param roleSet la lista de roles que tiene como anotacion el metodo que
     * se ejecutara despues
     * @throws CredentialException
     */
    private void validateToken(String token, List<String> roleSet) throws CredentialException {
        //TODO: tiempo de espera del token
        Credentials c = getCredentialsFromToken(token);
        for (String s : roleSet) {
            int i = Integer.parseInt(s);
            if (i > c.getAccessLevel()) {
                throw new CredentialException("Acceso denegado");
            }
        }
    }

    public static Credentials getCredentialsFromToken(String token) throws CredentialNotFoundException {
        Credentials c = PersonalFacadeREST.tokens.get(token);
        if (c == null) {
            throw new CredentialNotFoundException("Credenciales no encontradas");
        }
        return c;
    }
}
