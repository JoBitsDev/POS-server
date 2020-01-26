/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jobits.pos.controllers;

import com.jobits.pos.persistence.Cocina;
import com.jobits.pos.persistence.TransaccionSalida;
import com.jobits.pos.persistence.Insumo;
import com.jobits.pos.persistence.TransaccionMerma;
import com.jobits.pos.persistence.Almacen;
import com.jobits.pos.persistence.Operacion;
import com.jobits.pos.persistence.Transaccion;
import com.jobits.pos.persistence.TransaccionEntrada;
import java.util.Date;
import javax.persistence.EntityManager;
import com.jobits.utils.utils;

/**
 * FirstDream
 *
 * @author Jorge
 *
 */
public class TransaccionController {

    private final EntityManager em1;

    public TransaccionController(EntityManager em1) {
        this.em1 = em1;
    }

    public TransaccionEntrada addTransaccionEntrada(Operacion o, Insumo insumo, Date fecha, Date hora, Almacen a, float cantidad, float importe) {
        Transaccion t = nuevaTransaccion(o, insumo, fecha, hora, a, cantidad);
        TransaccionEntrada ret = new TransaccionEntrada(t.getNoTransaccion());
        ret.setJustificado(false);
        ret.setTransaccion(t);
        ret.setValorTotal(importe);
        ret.setPrecioPorUnidad(ret.getValorTotal() / ret.getTransaccion().getCantidad());
        t.setTransaccionEntrada(ret);
        //  a.getTransaccionList().add(t); //TODO: revisar esto
        createNewTransaccionEntrada(ret,a);
        return ret;

    }

    public TransaccionSalida addTransaccionSalida(Operacion o, Insumo insumo, Date fecha, Date hora, Almacen a, Cocina cocina, float cantidad) {
        Transaccion t = nuevaTransaccion(o, insumo, fecha, hora, a, cantidad);
        TransaccionSalida salida = new TransaccionSalida(t.getNoTransaccion());
        salida.setTransaccion(t);
        salida.setCocinacodCocina(cocina);
        createNewTransaccionSalida(salida,a);
        return salida;

    }

    public TransaccionMerma addTransaccionRebaja(Operacion o, Insumo insumo, Date fecha, Date hora, Almacen a, float cantidad, String causaRebaja) {
        Transaccion t = nuevaTransaccion(o, insumo, fecha, hora, a, cantidad);
        TransaccionMerma rebaja = new TransaccionMerma(t.getNoTransaccion());
        rebaja.setTransaccion(t);
        rebaja.setRazon(causaRebaja);
        // a.getTransaccionList().add(t); //TODO: Revisar esto
        createNewTransaccionRebaja(rebaja,a);
        return rebaja;
    }

    //
    //Private methods
    //
    void createNewTransaccionRebaja(TransaccionMerma transaccion,Almacen a) {
        startTransaction();
        AlmacenController almacenController = new AlmacenController(em1,a);
        almacenController.darMermaInsumo(transaccion);
        em1.persist(transaccion);
        commitTransaction();

    }

    void createNewTransaccionSalida(TransaccionSalida transaccion,Almacen a) {
        startTransaction();
        AlmacenController almacenController = new AlmacenController(em1,a);
        almacenController.darSalidaAInsumo(transaccion);
        em1.persist(transaccion);
        commitTransaction();

    }

    void createNewTransaccionEntrada(TransaccionEntrada transaccion,Almacen a) {
        startTransaction();
        AlmacenController controller = new AlmacenController(em1,a);
        controller.darEntradaAInsumo(transaccion);
        em1.persist(transaccion);
        commitTransaction();
    }

    private Transaccion nuevaTransaccion(Operacion o, Insumo insumo, Date fecha, Date hora, Almacen a, float cantidad) {
        Transaccion t = new Transaccion();
        if (o != null) {
            t.setOperacionnoOperacion(o);
        }
        t.setCantidad(cantidad);
        t.setInsumocodInsumo(insumo);
        t.setFecha(fecha);
        t.setHora(hora);
        em1.persist(t);
        return t;
    }

    private void startTransaction() {
        if (!em1.getTransaction().isActive()) {
            em1.getTransaction().begin();
        }
    }

    private void commitTransaction() {
        if (em1.getTransaction().isActive()) {
            em1.getTransaction().commit();
        }
    }

}
