/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ataban.foodcoop.web;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import ataban.foodcoop.ejb.ProducerBean;
import ataban.foodcoop.entity.Producer;
import javax.ejb.EJB;

/**
 *
 * @author atabn
 */
@Named(value = "producerController")
@SessionScoped
public class ProducerController implements Serializable {

    @EJB
    private ProducerBean producerBean;
    
    private Producer current = new Producer();
    
    /**
     * Creates a new instance of ProducerController
     */
    public ProducerController() {
    }

    public Producer getCurrent() {
        return current;
    }

    public void setCurrent(Producer current) {
        this.current = current;
    }
    
    public void create() {
        producerBean.create(current);
        current = new Producer(); // TODO: Remove this after image uploading is implemented.
    }
    
}
