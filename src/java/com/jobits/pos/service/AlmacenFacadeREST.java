/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jobits.pos.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobits.pos.authentication.Secured;
import com.jobits.pos.persistence.Almacen;
import com.jobits.pos.persistence.Cocina;
import com.jobits.pos.persistence.Insumo;
import com.jobits.pos.persistence.InsumoAlmacen;
import com.jobits.pos.persistence.Ipv;
import com.jobits.pos.controllers.TransaccionController;
import com.jobits.pos.persistence.IpvRegistro;
import com.jobits.pos.persistence.TransaccionEntrada;
import com.jobits.pos.persistence.TransaccionSalida;
import com.jobits.pos.printservice.Impresion;
import com.jobits.utils.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Jorge
 */
@Path("almacen/")
public class AlmacenFacadeREST extends AbstractFacade<Almacen> {

    @PersistenceContext(unitName = "Restaurant_Manager_Web_ServicePU")
    private EntityManager em;

    private final String PTO_ELAB = "ptoElab";

    public AlmacenFacadeREST() {
        super(Almacen.class);

    }

    @RolesAllowed("2")
    @Secured
    @GET
    public Response getPrimerAlmacen() {
        if (findAll().isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).entity("No existe un almacen principal. por favor cree uno.").build();
        }
        return toJsonString(Response.Status.OK, findAll().get(0).getInsumoAlmacenList());
    }

    /**
     * Metodo que filtra los insumos del almacen principal por una cocina
     * especifica
     *
     * @param codCocina el codigo de la cocina a filtrar
     * @return la lista de {@link InsumoAlmacen} que contienen esa cocina
     */
    @RolesAllowed("2")
    @GET
    @Secured
    @Path("FILTRAR")
    public Response filterBy(@QueryParam(PTO_ELAB) String codCocina) {
        if (codCocina == null) {
            return toJsonString(Response.Status.BAD_REQUEST, "Peticion no válida");
        }
        List<InsumoAlmacen> lista = super.findAll().get(0).getInsumoAlmacenList();
        List<InsumoAlmacen> ret = new ArrayList<>();
        for (InsumoAlmacen x : lista) {
            for (Ipv v : x.getInsumo().getIpvList()) {
                if (v.getCocina().getCodCocina().equals(codCocina)) {
                    ret.add(x);
                }
            }
        }
        return toJsonString(Response.Status.OK, ret);
    }

    @RolesAllowed("2")
    @POST
    @Path("ENTRADA")
    public Response entrada(String hashMap) {
        HashMap<String, Object> values;
        try {
            values = new ObjectMapper().readValue(hashMap, HashMap.class);
        } catch (JsonProcessingException ex) {
            return handleException(ex);
        }
        String almacenCod = (String) values.get("almacenCod");
        String insumoCod = (String) values.get("insumoCod");
        float cant = Float.parseFloat(values.get("cantidad").toString());
        float valor = Float.parseFloat(values.get("monto").toString());
        TransaccionController controller = new TransaccionController(em1);
        TransaccionEntrada entrada = controller.addTransaccionEntrada(null, em1.find(Insumo.class, insumoCod), findVenta().getFecha(), new Date(), super.find(almacenCod), cant, valor);

        return toJsonString(Response.Status.OK, entrada.getTransaccion()); //TODO cambiar a 200
    }

    @RolesAllowed("2")
    @Secured
    @GET
    @Path("IMPRIMIR-ESTADO-ALMACEN")
    public Response ticketEntrada() {
        Impresion i = Impresion.getDefaultInstance();
        i.printResumenAlmacen(super.findAll().get(0));//TODO: solo funcionando con el almacen 1;
        return toJsonString(Response.Status.OK, "Impresion Exitosa");
    }

    @RolesAllowed("2")
    @Secured
    @GET
    @Path("IMPRIMIR-TICKET-COMPRA")
    public Response ticketCompra() {
        Impresion i = Impresion.getDefaultInstance();
        i.printTicketCompra(super.findAll().get(0));
        return toJsonString(Response.Status.OK, "Impresion Exitosa");
    }

    @RolesAllowed("2")
    @Secured
    @POST
    @Path("SALIDA")
    public Response salida(String hashMap) {
        HashMap<String, Object> params;
        try {
            params = new ObjectMapper().readValue(hashMap, HashMap.class);
        } catch (JsonProcessingException ex) {
            return handleException(ex);
        }
        String almacenCod = (String) params.get("almacenCod");
        String insumoCod = (String) params.get("insumoCod");
        float cant = Float.parseFloat(params.get("cantidad").toString());
        String destino = (String) params.get("destino");
        TransaccionSalida salida = new TransaccionController(em1).addTransaccionSalida(null, em1.find(Insumo.class, insumoCod), findVenta().getFecha(), new Date(),
                super.find(almacenCod), em1.find(Cocina.class, destino), cant);
        return toJsonString(Response.Status.OK, salida.getTransaccion());

    }

    @RolesAllowed("4")
    @DELETE
    @Path("MERMAR_{almacenCod}_{insumoCod}_{cant}_{razon}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public String rebaja(@PathParam("almacenCod") String almacenCod,
            @PathParam("insumoCod") String insumoCod,
            @PathParam("cant") float cant,
            @PathParam("razon") String razon) {
        return new TransaccionController(em1).addTransaccionRebaja(null, em1.find(Insumo.class, insumoCod), findVenta().getFecha(), new Date(),
                super.find(almacenCod), cant, razon).toString();

    }

    @RolesAllowed("2")
    @Secured
    @GET
    @Path("IPVS-DE-INSUMO")
    public Response getIPVS(@QueryParam("insumoCod") String codInsumo) {
        em1 = e.createEntityManager();
        ArrayList<Ipv> ipvs = new ArrayList<>(em1.createNamedQuery("Ipv.findByInsumocodInsumo")
                .setParameter("insumocodInsumo", codInsumo)
                .getResultList());
        List<String> cocinas = new ArrayList<>();
        for (Ipv ipv : ipvs) {
            cocinas.add(ipv.getCocina().getCodCocina());
        }
        return toJsonString(Response.Status.OK, cocinas);
    }

    @RolesAllowed("2")
    @Secured
    @GET
    @Path("REGISTRO-IPVS")
    public Response getRegistroIpvs(@QueryParam(PTO_ELAB) String puntoElaboracion) {
        ArrayList<IpvRegistro> ret = new ArrayList<>(
                em1.createNamedQuery("IpvRegistro.findByIpvcocinacodCocinaAndFecha")
                        .setParameter("ipvcocinacodCocina", puntoElaboracion)
                        .setParameter("fecha", findVenta().getFecha())
                        .getResultList());
        for (IpvRegistro x : ret) {
            x.getIpvRegistroPK().setIpvinsumocodInsumo(x.getIpv().getInsumo().toString());
        }
        return toJsonString(Response.Status.OK, ret);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em1;
    }

}
