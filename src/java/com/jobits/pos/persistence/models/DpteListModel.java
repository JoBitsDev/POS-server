/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jobits.pos.persistence.models;

import com.jobits.utils.utils;

/**
 * FirstDream
 * @author Jorge
 * 
 */
public class DpteListModel {

    private String usuario;
    private float monto,ordenesAtendidas,pagoPorVentas;

    public DpteListModel(String usuario, float monto, float ordenesAtendidas, float pagoPorVentas) {
        this.usuario = usuario;
        this.monto = utils.setDosLugaresDecimalesFloat(monto);
        this.ordenesAtendidas = utils.setDosLugaresDecimalesFloat(ordenesAtendidas);
        this.pagoPorVentas = utils.setDosLugaresDecimalesFloat(pagoPorVentas);
    }

    public String getUsuario() {
        return usuario;
    }

    public float getMonto() {
        return monto;
    }

    public float getOrdenesAtendidas() {
        return ordenesAtendidas;
    }

    public float getPagoPorVentas() {
        return pagoPorVentas;
    }
    
}
