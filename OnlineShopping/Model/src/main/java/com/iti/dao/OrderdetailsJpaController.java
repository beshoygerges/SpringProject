/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.iti.dao;

import com.iti.dao.exceptions.NonexistentEntityException;
import com.iti.dao.exceptions.PreexistingEntityException;
import com.iti.entity.Orderdetails;
import com.iti.entity.OrderdetailsPK;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.iti.entity.Orders;
import com.iti.entity.Products;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;


public class OrderdetailsJpaController implements Serializable {

    public OrderdetailsJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Orderdetails orderdetails) throws PreexistingEntityException, Exception {
        if (orderdetails.getOrderdetailsPK() == null) {
            orderdetails.setOrderdetailsPK(new OrderdetailsPK());
        }
        orderdetails.getOrderdetailsPK().setProductsProductId(orderdetails.getProducts().getProductId());
        orderdetails.getOrderdetailsPK().setOrderId(orderdetails.getOrders().getId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Orders orders = orderdetails.getOrders();
            if (orders != null) {
                orders = em.getReference(orders.getClass(), orders.getId());
                orderdetails.setOrders(orders);
            }
            Products products = orderdetails.getProducts();
            if (products != null) {
                products = em.getReference(products.getClass(), products.getProductId());
                orderdetails.setProducts(products);
            }
            em.persist(orderdetails);
            if (orders != null) {
                orders.getOrderdetailsCollection().add(orderdetails);
                orders = em.merge(orders);
            }
            if (products != null) {
                products.getOrderdetailsCollection().add(orderdetails);
                products = em.merge(products);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findOrderdetails(orderdetails.getOrderdetailsPK()) != null) {
                throw new PreexistingEntityException("Orderdetails " + orderdetails + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Orderdetails orderdetails) throws NonexistentEntityException, Exception {
        orderdetails.getOrderdetailsPK().setProductsProductId(orderdetails.getProducts().getProductId());
        orderdetails.getOrderdetailsPK().setOrderId(orderdetails.getOrders().getId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Orderdetails persistentOrderdetails = em.find(Orderdetails.class, orderdetails.getOrderdetailsPK());
            Orders ordersOld = persistentOrderdetails.getOrders();
            Orders ordersNew = orderdetails.getOrders();
            Products productsOld = persistentOrderdetails.getProducts();
            Products productsNew = orderdetails.getProducts();
            if (ordersNew != null) {
                ordersNew = em.getReference(ordersNew.getClass(), ordersNew.getId());
                orderdetails.setOrders(ordersNew);
            }
            if (productsNew != null) {
                productsNew = em.getReference(productsNew.getClass(), productsNew.getProductId());
                orderdetails.setProducts(productsNew);
            }
            orderdetails = em.merge(orderdetails);
            if (ordersOld != null && !ordersOld.equals(ordersNew)) {
                ordersOld.getOrderdetailsCollection().remove(orderdetails);
                ordersOld = em.merge(ordersOld);
            }
            if (ordersNew != null && !ordersNew.equals(ordersOld)) {
                ordersNew.getOrderdetailsCollection().add(orderdetails);
                ordersNew = em.merge(ordersNew);
            }
            if (productsOld != null && !productsOld.equals(productsNew)) {
                productsOld.getOrderdetailsCollection().remove(orderdetails);
                productsOld = em.merge(productsOld);
            }
            if (productsNew != null && !productsNew.equals(productsOld)) {
                productsNew.getOrderdetailsCollection().add(orderdetails);
                productsNew = em.merge(productsNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                OrderdetailsPK id = orderdetails.getOrderdetailsPK();
                if (findOrderdetails(id) == null) {
                    throw new NonexistentEntityException("The orderdetails with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(OrderdetailsPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Orderdetails orderdetails;
            try {
                orderdetails = em.getReference(Orderdetails.class, id);
                orderdetails.getOrderdetailsPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The orderdetails with id " + id + " no longer exists.", enfe);
            }
            Orders orders = orderdetails.getOrders();
            if (orders != null) {
                orders.getOrderdetailsCollection().remove(orderdetails);
                orders = em.merge(orders);
            }
            Products products = orderdetails.getProducts();
            if (products != null) {
                products.getOrderdetailsCollection().remove(orderdetails);
                products = em.merge(products);
            }
            em.remove(orderdetails);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Orderdetails> findOrderdetailsEntities() {
        return findOrderdetailsEntities(true, -1, -1);
    }

    public List<Orderdetails> findOrderdetailsEntities(int maxResults, int firstResult) {
        return findOrderdetailsEntities(false, maxResults, firstResult);
    }

    private List<Orderdetails> findOrderdetailsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Orderdetails.class));
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

    public Orderdetails findOrderdetails(OrderdetailsPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Orderdetails.class, id);
        } finally {
            em.close();
        }
    }

    public int getOrderdetailsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Orderdetails> rt = cq.from(Orderdetails.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
