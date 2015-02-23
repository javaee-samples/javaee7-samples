package org.javaee7.jaspic.asyncauthentication.bean;

import static java.lang.Thread.interrupted;
import static java.lang.Thread.sleep;

import java.io.IOException;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.servlet.AsyncContext;

/**
 * 
 * @author Arjan Tijms
 *
 */
@Stateless
public class AsyncBean {

    @Asynchronous
    public void doAsync(AsyncContext asyncContext) {

        try {
            sleep(1000);
        } catch (InterruptedException e) {
            interrupted();
        }

        try {
            asyncContext.getResponse().getWriter().write("async response");
        } catch (IOException e) {
            e.printStackTrace();
        }

        asyncContext.complete();
    }

}
