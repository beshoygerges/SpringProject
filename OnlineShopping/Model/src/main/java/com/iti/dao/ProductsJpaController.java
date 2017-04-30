/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iti.dao;

import java.io.Serializable;
import com.iti.entity.Products;
import com.iti.productInterface.ProductInteface;
import java.util.ArrayList;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class ProductsJpaController implements Serializable, ProductInteface {

    @PersistenceContext
    private EntityManager emf;

    @Override
    public boolean addProduct(Products productObj) {
        // Products product = new Products(12, "112", 0, 0, "imageUrl", "description", "categoryName");
        emf.persist(productObj);
        return false;
    }

    @Override
    public ArrayList<Products> findProductByCategory(Products productObj) {
        return (ArrayList<Products>) emf.createNamedQuery("Products.findByCategoryName").setParameter("categoryName", productObj.getCategoryName()).getResultList();

    }

    @Override
    public ArrayList<Products> findProductById(int productId) {
        return (ArrayList<Products>) emf.createNamedQuery("Products.findByProductId").setParameter("productId", productId).getResultList();
    }

    @Override
    public ArrayList<Products> getAllProduct() {
        ArrayList<Products> productList = (ArrayList<Products>) emf.createNamedQuery("Products.findAll").getResultList();
        return productList;
    }

    @Override
    public boolean removeProduct(Products productObj) {
        ArrayList<Products> p = (ArrayList<Products>) emf.createNamedQuery("Products.findByProductId").setParameter("productId", productObj.getProductId()).getResultList();
        if (p.get(0) != null) {
            emf.remove(p.get(0));
            return true;
        } else {
            System.out.println("product not fonund");
        }
        return false;
    }

    @Override
    public boolean updateProduct(Products productObj) {
        ArrayList<Products> l = (ArrayList<Products>) emf.createNamedQuery("Products.findByProductId").setParameter("productId", productObj.getProductId()).getResultList();
        if (l.get(0) != null) {
            l.get(0).setImageUrl(productObj.getImageUrl());
            l.get(0).setDescription(productObj.getDescription());
            l.get(0).setDiscount(productObj.getDiscount());
            l.get(0).setPrice(productObj.getPrice());
            l.get(0).setQuantity(productObj.getQuantity());
            emf.merge(l.get(0));
            return true;
        } else {
            System.out.println("product not fonund");
        }

        return false;
    }

    @Override
    public boolean findProductByProductName(Products productObj) {
        ArrayList<Products> pList = (ArrayList<Products>) emf.createNamedQuery("Products.findByProductName").setParameter("productName", productObj.getProductName()).getResultList();
        if (pList.size() == 0) {
            return false;
        }
        return true;
    }
    /*   public ProductsJpaController(EntityManagerFactory emf) {
        this.emf = emf;
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
     */

}
