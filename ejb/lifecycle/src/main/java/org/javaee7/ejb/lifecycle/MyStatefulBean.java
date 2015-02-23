package org.javaee7.ejb.lifecycle;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.PostActivate;
import javax.ejb.PrePassivate;
import javax.ejb.Stateful;
import javax.enterprise.context.Dependent;
import javax.interceptor.InvocationContext;

/**
 * @author Arun Gupta
 */
@Dependent
@MyAroundConstructInterceptorBinding
@Stateful
public class MyStatefulBean {
    private List<String> list;

    public MyStatefulBean() {
        System.out.println("MyStatefulBean.ctor");
    }

    @PostConstruct
    private void postConstruct() {
        list = new ArrayList<>();
        System.out.println("MyStatefulBean.postConstruct");
    }

    @PreDestroy
    private void preDestroy() {
        System.out.println("MyStatefulBean.preDestroy");
    }

    @PrePassivate
    private void prePassivate() {
        System.out.println("MyStatefulBean.prePassivate");
    }

    @PostActivate
    private void postActivate() {
        System.out.println("MyStatefulBean.postActivate");
    }

    public void addItem(String item) {
        list.add(item);
        System.out.println("MyBean.addItem");
    }

    public void removeItem(String item) {
        list.remove(item);
        System.out.println("MyBean.removeItem");
    }

    public List<String> items() {
        return list;
    }
}
