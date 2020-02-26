/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jobits.pos.controllers;

import com.jobits.pos.persistence.*;
import com.jobits.utils.utils;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;

/**
 * FirstDream
 *
 * @author Jorge
 *
 */
public class IPVController extends AbstractController {

    public IPVController(EntityManager em1) {
        super(em1);
    }

    public int getRestantes(String codProducto, Date fecha) {
        ProductoVenta producto = em1.find(ProductoVenta.class, codProducto);
        int cantidadMinima = Integer.MAX_VALUE;
        for (ProductoInsumo x : producto.getProductoInsumoList()) {
            IpvRegistroPK pk = new IpvRegistroPK(x.getInsumo().getCodInsumo(), producto.getCocinacodCocina().getCodCocina(), fecha);
            IpvRegistro registro = em1.find(IpvRegistro.class, pk);
            if (registro != null) {
                int aux = (int) (registro.getDisponible() / x.getCantidad());
                if (aux < cantidadMinima) {
                    cantidadMinima = aux;
                }
            }
        }
        return cantidadMinima == Integer.MAX_VALUE ? 0 : cantidadMinima;
    }

    public void consumir(ProductovOrden productoVenta, float cantidad) {
        List<IpvRegistro> updateList = new ArrayList<>();
        for (ProductoInsumo productoInsumo : productoVenta.getProductoVenta().getProductoInsumoList()) {
            IpvRegistro registro
                    = getIpvRegistro(productoVenta.getProductoVenta().getCocinacodCocina(),
                            productoVenta.getOrden().getVentafecha().getFecha(),
                            productoInsumo.getInsumo());
            if (registro != null) {
                float cantidadaRebajar = productoInsumo.getCantidad() * cantidad;
                registro.setConsumo(registro.getConsumo() + cantidadaRebajar);
                updateList.add(registro);
            }
        }
        for (IpvRegistro registro : updateList) {
            updateInstance(registro);
        }
        IpvVentaRegistroPK pk = new IpvVentaRegistroPK(productoVenta.getOrden().getVentafecha().getFecha(), productoVenta.getProductoVenta().getPCod());
        IpvVentaRegistro ipvVenta = getEntityManager().find(IpvVentaRegistro.class, pk);
        if (ipvVenta != null) {
            ipvVenta.setVendidos(ipvVenta.getVendidos()+ cantidad);
            updateInstance(ipvVenta);
        }
    }

    //esto solo pincha cuando ponen el de la casa al final
    public void consumirPorLaCasa(List<ProductovOrden> listaProductos) {
        for (ProductovOrden x : listaProductos) {
            IpvVentaRegistroPK pk = new IpvVentaRegistroPK(x.getOrden().getVentafecha().getFecha(), x.getProductoVenta().getPCod());
            IpvVentaRegistro ipvVenta = getEntityManager().find(IpvVentaRegistro.class, pk);
            if (ipvVenta != null) {
                ipvVenta.setVendidos(ipvVenta.getVendidos()- x.getCantidad());
                ipvVenta.setAutorizos(ipvVenta.getAutorizos() + x.getCantidad());
                updateInstance(ipvVenta);
            }
        }

    }

    //esto solo pincha cuando ponen el de la casa al final
    public void devolverPorLaCasa(List<ProductovOrden> listaProductos) {
        for (ProductovOrden x : listaProductos) {
            IpvVentaRegistroPK pk = new IpvVentaRegistroPK(x.getOrden().getVentafecha().getFecha(), x.getProductoVenta().getPCod());
            IpvVentaRegistro ipvVenta = getEntityManager().find(IpvVentaRegistro.class, pk);
            if (ipvVenta != null) {
                ipvVenta.setAutorizos(ipvVenta.getAutorizos() - x.getCantidad());
                ipvVenta.setVendidos(ipvVenta.getVendidos()+ x.getCantidad());
                updateInstance(ipvVenta);
            }
        }
    }

    public void devolver(ProductovOrden productoVenta, float diferencia) {
        List<IpvRegistro> updateList = new ArrayList<>();
        for (ProductoInsumo productoInsumo : productoVenta.getProductoVenta().getProductoInsumoList()) {
            IpvRegistro registro
                    = getIpvRegistro(productoVenta.getProductoVenta().getCocinacodCocina(),
                            productoVenta.getOrden().getVentafecha().getFecha(),
                            productoInsumo.getInsumo());
            if (registro != null) {
                float cantidadaRebajar = productoInsumo.getCantidad() * diferencia;
                registro.setConsumo(registro.getConsumo() - cantidadaRebajar);
                updateList.add(registro);
            }
        }
        for (IpvRegistro registro : updateList) {
            updateInstance(registro);
        }
        IpvVentaRegistroPK pk = new IpvVentaRegistroPK(productoVenta.getOrden().getVentafecha().getFecha(), productoVenta.getProductoVenta().getPCod());
        IpvVentaRegistro ipvVenta = getEntityManager().find(IpvVentaRegistro.class, pk);
        if (ipvVenta != null) {
            ipvVenta.setVendidos(ipvVenta.getVendidos()- diferencia);
            updateInstance(ipvVenta);
        }
    }

    public void updateInstance(IpvRegistro instance) {
        if (instance.getEntrada() == null) {
            instance.setEntrada((float) 0);
        }
        if (instance.getInicio() == null) {
            instance.setInicio((float) 0);
        }
        if (instance.getConsumo() == null) {
            instance.setConsumo((float) 0);
        }
        if (instance.getConsumoReal() == null) {
            instance.setConsumoReal((float) 0);
        }
        if (instance.getFinalCalculado() == null) {
            instance.setFinalCalculado((float) 0);
        }
        if (instance.getFinalAjustado() == null) {
            instance.setFinalAjustado((float) 0);
        }
        if (instance.getDisponible() == null) {
            instance.setDisponible((float) 0);
        }
        instance.setDisponible(instance.getEntrada() + instance.getInicio());
        instance.setFinalCalculado(utils.setDosLugaresDecimalesFloat(instance.getDisponible() - instance.getConsumo()));
        if (instance.getConsumoReal() != null) {
            if (instance.getConsumoReal() > 0) {
                instance.setFinalCalculado(utils.setDosLugaresDecimalesFloat(instance.getDisponible() - instance.getConsumoReal()));
            }
        }
        getEntityManager().getTransaction().begin();
        getEntityManager().merge(instance);
        getEntityManager().getTransaction().commit();
    }

    public void updateInstance(IpvVentaRegistro instance) {
        if (instance.getEntrada() == null) {
            instance.setEntrada((float) 0);
        }
        if (instance.getInicio() == null) {
            instance.setInicio((float) 0);
        }
        if (instance.getVendidos() == null) {
            instance.setVendidos((float) 0);
        }
        if (instance.getFinal1() == null) {
            instance.setFinal1((float) 0);
        }
        if (instance.getDisponible() == null) {
            instance.setDisponible((float) 0);
        }
        if (instance.getAutorizos() == null) {
            instance.setAutorizos((float) 0);
        }
        instance.setDisponible(instance.getEntrada() + instance.getInicio());
        instance.setFinal1(utils.setDosLugaresDecimalesFloat(instance.getDisponible() - instance.getVendidos() - instance.getAutorizos()));
        getEntityManager().getTransaction().begin();
        getEntityManager().merge(instance);
        getEntityManager().getTransaction().commit();
    }

    public List<Date> getIpvRegistroList(Cocina cocina) {
        return getEntityManager().createNamedQuery("IpvRegistro.findByIpvcocinacodCocina")
                .setParameter("ipvcocinacodCocina", cocina.getCodCocina())
                .getResultList();
    }

    public List<IpvRegistro> getIpvRegistroList(Cocina cocina, Date fecha) {
        getEntityManager().getEntityManagerFactory().getCache().evict(IpvRegistro.class);
        return new ArrayList<>(getEntityManager().createNamedQuery("IpvRegistro.findByIpvcocinacodCocinaAndFecha")
                .setParameter("ipvcocinacodCocina", cocina.getCodCocina())
                .setParameter("fecha", fecha)
                .getResultList());

    }

    public IpvRegistro getIpvRegistro(Cocina c, Date fecha, Insumo i) throws NoResultException, PersistenceException {
        return (IpvRegistro) getEntityManager().createNamedQuery("IpvRegistro.findByIpvcocinacodCocinaAndFechaAndInsumo")
                .setParameter("ipvcocinacodCocina", c.getCodCocina())
                .setParameter("fecha", fecha)
                .setParameter("codinsumo", i.getCodInsumo())
                .getSingleResult();

    }
}
