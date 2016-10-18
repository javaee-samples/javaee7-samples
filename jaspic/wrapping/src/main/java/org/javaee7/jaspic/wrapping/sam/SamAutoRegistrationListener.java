package org.javaee7.jaspic.wrapping.sam;

import static java.util.EnumSet.allOf;

import javax.servlet.DispatcherType;
import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;

import org.javaee7.jaspic.common.BaseServletContextListener;
import org.javaee7.jaspic.common.JaspicUtils;
import org.javaee7.jaspic.wrapping.servlet.ProgrammaticFilter;

/**
 * 
 * @author Arjan Tijms
 * 
 */
@WebListener
public class SamAutoRegistrationListener extends BaseServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        JaspicUtils.registerSAM(sce.getServletContext(), new TestWrappingServerAuthModule());
        
        sce.getServletContext()
           .addFilter("Programmatic filter", ProgrammaticFilter.class)
           .addMappingForUrlPatterns(allOf(DispatcherType.class), false, "/*");
    }

}