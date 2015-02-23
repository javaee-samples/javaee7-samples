package org.javaee7.jsf.contracts;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 * @author Arun Gupta
 */
@Named
@SessionScoped
public class ContractsBean implements Serializable {

    String contract = "red";

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }
}
