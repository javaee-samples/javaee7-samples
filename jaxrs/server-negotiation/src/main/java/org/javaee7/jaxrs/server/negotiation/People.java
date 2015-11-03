package org.javaee7.jaxrs.server.negotiation;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class People extends ArrayList<Person> {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = "person")
    public List<Person> getPeople() {
        return this;
    }
}
