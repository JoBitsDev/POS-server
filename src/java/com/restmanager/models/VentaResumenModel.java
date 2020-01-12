/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.restmanager.models;

import com.restmanager.Venta;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Clase para enviar la informacion en JSON y parsearla en el dispositivo
 * destino
 *
 * @author Jorge
 *
 */
@XmlRootElement
public class VentaResumenModel {

    private final float ventaTotal,
            ventaNeta,
            gastosInsumo,
            gastosSalario,
            autorizos,
            gastosOtros;

    public VentaResumenModel(Venta v) {
        ventaTotal = VentaCalculator.getValorTotalVentas(v);
        ventaNeta = VentaCalculator.getValorTotalVentasNeta(v);
        gastosInsumo = VentaCalculator.getValorTotalGastosInsumo(v);
        gastosSalario = VentaCalculator.getValorTotalPagoTrabajadores(v);
        float total = 0;
        autorizos = VentaCalculator.getValorTotalVentasCasa(v);
        gastosOtros = VentaCalculator.getValorTotalOtrosGastos(v);
    }

    public float getVentaTotal() {
        return ventaTotal;
    }

    public float getVentaNeta() {
        return ventaNeta;
    }

    public float getGastosInsumo() {
        return gastosInsumo;
    }

    public float getGastosSalario() {
        return gastosSalario;
    }

    public float getAutorizos() {
        return autorizos;
    }

    public float getGastosOtros() {
        return gastosOtros;
    }

}
