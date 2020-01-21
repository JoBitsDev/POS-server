/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jobits.pos.controllers;

import com.jobits.pos.persistence.Area;
import com.jobits.pos.persistence.Cocina;
import com.jobits.pos.persistence.Personal;
import com.jobits.pos.persistence.Venta;
import com.jobits.pos.persistence.models.AreaListModel;
import com.jobits.pos.persistence.models.DpteListModel;
import com.jobits.pos.persistence.models.PuntoElaboracionListModel;
import com.jobits.pos.persistence.models.VentaCalculator;
import com.jobits.pos.persistence.models.VentaResumenModel;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Capa: Controllers
 *
 * clase encargada de crear el objeto {@link  VentaResumenModel}
 *
 * FirstDream
 *
 * @author Jorge
 *
 */
public class VentaResumenController {

    protected static EntityManagerFactory e = Persistence.createEntityManagerFactory("Restaurant_Manager_Web_ServicePU");
    protected static EntityManager em1 = e.createEntityManager();

    public static VentaResumenModel createResumenFromVenta(Venta v) {

        List<DpteListModel> dptes = getDpteListModel(v);
        List<AreaListModel> areas = getAreaListModel(v);
        List<PuntoElaboracionListModel> ptosElaboracion = getPtosElaboracionListmodel(v);
        return new VentaResumenModel(v, areas, dptes, ptosElaboracion);

    }

    private static List<DpteListModel> getDpteListModel(Venta v) {
        List<DpteListModel> dptes = new ArrayList<>();

        for (Personal p : (List<Personal>) findAll(Personal.class)) {
            DpteListModel auxModel = VentaCalculator.getResumenVentasCamareroOnModel(v, p);
            if (auxModel != null) {
                dptes.add(auxModel);
            }
        }
        return dptes;
    }

    private static List<AreaListModel> getAreaListModel(Venta v) {
        List<AreaListModel> areas = new ArrayList<>();
        for (Area a : (List<Area>)findAll(Area.class)) {
            AreaListModel auxModel = VentaCalculator.getResumenVentaPorAreaOnModel(v, a);
            if (auxModel != null) {
                areas.add(auxModel);
            }
        }
        return areas;
    }

    private static List<PuntoElaboracionListModel> getPtosElaboracionListmodel(Venta v) {
        List<PuntoElaboracionListModel> puntos = new ArrayList<>();
        for (Cocina c : (List<Cocina>)findAll(Cocina.class)) {
            PuntoElaboracionListModel auxModel = VentaCalculator.getResumenVentasCocinaOnTable(v, c);
            if (auxModel != null) {
                puntos.add(auxModel);
            }
        }
        return puntos;
        
    }

    private static List findAll(Class entityClass) {
        e.getCache().evictAll();
        em1.close();
        em1 = e.createEntityManager();

        javax.persistence.criteria.CriteriaQuery cq = em1.getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return em1.createQuery(cq).getResultList();

    }

}
