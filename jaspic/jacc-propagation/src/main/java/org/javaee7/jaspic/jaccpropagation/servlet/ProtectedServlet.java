package org.javaee7.jaspic.jaccpropagation.servlet;

import static org.javaee7.jaspic.jaccpropagation.jacc.JACC.getSubject;
import static org.javaee7.jaspic.jaccpropagation.jacc.JACC.hasAccess;

import java.io.IOException;

import javax.security.auth.Subject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author Arjan Tijms
 * 
 */
@WebServlet(urlPatterns = "/protected/servlet")
public class ProtectedServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Obtain the active subject via a JACC policy handler
        Subject subject = getSubject();
       
        if (subject == null) {
            response.getWriter().write("Can't get Subject. JACC doesn't seem to be available.");
            return;
        }

        // Check with JACC if the caller has access to this Servlet. As we're
        // currently in this very Servlet the answer can't be anything than "true" if
        // JASPIC, JACC and role propagation all work correctly.
        response.getWriter().write("Has access to /protected/servlet: " + hasAccess("/protected/servlet", subject));
    }

}
