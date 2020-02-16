/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jobits.pos.persistence.models;

import com.jobits.pos.persistence.InsumoAlmacen;
import java.util.List;

/**
 * FirstDream
 *
 * @author Jorge
 *
 */
public class TransformacionModel {

    private List<InsumoAlmacen> entradas;
    private List<InsumoAlmacen> salidas;

    public TransformacionModel() {
    }

    public TransformacionModel(List<InsumoAlmacen> entradas, List<InsumoAlmacen> salidas) {
        this.entradas = entradas;
        this.salidas = salidas;
    }

    public List<InsumoAlmacen> getEntradas() {
        return entradas;
    }

    public void setEntradas(List<InsumoAlmacen> entradas) {
        this.entradas = entradas;
    }

    public List<InsumoAlmacen> getSalidas() {
        return salidas;
    }

    public void setSalidas(List<InsumoAlmacen> salidas) {
        this.salidas = salidas;
    }

}
