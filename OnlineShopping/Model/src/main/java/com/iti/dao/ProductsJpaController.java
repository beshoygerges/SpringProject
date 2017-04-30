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
}
