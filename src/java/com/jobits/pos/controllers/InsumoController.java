/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jobits.pos.controllers;

import com.jobits.pos.persistence.Insumo;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;

/**
 * FirstDream
 *
 * @author Jorge
 *
 */
public class InsumoController {

    private final EntityManager em1;

    public InsumoController(EntityManager em1) {
        this.em1 = em1;
    }

//    public InsumoController() {
//        em1 = Persistence.createEntityManagerFactory("Restaurant_Manager_Web_ServicePU").createEntityManager();
//    }

    public Insumo create(String insumoNombre, String um, float estimacionStock) {
        Insumo instance = new Insumo(generateStringCode("In-"));
        instance.setElaborado(false);
        instance.setCostoPorUnidad(Float.valueOf("0"));
        instance.setStockEstimation(estimacionStock);

        instance.setNombre(insumoNombre);
        instance.setUm(um);
        em1.persist(instance);
        return instance;

    }

    /**
     * Tu generate IDs for the relational model of the application
     *
     * @param prefix includign the '-' char ej: "P-"
     * @return
     */
    public String generateStringCode(String prefix) {
        int cont = 1;
        Insumo a = em1.find(Insumo.class, prefix + "" + cont);
        while (a != null) {
            cont++;
            a = em1.find(Insumo.class, prefix + "" + cont);
        }

        return prefix + "" + cont;
    }
    
    

}
