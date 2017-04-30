/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iti.productInterface;

import com.iti.entity.Products;
import java.util.ArrayList;

/**
 *
 * @author Sama
 */
public interface ProductInteface {
    public boolean addProduct(Products productObj);
    public ArrayList<Products> getAllProduct();
    public ArrayList<Products> findProductById(int productId);
    public boolean findProductByProductName(Products productObj);
    public ArrayList<Products> findProductByCategory(Products productObj);
    public boolean removeProduct(Products productObj);
    public boolean updateProduct(Products productObj);
    
    
}
