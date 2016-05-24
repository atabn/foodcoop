/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ataban.foodcoop.ejb;

import java.util.Date;
import java.util.List;
import ataban.foodcoop.entity.CoopMember;
import ataban.foodcoop.entity.VolunteerTask;
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
public class VolunteerTaskBean {

    @PersistenceContext
    private EntityManager em;
    
    public void create(String text, String location, Date taskDate) {
        
        VolunteerTask vt = new VolunteerTask();
        
        vt.setText(text);
        vt.setLocation(location);
        vt.setTaskDate(taskDate);
        
        em.persist(vt);
    }
    
    public void delete(long id) {
        VolunteerTask vt = em.find(VolunteerTask.class, id);
        if(vt != null) {
            em.remove(vt);
        }
    }

    public boolean isMember(long taskId, long memberId) {
        
        VolunteerTask vt = find(taskId);
        if(null == vt)
            return false;
        
        for(CoopMember cm : vt.getAssignedMembers()) {
            System.out.println("Member in " + cm.getId() + " Member out " + memberId);
            if(cm.getId() == memberId)
                return true;
        }
        
        return false;
    }
    
    public void addMember(long volunteerTaskId, long memberId) {
               
        CoopMember cm = em.find(CoopMember.class, memberId);
        if(cm == null)
            return;
        
        VolunteerTask vt = em.find(VolunteerTask.class, volunteerTaskId);
        if(vt == null)
            return;
        
        vt.getAssignedMembers().add(cm);
        em.merge(vt);
    }
    
    public void removeMember(long volunteerTaskId, long memberId) {

        CoopMember cm = em.find(CoopMember.class, memberId);
        if(cm == null)
            return;
        
        VolunteerTask vt = em.find(VolunteerTask.class, volunteerTaskId);
        if(vt == null)
            return;
        
        vt.getAssignedMembers().remove(cm);
        em.merge(vt);
    }
    
    public List<VolunteerTask> findAllVolunteerTasks() {
        Query createNamedQuery = em.createNamedQuery("VolunteerTask.findAll");
        
        List<VolunteerTask> lvt =  createNamedQuery.getResultList();
        
        System.out.print(lvt);
        
        return lvt;
    }
    
    public VolunteerTask find(long id) {
        return em.find(VolunteerTask.class, id);
    }
}
