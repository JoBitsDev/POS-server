/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jobits.pos.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobits.pos.persistence.Venta;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.core.Response;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author Jorge
 */
public abstract class AbstractFacade<T> {

    @Resource
    WebServiceContext webServiceContext;

    private Class<T> entityClass;

    protected EntityManagerFactory e = Persistence.createEntityManagerFactory("Restaurant_Manager_Web_ServicePU");
    protected EntityManager em1 = e.createEntityManager();

    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager();

    public void create(T entity) {
        if (em1.getTransaction().isActive()) {
            em1.persist(entity);
        } else {
            em1.getTransaction().begin();
            em1.persist(entity);
            em1.getTransaction().commit();
        }

    }

    public void edit(T entity) {
        if (em1.getTransaction().isActive()) {
            em1.merge(entity);
        } else {
            em1.getTransaction().begin();
            em1.merge(entity);
            em1.getTransaction().commit();
        }
    }

    public void remove(T entity) {
        if (em1.getTransaction().isActive()) {
            em1.remove(em1.merge(entity));
        } else {
            em1.getTransaction().begin();
            em1.remove(em1.merge(entity));
            em1.getTransaction().commit();
        }
    }

    public T find(Object id) {
        e.getCache().evictAll();
        EntityManager aux = e.createEntityManager();
        return aux.find(entityClass, id);
    }

    public List<T> findAll() {
        e.getCache().evictAll();
        em1.close();
        em1 = e.createEntityManager();

        javax.persistence.criteria.CriteriaQuery cq = em1.getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return em1.createQuery(cq).getResultList();

    }

    public List findAll(Class entity) {
        e.getCache().evictAll();
        em1.close();
        em1 = e.createEntityManager();

        javax.persistence.criteria.CriteriaQuery cq = em1.getCriteriaBuilder().createQuery();
        cq.select(cq.from(entity));
        return em1.createQuery(cq).getResultList();

    }

    public List<T> findRange(int[] range) {
        javax.persistence.criteria.CriteriaQuery cq = em1.getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        javax.persistence.Query q = em1.createQuery(cq);
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        javax.persistence.criteria.CriteriaQuery cq = em1.getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        cq.select(em1.getCriteriaBuilder().count(rt));
        javax.persistence.Query q = em1.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    public Venta findVenta() {
        e.getCache().evictAll();
        javax.persistence.criteria.CriteriaQuery cq = em1.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Venta.class));
        List<Venta> ventas = em1.createQuery(cq).getResultList();
        for (int i = ventas.size() - 1; i >= 0; i--) {
            if (ventas.get(i).getVentaTotal() == null) {
                return ventas.get(i);
            }
        }

        return null;
    }

    protected Response handleException(Exception ex) {
        if (ex instanceof JsonProcessingException) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error en el Object Mapper. Contacte con soporte").build();
        }
        System.out.println(ex.getStackTrace()[0]);
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Contacte con soporte").build();
    }

    protected Response toJsonString(Response.Status status, Object o) {
        try {
            return Response.status(status).entity(new ObjectMapper().writeValueAsString(o)).build();
        } catch (JsonProcessingException ex) {
            return handleException(ex);
        }
    }
}
