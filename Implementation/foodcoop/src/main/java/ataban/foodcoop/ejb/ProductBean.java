/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ataban.foodcoop.ejb;

import java.util.List;
import ataban.foodcoop.entity.Product;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author atabn
 */
@Stateless
@Named
public class ProductBean {

    @PersistenceContext
    private EntityManager em;

    public void create(Product product) {
        em.persist(product);
    }
    
    public void edit(Product product) {
        em.merge(product);
    }

    public void refresh(Product product) {
        Product pr2 = em.find(Product.class, product.getId());
        em.refresh(pr2);
        System.out.println("Num ratings " + pr2.getNumRatings());
    }
    
    public void delete(long id) {
        Product prod = em.find(Product.class, id);
        if(prod != null) {
            em.remove(prod);
        }
    }
    
    public List<Product> findAllProducts() {
        Query createNamedQuery = em.createNamedQuery("Product.findAll");
        
        List<Product> lp =  createNamedQuery.getResultList();
        return lp;
    }
    
    public Product find(long id) {
        return em.find(Product.class, id);
    }
}
