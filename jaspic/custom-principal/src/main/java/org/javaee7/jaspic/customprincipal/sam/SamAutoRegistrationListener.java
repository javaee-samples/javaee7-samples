package org.javaee7.jaspic.customprincipal.sam;

import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;

import org.javaee7.jaspic.common.BaseServletContextListener;
import org.javaee7.jaspic.common.JaspicUtils;

/**
 * 
 * @author Arjan Tijms
 * 
 */
@WebListener
public class SamAutoRegistrationListener extends BaseServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        JaspicUtils.registerSAM(sce.getServletContext(), new TestServerAuthModule());
    }

}