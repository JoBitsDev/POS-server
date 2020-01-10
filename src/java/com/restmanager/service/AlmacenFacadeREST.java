/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.restmanager.service;

import com.restmanager.Almacen;
import com.restmanager.Cocina;
import com.restmanager.Insumo;
import com.restmanager.InsumoAlmacen;
import com.restmanager.Ipv;
import com.restmanager.controller.TransaccionController;
import com.restmanager.printservice.Impresion;
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
@Path("com.restmanager.almacen")
public class AlmacenFacadeREST extends AbstractFacade<Almacen> {

    @PersistenceContext(unitName = "Restaurant_Manager_Web_ServicePU")
    private EntityManager em;

    public AlmacenFacadeREST() {
        super(Almacen.class);

    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Almacen entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") String id, Almacen entity) {
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
    public List<InsumoAlmacen> find(@PathParam("id") String id) {
        return super.find(id).getInsumoAlmacenList();
    }

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<InsumoAlmacen> findFirst() {
        return super.findAll().get(0).getInsumoAlmacenList();
    }

    @GET
    @Path("FILTRAR_{codCocina}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<InsumoAlmacen> filterBy(@PathParam("codCocina") String codCocina) {
        List<InsumoAlmacen> lista = super.findAll().get(0).getInsumoAlmacenList();
        List<InsumoAlmacen> ret = new ArrayList<>();
        for (InsumoAlmacen x : lista) {
            for (Ipv v : x.getInsumo().getIpvList()) {
                if (v.getCocina().getCodCocina().equals(codCocina)) {
                    ret.add(x);
                }
            }
        }

        return ret;
    }

    @GET
    @Path("ENTRADA_{almacenCod}_{insumoCod}_{cant}_{valor}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public String entrada(@PathParam("almacenCod") String almacenCod,
            @PathParam("insumoCod") String insumoCod,
            @PathParam("cant") float cant,
            @PathParam("valor") float valor) {
        return "1";
//   return new TransaccionController(em1).addTransaccionEntrada(em1.find(Insumo.class, insumoCod), findVenta().getFecha(), new Date(), super.find(almacenCod), cant, valor).toString();

    }

    @GET
    @Path("IMPRIMIR_ESTADO_ALMACEN")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public String ticketEntrada() {
        Impresion i = Impresion.getDefaultInstance();
        i.printResumenAlmacen(super.findAll().get(0));
        return "1";
    }

    @GET
    @Path("IMPRIMIR_TICKET_COMPRA")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public String ticketCompra() {
        Impresion i = Impresion.getDefaultInstance();
        i.printTicketCompra(super.findAll().get(0));
        return "1";
    }

    @GET
    @Path("SALIDA_{almacenCod}_{insumoCod}_{cant}_{destino}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public String salida(@PathParam("almacenCod") String almacenCod,
            @PathParam("insumoCod") String insumoCod,
            @PathParam("cant") float cant,
            @PathParam("destino") String destino) {
        return new TransaccionController(em1).addTransaccionSalida(em1.find(Insumo.class, insumoCod), findVenta().getFecha(), new Date(),
                super.find(almacenCod), em1.find(Cocina.class, destino.substring(destino.length() - 4, destino.length() - 1)), cant).toString();

    }

    @GET
    @Path("MERMAR_{almacenCod}_{insumoCod}_{cant}_{razon}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public String rebaja(@PathParam("almacenCod") String almacenCod,
            @PathParam("insumoCod") String insumoCod,
            @PathParam("cant") float cant,
            @PathParam("razon") String razon) {
        return new TransaccionController(em1).addTransaccionRebaja(em1.find(Insumo.class, insumoCod), findVenta().getFecha(), new Date(),
                super.find(almacenCod), cant, razon).toString();

    }

    @GET
    @Path("IPVS_{insumoCod}")
    @Produces({MediaType.TEXT_PLAIN})
    public String getIPVS(@PathParam("insumoCod") String codInsumo) {
        em1 = e.createEntityManager();
        ArrayList<Ipv> ipvs = new ArrayList<>(em1.createNamedQuery("Ipv.findByInsumocodInsumo")
                .setParameter("insumocodInsumo", codInsumo)
                .getResultList());

        String cocinas = "";

        for (Ipv ipv : ipvs) {
            cocinas += ipv.getCocina() + ",";
        }

        return cocinas.isEmpty() ? "" : cocinas.substring(0, cocinas.length() - 1);
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    @Override
    protected EntityManager getEntityManager() {
        return em1;
    }

}
