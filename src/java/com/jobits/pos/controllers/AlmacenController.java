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
import com.jobits.pos.persistence.Transaccion;
import com.jobits.pos.persistence.TransaccionEntrada;
import com.jobits.pos.persistence.TransaccionMerma;
import com.jobits.pos.persistence.TransaccionSalida;
import javax.persistence.EntityManager;
import static com.jobits.utils.R.AUTO_UPDATE_INSUMO_PRICE;
import com.jobits.utils.utils;

/**
 * FirstDream
 *
 * @author Jorge
 *
 */
public class AlmacenController {

    private EntityManager em1;

    public AlmacenController(EntityManager em1) {
        this.em1 = em1;
    }

    public void darEntradaAInsumo(TransaccionEntrada x) {
        Insumo insumo = x.getTransaccion().getInsumocodInsumo();
        float cantidad = x.getTransaccion().getCantidad();
        float valorTotal = x.getValorTotal();
        Almacen a = x.getTransaccion().getAlmacencodAlmacen();
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

    void darSalidaAInsumo(TransaccionSalida x) {
        InsumoAlmacen insumoADarSalida = null;
        for (InsumoAlmacen i : x.getTransaccion().getAlmacencodAlmacen().getInsumoAlmacenList()) {
            if (i.getInsumo().equals(x.getTransaccion().getInsumocodInsumo())) {
                insumoADarSalida = i;

            }
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

        for (InsumoAlmacen i : x.getTransaccion().getAlmacencodAlmacen().getInsumoAlmacenList()) {
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

}
