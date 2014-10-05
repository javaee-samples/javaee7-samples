package org.javaee7.ejb.stateful;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;

/**
 * 
 * @author Arjan Tijms
 * 
 */
@Stateful
public class ReentrantStatefulBean {

    @Resource
    private SessionContext sessionConext;

    public void initialMethod() {
        sessionConext.getBusinessObject(ReentrantStatefulBean.class).reentrantMehthod();
    }

    public void reentrantMehthod() {

    }

}
