package org.javaee7.cdi.bean.scopes;

import java.io.IOException;
import java.io.PrintWriter;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author arungup
 */
@WebServlet(urlPatterns = { "/ServerServlet" })
public class ServerServlet extends HttpServlet {

    @Inject
    MyRequestScopedBean requestBean;
    @Inject
    MyRequestScopedBean requestBean2;

    @Inject
    MySessionScopedBean sessionBean;
    @Inject
    MySessionScopedBean sessionBean2;

    @Inject
    MyApplicationScopedBean applicationBean;
    @Inject
    MySingletonScopedBean singletonBean;

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        try (PrintWriter out = response.getWriter()) {
            out.println("<b>Request-scoped bean</b>");
            out.println("<br><br>(1): " + requestBean.getID());
            out.println("<br>(2): " + requestBean2.getID());
            out.println("<br><br><b>Session-scoped bean</b>");
            out.println("<br><br>(1): " + sessionBean.getID());
            out.println("<br>(2): " + sessionBean2.getID());
            out.println("<br><br><b>Application-scoped bean</b>: " + applicationBean.getID());
            out.println("<br><br><b>Singleton-scoped bean</b>: " + singletonBean.getID());
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        processRequest(request, response);
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
