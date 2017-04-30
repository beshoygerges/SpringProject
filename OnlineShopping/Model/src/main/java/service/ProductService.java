
package service;

import com.iti.entity.Products;
import com.iti.productInterface.ProductInteface;
import java.util.ArrayList;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class ProductService {

    private ProductInteface controller;

    public ProductService() {
        ApplicationContext contex = new FileSystemXmlApplicationContext("src/main/resources/applicationContext.xml");
        controller = (ProductInteface) contex.getBean("productDao");
    }

    public boolean addProduct(Products productObj) {
        if (!controller.findProductByProductName(productObj)) {
            return controller.addProduct(productObj);
        } else {
            System.out.println("product name already exist");
            return false;
        }
    }

    public ArrayList<Products> getAllProduct() {
        return controller.getAllProduct();
    }

    public ArrayList<Products> getProducsByCategoryName(Products productObj) {
        return controller.findProductByCategory(productObj);
    }

    public Products findProductById(int productId) {
        return controller.findProductById(productId).get(0);
    }

    public boolean removeProduct(Products productObj) {
        Products p = controller.findProductById(productObj.getProductId()).get(0);
        if (p != null) {
            return controller.removeProduct(p);
        }
        return false;
    }
    public boolean updateProduct(Products productObj) {
        return controller.updateProduct(productObj);
    }

}
