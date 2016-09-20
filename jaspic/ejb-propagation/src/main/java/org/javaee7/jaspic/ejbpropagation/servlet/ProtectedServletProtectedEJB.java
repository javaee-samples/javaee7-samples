package org.javaee7.jaspic.ejbpropagation.servlet;

import static java.util.logging.Level.SEVERE;

import java.io.IOException;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.javaee7.jaspic.ejbpropagation.ejb.ProtectedEJB;

/**
 * 
 * @author Arjan Tijms
 * 
 */
@WebServlet(urlPatterns = "/protected/servlet-protected-ejb")
public class ProtectedServletProtectedEJB extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final static Logger logger = Logger.getLogger(ProtectedServletProtectedEJB.class.getName());

    @EJB
    private ProtectedEJB protectedEJB;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String webName = null;
        if (request.getUserPrincipal() != null) {
            webName = request.getUserPrincipal().getName();
        }

        String ejbName = "";
        try {
            ejbName = protectedEJB.getUserName();
        } catch (Exception e) {
            logger.log(SEVERE, "", e);
        }

        response.getWriter().write("web username: " + webName + "\n" + "EJB username: " + ejbName + "\n");

        boolean webHasRole = request.isUserInRole("architect");
        
        boolean ejbHasRole = false;
        try {
            ejbHasRole = protectedEJB.isUserArchitect();
        } catch (Exception e) {
            logger.log(SEVERE, "", e);
        }

        response.getWriter().write(
            "web user has role \"architect\": " + webHasRole + "\n" + "EJB user has role \"architect\": " + ejbHasRole
                + "\n");

    }

}
