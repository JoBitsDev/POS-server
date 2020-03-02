/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jobits.pos.exceptionshandlers;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * FirstDream
 *
 * @author Jorge
 *
 */
@Provider
public class ExceptionHandler
        implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception e) {
        String response
                = "Tipo: " + e.getClass().toString() + ":\n"
                + e.getMessage() + "\n";
        for (StackTraceElement s : e.getStackTrace()) {
            if (s.getClassName().contains("com.jobits.pos.")) {
            response += "C:" + s.getClassName() + " L:" + s.getLineNumber() + "\n";
            }
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
    }
}
