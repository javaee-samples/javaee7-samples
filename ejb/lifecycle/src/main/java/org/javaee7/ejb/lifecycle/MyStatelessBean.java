package org.javaee7.ejb.lifecycle;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Stateless;

/**
 * @author Arun Gupta
 */
@Stateless
public class MyStatelessBean {
    @MyAroundConstructInterceptorBinding
    public MyStatelessBean() {
        System.out.println("MyStatelessBean.ctor");
    }
    
    @PostConstruct
    private void postConstruct() {
        System.out.println("MyStatelessBean.postConstruct");
    }
    
    @PreDestroy
    private void preDestroy() {
        System.out.println("MyStatelessBean.preDestroy");
    }
    
    public void method1() {
        System.out.println("MyBean.method1");
    }
}
