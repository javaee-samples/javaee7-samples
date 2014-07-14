package org.javaee7.jta.transaction.scope;

import java.io.Serializable;
import javax.transaction.TransactionScoped;

/**
 * @author Arun Gupta
 */
@TransactionScoped
public class MyTransactionScopedBean implements Serializable {

    public String getId() {
        return this + "";
    }
}
