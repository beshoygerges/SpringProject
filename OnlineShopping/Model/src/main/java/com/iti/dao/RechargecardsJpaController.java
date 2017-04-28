/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.iti.dao;

import com.iti.dao.exceptions.NonexistentEntityException;
import com.iti.dao.exceptions.PreexistingEntityException;
import com.iti.entity.Rechargecards;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;


public class RechargecardsJpaController implements Serializable {

    public RechargecardsJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Rechargecards rechargecards) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(rechargecards);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findRechargecards(rechargecards.getNumber()) != null) {
                throw new PreexistingEntityException("Rechargecards " + rechargecards + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Rechargecards rechargecards) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            rechargecards = em.merge(rechargecards);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = rechargecards.getNumber();
                if (findRechargecards(id) == null) {
                    throw new NonexistentEntityException("The rechargecards with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Rechargecards rechargecards;
            try {
                rechargecards = em.getReference(Rechargecards.class, id);
                rechargecards.getNumber();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The rechargecards with id " + id + " no longer exists.", enfe);
            }
            em.remove(rechargecards);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Rechargecards> findRechargecardsEntities() {
        return findRechargecardsEntities(true, -1, -1);
    }

    public List<Rechargecards> findRechargecardsEntities(int maxResults, int firstResult) {
        return findRechargecardsEntities(false, maxResults, firstResult);
    }

    private List<Rechargecards> findRechargecardsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Rechargecards.class));
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

    public Rechargecards findRechargecards(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Rechargecards.class, id);
        } finally {
            em.close();
        }
    }

    public int getRechargecardsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Rechargecards> rt = cq.from(Rechargecards.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
