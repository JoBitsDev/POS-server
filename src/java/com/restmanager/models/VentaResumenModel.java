/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.restmanager.models;

import com.restmanager.Venta;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
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

    private final  List<AreaListModel> areas;
    private final List<DpteListModel> dptes;
    private final List<PuntoElaboracionListModel> ptosElaboracion;

    public VentaResumenModel(Venta v,List<AreaListModel> areas, List<DpteListModel> dptes, List<PuntoElaboracionListModel> ptosElaboracion) {
        ventaTotal = VentaCalculator.getValorTotalVentas(v);
        ventaNeta = VentaCalculator.getValorTotalVentasNeta(v);
        gastosInsumo = VentaCalculator.getValorTotalGastosInsumo(v);
        gastosSalario = VentaCalculator.getValorTotalPagoTrabajadores(v);
        autorizos = VentaCalculator.getValorTotalVentasCasa(v);
        gastosOtros = VentaCalculator.getValorTotalOtrosGastos(v);
        this.areas = areas;
        this.dptes = dptes;
        this.ptosElaboracion = ptosElaboracion;
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

    public List<AreaListModel> getAreas() {
        return areas;
    }

    public List<DpteListModel> getDptes() {
        return dptes;
    }

    public List<PuntoElaboracionListModel> getPtosElaboracion() {
        return ptosElaboracion;
    }    
}
