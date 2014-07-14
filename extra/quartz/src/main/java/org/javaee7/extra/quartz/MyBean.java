package org.javaee7.extra.quartz;

import java.io.Serializable;
import javax.transaction.TransactionScoped;

/**
 * @author Arun Gupta
 */
@TransactionScoped
public class MyBean implements Serializable {

    public String getId() {
        return this + "";
    }
}
