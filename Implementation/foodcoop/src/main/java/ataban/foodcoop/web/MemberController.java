/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ataban.foodcoop.web;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import ataban.foodcoop.ejb.MemberBean;

import ataban.foodcoop.entity.CoopMember;
import ataban.foodcoop.util.MD5Util;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;


/**
 *
 * @author atabn
 */
@Named(value = "memberController")
@SessionScoped
public class MemberController implements Serializable {

    @EJB
    private MemberBean memberBean;
    
    private CoopMember coopMember;
    
    private CoopMember newMember = new CoopMember();
    
    private String username;
    private String password;
    
    long mstimeLoggedIn;
    
    /**
     * Creates a new instance of MemberController
     */
    public MemberController() {        
    }
    
    @PostConstruct
    private void afterConstructor() {
        
        final String TestAdminName = "admin";
        
        CoopMember admin;
        boolean adminCreated = false;
        
        admin = memberBean.findMemberByUsername(TestAdminName);
        if(admin == null) {
            admin = new CoopMember();
            adminCreated = true;
        }
        
        admin.setAdmin(true);
        admin.setSalesman(true);
        admin.setUsername(TestAdminName);
        admin.setPassword(MD5Util.generateMD5(TestAdminName));
        admin.setApproved(true);
        admin.setFirstname(TestAdminName);
        admin.setLastname(TestAdminName);

        if(adminCreated)
            memberBean.createMember(admin);
        else
            memberBean.edit(admin);
        
        /*
        coopMember = memberBean.findMemberByUsername(TestAdminName);
        mstimeLoggedIn = System.currentTimeMillis();
        */
    }
    
    public String login() throws Exception{
        
        FacesContext ctx = FacesContext.getCurrentInstance();

        coopMember = memberBean.findMemberByUsername(getUsername());

        if(coopMember == null) {
            ctx.addMessage("formnonmember:loginbtn", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No such user"));
            return null;
        }
        else if(coopMember.isApproved() == false) {
            coopMember = null;
            ctx.addMessage("formnonmember:loginbtn", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Membership not approved yet"));
            return null;
        }
        else if(coopMember.getPassword().equals(MD5Util.generateMD5(password)) == false)
        {
            coopMember = null;
            ctx.addMessage("formnonmember:loginbtn", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Incorrect password"));
            return null;           
        }
        
        mstimeLoggedIn = System.currentTimeMillis();
        return null;
    }
    
    public String create() {
        
        FacesContext ctx = FacesContext.getCurrentInstance();
        
        newMember.setApproved(false);
        newMember.setPassword(MD5Util.generateMD5(newMember.getPassword()));
        
        CoopMember res = memberBean.createMember(newMember);
        
        newMember = new CoopMember(); // Tricky.
        
        if(null == res) {
            
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Username already exists.", "Username already exists."));
            return null;
        }
           
        return "/signupreminder.xhtml";
    }
    
    public String logout() {
        coopMember = null;
        return "/welcome.xhtml";
    }
    
    public boolean isLogged() {
        return (getCoopMember() == null) ? false : true;
    }
    
    public boolean isAdmin() {
        return coopMember != null && coopMember.isAdmin();
    }
    
    public boolean isSalesman() {
        return coopMember != null && (coopMember.isAdmin() || coopMember.isSalesman());
    } 
    
    public long getTimeSinceLogin() {
        return (System.currentTimeMillis() - mstimeLoggedIn) / 60000;
    }
    
    public CoopMember getCoopMember() {
        return coopMember;
    }
    
    public Long getMemberId() {
        if(coopMember != null)
            return coopMember.getId();
        else
            return 0L;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public CoopMember getNewMember() {
        return newMember;
    }

    public void setNewMember(CoopMember newMember) {
        this.newMember = newMember;
    }
}
