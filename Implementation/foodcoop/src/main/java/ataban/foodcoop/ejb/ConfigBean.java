/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ataban.foodcoop.ejb;

import ataban.foodcoop.entity.CoopMember;
import ataban.foodcoop.util.MD5Util;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

/**
 *
 * @author ataban
 */
/*
@Singleton
@Startup
*/
public class ConfigBean {

    @EJB
    private MemberBean memberbean;
    
    private void createDefaultAdmin() {
        CoopMember admin;
        boolean adminCreated = false;
        
        admin = memberbean.findMemberByUsername("ad");
        if(admin == null) {
            admin = new CoopMember();
            adminCreated = true;
        }
        
        admin.setAdmin(true);
        admin.setUsername("ad");
        admin.setPassword(MD5Util.generateMD5("ad"));
        admin.setApproved(true);
        if(adminCreated)
            memberbean.createMember(admin);
        else
            memberbean.edit(admin);
    }
    
    private void createDefaultUser() {
        CoopMember ataban = new CoopMember();
        boolean atabanCreated = false;
        
        ataban = memberbean.findMemberByUsername("at");
        if(ataban == null) {
            ataban = new CoopMember();
            atabanCreated = true;
        }
        
        ataban.setSalesman(true);
        ataban.setUsername("at");
        ataban.setPassword(MD5Util.generateMD5("at"));
        ataban.setApproved(true);
        memberbean.createMember(ataban);
        
        if(atabanCreated)
            memberbean.createMember(ataban);
        else
            memberbean.edit(ataban);
    }
    
    @PostConstruct
    public void createData() {
        
        // Create hardcoded admin account to handle database failures.
        // TODO: This will be removed in production code.
        createDefaultAdmin();
        
        // For convenience during development
        createDefaultUser();
    }
}
