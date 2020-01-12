/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.restmanager.models;

/**
 * FirstDream
 *
 * @author Jorge
 *
 */
public class AreaListModel {

    private final String cod, nombre;
    private final float ventaReal, ventaNeta;

    public AreaListModel(String cod, String nombre, float ventaReal, float ventaNeta) {
        this.cod = cod;
        this.nombre = nombre;
        this.ventaReal = ventaReal;
        this.ventaNeta = ventaNeta;
    }

    public String getCod() {
        return cod;
    }

    public String getNombre() {
        return nombre;
    }

    public float getVentaReal() {
        return ventaReal;
    }

    public float getVentaNeta() {
        return ventaNeta;
    }

}
