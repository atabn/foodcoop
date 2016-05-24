/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ataban.foodcoop.web;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import ataban.foodcoop.ejb.CategoryBean;
import ataban.foodcoop.entity.Category;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author atabn
 */
@Named(value = "categoryController")
@SessionScoped
public class CategoryController implements Serializable {

    @EJB
    private CategoryBean categoryBean;
    
    private String name; 
    
    /**
     * Creates a new instance of CategoryController
     */
    public CategoryController() {
    }
    
    public void create() {
        categoryBean.create(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public void delete(long id) {
        try {
            categoryBean.delete(id);
        }
        catch(Exception e) {
            System.out.println("Ahmet Error: " + e);
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cannot delete. There are some products associated with this category.", "Error"));
        }
    }
    
}
