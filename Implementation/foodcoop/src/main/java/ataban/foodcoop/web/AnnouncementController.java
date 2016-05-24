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
import ataban.foodcoop.ejb.AnnouncementBean;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author ataban
 */
@Named(value = "announcementController")
@SessionScoped
public class AnnouncementController implements Serializable {

    @EJB
    private AnnouncementBean announcementBean;
    
    private String text;
    private Date dateOfInvalidity;
    
    /**
     * Creates a new instance of AnnouncementController
     */
    public AnnouncementController() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDateOfInvalidity() {
        return dateOfInvalidity;
    }

    public void setDateOfInvalidity(Date dateOfInvalidity) {
        this.dateOfInvalidity = dateOfInvalidity;
    }
    
    public void create() {
        
        Date now = new Date();
      
        FacesContext ctx = FacesContext.getCurrentInstance();
        
        if(dateOfInvalidity.compareTo(now) < 0) {
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Date of invalidity cannot be in the past!", "Date of invalidity cannot be in the past!"));
            return;
        }
        
        announcementBean.create(text, now, dateOfInvalidity);
    }
}
