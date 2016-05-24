/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ataban.foodcoop.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author atabn
 */
@Entity
@NamedQuery(name = "Product.findAll", query = "SELECT p FROM Product p ORDER BY p.name ASC")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String img;
    @Lob
    @Basic(fetch=FetchType.LAZY)
    @Column(name = "IMG_SRC")
    @XmlTransient
    private byte[] imgSrc;
    @OneToOne
    private Category category;
    @OneToOne
    private Producer producer;
    private String scale;
    private float stockLevel;
    private float price;
    @OneToMany(mappedBy = "product")
    List<ProductRating> ratings;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
  
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getImg() {
        return img;
    }

    public void setImg(String simg) {
        this.img = simg;
    }

    public byte[] getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(byte[] imgSrc) {
        this.imgSrc = imgSrc;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Producer getProducer() {
        return producer;
    }

    public void setProducer(Producer producer) {
        this.producer = producer;
    }   
    
    public String getScale() {
        return scale;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }

    public float getStockLevel() {
        return stockLevel;
    }

    public void setStockLevel(float stockLevel) {
        this.stockLevel = stockLevel;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
    
    public float getAvgRating() {
        
        if(ratings.size() == 0)
            return 0;
        
        float sum = 0;
        for(ProductRating pr : ratings)
            sum += pr.getRating();
        
        return sum / ratings.size();
    }
    
    public int getNumRatings() {
        return ratings.size();
    }
    
    public void addRating(ProductRating rating) {
        if(false == ratings.contains(rating))
            ratings.add(rating);
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Product)) {
            return false;
        }
        Product other = (Product) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "javaeetutorial.order.entity.Product[ id=" + id + " ]";
    }
    
}
