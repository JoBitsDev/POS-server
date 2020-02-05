/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jobits.pos.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobits.pos.authentication.Secured;
import com.jobits.pos.persistence.Cocina;
import com.jobits.pos.persistence.Impresora;
import com.jobits.pos.persistence.NotificacionEnvioCocina;
import com.jobits.pos.notificationdelivery.Notificable;
import com.jobits.pos.notificationdelivery.Notificador;
import com.jobits.pos.persistence.models.ProductoVentaOrdenModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

/**
 * FirstDream
 *
 * @author Jorge
 *
 */
@Path("notificacion/")
public class NotificacionEnvioCocinaFacadeREST extends AbstractFacade<NotificacionEnvioCocina> {

    @PersistenceContext(unitName = "Restaurant_Manager_Web_ServicePU")
    private EntityManager em;

    public NotificacionEnvioCocinaFacadeREST() {
        super(NotificacionEnvioCocina.class);
    }

    @Secured
    @RolesAllowed("0")
    @GET
    @Path("PENDING")
    public Response showPending(@QueryParam("codCocina") String codCocina, @Context HttpServletRequest inRequest) {
        registerDevice(inRequest, codCocina);
        List<ProductoVentaOrdenModel> ret = new ArrayList<>();
        List<NotificacionEnvioCocina> all = findAll();
        for (NotificacionEnvioCocina x : all) {
            if (x.getProductovOrden().getOrden().getHoraTerminada() != null) {
                em1.getTransaction().begin();
                super.remove(x);
                em1.getTransaction().commit();
                continue;
            }
            if (x.getCocina().getCodCocina().equals(codCocina)) {
                x.getProductovOrden().setCantidad(x.getCantidad());
                ret.add(addProductoVentaOrdenModel(x));
            }
        }
        return toJsonString(Response.Status.OK, ret);
    }

    @Secured
    @RolesAllowed("0")
    @POST
    @Path("NOTIFY")
    public Response notifyCocina(String hashMap) {
        HashMap<String, Object> values;
        try {
            values = new ObjectMapper().readValue(hashMap, HashMap.class);
        } catch (JsonProcessingException ex) {
            return handleException(ex);
        }

        String codOrden = values.get("codOrden").toString();
        String codProducto = values.get("codProducto").toString();

        for (NotificacionEnvioCocina x : findAll()) {
            if (x.getNotificacionEnvioCocinaPK().getProductovOrdenordencodOrden().equals(codOrden)
                    && x.getNotificacionEnvioCocinaPK().getProductovOrdenproductoVentapCod().equals(codProducto)) {
                x.getProductovOrden().setListoParaRecoger(Boolean.TRUE);
                x.getProductovOrden().setEnviadosacocina(x.getProductovOrden().getCantidad());
                super.em1.getTransaction().begin();
                super.em1.merge(x.getProductovOrden());
                super.em1.getTransaction().commit();
                super.remove(x);
                new Notificador(x.getIpDependiente(), new Notificable() {
                    @Override
                    public String getMensajeNotificacion() {
                        return "Productos a recoger en " + x.getProductovOrden().getProductoVenta().getCocinacodCocina().getNombreCocina();
                    }

                    @Override
                    public String getTituloNotificacion() {
                        return "Restaurant Manager";
                    }

                    @Override
                    public String getDescripcionNotificacion() {
                        return x.getCantidad() + " de " + x.getProductovOrden().getProductoVenta().getNombre();
                    }
                }).notificar();
                return toJsonString(Response.Status.OK, "Notificacion Exitosa");

            }
        }
        return toJsonString(Response.Status.NOT_MODIFIED, "Los parámetros no son válidos o la notificación ya fue enviada \n "
                + "o el receptor no esta conectado");
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    private void registerDevice(HttpServletRequest inRequest, String cod_cocina) {
        boolean founded = false;
        Cocina c = em1.find(Cocina.class, cod_cocina);
        Impresora imp = null;
        String host = inRequest.getRemoteHost();
        for (Impresora i : c.getImpresoraList()) {
            if (!i.getIpImpresora().equals(host)) {
                i.setIpImpresora(host);
            }
            imp = i;
            founded = true;
        }
        if (!founded) {
            Impresora i = new Impresora();
            i.setCocinacodCocina(c);
            i.setEstaactiva(true);
            i.setIpImpresora(host);
            i.setNombreImpresora("Device");
            i.setCodImpresora("D-" + c.getCodCocina());
            em1.getTransaction().begin();
            em1.persist(i);
            em1.getTransaction().commit();
        } else {
            em1.getTransaction().begin();
            em1.merge(imp);
            em1.getTransaction().commit();
        }
    }

    private void logOutDevice(HttpServletRequest inRequest, String cod_cocina) {
        //TODO : finish
    }

    private ProductoVentaOrdenModel addProductoVentaOrdenModel(NotificacionEnvioCocina x) {
        return new ProductoVentaOrdenModel(x.getProductovOrden().getEnviadosacocina()
                , x.getProductovOrden().getProductovOrdenPK()
                , x.getCantidad()
                , x.getProductovOrden().getOrden()
                , x.getProductovOrden().getProductoVenta()
                , x.getProductovOrden().getNumeroComensal()
                , x.getProductovOrden().getOrden().getMesacodMesa()
                ,x.getProductovOrden().getNota() == null ? "" : x.getProductovOrden().getNota().getDescripcion());
    }

}
