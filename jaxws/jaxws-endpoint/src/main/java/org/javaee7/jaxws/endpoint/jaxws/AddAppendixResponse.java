
package org.javaee7.jaxws.endpoint.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.javaee7.jaxws.endpoint.EBook;

@XmlRootElement(name = "addAppendixResponse", namespace = "http://endpoint.jaxws.javaee7.org/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "addAppendixResponse", namespace = "http://endpoint.jaxws.javaee7.org/")
public class AddAppendixResponse {

    @XmlElement(name = "return", namespace = "")
    private EBook _return;

    /**
     * 
     * @return
     *     returns EBook
     */
    public EBook getReturn() {
        return this._return;
    }

    /**
     * 
     * @param _return
     *     the value for the _return property
     */
    public void setReturn(EBook _return) {
        this._return = _return;
    }

}
