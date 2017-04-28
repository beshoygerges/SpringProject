/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.iti.dao;

import com.iti.dao.exceptions.IllegalOrphanException;
import com.iti.dao.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.iti.entity.Orderdetails;
import com.iti.entity.Products;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;


public class ProductsJpaController implements Serializable {

    public ProductsJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Products products) {
        if (products.getOrderdetailsCollection() == null) {
            products.setOrderdetailsCollection(new ArrayList<Orderdetails>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Orderdetails> attachedOrderdetailsCollection = new ArrayList<Orderdetails>();
            for (Orderdetails orderdetailsCollectionOrderdetailsToAttach : products.getOrderdetailsCollection()) {
                orderdetailsCollectionOrderdetailsToAttach = em.getReference(orderdetailsCollectionOrderdetailsToAttach.getClass(), orderdetailsCollectionOrderdetailsToAttach.getOrderdetailsPK());
                attachedOrderdetailsCollection.add(orderdetailsCollectionOrderdetailsToAttach);
            }
            products.setOrderdetailsCollection(attachedOrderdetailsCollection);
            em.persist(products);
            for (Orderdetails orderdetailsCollectionOrderdetails : products.getOrderdetailsCollection()) {
                Products oldProductsOfOrderdetailsCollectionOrderdetails = orderdetailsCollectionOrderdetails.getProducts();
                orderdetailsCollectionOrderdetails.setProducts(products);
                orderdetailsCollectionOrderdetails = em.merge(orderdetailsCollectionOrderdetails);
                if (oldProductsOfOrderdetailsCollectionOrderdetails != null) {
                    oldProductsOfOrderdetailsCollectionOrderdetails.getOrderdetailsCollection().remove(orderdetailsCollectionOrderdetails);
                    oldProductsOfOrderdetailsCollectionOrderdetails = em.merge(oldProductsOfOrderdetailsCollectionOrderdetails);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Products products) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Products persistentProducts = em.find(Products.class, products.getProductId());
            Collection<Orderdetails> orderdetailsCollectionOld = persistentProducts.getOrderdetailsCollection();
            Collection<Orderdetails> orderdetailsCollectionNew = products.getOrderdetailsCollection();
            List<String> illegalOrphanMessages = null;
            for (Orderdetails orderdetailsCollectionOldOrderdetails : orderdetailsCollectionOld) {
                if (!orderdetailsCollectionNew.contains(orderdetailsCollectionOldOrderdetails)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Orderdetails " + orderdetailsCollectionOldOrderdetails + " since its products field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Orderdetails> attachedOrderdetailsCollectionNew = new ArrayList<Orderdetails>();
            for (Orderdetails orderdetailsCollectionNewOrderdetailsToAttach : orderdetailsCollectionNew) {
                orderdetailsCollectionNewOrderdetailsToAttach = em.getReference(orderdetailsCollectionNewOrderdetailsToAttach.getClass(), orderdetailsCollectionNewOrderdetailsToAttach.getOrderdetailsPK());
                attachedOrderdetailsCollectionNew.add(orderdetailsCollectionNewOrderdetailsToAttach);
            }
            orderdetailsCollectionNew = attachedOrderdetailsCollectionNew;
            products.setOrderdetailsCollection(orderdetailsCollectionNew);
            products = em.merge(products);
            for (Orderdetails orderdetailsCollectionNewOrderdetails : orderdetailsCollectionNew) {
                if (!orderdetailsCollectionOld.contains(orderdetailsCollectionNewOrderdetails)) {
                    Products oldProductsOfOrderdetailsCollectionNewOrderdetails = orderdetailsCollectionNewOrderdetails.getProducts();
                    orderdetailsCollectionNewOrderdetails.setProducts(products);
                    orderdetailsCollectionNewOrderdetails = em.merge(orderdetailsCollectionNewOrderdetails);
                    if (oldProductsOfOrderdetailsCollectionNewOrderdetails != null && !oldProductsOfOrderdetailsCollectionNewOrderdetails.equals(products)) {
                        oldProductsOfOrderdetailsCollectionNewOrderdetails.getOrderdetailsCollection().remove(orderdetailsCollectionNewOrderdetails);
                        oldProductsOfOrderdetailsCollectionNewOrderdetails = em.merge(oldProductsOfOrderdetailsCollectionNewOrderdetails);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = products.getProductId();
                if (findProducts(id) == null) {
                    throw new NonexistentEntityException("The products with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Products products;
            try {
                products = em.getReference(Products.class, id);
                products.getProductId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The products with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Orderdetails> orderdetailsCollectionOrphanCheck = products.getOrderdetailsCollection();
            for (Orderdetails orderdetailsCollectionOrphanCheckOrderdetails : orderdetailsCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Products (" + products + ") cannot be destroyed since the Orderdetails " + orderdetailsCollectionOrphanCheckOrderdetails + " in its orderdetailsCollection field has a non-nullable products field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(products);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Products> findProductsEntities() {
        return findProductsEntities(true, -1, -1);
    }

    public List<Products> findProductsEntities(int maxResults, int firstResult) {
        return findProductsEntities(false, maxResults, firstResult);
    }

    private List<Products> findProductsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Products.class));
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

    public Products findProducts(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Products.class, id);
        } finally {
            em.close();
        }
    }

    public int getProductsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Products> rt = cq.from(Products.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
