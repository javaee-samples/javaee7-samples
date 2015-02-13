package org.javaee7.jta.transaction.scope;

import javax.inject.Inject;
import javax.transaction.Transactional;

/**
 * @author Arun Gupta
 */
public class MyTransactionalBean {

    @Inject
    MyTransactionScopedBean bean1;

    @Inject
    MyTransactionScopedBean bean2;

    String id1;
    String id2;

    @Transactional
    public void withTransaction() {
        id1 = bean1.getId();
        id2 = bean2.getId();
    }

    public void withoutTransaction() {
        id1 = bean1.getId();
        id2 = bean2.getId();
    }
}
