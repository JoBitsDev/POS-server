/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jobits.pos.persistence.models;

import com.jobits.utils.utils;

/**
 * FirstDream
 *
 * @author Jorge
 *
 */
public class PuntoElaboracionListModel {

    private final String codigo, nombre;
    private final float monto;

    public PuntoElaboracionListModel(String codigo, String nombre, float monto) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.monto = utils.setDosLugaresDecimalesFloat(monto);
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public float getMonto() {
        return monto;
    }

}
