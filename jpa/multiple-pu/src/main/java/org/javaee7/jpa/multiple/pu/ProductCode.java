package org.javaee7.jpa.multiple.pu;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Arun Gupta
 */
@Entity
@Table(name = "PRODUCT_CODE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProductCode.findAll", query = "SELECT p FROM ProductCode p"),
    @NamedQuery(name = "ProductCode.findByProdCode", query = "SELECT p FROM ProductCode p WHERE p.prodCode = :prodCode"),
    @NamedQuery(name = "ProductCode.findByDiscountCode", query = "SELECT p FROM ProductCode p WHERE p.discountCode = :discountCode"),
    @NamedQuery(name = "ProductCode.findByDescription", query = "SELECT p FROM ProductCode p WHERE p.description = :description") })
public class ProductCode implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "PROD_CODE")
    private String prodCode;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DISCOUNT_CODE")
    private char discountCode;
    @Size(max = 10)
    @Column(name = "DESCRIPTION")
    private String description;

    public ProductCode() {
    }

    public ProductCode(String prodCode) {
        this.prodCode = prodCode;
    }

    public ProductCode(String prodCode, char discountCode) {
        this.prodCode = prodCode;
        this.discountCode = discountCode;
    }

    public String getProdCode() {
        return prodCode;
    }

    public void setProdCode(String prodCode) {
        this.prodCode = prodCode;
    }

    public char getDiscountCode() {
        return discountCode;
    }

    public void setDiscountCode(char discountCode) {
        this.discountCode = discountCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (prodCode != null ? prodCode.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProductCode)) {
            return false;
        }
        ProductCode other = (ProductCode) object;
        if ((this.prodCode == null && other.prodCode != null) || (this.prodCode != null && !this.prodCode.equals(other.prodCode))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.javaee7.jpa.multiple.pu.ProductCode[ prodCode=" + prodCode + " ]";
    }

}
