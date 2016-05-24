/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ataban.foodcoop.ejb;

import ataban.foodcoop.entity.Category;
import java.util.List;
import ataban.foodcoop.entity.ProductSelling;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author atabn
 */
@Stateless
public class ProductSellingBean {

    @PersistenceContext
    private EntityManager em;
    
    
    public void create(List<ProductSelling> pss) {
        for(ProductSelling ps : pss)
            em.persist(ps);
    }
    
    public List<ProductSelling> findAllProductSellings() {
        Query createNamedQuery = em.createNamedQuery("ProductSelling.findAll");
        
        List<ProductSelling> lps =  createNamedQuery.getResultList();
        return lps;
    }
    
}
