/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jobits.pos.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobits.pos.authentication.AuthenticationFilter;
import com.jobits.pos.authentication.Secured;
import com.jobits.pos.controllers.IPVController;
import com.jobits.pos.persistence.*;
import com.jobits.pos.notificationdelivery.Notificable;
import com.jobits.pos.notificationdelivery.Notificador;
import com.jobits.pos.printservice.Impresion;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import com.jobits.utils.R;
import java.util.HashMap;
import javax.annotation.security.RolesAllowed;
import javax.security.auth.login.CredentialNotFoundException;
import javax.ws.rs.core.Response;

/**
 *
 * @author Jorge
 */
@Path("orden/")
public class OrdenFacadeREST extends AbstractFacade<Orden> {

    SimpleDateFormat FormatDate = new SimpleDateFormat("MM'/'dd'/'yy");
    SimpleDateFormat FormatTime = new SimpleDateFormat(" hh ':' mm ' ' a ");
    IPVController ipvController = new IPVController(getEntityManager());
    private Date today = new Date();

    public static final String ESTADO_MESA_VACIA = "vacia",
            ESTADO_MESA_ESPERANDO_CONFIRMACION = "esperando confirmacion";

    public OrdenFacadeREST() {
        super(Orden.class);

    }

    @RolesAllowed("0")
    @GET
    @Secured
    public Response getOrden(@QueryParam("codOrden") String codOrden) {
        return toJsonString(Response.Status.OK, find(codOrden));
    }

    @RolesAllowed("0")
    @Secured
    @POST
    @Path("CREATE")
    public Response create(String codMesa, @Context HttpServletRequest inRequest) {
        Orden o = new Orden(ajustarNoOrden());
        Mesa m = getEntityManager().find(Mesa.class, codMesa);
        String usuarioTrabajando = null;
        try {
            usuarioTrabajando = AuthenticationFilter.getCredentialsFromToken(getToken(inRequest)).getUsername();
        } catch (CredentialNotFoundException ex) {
            return toJsonString(Response.Status.NOT_FOUND, "Credenciales no encontradas");
        }
        Personal p = getEntityManager().find(Personal.class, usuarioTrabajando);

        Venta v = findVenta();

        if (v == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("El Cajero debe comenzar el dia de trabajo para crear ordenes").build();
        }

        m.setEstado(o.getCodOrden() + " " + usuarioTrabajando);
        o.setMesacodMesa(m);
        o.setPersonalusuario(p);
        o.setVentafecha(v);
        o.setDeLaCasa(false);
        o.setHoraComenzada(new Date());
        o.setOrdenvalorMonetario(Float.valueOf("0"));
        o.setPorciento(m.getAreacodArea().getPorcientoPorServicio().floatValue());

        em1.getTransaction().begin();
        super.create(o);
        em1.getTransaction().commit();
        return toJsonString(Response.Status.OK, o);
    }

    @RolesAllowed("0")
    @Secured
    @GET
    @Path("VALIDATE")
    public Response isValid(@QueryParam("codOrden") String codOrden) {
        Orden o = super.find(codOrden);

        if (o != null) {
            if (o.getHoraTerminada() != null) {
                Mesa m = o.getMesacodMesa();
                m.setEstado(ESTADO_MESA_VACIA);

                em1.getTransaction().begin();
                em1.merge(m);
                em1.flush();
                if (em1.getTransaction().isActive()) {
                    em1.getTransaction().commit();
                }
                return toJsonString(Response.Status.GONE, "La mesa ya no se encuentra abierta");
            } else {
                return toJsonString(Response.Status.OK, "Orden validada");
            }
        } else {
            return toJsonString(Response.Status.GONE, "La orden se elimino de manera inesperada");
        }
    }

    @RolesAllowed("0")
    @Secured
    @POST
    @Path("ADD")
    public Response addProducto(String hashMap) {
        HashMap<String, Object> values;
        try {
            values = new ObjectMapper().readValue(hashMap, HashMap.class);
        } catch (JsonProcessingException ex) {
            return handleException(ex);
        }

        String codOrden = values.get("codOrden").toString();
        String codProducto = values.get("codProducto").toString();
        float cantidad = Float.parseFloat(values.get("cantidad").toString());

        Orden o = super.find(codOrden);
        ProductoVenta producto = getEntityManager().find(ProductoVenta.class, codProducto);
        ArrayList<ProductovOrden> po = new ArrayList<>(o.getProductovOrdenList());
        ProductovOrden founded = null;
        int contains = -1;
        if (producto.getCocinacodCocina().getLimitarVentaInsumoAgotado()) {
            if (!ipvController.hayDisponibilidad(producto, findVenta().getFecha(), cantidad)) {
                return toJsonString(Response.Status.EXPECTATION_FAILED, "No hay suficiente " + producto + "para elaborar. el producto se marcara como no visible");
            }
        }
        if (!po.isEmpty()) {
            for (int i = 0; contains == -1 && i < po.size(); i++) {
                if (po.get(i).getProductoVenta().getPCod().equals(codProducto)) {
                    contains = i;
                }
            }
        }

        if (contains != -1) {
            founded = po.get(contains);
            float cant = founded.getCantidad();
            founded.setCantidad(cant + cantidad);

        } else {
            founded = new ProductovOrden(codProducto, codOrden);
            founded.setOrden(o);
            founded.setProductoVenta(producto);
            founded.setCantidad(cantidad);
            founded.setEnviadosacocina((float) 0);
            founded.setNumeroComensal(0);

            //em.persist(aux);
            po.add(founded);

        }
        o.setProductovOrdenList(po);
        o.setOrdenvalorMonetario(calcularValorTotal(o));
        if (o.getDeLaCasa()) {
            ipvController.consumirPorLaCasa(founded, cantidad);
        } else {
            ipvController.consumir(founded, cantidad);
        }
        super.edit(o);
        return toJsonString(Response.Status.OK, o);
    }//TODO: Respuesta del servidor incorrecta

    @RolesAllowed("0")
    @Secured
    @POST
    @Path("REMOVE")
    public Response removeProducto(String hashMap) {

        HashMap<String, Object> values;
        try {
            values = new ObjectMapper().readValue(hashMap, HashMap.class);
        } catch (JsonProcessingException ex) {
            return handleException(ex);
        }

        String codOrden = values.get("codOrden").toString();
        String codProducto = values.get("codProducto").toString();
        float cantidad = Float.parseFloat(values.get("cantidad").toString());

        Orden o = super.find(codOrden);

        ArrayList<ProductovOrden> po = new ArrayList<>(o.getProductovOrdenList());
        int contains = -1;

        for (int i = 0; contains == -1 && i < po.size(); i++) {
            if (po.get(i).getProductoVenta().getPCod().equals(codProducto)) {
                contains = i;
            }
        }

        if (contains != -1) {
            ProductovOrden p = po.get(contains);
            float cant = p.getCantidad();
            if (cant > cantidad) {
                p.setCantidad(cant - cantidad);

            } else {
                po.get(contains).setCantidad(0);
                Impresion i = new Impresion();
                i.printCancelationTicket(o);
                po.remove(contains);
                getEntityManager().getTransaction().begin();
                p = getEntityManager().find(ProductovOrden.class, p.getProductovOrdenPK());
                getEntityManager().remove(p);
                getEntityManager().getTransaction().commit();

            }
            o.setProductovOrdenList(po);
            o.setOrdenvalorMonetario(calcularValorTotal(o));
            if (o.getDeLaCasa()) {
                ipvController.devolverPorLaCasa(p, cantidad);
            } else {
                ipvController.devolver(p, cantidad);
            }
            super.edit(o);
        }//TODO: aqui hay que disminuir tambien los contadores para enviado a cocina junto con los contadores de cantidad

        return toJsonString(Response.Status.OK, o);
    }//TODO: METODoS ARCAICOS'

    @RolesAllowed("0")
    @Secured
    @POST
    @Path("FINISH")
    public Response finish(String codOrden) {

        Orden o = super.find(codOrden);
        Mesa m = getEntityManager().find(Mesa.class, o.getMesacodMesa().getCodMesa());

        for (ProductovOrden x : o.getProductovOrdenList()) {

            if (R.TABLETS_EN_COCINA) {
                if (!x.getNotificacionEnvioCocinaList().isEmpty()) {
                    return toJsonString(Response.Status.PRECONDITION_FAILED, "Existen Productos que faltan por enviar a elaborar. Envie a elaborar");
                }
            }
//            if (Impresion.getDefaultInstance().IMPRIMIR_TICKET_COCINA) {
            if (x.getCantidad() > x.getEnviadosacocina()) {
                return toJsonString(Response.Status.PRECONDITION_FAILED, "Existen Productos que faltan por enviar a elaborar. Envie a elaborar");
            }

            //         }
        }

        o.setHoraTerminada(new Date());
        Impresion i = new Impresion();
        i.print(o, false);

        m.setEstado(ESTADO_MESA_VACIA);

        o.setOrdengastoEninsumos(calcularGastoTotal(o));
        o.setOrdenvalorMonetario(calcularValorTotal(o));

        em1.getTransaction().begin();
        em1.merge(m);
        em1.flush();
        if (em1.getTransaction().isActive()) {
            em1.getTransaction().commit();
        }
        super.edit(o);

        return toJsonString(Response.Status.OK, "Orden cerrada exitosamente");
    }//TODO: METODoS ARCAICOS

    @RolesAllowed("0")
    @Secured
    @POST
    @Path("ENVIAR-COCINA")
    public Response enviarACocina(String codOrden, @Context HttpServletRequest inRequest) {
        Orden o = super.find(codOrden);
        Mesa m = getEntityManager().find(Mesa.class, o.getMesacodMesa().getCodMesa());
        boolean notificacionEnviada = true;
        if (R.TABLETS_EN_COCINA) {
            notificacionEnviada = false;
            for (ProductovOrden x : o.getProductovOrdenList()) {
                if (x.getEnviadosacocina() < x.getCantidad()) {
                    NotificacionEnvioCocinaPK notPK = new NotificacionEnvioCocinaPK();
                    notPK.setCocinacodCocina(x.getProductoVenta().getCocinacodCocina().getCodCocina());
                    notPK.setProductovOrdenordencodOrden(o.getCodOrden());
                    notPK.setProductovOrdenproductoVentapCod(x.getProductoVenta().getPCod());
                    NotificacionEnvioCocina not = super.em1.find(NotificacionEnvioCocina.class, notPK);
                    boolean exist = true;
                    if (not == null) {
                        not = new NotificacionEnvioCocina(notPK);
                        exist = false;
                    }
                    not.setCocina(x.getProductoVenta().getCocinacodCocina());
                    not.setHoraNotificacion(new Date());
                    not.setProductovOrden(x);
                    not.setIpDependiente(inRequest.getRemoteHost());
                    super.em1.getTransaction().begin();
                    if (exist) {
                        not.setCantidad(not.getCantidad() + (x.getCantidad() - x.getEnviadosacocina()));
                        super.em1.merge(not);
                    } else {
                        not.setCantidad(x.getCantidad() - x.getEnviadosacocina());
                        super.em1.persist(not);
                    }

                    notificacionEnviada = enviarNotificacion(not);
                    //if (enviarNotificacion(not)) {
                    super.em1.getTransaction().commit();
                    //} else {
                    //  super.em1.getTransaction().rollback();
                    // }
                    if (!Impresion.getDefaultInstance().IMPRIMIR_TICKET_COCINA) {
                        x.setEnviadosacocina(x.getCantidad());
                    }
                    x.setListoParaRecoger(false);
                }
            }
        }
        if (Impresion.getDefaultInstance().IMPRIMIR_TICKET_COCINA) {
            Impresion i = new Impresion();
            i.printKitchen(o);
        }

        super.edit(o);

        return notificacionEnviada ? toJsonString(Response.Status.OK, o) : toJsonString(Response.Status.EXPECTATION_FAILED, "La notificacion no pudo ser enviada porque el destinatario no pudo ser contactado o tiene el servicio desactivado. Notifique manualmente");
    }//TODO: METODoS ARCAICOS

    @POST
    @RolesAllowed("0")
    @Secured
    @Path("ADD-NOTA")
    @Produces(MediaType.TEXT_PLAIN)
    public Response addNota(String hashMap) {
        HashMap<String, Object> values;
        try {
            values = new ObjectMapper().readValue(hashMap, HashMap.class);
        } catch (JsonProcessingException ex) {
            return handleException(ex);
        }

        String codOrden = values.get("codOrden").toString();
        String pCod = values.get("codProd").toString();
        String nota = values.get("nota").toString();

        Orden o = super.find(codOrden);
        ProductovOrden pv = null;
        nota = nota.replace('%', ' ');

        for (ProductovOrden x : o.getProductovOrdenList()) {
            if (x.getProductoVenta().getPCod().equals(pCod)) {
                if (x.getNota() == null) {
                    NotaPK notaPk = new NotaPK(pCod, codOrden);
                    Nota newNota = new Nota(notaPk);
                    newNota.setDescripcion(nota);
                    x.setNota(newNota);
                    pv = x;
                } else {
                    x.getNota().setDescripcion(nota);
                    pv = x;
                }
            }

        }
        em1.getTransaction().begin();
        em1.merge(pv.getNota());
        em1.merge(pv);
        em1.getTransaction().commit();
        super.edit(o);

        return toJsonString(Response.Status.OK, "Notificacion exitosa");
    }//TODO: METODoS ARCAICOS

    @RolesAllowed("0")
    @Secured
    @GET
    @Path("GET-NOTA")
    public Response getNota(@QueryParam("codOrden") String codOrden, @QueryParam("codProd") String pCod) {
        Orden o = super.find(codOrden);
        for (ProductovOrden x : o.getProductovOrdenList()) {
            if (x.getProductoVenta().getPCod().equals(pCod)) {
                if (x.getNota() == null) {
                    return toJsonString(Response.Status.OK, "");
                }
                return toJsonString(Response.Status.OK, x.getNota().getDescripcion());
            }

        }
        return toJsonString(Response.Status.OK, "");

    }

    @RolesAllowed("0")
    @Secured
    @GET
    @Path("GET-COMENSAL")
    public Response getComensal(
            @QueryParam("codOrden") String codOrden,
            @QueryParam("codProd") String pCod) {
        Orden o = super.find(codOrden);

        for (ProductovOrden x : o.getProductovOrdenList()) {
            if (x.getProductoVenta().getPCod().equals(pCod)) {
                if (x.getNumeroComensal() == null) {
                    return toJsonString(Response.Status.OK, 0);
                }
                return toJsonString(Response.Status.OK, x.getNumeroComensal());
            }

        }
        return toJsonString(Response.Status.OK, 0);

    }//TODO: METODoS ARCAICOS

    @RolesAllowed("0")
    @Secured
    @POST
    @Path("ADD-COMENSAL")
    public Response addComensal(
            String hashMap) {

        HashMap<String, Object> values;
        try {
            values = new ObjectMapper().readValue(hashMap, HashMap.class);
        } catch (JsonProcessingException ex) {
            return handleException(ex);
        }

        String codOrden = values.get("codOrden").toString();
        String pCod = values.get("codProd").toString();
        int numero = Integer.parseInt(values.get("comensal").toString());

        Orden o = super.find(codOrden);
        ProductovOrden pv = null;
        for (ProductovOrden x : o.getProductovOrdenList()) {
            if (x.getProductoVenta().getPCod().equals(pCod)) {
                x.setNumeroComensal(numero);
                pv = x;
            }
        }

        em1.getTransaction().begin();
        em1.merge(pv);
        em1.getTransaction().commit();
        super.edit(o);
        return toJsonString(Response.Status.OK, "Exito");

    }//TODO: METODoS ARCAICOS

    @RolesAllowed("0")
    @Secured
    @POST
    @Path("SET-DE-LA-CASA")
    public Response setDeLaCasa(String hashMap) {
        HashMap<String, Object> values;
        try {
            values = new ObjectMapper().readValue(hashMap, HashMap.class);
        } catch (JsonProcessingException ex) {
            return handleException(ex);
        }
        String codOrden = values.get("codOrden").toString();
        boolean deLaCasa = Boolean.parseBoolean(values.get("deLaCasa").toString());

        Orden o = super.find(codOrden);
        o.setDeLaCasa(deLaCasa);
        super.edit(o);
        if (deLaCasa) {
            ipvController.consumirPorLaCasa(o.getProductovOrdenList());
        } else {
            ipvController.devolverPorLaCasa(o.getProductovOrdenList());
        }
        return toJsonString(Response.Status.OK, o);
    }//TODO: METODoS ARCAICOS

    @RolesAllowed("0")
    @Secured
    @POST
    @Path("MOVER-A-MESA")
    public Response setMesa(String hashMap) {
        HashMap<String, Object> values;
        try {
            values = new ObjectMapper().readValue(hashMap, HashMap.class);
        } catch (JsonProcessingException ex) {
            return handleException(ex);
        }

        String codOrden = values.get("codOrden").toString();
        String codMesa = values.get("codMesa").toString();

        getEntityManager().getTransaction().begin();
        Orden o = super.find(codOrden);
        Mesa mesaDestino = getEntityManager().find(Mesa.class, codMesa);
        Mesa mesaOrigen = o.getMesacodMesa();
        if (mesaDestino.getEstado().equals("ocupada")) {
            getEntityManager().getTransaction().rollback();
            return toJsonString(Response.Status.BAD_REQUEST, "La mesa de destino esta ocupada");
        }
        mesaDestino.setEstado(o.getCodOrden() + " " + o.getPersonalusuario().getUsuario());
        getEntityManager().merge(mesaDestino);
        o.setMesacodMesa(mesaDestino);
        if (mesaDestino.getAreacodArea().getPorcientoPorServicio() != null) {
            o.setPorciento(mesaDestino.getAreacodArea().getPorcientoPorServicio().floatValue());
        }
        mesaOrigen.setEstado(ESTADO_MESA_VACIA);
        getEntityManager().merge(mesaOrigen);
        super.edit(o);
        getEntityManager().getTransaction().commit();

        return toJsonString(Response.Status.OK, "Cambiado correctamente");
    }//TODO: METODoS ARCAICOS

    @RolesAllowed("0")
    @Secured
    @POST
    @Path("CEDER-ORDEN")
    public Response setUsuario(String hashMap) {
        HashMap<String, Object> values;
        try {
            values = new ObjectMapper().readValue(hashMap, HashMap.class);
        } catch (JsonProcessingException ex) {
            return handleException(ex);
        }

        String codOrden = values.get("codOrden").toString();
        String usuario = values.get("usuario").toString();

        getEntityManager().getTransaction().begin();
        Orden o = super.find(codOrden);
        Personal personalDestino = getEntityManager().find(Personal.class, usuario);

        o.setPersonalusuario(personalDestino);
        o.getMesacodMesa().setEstado(o.getCodOrden() + " " + personalDestino.getUsuario());
        getEntityManager().merge(o.getMesacodMesa());
        super.edit(o);

        getEntityManager().getTransaction().commit();
        return toJsonString(Response.Status.OK, "Exito");
    }//TODO: METODoS ARCAICOS

    @RolesAllowed("0")
    @Secured
    @GET
    @Path("FETCH")
    public Response siguientNoOrden() {
        Configuracion c = getEntityManager().find(Configuracion.class, "O");
        int ret = c.getValor();
        c.setValor(ret + 1);
        getEntityManager().persist(c);
        String r = "O-" + ret;
        return toJsonString(Response.Status.OK, r);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em1;
    }

    private String ajustarNoOrden() {

        try {
            String numeroOrdenNuevo = new ObjectMapper().readValue(siguientNoOrden().getEntity().toString(), String.class);
            boolean existe = super.find(numeroOrdenNuevo) != null;

            while (existe) {
                numeroOrdenNuevo = new ObjectMapper().readValue(siguientNoOrden().getEntity().toString(), String.class);
                existe = super.find(numeroOrdenNuevo) != null;

            }
            return numeroOrdenNuevo;
        } catch (JsonProcessingException ex) {
            handleException(ex);
        }
        return null;
    }

    private float calcularValorTotal(Orden o) {
        float ordenValorMonetario = 0;

        for (ProductovOrden x : o.getProductovOrdenList()) {
            ordenValorMonetario += x.getProductoVenta().getPrecioVenta() * x.getCantidad();
        }

        if (o.getPorciento() != 0) {
            if (!o.getDeLaCasa()) {
                ordenValorMonetario += (o.getPorciento() / 100) * ordenValorMonetario;
            }
        }

        return ordenValorMonetario;

    }

    private float calcularGastoTotal(Orden o) {
        float ordenGastosEnInsumos = 0;

        for (ProductovOrden x : o.getProductovOrdenList()) {
            ordenGastosEnInsumos += x.getProductoVenta().getGasto() * x.getCantidad();
        }
        return ordenGastosEnInsumos;
    }

    private boolean enviarNotificacion(NotificacionEnvioCocina c) {
        Notificador n;
        for (Impresora i : c.getCocina().getImpresoraList()) {
            if (i.getIpImpresora() != null) {
                n = new Notificador(i.getIpImpresora(), new Notificable() {
                    @Override
                    public String getMensajeNotificacion() {
                        return "Productos a elaborar " + c.getProductovOrden().getProductoVenta().getNombre();
                    }

                    @Override
                    public String getTituloNotificacion() {
                        return "Restaurant Manager";
                    }

                    @Override
                    public String getDescripcionNotificacion() {
                        return c.getCantidad() + " de " + c.getProductovOrden().getProductoVenta().getNombre();
                    }
                });
                n.notificar();
                return n.NOTIFICACION_ENVIADA;
            }
        }
        return false;

    }

}
