/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jobits.pos.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobits.pos.authentication.Secured;
import com.jobits.pos.persistence.Venta;
import com.jobits.pos.controllers.VentaResumenController;
import com.jobits.pos.persistence.models.VentaResumenModel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.ws.handler.MessageContext;
import com.jobits.utils.R;

/**
 * FirstDream
 *
 * @author Jorge
 *
 */
@Path("venta")
public class VentaFacadeREST extends AbstractFacade<Venta> {

    @PersistenceContext(unitName = "Restaurant_Manager_Web_ServicePU")
    private EntityManager em;

    private Date d;
    SimpleDateFormat Format = new SimpleDateFormat("dd'/'MM'/'yy"),
            hour = new SimpleDateFormat(" hh ':' mm ' ' a ");

    public VentaFacadeREST() {
        super(Venta.class);
    }

    /**
     * <h3> Metodo para devolver el resumen general de las ventas de un dia en
     * especifico </h3>
     * este metodo devuelve un json que es necesario parsearlo
     * metodo con nivel 3 de seguridad
     *
     * @param fecha - la fecha que se pasa por parametro debe estar en el
     * formato  <h3>dd/mm/aaaa</h3>
     * @return un objeto de tipo {@link VentaResumenModel} convertido a json
     */
    @RolesAllowed("3")
    @POST
    @Path("SALES")
    @Secured
    @Consumes(MediaType.TEXT_PLAIN)
    public Response getResumenVentas(String fecha) {
        Venta v;
        try {
            v = find(R.DATE_FORMAT.parse(fecha));
            if (v == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("No existe una venta registrada en la fecha seleccionada").build();
            }
            return Response.ok(new ObjectMapper().writeValueAsString(VentaResumenController.createResumenFromVenta(v))).build();
        } catch (ParseException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Formato de entrada incorrecto").build();
        } catch (JsonProcessingException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error en el Object Mapper. Contacte con soporte").build();
        }

    }

    @GET
    @Path("ip")
    @Consumes(MediaType.TEXT_PLAIN)
    public String ip() {
        MessageContext messageContext = webServiceContext.getMessageContext();
        HttpServletRequest request = (HttpServletRequest) messageContext.get(MessageContext.SERVLET_REQUEST);
        String callerIpAddress = request.getRemoteAddr();

        return ("Caller IP = " + callerIpAddress);
    }

    @GET
    @Path("START")
    @Produces(MediaType.TEXT_PLAIN)
    public String addVenta() {
        super.create(new Venta(d));
        return "1";
    }

    @GET
    @Path("date")
    @Produces(MediaType.TEXT_PLAIN)
    public String getToday() {
        return Format.format(new Date());
    }

    @GET
    @Path("hour")
    @Produces(MediaType.TEXT_PLAIN)
    public String getHour() {
        return hour.format(new Date());
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Venta find(@PathParam("id") Date id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Venta> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Venta> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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
