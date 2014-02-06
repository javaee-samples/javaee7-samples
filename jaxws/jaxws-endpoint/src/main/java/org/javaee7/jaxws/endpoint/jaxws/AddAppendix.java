
package org.javaee7.jaxws.endpoint.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.javaee7.jaxws.endpoint.EBook;

@XmlRootElement(name = "addAppendix", namespace = "http://endpoint.jaxws.javaee7.org/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "addAppendix", namespace = "http://endpoint.jaxws.javaee7.org/", propOrder = {
    "arg0",
    "arg1"
})
public class AddAppendix {

    @XmlElement(name = "arg0", namespace = "")
    private EBook arg0;
    @XmlElement(name = "arg1", namespace = "")
    private int arg1;

    /**
     * 
     * @return
     *     returns EBook
     */
    public EBook getArg0() {
        return this.arg0;
    }

    /**
     * 
     * @param arg0
     *     the value for the arg0 property
     */
    public void setArg0(EBook arg0) {
        this.arg0 = arg0;
    }

    /**
     * 
     * @return
     *     returns int
     */
    public int getArg1() {
        return this.arg1;
    }

    /**
     * 
     * @param arg1
     *     the value for the arg1 property
     */
    public void setArg1(int arg1) {
        this.arg1 = arg1;
    }

}
