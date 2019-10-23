/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.restmanager.controller;

import com.restmanager.TransaccionEntrada;
import com.restmanager.*;
import java.util.Date;
import javax.persistence.EntityManager;
import restmanager.resources.comun;

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

    public TransaccionEntrada addTransaccionEntrada(Insumo insumo, Date fecha, Date hora, Almacen a, float cantidad, float importe) {
        Transaccion t = nuevaTransaccion(insumo, fecha, hora, a, cantidad);
        TransaccionEntrada ret = new TransaccionEntrada(t.getNoTransaccion());
        ret.setJustificado(false);
        ret.setTransaccion(t);
        ret.setValorTotal(importe);
        ret.setPrecioPorUnidad(ret.getValorTotal() / ret.getTransaccion().getCantidad());
        t.setTransaccionEntrada(ret);
        a.getTransaccionList().add(t);
        createNewTransaccionEntrada(ret);
        return ret;

    }

    public TransaccionSalida addTransaccionSalida(Insumo insumo, Date fecha, Date hora, Almacen a, Cocina cocina, float cantidad) {
        Transaccion t = nuevaTransaccion(insumo, fecha, hora, a, cantidad);
        TransaccionSalida salida = new TransaccionSalida(t.getNoTransaccion());
        salida.setTransaccion(t);
        salida.setCocinacodCocina(cocina);
        createNewTransaccionSalida(salida);
        return salida;

    }

    public TransaccionMerma addTransaccionRebaja(Insumo insumo, Date fecha, Date hora, Almacen a, float cantidad, String causaRebaja) {
        Transaccion t = nuevaTransaccion(insumo, fecha, hora, a, cantidad);
        TransaccionMerma rebaja = new TransaccionMerma(t.getNoTransaccion());
        rebaja.setTransaccion(t);
        rebaja.setRazon(causaRebaja);
        a.getTransaccionList().add(t);
        createNewTransaccionRebaja(rebaja);
        return rebaja;
    }

    //
    //Private methods
    //
    
    void createNewTransaccionRebaja(TransaccionMerma transaccion) {
        startTransaction();
        AlmacenController almacenController = new AlmacenController(em1);
        almacenController.darMermaInsumo(transaccion);
        em1.persist(transaccion);
        commitTransaction();

    }

    void createNewTransaccionSalida(TransaccionSalida transaccion) {
        startTransaction();
        AlmacenController almacenController = new AlmacenController(em1);
        almacenController.darSalidaAInsumo(transaccion);
        em1.persist(transaccion);
        commitTransaction();

    }

    void createNewTransaccionEntrada(TransaccionEntrada transaccion) {
        startTransaction();
        AlmacenController controller = new AlmacenController(em1);
        controller.darEntradaAInsumo(transaccion);
        em1.persist(transaccion);
        commitTransaction();
    }

    private Transaccion nuevaTransaccion(Insumo insumo, Date fecha, Date hora, Almacen a, float cantidad) {
        Transaccion t = new Transaccion();
        t.setAlmacencodAlmacen(a);
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
