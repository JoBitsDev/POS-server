/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jobits.pos.persistence.models;

import com.jobits.pos.persistence.ProductovOrden;
import com.jobits.utils.utils;
import java.util.ArrayList;
import java.util.List;

/**
 * FirstDream
 *
 * @author Jorge
 *
 */
public class DetallesVentasModel {

    private String nombreProducto, productoCod;
    private float cantidad;
    private String precioVenta, montoVenta;

    public static List<DetallesVentasModel> createDetallesVentaFromEntity(List<ProductovOrden> productoVentaList) {
        List<DetallesVentasModel> ret = new ArrayList<>();
        for (ProductovOrden x : productoVentaList) {
            ret.add(new DetallesVentasModel(x.getProductoVenta().getNombre()
                    , x.getProductoVenta().getPCod()
                    , x.getCantidad()
                    , utils.setDosLugaresDecimales(x.getProductoVenta().getPrecioVenta())
                    , utils.setDosLugaresDecimales(x.getCantidad() * x.getProductoVenta().getPrecioVenta())));
        }
        return ret;
    }

    public DetallesVentasModel(String nombreProducto, String productoCod, float cantidad, String precioVenta, String montoVenta) {
        this.nombreProducto = nombreProducto;
        this.productoCod = productoCod;
        this.cantidad = cantidad;
        this.precioVenta = precioVenta;
        this.montoVenta = montoVenta;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getProductoCod() {
        return productoCod;
    }

    public void setProductoCod(String productoCod) {
        this.productoCod = productoCod;
    }

    public float getCantidad() {
        return cantidad;
    }

    public void setCantidad(float cantidad) {
        this.cantidad = cantidad;
    }

    public String getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(String precioVenta) {
        this.precioVenta = precioVenta;
    }

    public String getMontoVenta() {
        return montoVenta;
    }

    public void setMontoVenta(String montoVenta) {
        this.montoVenta = montoVenta;
    }

}
