/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ataban.foodcoop.ejb;

import java.util.List;
import ataban.foodcoop.entity.Producer;
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
public class ProducerBean {

    @PersistenceContext
    private EntityManager em;
    
    public void create(Producer producer) {
        em.persist(producer);
    }
    
    public void edit(Producer producer) {
        em.merge(producer);
    }
    
    public void delete(long id) {
        Producer prod = em.find(Producer.class, id);
        if(prod != null) {
            em.remove(prod);
        }
    }
    
    public List<Producer> findAllProducers() {
        Query createNamedQuery = em.createNamedQuery("Producer.findAll");
        
        List<Producer> lp =  createNamedQuery.getResultList();
        
        System.out.println("Dumping prlist");
        System.out.println(lp);
        
        return lp;
    }
    
    public Producer find(long id) {
        return em.find(Producer.class, id);
    }
}
