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
import ataban.foodcoop.ejb.ProductBean;
import ataban.foodcoop.ejb.ProductRatingBean;
import ataban.foodcoop.entity.CoopMember;
import ataban.foodcoop.entity.Product;
import ataban.foodcoop.entity.ProductRating;
import java.util.List;
import javax.ejb.EJB;

/**
 *
 * @author atabn
 */
@Named(value = "productRatingController")
@SessionScoped
public class ProductRatingController implements Serializable {

    @EJB
    private ProductBean productBean;

    @EJB
    private MemberBean memberBean;
    
    @EJB
    private ProductRatingBean productRatingBean;
    
    ProductRating newPr;
    
    /**
     * Creates a new instance of ProductRatingController
     */
    public ProductRatingController() {
    }

    public ProductRating getNewPr() {
        return newPr;
    }

    public void setNewPr(ProductRating newPr) {
        this.newPr = newPr;
    }
    
    public String prepare(long memberId, long productId) {
        newPr = new ProductRating();
        newPr.setProduct(productBean.find(productId));
        newPr.setMember(memberBean.find(memberId));
        
        return "/productrating.xhtml";
    }
    
    public boolean isRated(long memberId, long productId) {
        List<ProductRating> lpr = productRatingBean.findAllProductRatings();
        
        for(ProductRating pr : lpr) {
            if(pr.getProduct().getId() == productId && pr.getMember().getId() == memberId) {
                return true;
            }
        }
        
        return false;
    }
    
    public String create() {
        productRatingBean.create(newPr);
        newPr.getProduct().addRating(newPr);
        productBean.edit(newPr.getProduct());
        return "/products.xhtml";
    }
}
