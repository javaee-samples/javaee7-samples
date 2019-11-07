package org.javaee7.servlet.programmatic.registration;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * @author OrelGenya
 */
@WebListener
public class SimpleServletContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent contextEvent) {
        System.out.println("Servlet context initialized: " + contextEvent.getServletContext().getContextPath());
        
        contextEvent.getServletContext().addServlet("dynamic", DynamicServlet.class)
                               .addMapping("/dynamic");
    }

    @Override
    public void contextDestroyed(ServletContextEvent contextEvent) {
        System.out.println("Servlet context destroyed: " + contextEvent.getServletContext().getContextPath());
    }
}
