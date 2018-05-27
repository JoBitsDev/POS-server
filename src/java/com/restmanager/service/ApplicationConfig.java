/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.restmanager.service;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author Jorge
 */
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    @Override
    public Set<Object> getSingletons() {
        return super.getSingletons(); 
    }
    
    

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(com.restmanager.service.AlmacenFacadeREST.class);
        resources.add(com.restmanager.service.AreaFacadeREST.class);
        resources.add(com.restmanager.service.CartaFacadeREST.class);
        resources.add(com.restmanager.service.ClienteFacadeREST.class);
        resources.add(com.restmanager.service.CocinaFacadeREST.class);
        resources.add(com.restmanager.service.DatosPersonalesFacadeREST.class);
        resources.add(com.restmanager.service.InsumoFacadeREST.class);
        resources.add(com.restmanager.service.MesaFacadeREST.class);
        resources.add(com.restmanager.service.OrdenFacadeREST.class);
        resources.add(com.restmanager.service.PersonalFacadeREST.class);
        resources.add(com.restmanager.service.ProductoInsumoFacadeREST.class);
        resources.add(com.restmanager.service.ProductoVentaFacadeREST.class);
        resources.add(com.restmanager.service.ProductovOrdenFacadeREST.class);
        resources.add(com.restmanager.service.PuestoTrabajoFacadeREST.class);
        resources.add(com.restmanager.service.SeccionFacadeREST.class);
        resources.add(com.restmanager.service.VentaFacadeREST.class);
    }
    
}
