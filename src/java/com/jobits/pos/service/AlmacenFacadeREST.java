/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jobits.pos.service;

import com.jobits.pos.controllers.InsumoController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jobits.pos.authentication.Secured;
import com.jobits.pos.controllers.AlmacenController;
import com.jobits.pos.persistence.Almacen;
import com.jobits.pos.persistence.Cocina;
import com.jobits.pos.persistence.Insumo;
import com.jobits.pos.persistence.InsumoAlmacen;
import com.jobits.pos.persistence.Ipv;
import com.jobits.pos.controllers.TransaccionController;
import com.jobits.pos.persistence.InsumoAlmacenPK;
import com.jobits.pos.persistence.InsumoElaborado;
import com.jobits.pos.persistence.IpvRegistro;
import com.jobits.pos.persistence.Transaccion;
import com.jobits.pos.persistence.TransaccionEntrada;
import com.jobits.pos.persistence.TransaccionSalida;
import com.jobits.pos.persistence.TransaccionTransformacion;
import com.jobits.pos.persistence.models.TransformacionModel;
import com.jobits.pos.printservice.Impresion;
import com.jobits.utils.R;
import java.util.AbstractList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;

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

    private EntityManager em = e.createEntityManager();

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
        List<InsumoAlmacen> ret = new ArrayList<>(findAll().get(0).getInsumoAlmacenList());
        Collections.sort(ret);
        return toJsonString(Response.Status.OK, ret);
    }

    @RolesAllowed("2")
    @Secured
    @POST
    @Path("AGREGAR-INSUMO")
    public Response addIinsumo(String hashMap) {
        HashMap<String, Object> values;
        try {
            values = new ObjectMapper().readValue(hashMap, HashMap.class);
        } catch (JsonProcessingException ex) {
            return handleException(ex);
        }

        try {
            String insumoNombre = (String) values.get("insumoNombre");
            String um = (String) values.get("um");
            float estimacionStock = Float.parseFloat(values.get("estimacionStock").toString());
            startTransaction();
            InsumoController insController = new InsumoController(em);
            Insumo i = insController.create(insumoNombre, um, estimacionStock);

            if (insumoNombre.isEmpty() || um.isEmpty()) {
                return toJsonString(Response.Status.BAD_GATEWAY, "Valores vacios en nombre o unidad de medida");
            }
            if (estimacionStock < 0) {
                return toJsonString(Response.Status.BAD_REQUEST, "La estimacion del stock debe ser mayor que 0");
            }
            AlmacenController almacenController = new AlmacenController(em, findAll().get(0));
            almacenController.registrarInsumoEnAlmacen(i);
            commitTransaction();
        } catch (Exception e) {
            return handleException(e);
        }
        return toJsonString(Response.Status.OK, "Operacion exitosa");
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
        if (cant <= 0) {
            return toJsonString(Response.Status.BAD_REQUEST, "La cantidad de entrada no puede ser menor que 0");
        }
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
            String almacenCod = (String) params.get("almacenCod");
            String insumoCod = (String) params.get("insumoCod");
            float cant = Float.parseFloat(params.get("cantidad").toString());
            if (cant <= 0) {
                return toJsonString(Response.Status.BAD_REQUEST, "La cantidad a dar salida no puede ser menor que 0");
            }
            String destino = (String) params.get("destino");
            TransaccionSalida salida = new TransaccionController(em1).addTransaccionSalida(null, em1.find(Insumo.class, insumoCod), findVenta().getFecha(), new Date(),
                    super.find(almacenCod), em1.find(Cocina.class, destino), cant);
            return toJsonString(Response.Status.OK, salida.getTransaccion());
        } catch (JsonProcessingException | NumberFormatException | BadRequestException ex) {
            return handleException(ex);
        }

    }

    @RolesAllowed("4")
    @DELETE
    @Path("MERMAR_{almac-enCod}_{insumoCod}_{cant}_{razon}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public String rebaja(@PathParam("almacenCod") String almacenCod,
            @PathParam("insumoCod") String insumoCod,
            @PathParam("cant") float cant,
            @PathParam("razon") String razon) {
        return new TransaccionController(em1).addTransaccionRebaja(null, em1.find(Insumo.class, insumoCod), findVenta().getFecha(), new Date(),
                super.find(almacenCod), cant, razon).toString();

    }

    //@RolesAllowed("2")
    //@Secured
    @POST
    @Path("TRANSFORMAR")
    public Response transformacion(String listas) {
        try {
            TransformacionModel model = new ObjectMapper().readValue(listas, TransformacionModel.class);
            if (model.getEntradas().isEmpty() || model.getSalidas().isEmpty()) {
                throw new BadRequestException("Las listas no pueden estar vacias");
            }
            if (model.getSalidas().size() > 1) {
                throw new BadRequestException("La lista de salidas debe ser 1");
            }

            InsumoAlmacen salida = em1.find(InsumoAlmacen.class, model.getSalidas().get(0).getInsumoAlmacenPK());
            em1.refresh(salida);
            boolean derivanteValido = false;
            for (InsumoAlmacen ia : model.getEntradas()) {
                derivanteValido = false;
                for (InsumoElaborado derivante : em1.find(Insumo.class, ia.getInsumo().getCodInsumo()).getProductosDerivantes()) {
                    if (derivante.getDerivante().equals(salida.getInsumo())) {
                        derivanteValido = true;
                    }
                }
            }
            if (!derivanteValido) {
                throw new BadRequestException("Existe un insumo de salida no es derivante del insumo de entrada");
            }
            List<TransaccionTransformacion> aux = new ArrayList<>();
            for (InsumoAlmacen entrada : model.getEntradas()) {
                aux.add(transformInsumoAlmacen(entrada, 0));
            }
            getEntityManager().getTransaction().begin();
            new AlmacenController(getEntityManager(), findAll().get(0)).crearTransformacion(salida, model.getSalidas().get(0).getCantidad(), aux, findAll().get(0));
            if (getEntityManager().getTransaction().isActive()) {
                getEntityManager().getTransaction().commit();
            }

            return toJsonString(Response.Status.OK, "Accion realizada exitosamente");
        } catch (BadRequestException | IllegalArgumentException ex) {
            return toJsonString(Response.Status.BAD_REQUEST, ex.getMessage());
        } catch (Exception ex) {
            return toJsonString(Response.Status.INTERNAL_SERVER_ERROR, ex.getMessage() + ex.getStackTrace()[0].toString());
        }
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

    @RolesAllowed("2")
    @Secured
    @GET
    @Path("OPERACIONES-REALIZADAS")
    public Response getTransaccionList() {
        return toJsonString(Response.Status.OK, prepareTransacciones());
    }

    @RolesAllowed("2")
    @Secured
    @POST
    @Path("COMBINACIONES-CON")
    public Response getOperacionesCon(String listaInsumo) {
        List<InsumoAlmacen> aux = findAll().get(0).getInsumoAlmacenList(), ret = new ArrayList<>();
        List<Insumo> admitidos = new ArrayList<>();
        try {
            ObjectMapper om = new ObjectMapper();
            List<InsumoAlmacen> lista = new ObjectMapper().readValue(listaInsumo, om.getTypeFactory().constructCollectionType(List.class,
                    InsumoAlmacen.class
            ));

            for (InsumoAlmacen i : lista) {
                for (InsumoElaborado ie : getEntityManager().find(Insumo.class,
                        i.getInsumo().getCodInsumo()).getProductosDerivados()) {
                    admitidos.add(ie.getInsumo());
                }
            }
            for (Insumo a : admitidos) {
                for (InsumoAlmacen i : aux) {
                    if (i.getInsumo().getCodInsumo().equals(a.getCodInsumo())) {
                        i.setCantidad((float) 0);
                        ret.add(i);
                    }
                }
            }
            return toJsonString(Response.Status.OK, ret);

        } catch (Exception ex) {
            Logger.getLogger(AlmacenFacadeREST.class
                    .getName()).log(Level.SEVERE, null, ex);
            return toJsonString(Response.Status.BAD_REQUEST, "La peticion se proceso incorrectamente " + ex.getMessage());
        }
    }

    public List<Transaccion> prepareTransacciones() {
        List<Transaccion> ret = super.findAll(Transaccion.class
        );
        Collections.sort(ret, (Transaccion o1, Transaccion o2) -> {
            int comp = o1.getFecha().compareTo(o2.getFecha()) * -1;
            return comp == 0 ? o1.getHora().compareTo(o2.getHora()) * -1 : comp;
        });
        for (Transaccion t : ret) {
            if (t.getTransaccionEntrada() != null) {
                t.setDescripcion("ENTRADA: " + t.getTransaccionEntrada().getValorTotal() + R.COIN_SUFFIX);
            }
            if (t.getTransaccionMerma() != null) {
                t.setDescripcion(t.getTransaccionMerma().getRazon().toUpperCase());
            }
            if (t.getTransaccionSalida() != null) {
                t.setDescripcion("SALIDA: " + t.getTransaccionSalida().getCocinacodCocina());
            }
            if (t.getTransaccionTraspaso() != null) {
                t.setDescripcion("TRASPASO: " + t.getTransaccionTraspaso().getAlmacenDestino());
            }
            if (t.getTransaccionTransformacionList() != null) {
                if (!t.getTransaccionTransformacionList().isEmpty()) {
                    t.setDescripcion("TRANSFORMACION: ");
                }
            }
        }
        return ret;
    }

    public TransaccionTransformacion transformInsumoAlmacen(InsumoAlmacen selected, float cantidadUsada) {
        AlmacenController controller = new AlmacenController(findAll().get(0));
        TransaccionTransformacion nueva = new TransaccionTransformacion();
        nueva.setCantidadCreada(selected.getCantidad());
        nueva.setCantidadUsada(cantidadUsada);
        nueva.setDireccionInversa(false);
        nueva.setInsumo(controller.findInsumo(findAll().get(0).getCodAlmacen(), selected.getInsumo().getCodInsumo()).getInsumo());
        return nueva;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}
