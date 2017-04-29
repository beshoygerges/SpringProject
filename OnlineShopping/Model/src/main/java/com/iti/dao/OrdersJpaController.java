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
import com.iti.entity.User;
import com.iti.entity.Orderdetails;
import com.iti.entity.Orders;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;


public class OrdersJpaController implements Serializable {

    public OrdersJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Orders orders) {
        if (orders.getOrderdetailsCollection() == null) {
            orders.setOrderdetailsCollection(new ArrayList<Orderdetails>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            User useremail = orders.getUseremail();
            if (useremail != null) {
                useremail = em.getReference(useremail.getClass(), useremail.getEmail());
                orders.setUseremail(useremail);
            }
            Collection<Orderdetails> attachedOrderdetailsCollection = new ArrayList<Orderdetails>();
            for (Orderdetails orderdetailsCollectionOrderdetailsToAttach : orders.getOrderdetailsCollection()) {
                orderdetailsCollectionOrderdetailsToAttach = em.getReference(orderdetailsCollectionOrderdetailsToAttach.getClass(), orderdetailsCollectionOrderdetailsToAttach.getOrderdetailsPK());
                attachedOrderdetailsCollection.add(orderdetailsCollectionOrderdetailsToAttach);
            }
            orders.setOrderdetailsCollection(attachedOrderdetailsCollection);
            em.persist(orders);
            if (useremail != null) {
                useremail.getOrdersCollection().add(orders);
                useremail = em.merge(useremail);
            }
            for (Orderdetails orderdetailsCollectionOrderdetails : orders.getOrderdetailsCollection()) {
                Orders oldOrdersOfOrderdetailsCollectionOrderdetails = orderdetailsCollectionOrderdetails.getOrders();
                orderdetailsCollectionOrderdetails.setOrders(orders);
                orderdetailsCollectionOrderdetails = em.merge(orderdetailsCollectionOrderdetails);
                if (oldOrdersOfOrderdetailsCollectionOrderdetails != null) {
                    oldOrdersOfOrderdetailsCollectionOrderdetails.getOrderdetailsCollection().remove(orderdetailsCollectionOrderdetails);
                    oldOrdersOfOrderdetailsCollectionOrderdetails = em.merge(oldOrdersOfOrderdetailsCollectionOrderdetails);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Orders orders) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Orders persistentOrders = em.find(Orders.class, orders.getId());
            User useremailOld = persistentOrders.getUseremail();
            User useremailNew = orders.getUseremail();
            Collection<Orderdetails> orderdetailsCollectionOld = persistentOrders.getOrderdetailsCollection();
            Collection<Orderdetails> orderdetailsCollectionNew = orders.getOrderdetailsCollection();
            List<String> illegalOrphanMessages = null;
            for (Orderdetails orderdetailsCollectionOldOrderdetails : orderdetailsCollectionOld) {
                if (!orderdetailsCollectionNew.contains(orderdetailsCollectionOldOrderdetails)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Orderdetails " + orderdetailsCollectionOldOrderdetails + " since its orders field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (useremailNew != null) {
                useremailNew = em.getReference(useremailNew.getClass(), useremailNew.getEmail());
                orders.setUseremail(useremailNew);
            }
            Collection<Orderdetails> attachedOrderdetailsCollectionNew = new ArrayList<Orderdetails>();
            for (Orderdetails orderdetailsCollectionNewOrderdetailsToAttach : orderdetailsCollectionNew) {
                orderdetailsCollectionNewOrderdetailsToAttach = em.getReference(orderdetailsCollectionNewOrderdetailsToAttach.getClass(), orderdetailsCollectionNewOrderdetailsToAttach.getOrderdetailsPK());
                attachedOrderdetailsCollectionNew.add(orderdetailsCollectionNewOrderdetailsToAttach);
            }
            orderdetailsCollectionNew = attachedOrderdetailsCollectionNew;
            orders.setOrderdetailsCollection(orderdetailsCollectionNew);
            orders = em.merge(orders);
            if (useremailOld != null && !useremailOld.equals(useremailNew)) {
                useremailOld.getOrdersCollection().remove(orders);
                useremailOld = em.merge(useremailOld);
            }
            if (useremailNew != null && !useremailNew.equals(useremailOld)) {
                useremailNew.getOrdersCollection().add(orders);
                useremailNew = em.merge(useremailNew);
            }
            for (Orderdetails orderdetailsCollectionNewOrderdetails : orderdetailsCollectionNew) {
                if (!orderdetailsCollectionOld.contains(orderdetailsCollectionNewOrderdetails)) {
                    Orders oldOrdersOfOrderdetailsCollectionNewOrderdetails = orderdetailsCollectionNewOrderdetails.getOrders();
                    orderdetailsCollectionNewOrderdetails.setOrders(orders);
                    orderdetailsCollectionNewOrderdetails = em.merge(orderdetailsCollectionNewOrderdetails);
                    if (oldOrdersOfOrderdetailsCollectionNewOrderdetails != null && !oldOrdersOfOrderdetailsCollectionNewOrderdetails.equals(orders)) {
                        oldOrdersOfOrderdetailsCollectionNewOrderdetails.getOrderdetailsCollection().remove(orderdetailsCollectionNewOrderdetails);
                        oldOrdersOfOrderdetailsCollectionNewOrderdetails = em.merge(oldOrdersOfOrderdetailsCollectionNewOrderdetails);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = orders.getId();
                if (findOrders(id) == null) {
                    throw new NonexistentEntityException("The orders with id " + id + " no longer exists.");
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
            Orders orders;
            try {
                orders = em.getReference(Orders.class, id);
                orders.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The orders with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Orderdetails> orderdetailsCollectionOrphanCheck = orders.getOrderdetailsCollection();
            for (Orderdetails orderdetailsCollectionOrphanCheckOrderdetails : orderdetailsCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Orders (" + orders + ") cannot be destroyed since the Orderdetails " + orderdetailsCollectionOrphanCheckOrderdetails + " in its orderdetailsCollection field has a non-nullable orders field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            User useremail = orders.getUseremail();
            if (useremail != null) {
                useremail.getOrdersCollection().remove(orders);
                useremail = em.merge(useremail);
            }
            em.remove(orders);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Orders> findOrdersEntities() {
        return findOrdersEntities(true, -1, -1);
    }

    public List<Orders> findOrdersEntities(int maxResults, int firstResult) {
        return findOrdersEntities(false, maxResults, firstResult);
    }

    private List<Orders> findOrdersEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Orders.class));
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

    public Orders findOrders(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Orders.class, id);
        } finally {
            em.close();
        }
    }

    public int getOrdersCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Orders> rt = cq.from(Orders.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
