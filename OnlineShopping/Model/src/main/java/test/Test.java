/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import com.iti.entity.Products;
import com.iti.productInterface.ProductInteface;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import service.ProductService;

/**
 *
 * @author Sama
 */
public class Test {

    public static void main(String[] args) {
        System.out.println("==========Start=============");
       /* ApplicationContext contex = new FileSystemXmlApplicationContext("src/main/resources/applicationContext.xml");
        ProductInteface productIntf = (ProductInteface) contex.getBean("productDao");
        //Products pObj = new Products(1, "productName1", 10, 10, "imageUrl1", "description1", "categoryName1");
        
     //   System.out.println("rempver product ===>"+productIntf.findProductById(1).get(0));
        
       // productIntf.addProduct(new Products());
        System.out.println("product updated"+productIntf.updateProduct( new Products(3, "productName1", 1000, 10, "imageUrl1", "description1", "categoryName"))); 
       System.out.println("product size ===>"+productIntf.getAllProduct().size());
        */
       ProductService proService=new ProductService();
//        System.out.println(" add function "+proService.addProduct(new Products("new PRODUCT", 12, 14, "imageUrl", "description", "categoryName")));
      //  System.out.println("find product by id"+proService.findProductById(9).toString());     
      Products p=new Products();
      Products pObj = new Products(7, "productName1", 10, 10, "imageUrl1", "fghjh", "categoryName1");
             
// p.setCategoryName("categoryName1");
    //    System.out.println("product list by id is "+proService.getProducsByCategoryName(p).size()); 
        System.out.println(""+proService.updateProduct(pObj)); 
    System.out.println("==========end=============");

    }

}
