/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ataban.foodcoop.web;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.Date;
import ataban.foodcoop.ejb.MemberBean;
import ataban.foodcoop.ejb.VolunteerTaskBean;
import ataban.foodcoop.entity.CoopMember;
import ataban.foodcoop.entity.VolunteerTask;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author ataban
 */
@Named(value = "volunteerTaskController")
@SessionScoped
public class VolunteerTaskController implements Serializable {

    @EJB
    private VolunteerTaskBean voluntaryTaskBean;
    
    private String text;
    private String location;
    private Date taskDate;
    
    /**
     * Creates a new instance of VoluntaryTaskController
     */
    public VolunteerTaskController() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getTaskDate() {
        return taskDate;
    }

    public void setTaskDate(Date taskDate) {
        this.taskDate = taskDate;
    }
    
    public void create() {    
        FacesContext ctx = FacesContext.getCurrentInstance();
        
        voluntaryTaskBean.create(text, location, taskDate);
    }
}
