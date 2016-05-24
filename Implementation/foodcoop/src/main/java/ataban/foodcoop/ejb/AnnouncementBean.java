/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ataban.foodcoop.ejb;

import java.util.Date;
import java.util.List;
import ataban.foodcoop.entity.Announcement;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author ataban
 */
@Stateless
@Named
public class AnnouncementBean {

    @PersistenceContext
    private EntityManager em;
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    public void create(String text, Date creationDate, Date validityEndDate) {
        
        Announcement ann = new Announcement();
        
        ann.setText(text);
        ann.setDateOfCreation(creationDate);
        ann.setDateOfInvalidity(validityEndDate);
        
        em.persist(ann);
    }
    
    public void delete(long id) {
        Announcement ann = em.find(Announcement.class, id);
        if(ann != null) {
            em.remove(ann);
        }
    }
    
    public List<Announcement> findAllAnnouncements() {
        Query createNamedQuery = em.createNamedQuery("Announcement.findAll");
        
        List<Announcement> la =  createNamedQuery.getResultList();
        return la;
    }
    
    public Announcement find(long id) {
        return em.find(Announcement.class, id);
    }
}
