/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jobits.pos.service;

import java.util.Map;
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
     * Do not modify addRestResourceClasses() method. It is automatically
     * populated with all resources defined in the project. If required, comment
     * out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(com.jobits.pos.service.AlmacenFacadeREST.class);
        resources.add(com.jobits.pos.service.CartaFacadeREST.class);
        resources.add(com.jobits.pos.service.CocinaFacadeREST.class);
        resources.add(com.jobits.pos.service.MesaFacadeREST.class);
        resources.add(com.jobits.pos.service.OrdenFacadeREST.class);
        resources.add(com.jobits.pos.service.PersonalFacadeREST.class);
        resources.add(com.jobits.pos.service.ProductoVentaFacadeREST.class);
        resources.add(com.jobits.pos.service.ProductovOrdenFacadeREST.class);
        resources.add(com.jobits.pos.service.SeccionFacadeREST.class);
        resources.add(com.jobits.pos.service.VentaFacadeREST.class);
        resources.add(com.jobits.pos.service.NotificacionEnvioCocinaFacadeREST.class);

        resources.add(com.jobits.pos.authentication.AuthenticationFilter.class);
    }

}
