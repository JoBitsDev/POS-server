/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.restmanager;

import com.restmanager.exceptions.IllegalOrphanException;
import com.restmanager.exceptions.NonexistentEntityException;
import com.restmanager.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * FirstDream
 * @author Jorge
 * 
 */
public class CocinaJpaController implements Serializable {

    public CocinaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Cocina cocina) throws PreexistingEntityException, Exception {
        if (cocina.getProductoVentaList() == null) {
            cocina.setProductoVentaList(new ArrayList<ProductoVenta>());
        }
        if (cocina.getIpvList() == null) {
            cocina.setIpvList(new ArrayList<Ipv>());
        }
        if (cocina.getImpresoraList() == null) {
            cocina.setImpresoraList(new ArrayList<Impresora>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<ProductoVenta> attachedProductoVentaList = new ArrayList<ProductoVenta>();
            for (ProductoVenta productoVentaListProductoVentaToAttach : cocina.getProductoVentaList()) {
                productoVentaListProductoVentaToAttach = em.getReference(productoVentaListProductoVentaToAttach.getClass(), productoVentaListProductoVentaToAttach.getPCod());
                attachedProductoVentaList.add(productoVentaListProductoVentaToAttach);
            }
            cocina.setProductoVentaList(attachedProductoVentaList);
            List<Ipv> attachedIpvList = new ArrayList<Ipv>();
            for (Ipv ipvListIpvToAttach : cocina.getIpvList()) {
                ipvListIpvToAttach = em.getReference(ipvListIpvToAttach.getClass(), ipvListIpvToAttach.getIpvPK());
                attachedIpvList.add(ipvListIpvToAttach);
            }
            cocina.setIpvList(attachedIpvList);
            List<Impresora> attachedImpresoraList = new ArrayList<Impresora>();
            for (Impresora impresoraListImpresoraToAttach : cocina.getImpresoraList()) {
                impresoraListImpresoraToAttach = em.getReference(impresoraListImpresoraToAttach.getClass(), impresoraListImpresoraToAttach.getCodImpresora());
                attachedImpresoraList.add(impresoraListImpresoraToAttach);
            }
            cocina.setImpresoraList(attachedImpresoraList);
            em.persist(cocina);
            for (ProductoVenta productoVentaListProductoVenta : cocina.getProductoVentaList()) {
                Cocina oldCocinacodCocinaOfProductoVentaListProductoVenta = productoVentaListProductoVenta.getCocinacodCocina();
                productoVentaListProductoVenta.setCocinacodCocina(cocina);
                productoVentaListProductoVenta = em.merge(productoVentaListProductoVenta);
                if (oldCocinacodCocinaOfProductoVentaListProductoVenta != null) {
                    oldCocinacodCocinaOfProductoVentaListProductoVenta.getProductoVentaList().remove(productoVentaListProductoVenta);
                    oldCocinacodCocinaOfProductoVentaListProductoVenta = em.merge(oldCocinacodCocinaOfProductoVentaListProductoVenta);
                }
            }
            for (Ipv ipvListIpv : cocina.getIpvList()) {
                Cocina oldCocinaOfIpvListIpv = ipvListIpv.getCocina();
                ipvListIpv.setCocina(cocina);
                ipvListIpv = em.merge(ipvListIpv);
                if (oldCocinaOfIpvListIpv != null) {
                    oldCocinaOfIpvListIpv.getIpvList().remove(ipvListIpv);
                    oldCocinaOfIpvListIpv = em.merge(oldCocinaOfIpvListIpv);
                }
            }
            for (Impresora impresoraListImpresora : cocina.getImpresoraList()) {
                Cocina oldCocinacodCocinaOfImpresoraListImpresora = impresoraListImpresora.getCocinacodCocina();
                impresoraListImpresora.setCocinacodCocina(cocina);
                impresoraListImpresora = em.merge(impresoraListImpresora);
                if (oldCocinacodCocinaOfImpresoraListImpresora != null) {
                    oldCocinacodCocinaOfImpresoraListImpresora.getImpresoraList().remove(impresoraListImpresora);
                    oldCocinacodCocinaOfImpresoraListImpresora = em.merge(oldCocinacodCocinaOfImpresoraListImpresora);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCocina(cocina.getCodCocina()) != null) {
                throw new PreexistingEntityException("Cocina " + cocina + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Cocina cocina) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cocina persistentCocina = em.find(Cocina.class, cocina.getCodCocina());
            List<ProductoVenta> productoVentaListOld = persistentCocina.getProductoVentaList();
            List<ProductoVenta> productoVentaListNew = cocina.getProductoVentaList();
            List<Ipv> ipvListOld = persistentCocina.getIpvList();
            List<Ipv> ipvListNew = cocina.getIpvList();
            List<Impresora> impresoraListOld = persistentCocina.getImpresoraList();
            List<Impresora> impresoraListNew = cocina.getImpresoraList();
            List<String> illegalOrphanMessages = null;
            for (Ipv ipvListOldIpv : ipvListOld) {
                if (!ipvListNew.contains(ipvListOldIpv)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Ipv " + ipvListOldIpv + " since its cocina field is not nullable.");
                }
            }
            for (Impresora impresoraListOldImpresora : impresoraListOld) {
                if (!impresoraListNew.contains(impresoraListOldImpresora)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Impresora " + impresoraListOldImpresora + " since its cocinacodCocina field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<ProductoVenta> attachedProductoVentaListNew = new ArrayList<ProductoVenta>();
            for (ProductoVenta productoVentaListNewProductoVentaToAttach : productoVentaListNew) {
                productoVentaListNewProductoVentaToAttach = em.getReference(productoVentaListNewProductoVentaToAttach.getClass(), productoVentaListNewProductoVentaToAttach.getPCod());
                attachedProductoVentaListNew.add(productoVentaListNewProductoVentaToAttach);
            }
            productoVentaListNew = attachedProductoVentaListNew;
            cocina.setProductoVentaList(productoVentaListNew);
            List<Ipv> attachedIpvListNew = new ArrayList<Ipv>();
            for (Ipv ipvListNewIpvToAttach : ipvListNew) {
                ipvListNewIpvToAttach = em.getReference(ipvListNewIpvToAttach.getClass(), ipvListNewIpvToAttach.getIpvPK());
                attachedIpvListNew.add(ipvListNewIpvToAttach);
            }
            ipvListNew = attachedIpvListNew;
            cocina.setIpvList(ipvListNew);
            List<Impresora> attachedImpresoraListNew = new ArrayList<Impresora>();
            for (Impresora impresoraListNewImpresoraToAttach : impresoraListNew) {
                impresoraListNewImpresoraToAttach = em.getReference(impresoraListNewImpresoraToAttach.getClass(), impresoraListNewImpresoraToAttach.getCodImpresora());
                attachedImpresoraListNew.add(impresoraListNewImpresoraToAttach);
            }
            impresoraListNew = attachedImpresoraListNew;
            cocina.setImpresoraList(impresoraListNew);
            cocina = em.merge(cocina);
            for (ProductoVenta productoVentaListOldProductoVenta : productoVentaListOld) {
                if (!productoVentaListNew.contains(productoVentaListOldProductoVenta)) {
                    productoVentaListOldProductoVenta.setCocinacodCocina(null);
                    productoVentaListOldProductoVenta = em.merge(productoVentaListOldProductoVenta);
                }
            }
            for (ProductoVenta productoVentaListNewProductoVenta : productoVentaListNew) {
                if (!productoVentaListOld.contains(productoVentaListNewProductoVenta)) {
                    Cocina oldCocinacodCocinaOfProductoVentaListNewProductoVenta = productoVentaListNewProductoVenta.getCocinacodCocina();
                    productoVentaListNewProductoVenta.setCocinacodCocina(cocina);
                    productoVentaListNewProductoVenta = em.merge(productoVentaListNewProductoVenta);
                    if (oldCocinacodCocinaOfProductoVentaListNewProductoVenta != null && !oldCocinacodCocinaOfProductoVentaListNewProductoVenta.equals(cocina)) {
                        oldCocinacodCocinaOfProductoVentaListNewProductoVenta.getProductoVentaList().remove(productoVentaListNewProductoVenta);
                        oldCocinacodCocinaOfProductoVentaListNewProductoVenta = em.merge(oldCocinacodCocinaOfProductoVentaListNewProductoVenta);
                    }
                }
            }
            for (Ipv ipvListNewIpv : ipvListNew) {
                if (!ipvListOld.contains(ipvListNewIpv)) {
                    Cocina oldCocinaOfIpvListNewIpv = ipvListNewIpv.getCocina();
                    ipvListNewIpv.setCocina(cocina);
                    ipvListNewIpv = em.merge(ipvListNewIpv);
                    if (oldCocinaOfIpvListNewIpv != null && !oldCocinaOfIpvListNewIpv.equals(cocina)) {
                        oldCocinaOfIpvListNewIpv.getIpvList().remove(ipvListNewIpv);
                        oldCocinaOfIpvListNewIpv = em.merge(oldCocinaOfIpvListNewIpv);
                    }
                }
            }
            for (Impresora impresoraListNewImpresora : impresoraListNew) {
                if (!impresoraListOld.contains(impresoraListNewImpresora)) {
                    Cocina oldCocinacodCocinaOfImpresoraListNewImpresora = impresoraListNewImpresora.getCocinacodCocina();
                    impresoraListNewImpresora.setCocinacodCocina(cocina);
                    impresoraListNewImpresora = em.merge(impresoraListNewImpresora);
                    if (oldCocinacodCocinaOfImpresoraListNewImpresora != null && !oldCocinacodCocinaOfImpresoraListNewImpresora.equals(cocina)) {
                        oldCocinacodCocinaOfImpresoraListNewImpresora.getImpresoraList().remove(impresoraListNewImpresora);
                        oldCocinacodCocinaOfImpresoraListNewImpresora = em.merge(oldCocinacodCocinaOfImpresoraListNewImpresora);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = cocina.getCodCocina();
                if (findCocina(id) == null) {
                    throw new NonexistentEntityException("The cocina with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cocina cocina;
            try {
                cocina = em.getReference(Cocina.class, id);
                cocina.getCodCocina();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cocina with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Ipv> ipvListOrphanCheck = cocina.getIpvList();
            for (Ipv ipvListOrphanCheckIpv : ipvListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Cocina (" + cocina + ") cannot be destroyed since the Ipv " + ipvListOrphanCheckIpv + " in its ipvList field has a non-nullable cocina field.");
            }
            List<Impresora> impresoraListOrphanCheck = cocina.getImpresoraList();
            for (Impresora impresoraListOrphanCheckImpresora : impresoraListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Cocina (" + cocina + ") cannot be destroyed since the Impresora " + impresoraListOrphanCheckImpresora + " in its impresoraList field has a non-nullable cocinacodCocina field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<ProductoVenta> productoVentaList = cocina.getProductoVentaList();
            for (ProductoVenta productoVentaListProductoVenta : productoVentaList) {
                productoVentaListProductoVenta.setCocinacodCocina(null);
                productoVentaListProductoVenta = em.merge(productoVentaListProductoVenta);
            }
            em.remove(cocina);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Cocina> findCocinaEntities() {
        return findCocinaEntities(true, -1, -1);
    }

    public List<Cocina> findCocinaEntities(int maxResults, int firstResult) {
        return findCocinaEntities(false, maxResults, firstResult);
    }

    private List<Cocina> findCocinaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cocina.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Cocina findCocina(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cocina.class, id);
        } finally {
            em.close();
        }
    }

    public int getCocinaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cocina> rt = cq.from(Cocina.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
