/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jobits.pos.controllers;

import com.jobits.pos.persistence.Almacen;
import com.jobits.pos.persistence.Insumo;
import com.jobits.pos.persistence.InsumoAlmacen;
import com.jobits.pos.persistence.IpvRegistro;
import com.jobits.pos.persistence.ProductoInsumo;
import com.jobits.pos.persistence.InsumoAlmacenPK;
import com.jobits.pos.persistence.Transaccion;
import com.jobits.pos.persistence.TransaccionEntrada;
import com.jobits.pos.persistence.TransaccionMerma;
import com.jobits.pos.persistence.TransaccionSalida;
import com.jobits.pos.persistence.TransaccionTransformacion;
import javax.persistence.EntityManager;
import static com.jobits.utils.R.AUTO_UPDATE_INSUMO_PRICE;
import com.jobits.utils.utils;
import java.util.Date;
import java.util.List;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.ws.rs.BadRequestException;

/**
 * FirstDream
 *
 * @author Jorge
 *
 */
public class AlmacenController {

    private EntityManager em1;
    private Almacen a;

    public AlmacenController(EntityManager em1, Almacen a) {
        this.em1 = em1;
        this.a = a;
    }

    public AlmacenController(Almacen get) {
        this.a = get;
        em1 = Persistence.createEntityManagerFactory("Restaurant_Manager_Web_ServicePU").createEntityManager();
    }

    public InsumoAlmacen registrarInsumoEnAlmacen(Insumo selected) {
        InsumoAlmacenPK newInsumoPK = new InsumoAlmacenPK(selected.getCodInsumo(), a.getCodAlmacen());
        InsumoAlmacen newInsumo = new InsumoAlmacen(newInsumoPK);
        newInsumo.setAlmacen(a);
        newInsumo.setCantidad((float) 0);
        newInsumo.setInsumo(selected);
        newInsumo.setValorMonetario((float) 0);
        if (!em1.isOpen()) {
            em1 = Persistence.createEntityManagerFactory("Restaurant_Manager_Web_ServicePU").createEntityManager();
            em1.getTransaction().begin();
            em1.persist(newInsumo);
            em1.flush();
            em1.getTransaction().commit();
        } else {
            em1.persist(newInsumo);
        }
        return newInsumo;
    }

    public void darEntradaAInsumo(TransaccionEntrada x) {
        Insumo insumo = x.getTransaccion().getInsumocodInsumo();
        float cantidad = x.getTransaccion().getCantidad();
        float valorTotal = x.getValorTotal();
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
            if (utils.setDosLugaresDecimalesFloat(ins.getValorMonetario() / ins.getCantidad()) != insumo.getCostoPorUnidad()) {
                if (AUTO_UPDATE_INSUMO_PRICE) {
                    insumo.setCostoPorUnidad(utils.setDosLugaresDecimalesFloat(ins.getValorMonetario() / ins.getCantidad()));
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

    public void crearTransformacion(InsumoAlmacen selected, float cantidad, List<TransaccionTransformacion> items, Almacen destino) throws  IllegalArgumentException{

        // Validaciones
        if (selected.getCantidad() < cantidad || cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad a transformar no puede ser mayor que la cantidad existente en almacen"
                    + "\n Ni la cantidad a transformar ser igual o menor que cero ");
        }

        if (items.isEmpty()) {
            throw new IllegalArgumentException("La lista de insumos transformados esta vacia");
        }
        float sumaTransformacion = 0;
        for (TransaccionTransformacion i : items) {
            sumaTransformacion += i.getCantidadUsada();
            if (!selected.getInsumo().getProductosDerivados().contains(i.getInsumo())) {
                throw new IllegalArgumentException("El insumo " + i.getInsumo() + " no es un insumo derivado de " + selected.getInsumo()
                        + "\n y no es posible transformarlo");
            }
            if (findInsumo(destino.getCodAlmacen(), i.getInsumo().getCodInsumo()) == null) {
                throw new IllegalArgumentException("El insumo " + i.getInsumo() + " no se encuentra en el almacen destino (" + destino + ")");
            }
            if (i.getCantidadCreada() <= 0) {
                throw new IllegalArgumentException("Las cantidades creadas deben ser mayor que cero");
            }
        }
        if (sumaTransformacion > cantidad) {
            throw new IllegalArgumentException("La cantidad total transformada en insumos no puede ser mayor que la cantidad a transformar");
        }

        float merma = utils.setDosLugaresDecimalesFloat(sumaTransformacion - cantidad);
       
        TransaccionController controller = new TransaccionController(em1);
        controller.addTransaccionTransformacion(selected, new Date(), new Date(), items, cantidad, merma, destino);
    }

    void darSalidaAInsumo(TransaccionSalida x) throws BadRequestException {
        InsumoAlmacen insumoADarSalida = null;
        for (InsumoAlmacen i : a.getInsumoAlmacenList()) {
            if (i.getInsumo().equals(x.getTransaccion().getInsumocodInsumo())) {
                insumoADarSalida = i;

            }
        }
        if (insumoADarSalida.getCantidad() < x.getTransaccion().getCantidad()) {
            throw new BadRequestException("La cantidad de " + insumoADarSalida + " es mayor que la existencia actual");
        }
        IpvRegistro reg = (IpvRegistro) em1.createNamedQuery("IpvRegistro.findByIpvcocinacodCocinaAndFechaAndInsumo")
                .setParameter("ipvcocinacodCocina", x.getCocinacodCocina().getCodCocina())
                .setParameter("fecha", x.getTransaccion().getFecha())
                .setParameter("codinsumo", x.getTransaccion().getInsumocodInsumo().getCodInsumo())
                .getSingleResult();
        reg.setEntrada(reg.getEntrada() + x.getTransaccion().getCantidad());

        reg.setDisponible(reg.getEntrada() + reg.getInicio());
        reg.setFinal1(reg.getDisponible() - reg.getConsumo());
        if (reg.getConsumoReal() != null) {
            if (reg.getConsumoReal() > 0) {
                reg.setFinal1(reg.getDisponible() - reg.getConsumoReal());
            }
        }
        em1.merge(reg);
        float precioMedio
                = utils.setDosLugaresDecimalesFloat(insumoADarSalida.getValorMonetario() / insumoADarSalida.getCantidad());
        insumoADarSalida.setCantidad(insumoADarSalida.getCantidad() - x.getTransaccion().getCantidad());
        insumoADarSalida.setValorMonetario(insumoADarSalida.getValorMonetario() - x.getTransaccion().getCantidad() * precioMedio);
        em1.merge(insumoADarSalida);
        //updateValorTotalAlmacen(instance);
    }

    void darMermaInsumo(TransaccionMerma x) {
        InsumoAlmacen insumoaRebajar = null;

        for (InsumoAlmacen i : a.getInsumoAlmacenList()) {
            if (i.getInsumo().equals(x.getTransaccion().getInsumocodInsumo())) {
                insumoaRebajar = i;

            }
        }
        if (insumoaRebajar.getCantidad() < x.getTransaccion().getCantidad()) {
            if (em1.getTransaction().isActive()) {
                em1.getTransaction().rollback();
            }
            return;
        }
        float precioMedio = insumoaRebajar.getValorMonetario() / insumoaRebajar.getCantidad();
        insumoaRebajar.setCantidad(insumoaRebajar.getCantidad() - x.getTransaccion().getCantidad());
        insumoaRebajar.setValorMonetario(insumoaRebajar.getValorMonetario() - x.getTransaccion().getCantidad() * precioMedio);
        em1.merge(insumoaRebajar);
        //updateValorTotalAlmacen(instance);

    }

    private void darMermaInsumo(Insumo x, float cantidad) {
        InsumoAlmacen insumoaRebajar = null;

        for (InsumoAlmacen i : a.getInsumoAlmacenList()) {
            if (i.getInsumo().equals(x)) {
                insumoaRebajar = i;

            }
        }
        if (insumoaRebajar == null) {
            return;
        }

        if (insumoaRebajar.getCantidad() < cantidad) {
            if (em1.getTransaction().isActive()) {
                em1.getTransaction().rollback();
            }
            return;
        }
        float precioMedio = insumoaRebajar.getValorMonetario() / insumoaRebajar.getCantidad();
        insumoaRebajar.setCantidad(insumoaRebajar.getCantidad() - cantidad);
        insumoaRebajar.setValorMonetario(insumoaRebajar.getValorMonetario() - cantidad * precioMedio);
        em1.merge(insumoaRebajar);
        //updateValorTotalAlmacen(instance);

    }

    private void darEntradaAInsumo(Insumo i, float cantidad, float total) {
        InsumoAlmacen insu = null;
        for (InsumoAlmacen ins : a.getInsumoAlmacenList()) {
            if (ins.getInsumo().equals(i)) {
                insu = ins;

            }
        }
        if (insu != null) {
            insu.setCantidad(insu.getCantidad() + cantidad);
            insu.setValorMonetario(insu.getValorMonetario() + total);
            em1.merge(insu);
            if (utils.setDosLugaresDecimalesFloat(insu.getValorMonetario() / insu.getCantidad()) != i.getCostoPorUnidad()) {
                if (AUTO_UPDATE_INSUMO_PRICE) {
                    i.setCostoPorUnidad(utils.setDosLugaresDecimalesFloat(insu.getValorMonetario() / insu.getCantidad()));
                    em1.merge(i);
                    for (ProductoInsumo p : i.getProductoInsumoList()) {
                        p.setCosto(i.getCostoPorUnidad() * p.getCantidad());
                        em1.merge(p);
                    }
                }
            }
            a.setValorMonetario(a.getValorMonetario() + total);
            em1.merge(a);

            em1.getTransaction().commit();
        }
    }

    void darTransformacionAInsumo(Transaccion t, Almacen a) {
        darMermaInsumo(t.getInsumocodInsumo(), t.getCantidad());
        InsumoAlmacen ins = findInsumo(a.getCodAlmacen(), t.getInsumocodInsumo().getCodInsumo());
        float precioMedio = ins.getValorMonetario() / ins.getCantidad();
        for (TransaccionTransformacion x : t.getTransaccionTransformacionList()) {
            darEntradaAInsumo(x.getInsumo(), x.getCantidadCreada(), precioMedio * x.getCantidadCreada());
        }
    }

    private InsumoAlmacen findInsumo(String a, String i) {
        try {
            return (InsumoAlmacen) em1.createNamedQuery("InsumoAlmacen.findByAlmacenInsumo")
                    .setParameter("almacencodAlmacen", a)
                    .setParameter("insumo", i)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

}
