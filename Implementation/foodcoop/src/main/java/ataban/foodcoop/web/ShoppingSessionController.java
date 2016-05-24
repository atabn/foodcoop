/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ataban.foodcoop.web;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import ataban.foodcoop.ejb.MemberBean;
import ataban.foodcoop.ejb.ProductBean;
import ataban.foodcoop.ejb.ProductSellingBean;
import ataban.foodcoop.entity.CoopMember;
import ataban.foodcoop.entity.Product;
import ataban.foodcoop.entity.ProductSelling;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author atabn
 */
@Named(value = "shoppingSessionController")
@SessionScoped
public class ShoppingSessionController implements Serializable {

    @EJB
    private ProductBean productBean;

    @EJB
    private MemberBean memberBean;

    @EJB
    private ProductSellingBean productSellingBean;
    
    private String newUsername;
    private CoopMember member;
    private boolean active;
    private Product product;
    private float amount;
    int cartId;
    List<ProductSelling> cart;
    
    /**
     * Creates a new instance of ShoppingSessionController
     */
    public ShoppingSessionController() {
    }

    public String getNewUsername() {
        return newUsername;
    }

    public void setNewUsername(String username) {
        this.newUsername = username;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public List<ProductSelling> getCart() {
        return cart;
    }

    public void setCart(List<ProductSelling> cart) {
        this.cart = cart;
    }
      
    public String create() {
        String copyOfNewUserName = newUsername; // reset will clear it.
        reset();
        active = true;
        cart = new ArrayList<>();
        if(copyOfNewUserName.length() != 0) {
            member = memberBean.findMemberByUsername(copyOfNewUserName);
            if(null == member) {
                FacesContext ctx = FacesContext.getCurrentInstance();
                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cannot find member!", "Cannot find member!"));
                return null;
            }
        }
        return "/sellstarted.xhtml";
    }
    
    public String getUsernameString() {
        if(null == member)
            return "ananymous user";
        else
            return member.getUsername();
    }
    
    public void clearFields() {
        newUsername = "";
    }
    
    private void reset() {
        cart = null;
        member = null;
        product = null;
        active = false;
        newUsername = "";
        cartId = 0;
    }
    
    public String buy(long id) {
        product = productBean.find(id);
        amount = 0;
        return "buy.xhtml";
    }
    
    public String addToCart() {
        
        FacesContext ctx = FacesContext.getCurrentInstance();

        if(amount > product.getStockLevel()) {
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Insufficient stock level!", "Insufficient stock level!"));
            return null;
        }
        
        ProductSelling ps = new ProductSelling();
        ps.setCartId(cartId++);
        ps.setMember(member);
        ps.setProduct(product);
        ps.setQuantity(amount);
        
        cart.add(ps);
        
        return "/cart.xhtml";
    }
    
    public void remove(long cartId) {
        int index = -1;
        
        for(int i = 0;i < cart.size();i++)
            if(cart.get(i).getCartId() == cartId)
                index = i;
        
        if(index != -1)
            cart.remove(index);
    }
    
    public float getTotalPrice() {
 
        if(null == cart)
            return 0;
        
        float total = 0;
        for(ProductSelling ps : cart)
            total += ps.getQuantity() * ps.getProduct().getPrice();
        
        return total;
    }
    
    public String commit() {
        
        if(cart.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cart empty!", "Cart empty!"));
            return null;
        }
        
        for(ProductSelling ps : cart) {
            ps.getProduct().setStockLevel(ps.getProduct().getStockLevel() - ps.getQuantity());
            productBean.edit(ps.getProduct());
        }
        productSellingBean.create(cart);
        reset();
        return "/thankforshopping.xhtml";
    }
    
    public String abort() {
        reset();
        return "/welcome.xhtml";
    }
}
