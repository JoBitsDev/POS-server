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
import com.restmanager.IpvRegistro;
import com.restmanager.ProductoInsumo;
import com.restmanager.Transaccion;
import com.restmanager.TransaccionEntrada;
import com.restmanager.TransaccionEntradaPK;
import com.restmanager.TransaccionMerma;
import com.restmanager.TransaccionMermaPK;
import com.restmanager.TransaccionPK;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.Servlet;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import static restmanager.resources.R.AUTO_UPDATE_INSUMO_PRICE;
import restmanager.resources.comun;

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
    @Path("ENTRADA_{almacenCod}_{insumoCod}_{cant}_{valor}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public String entrada(@PathParam("almacenCod") String almacenCod,
            @PathParam("insumoCod") String insumoCod,
            @PathParam("cant") float cant,
            @PathParam("valor") float valor) {
        return addTransaccionEntrada(em1.find(Insumo.class, insumoCod), findVenta().getFecha(), new Date(), super.find(almacenCod), cant, valor).toString();

    }

    @GET
    @Path("SALIDA_{almacenCod}_{insumoCod}_{cant}_{destino}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public String salida(@PathParam("almacenCod") String almacenCod,
            @PathParam("insumoCod") String insumoCod,
            @PathParam("cant") float cant,
            @PathParam("destino") String destino) {
        return addTransaccionSalida(em1.find(Insumo.class, insumoCod), findVenta().getFecha(), new Date(),
                super.find(almacenCod), cant, destino.substring(destino.length() - 4, destino.length() - 1)).toString();

    }

    @GET
    @Path("MERMAR_{almacenCod}_{insumoCod}_{cant}_{razon}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public String rebaja(@PathParam("almacenCod") String almacenCod,
            @PathParam("insumoCod") String insumoCod,
            @PathParam("cant") float cant,
            @PathParam("razon") String razon) {
        return addTransaccionRebaja(em1.find(Insumo.class, insumoCod), findVenta().getFecha(), new Date(),
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

    public TransaccionEntrada addTransaccionEntrada(Insumo selected, Date fecha, Date hora, Almacen a, float cantidad, float valor) {
        em1.getTransaction().begin();
        TransaccionPK transPK = new TransaccionPK(selected.getCodInsumo(),
                a.getCodAlmacen(), fecha, hora);
        Transaccion t = new Transaccion(transPK);
        t.setCantidad(cantidad);
        t.setInsumo(selected);
        t.setAlmacen(a);
        TransaccionEntradaPK retPK
                = new TransaccionEntradaPK(selected.getCodInsumo(),
                        t.getTransaccionPK().getFecha(),
                        t.getTransaccionPK().getHora(), a.getCodAlmacen());
        TransaccionEntrada ret = new TransaccionEntrada(retPK);
        ret.setTransaccion(t);
        ret.setValorTotal(valor);
        ret.setPrecioPorUnidad(comun.setDosLugaresDecimalesFloat(ret.getValorTotal() / ret.getTransaccion().getCantidad()));
        t.setTransaccionEntrada(ret);
        a.getTransaccionList().add(t);
        em1.persist(ret);
        darEntradaAInsumo(a, selected, cantidad, valor);
        return ret;

    }

    void darEntradaAInsumo(Almacen a, Insumo insumo, Float cantidad, Float valorTotal) {
        InsumoAlmacen ins = null;
        for (InsumoAlmacen i : a.getInsumoAlmacenList()) {
            if (i.getInsumo().equals(insumo)) {
                ins = i;

            }
        }
        if (ins != null) {
            ins.setCantidad(ins.getCantidad() + cantidad);
            ins.setValorMonetario(ins.getValorMonetario() + valorTotal);
            em1.merge(ins);
            if (comun.setDosLugaresDecimalesFloat(ins.getValorMonetario() / ins.getCantidad()) != insumo.getCostoPorUnidad()) {
                if (AUTO_UPDATE_INSUMO_PRICE) {
                    insumo.setCostoPorUnidad(comun.setDosLugaresDecimalesFloat(ins.getValorMonetario() / ins.getCantidad()));
                    em1.merge(insumo);
                    for (ProductoInsumo p : insumo.getProductoInsumoList()) {
                        p.setCosto(insumo.getCostoPorUnidad() * p.getCantidad());
                        em1.merge(p);
                    }
                }
            }
            a.setValorMonetario(a.getValorMonetario() + valorTotal);
            em1.merge(a);

            em1.getTransaction().commit();
        }
    }

    private Transaccion addTransaccionSalida(Insumo selected, Date fecha, Date hora, Almacen a, float cantidad, String destino) {
        em1.getTransaction().begin();
        TransaccionPK transPK = new TransaccionPK(selected.getCodInsumo(),
                a.getCodAlmacen(), fecha, hora);
        Transaccion t = new Transaccion(transPK);
        t.setCantidad(cantidad);
        t.setInsumo(selected);
        t.setAlmacen(a);
        t.setCocina(em1.find(Cocina.class, destino));
        darSalidaAInsumo(t);
        em1.persist(t);
        em1.getTransaction().commit();
        return t;

    }

    void darSalidaAInsumo(Transaccion x) {
        InsumoAlmacen insumoADarSalida = null;
        for (InsumoAlmacen i : x.getAlmacen().getInsumoAlmacenList()) {
            if (i.getInsumo().equals(x.getInsumo())) {
                insumoADarSalida = i;

            }
        }
        IpvRegistro reg = (IpvRegistro) getEntityManager().createNamedQuery("IpvRegistro.findByIpvcocinacodCocinaAndFechaAndInsumo")
                .setParameter("ipvcocinacodCocina", x.getCocina().getCodCocina())
                .setParameter("fecha", x.getTransaccionPK().getFecha())
                .setParameter("codinsumo", x.getInsumo().getCodInsumo())
                .getSingleResult();
        reg.setEntrada(reg.getEntrada() + x.getCantidad());

        reg.setDisponible(reg.getEntrada() + reg.getInicio());
        reg.setFinal1(reg.getDisponible() - reg.getConsumo());
        if (reg.getConsumoReal() != null) {
            if (reg.getConsumoReal() > 0) {
                reg.setFinal1(reg.getDisponible() - reg.getConsumoReal());
            }
        }
        em1.merge(reg);
        float precioMedio
                = comun.setDosLugaresDecimalesFloat(insumoADarSalida.getValorMonetario() / insumoADarSalida.getCantidad());
        insumoADarSalida.setCantidad(insumoADarSalida.getCantidad() - x.getCantidad());
        insumoADarSalida.setValorMonetario(insumoADarSalida.getValorMonetario() - x.getCantidad() * precioMedio);
        em1.merge(insumoADarSalida);
        //updateValorTotalAlmacen(instance);
    }

    private Transaccion addTransaccionRebaja(Insumo i, Date fecha, Date hora, Almacen a, float cant, String razon) {
        em1.getTransaction().begin();
        TransaccionPK transPK = new TransaccionPK(i.getCodInsumo(),
                a.getCodAlmacen(), fecha, hora);
        Transaccion t = new Transaccion(transPK);
        t.setCantidad(cant);
        t.setInsumo(i);
        t.setAlmacen(a);

        TransaccionMermaPK pk = new TransaccionMermaPK();
        pk.setTransaccionalmacencodAlmacen(a.getCodAlmacen());
        pk.setTransaccionfecha(fecha);
        pk.setTransaccionhora(hora);
        pk.setTransaccioninsumocodInsumo(i.getCodInsumo());
        TransaccionMerma rebaja = new TransaccionMerma(pk);
        rebaja.setTransaccion(t);
        rebaja.setRazon(razon);
        
        mermarInsumo(t);
        em1.persist(t);
        em1.persist(rebaja);
        em1.getTransaction().commit();
        return t;
    }

    private void mermarInsumo(Transaccion x) {
        InsumoAlmacen insumoaRebajar = null;
        
        for (InsumoAlmacen i : x.getAlmacen().getInsumoAlmacenList()) {
            if (i.getInsumo().equals(x.getInsumo())) {
                insumoaRebajar = i;

            }
        }
      
        float precioMedio = insumoaRebajar.getValorMonetario() / insumoaRebajar.getCantidad();
        insumoaRebajar.setCantidad(insumoaRebajar.getCantidad() - x.getCantidad());
        insumoaRebajar.setValorMonetario(insumoaRebajar.getValorMonetario() - x.getCantidad() * precioMedio);
        em1.merge(insumoaRebajar);
        //updateValorTotalAlmacen(instance);
    }

}
