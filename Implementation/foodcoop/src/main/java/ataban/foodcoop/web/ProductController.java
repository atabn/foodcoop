/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ataban.foodcoop.web;

import java.io.InputStream;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import ataban.foodcoop.ejb.CategoryBean;
import ataban.foodcoop.ejb.ProducerBean;
import ataban.foodcoop.ejb.ProductBean;
import ataban.foodcoop.entity.Category;
import ataban.foodcoop.entity.Producer;
import ataban.foodcoop.entity.Product;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.Part;

/**
 *
 * @author atabn
 */
@Named(value = "productController")
@SessionScoped
public class ProductController implements Serializable {

    @EJB
    private ProductBean productBean;
    
    @EJB
    private CategoryBean categoryBean;

    @EJB
    private ProducerBean producerBean;
    
    private final static Logger logger = Logger.getLogger(ProductController.class.getCanonicalName());
    
    private Product current = new Product();
    private int step = 1;
    private Part filePart;
    
    private static final List<String> EXTENSIONS_ALLOWED = new ArrayList<>();

    private String[] categories = new String[0];
    private static SelectItem[] categoryItems;
    
    private String currentCategoryStr;
    
    private String currentProducerStr;
    
    static {
        // images only
        EXTENSIONS_ALLOWED.add(".jpg");
        EXTENSIONS_ALLOWED.add(".bmp");
        EXTENSIONS_ALLOWED.add(".png");
        EXTENSIONS_ALLOWED.add(".gif");
    }

    private String getFileName(Part part) {
        String partHeader = part.getHeader("content-disposition");
        logger.log(Level.INFO, "Part Header = {0}", partHeader);
        for (String cd : part.getHeader("content-disposition").split(";")) {
            if (cd.trim().startsWith("filename")) {
                return cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;

    }

    public void upload() {
        
        FacesContext ctx = FacesContext.getCurrentInstance();
        
        if(null == getFilePart()) {
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please specify a file to upload", "Please specify a file to upload"));
            return;
        }
        
        logger.info(getFilePart().getName());
        
        try {
            InputStream is = getFilePart().getInputStream();

            int i = is.available();
            byte[] b = new byte[i];
            is.read(b);

            logger.log(Level.INFO, "Length : {0}", b.length);
            String fileName = getFileName(getFilePart());
            logger.log(Level.INFO, "File name : {0}", fileName);

            // generate *unique* filename 
            final String extension = fileName.substring(fileName.length() - 4);

            if (!EXTENSIONS_ALLOWED.contains(extension)) {
                logger.severe("User tried to upload file that's not an image. Upload canceled.");
                
                
                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error trying to upload file", "Error trying to upload file"));
                //response.sendRedirect("admin/product/List.xhtml?errMsg=Error trying to upload file");
                return;
            }

//            Integer id = current.getId();
//            current = ejbFacade.find(2);
            current.setImgSrc(b);
            current.setImg(fileName);
            
            productBean.edit(current);
            setStep(3);
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Product image successfuly uploaded!", "Product image successfuly uploaded!"));
            
        } catch (Exception ex) {
        }

    }
    
    /**
     * Creates a new instance of ProductController
     */
    public ProductController() {
    }
    
    public Part getFilePart() {
        return filePart;
    }

    public void setFilePart(Part filePart) {
        this.filePart = filePart;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public Product getCurrent() {
        return current;
    }

    public void setCurrent(Product current) {
        this.current = current;
    }
    
    public String prepareCreate() {
        step = 1;
        current = new Product();
        return "/createproduct.xhtml";
    }
    
    public void create() {
        
        List<Category> lc = categoryBean.findAllCategories();
        for(Category cat : lc)
            if(cat.getName().equals(currentCategoryStr))
                current.setCategory(cat);

        List<Producer> lp = producerBean.findAllProducers();
        for(Producer pr : lp)
            if(pr.getName().equals(currentProducerStr))
                current.setProducer(pr);
        
        productBean.create(current);
        setStep(2);
    }
    
    public String done() {
        setStep(1);
        current = new Product();
        return "/products.xhtml";
    }

    public String getCurrentCategoryStr() {
        return currentCategoryStr;
    }

    public void setCurrentCategoryStr(String currentCategoryStr) {
        this.currentCategoryStr = currentCategoryStr;
    }

    public String getCurrentProducerStr() {
        return currentProducerStr;
    }

    public void setCurrentProducerStr(String currentProducerStr) {
        this.currentProducerStr = currentProducerStr;
    }
    
    public String[] getCategories() {
        return categories;
    }

    public void setCategories(String[] categories) {
        this.categories = categories;
    }
    
    public SelectItem[] getCategoryItems() {      
        List<Category> lc = categoryBean.findAllCategories();
        
        categoryItems = new SelectItem[lc.size()];
        
        for(int i = 0;i < lc.size();i++)
            categoryItems[i] = new SelectItem(lc.get(i).getName());
        
        return categoryItems;
    }
    
    public void applyCategoryFilter() {
        
    }
    
    public List<Product> findProductsOfCategories() {
        List<Product> lp = productBean.findAllProducts();
        
        if(0 == categories.length)
            return lp;
        
        List<Product> lres = new ArrayList<>();
        
        for(Product pr : lp) {
            for(String cs : categories) {
                if(pr.getCategory() != null && pr.getCategory().getName().equals(cs)) {
                    lres.add(pr);
                    break;
                }
            }
        }
        
        return lres;
    }
}
