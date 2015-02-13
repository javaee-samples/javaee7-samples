package org.javaee7.servlet.security.annotated;

import java.io.IOException;
import java.io.PrintWriter;
import javax.annotation.security.RolesAllowed;
import javax.servlet.ServletException;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.HttpMethodConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Arun Gupta
 */
@WebServlet("/SecureServlet")
//@ServletSecurity(value = @HttpConstraint(rolesAllowed = {"g1"}),
//        httpMethodConstraints = {
//            @HttpMethodConstraint(value = "GET", rolesAllowed = {"g1"}),
//            @HttpMethodConstraint(value = "POST", rolesAllowed = {"g1"})
//        })
@ServletSecurity(@HttpConstraint(rolesAllowed = { "g1" }))
@RolesAllowed("g1")
public class SecureServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response, String method)
        throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Servlet Security Annotated - Basic Auth with File-base Realm</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Basic Auth with File-base Realm (" + method + ")</h1>");
        out.println("<h2>Were you prompted for username/password ?</h2>");
        out.println("</body>");
        out.println("</html>");
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        processRequest(request, response, "GET");
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        processRequest(request, response, "POST");
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
