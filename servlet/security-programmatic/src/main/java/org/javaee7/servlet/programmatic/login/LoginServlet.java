package org.javaee7.servlet.programmatic.login;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Arun Gupta
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        String user = request.getParameter("user");
        String password = request.getParameter("password");

        if (user != null && password != null) {
            // Goes directly to the container's identity store, by-passing
            // any authentication mechanism
            request.login(user, password);
        }

        userDetails(response.getWriter(), request);
    }

    private void userDetails(PrintWriter out, HttpServletRequest request) {
        out.println("isUserInRole?" + request.isUserInRole("g1"));
        out.println("getRemoteUser?" + request.getRemoteUser());
        out.println("getUserPrincipal?" + (request.getUserPrincipal() != null? request.getUserPrincipal().getName() : null));
        out.println("getAuthType?" + request.getAuthType());
    }

}
