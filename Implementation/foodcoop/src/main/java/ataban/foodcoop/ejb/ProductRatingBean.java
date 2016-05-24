/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ataban.foodcoop.ejb;

import ataban.foodcoop.entity.ProductRating;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author atabn
 */
@Stateless
public class ProductRatingBean {

    @PersistenceContext
    private EntityManager em;
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    public void create(ProductRating rating) {
        em.persist(rating);
    }
    
    public List<ProductRating> findAllProductRatings() {
        Query createNamedQuery = em.createNamedQuery("ProductRating.findAll");
        
        List<ProductRating> lpr =  createNamedQuery.getResultList();
        return lpr;
    }
    
}
