package org.javaee7.jaspic.ejbpropagation.servlet;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.javaee7.jaspic.ejbpropagation.ejb.PublicEJB;

/**
 * 
 * @author Arjan Tijms
 * 
 */
@WebServlet(urlPatterns = "/public/servlet-public-ejb-logout")
public class PublicServletPublicEJBLogout extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @EJB
    private PublicEJB publicEJB;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String webName = null;
        if (request.getUserPrincipal() != null) {
            webName = request.getUserPrincipal().getName();
        }

        String ejbName = publicEJB.getUserName();

        request.logout();
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        String webNameAfterLogout = null;
        if (request.getUserPrincipal() != null) {
            webNameAfterLogout = request.getUserPrincipal().getName();
        }

        String ejbNameAfterLogout = publicEJB.getUserName();

        response.getWriter().write("web username: " + webName + "\n" + "EJB username: " + ejbName + "\n");
        response.getWriter().write("web username after logout: " + webNameAfterLogout + "\n" + "EJB username after logout: " + ejbNameAfterLogout + "\n");

    }

}
