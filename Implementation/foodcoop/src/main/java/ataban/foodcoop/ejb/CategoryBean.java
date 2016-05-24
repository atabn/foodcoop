/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ataban.foodcoop.ejb;

import java.util.List;
import ataban.foodcoop.entity.Category;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author atabn
 */
@Named
@Stateless
public class CategoryBean {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    @PersistenceContext
    private EntityManager em;
    
    public Category create(String name) {
        Category cat = new Category();
        cat.setName(name);
        
        System.out.println("Id + " + cat.getId());
        
        em.persist(cat);
        
        return cat;
    }
    
    public void delete(long id) {
        Category cat = em.find(Category.class, id);
        if(cat != null) {
            em.remove(cat);
        }
    }
    
    public List<Category> findAllCategories() {
        Query createNamedQuery = em.createNamedQuery("Category.findAll");
        
        List<Category> lc =  createNamedQuery.getResultList();
        return lc;
    }
    
    public String getName(long id) {
        Category cat = em.find(Category.class, id);
        if(cat != null)
            return cat.getName();
        
        return null;
    }
}
