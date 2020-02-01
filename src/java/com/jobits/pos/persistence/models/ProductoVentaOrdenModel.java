/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jobits.pos.persistence.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.jobits.pos.persistence.Mesa;
import com.jobits.pos.persistence.Orden;
import com.jobits.pos.persistence.ProductoVenta;
import com.jobits.pos.persistence.ProductovOrden;
import com.jobits.pos.persistence.ProductovOrdenPK;

/**
 * FirstDream
 *
 * @author Jorge
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductoVentaOrdenModel {

    private float enviadosACocina;
    private ProductovOrdenPK productoVentaOrdenPK;
    private float cantidad;
    private Orden orden;
    private ProductoVenta productoVenta;
    private int numeroComensal;
    private Mesa mesa;
    private String nota;

    public ProductoVentaOrdenModel(float enviadosACocina, ProductovOrdenPK productoVentaOrdenPK, float cantidad, Orden orden, ProductoVenta productoVenta, int numeroComensal, Mesa mesa, String nota) {
        this.enviadosACocina = enviadosACocina;
        this.productoVentaOrdenPK = productoVentaOrdenPK;
        this.cantidad = cantidad;
        this.orden = orden;
        this.productoVenta = productoVenta;
        this.numeroComensal = numeroComensal;
        this.mesa = mesa;
        this.nota = nota;
    }

    public float getEnviadosACocina() {
        return enviadosACocina;
    }

    public void setEnviadosACocina(float enviadosACocina) {
        this.enviadosACocina = enviadosACocina;
    }

    public ProductovOrdenPK getProductoVentaOrdenPK() {
        return productoVentaOrdenPK;
    }

    public void setProductoVentaOrdenPK(ProductovOrdenPK productoVentaOrdenPK) {
        this.productoVentaOrdenPK = productoVentaOrdenPK;
    }

    public float getCantidad() {
        return cantidad;
    }

    public void setCantidad(float cantidad) {
        this.cantidad = cantidad;
    }

    public Orden getOrden() {
        return orden;
    }

    public void setOrden(Orden orden) {
        this.orden = orden;
    }

    public ProductoVenta getProductoVenta() {
        return productoVenta;
    }

    public void setProductoVenta(ProductoVenta productoVenta) {
        this.productoVenta = productoVenta;
    }

    public int getNumeroComensal() {
        return numeroComensal;
    }

    public void setNumeroComensal(int numeroComensal) {
        this.numeroComensal = numeroComensal;
    }

    public Mesa getMesa() {
        return mesa;
    }

    public void setMesa(Mesa mesa) {
        this.mesa = mesa;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }
    
    

}
