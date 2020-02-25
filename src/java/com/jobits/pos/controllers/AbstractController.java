/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jobits.pos.controllers;

import com.jobits.pos.persistence.Almacen;
import javax.persistence.EntityManager;

/**
 * FirstDream
 *
 * @author Jorge
 *
 */
public abstract class AbstractController {

    protected EntityManager em1;

    public AbstractController(EntityManager em1) {
        this.em1 = em1;
    }
    

}
