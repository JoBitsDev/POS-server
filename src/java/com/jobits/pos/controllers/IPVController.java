/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jobits.pos.controllers;

import com.jobits.pos.persistence.IpvRegistro;
import com.jobits.pos.persistence.IpvRegistroPK;
import com.jobits.pos.persistence.ProductoInsumo;
import com.jobits.pos.persistence.ProductoVenta;
import java.util.Date;
import javax.persistence.EntityManager;

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

}
