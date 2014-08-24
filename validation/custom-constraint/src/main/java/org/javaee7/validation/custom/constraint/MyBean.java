package org.javaee7.validation.custom.constraint;

import javax.enterprise.context.RequestScoped;

/**
 * @author Arun Gupta
 */
@RequestScoped
public class MyBean {
    public void saveZip(@ZipCode String zip) {
        System.out.println("Saving zip code for default country (US)");
    }
    
    public void saveZipIndia(@ZipCode(country = ZipCode.Country.INDIA) String zip) {
        System.out.println("Saving zip code for India");
    }
}
