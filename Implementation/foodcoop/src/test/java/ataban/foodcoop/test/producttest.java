/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ataban.foodcoop.test;

import ataban.foodcoop.ejb.CategoryBean;
import ataban.foodcoop.ejb.ProducerBean;
import ataban.foodcoop.ejb.ProductBean;
import ataban.foodcoop.ejb.ProductSellingBean;
import ataban.foodcoop.entity.Category;
import ataban.foodcoop.entity.Producer;
import ataban.foodcoop.entity.Product;
import ataban.foodcoop.entity.ProductSelling;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author ataban
 * Purpose: Test various database access routines for Product entity and its associated entities.
 */
@Named(value = "producttest")
@RequestScoped
public class producttest {

    final String TestCategoryName = "Fruits2";
    final String TestProducerName = "Hasan Amca";
    final String TestProductName = "Green Apple";
    
    @EJB
    private ProducerBean producerBean;
 
    @EJB
    private CategoryBean categoryBean;

    @EJB
    private ProductBean productBean;
    
    @EJB
    private ProductSellingBean productSellingBean;
    
    /**
     * Creates a new instance of producttest
     */
    public producttest() {
    }
    
    public void run() {
        
        System.out.println("Test started.");
        
        Producer producer = new Producer();
        
        producer.setName(TestProducerName);
        producer.setCity("Adana");
        producer.setDescription("Organik Meyveler");
        
        producerBean.create(producer);
        producer = null;
        
        // Test producer creation
        producer = findProducerByName(TestProducerName);
        assert producer != null;
        
        categoryBean.create(TestCategoryName);

        // Test category creation
        Category category = findCategoryByName(TestCategoryName);
        assert category != null;
        
        Product greenApple = new Product();
        greenApple.setName(TestProductName);
        greenApple.setProducer(producer);
        greenApple.setCategory(category);
        greenApple.setPrice((float) 3.4);
        greenApple.setStockLevel(50);
        greenApple.setScale("kg");
        
        productBean.create(greenApple);
        greenApple = null;
        
        // Test product creation
        greenApple = findProductByName(TestProductName);
        assert greenApple != null;
        assert greenApple.getCategory() != null && greenApple.getCategory().getName().equals(category.getName());
        assert greenApple.getProducer() != null && greenApple.getProducer().getName().equals(producer.getName());
        
        ProductSelling productSelling = new ProductSelling();
        productSelling.setProduct(greenApple);
        productSelling.setMember(null); // Anonymous
        productSelling.setQuantity(3);
        ArrayList<ProductSelling> lps = new ArrayList<>();
        lps.add(productSelling);
        productSellingBean.create(lps);
        
        productSelling = null;
        
        // Test product selling creation
        productSelling = findProductSellingByProductId(greenApple.getId());
        assert productSelling != null;
        
        // Test product delete works
        productBean.delete(greenApple.getId());
        assert findProductByName(TestProducerName) == null;
        
        // Test cascading delete for productSelling (product was deleted, so we expect its associated productSelling to be deleted as well)
        assert findProductSellingByProductId(greenApple.getId()) == null;
        
        System.out.println("Test finished.");
    }
    
    /* TODO: Can be made faster by a specialized JPA query. */ 
    Category findCategoryByName(String name) {
        List<Category> listcat = categoryBean.findAllCategories();
        
        for(Category cat : listcat) {
            if(cat.getName().equals(name))
                return cat;
        }
        
        return null;
    }
    
    /* TODO: Can be made faster by a specialized JPA query. */    
    Producer findProducerByName(String name) {
        List<Producer> listprod = producerBean.findAllProducers();
        
        for(Producer prod : listprod) {
            if(prod.getName().equals(name))
                return prod;
        }
        
        return null;
    }

    /* TODO: Can be made faster by a specialized JPA query. */    
    Product findProductByName(String name) {
        List<Product> listprod = productBean.findAllProducts();
        
        for(Product prod : listprod) {
            if(prod.getName().equals(name))
                return prod;
        }
        
        return null;
    }

    /* TODO: Can be made faster by a specialized JPA query. */    
    ProductSelling findProductSellingByProductId(long productId) {
        List<ProductSelling> listprod = productSellingBean.findAllProductSellings();
        
        for(ProductSelling ps : listprod) {
            if(ps.getProduct().getId().equals(productId))
                return ps;
        }
        
        return null;
    }
    
}
