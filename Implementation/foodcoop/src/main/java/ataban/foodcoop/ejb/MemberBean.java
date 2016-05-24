/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ataban.foodcoop.ejb;

import java.util.List;
import ataban.foodcoop.entity.CoopMember;
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
public class MemberBean {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
  @PersistenceContext
  private EntityManager em;
    
  public CoopMember createMember(CoopMember member) {
      
    if(findMemberByUsername(member.getUsername()) != null)
        return null;
      
    em.persist(member);
    return member;
  }

  public List<CoopMember> findAllMembers(boolean approved) {
    Query createNamedQuery = em.createNamedQuery("CoopMember.findAll");
    createNamedQuery.setParameter("approved", approved);
    
    List<CoopMember> lm =  createNamedQuery.getResultList();
    return lm;
  }

  public CoopMember findMemberByUsername(String username) {
    Query createNamedQuery = em.createNamedQuery("CoopMember.findByUsername");

    createNamedQuery.setParameter("username", username);

    if (createNamedQuery.getResultList().size() > 0) {
        return (CoopMember) createNamedQuery.getSingleResult();
    }
    else {
        return null;
    }
  }
  
  public void approveMember(long id, boolean approved) {
        CoopMember member = em.find(CoopMember.class, id);
        if(member != null) {
            member.setApproved(approved);
            em.persist(member);
        }
    }
  
  public void makeMemberSalesman(long id, boolean salesman) {
        CoopMember member = em.find(CoopMember.class, id);
        if(member != null) {
            member.setSalesman(salesman);
            em.persist(member);
        }
    }
 
  public void makeMemberAdmin(long id, boolean admin) {
        CoopMember member = em.find(CoopMember.class, id);
        if(member != null) {
            member.setAdmin(admin);
            em.persist(member);
        }
    }
  
    public void deleteMember(long id) {        
        CoopMember member = em.find(CoopMember.class, id);
        if(member != null) {
            em.remove(member);
        }
    }
    
    public CoopMember find(long id) {
        return em.find(CoopMember.class, id);
    }
    
    public void edit(CoopMember cm) {
        em.merge(cm);
    }
}
