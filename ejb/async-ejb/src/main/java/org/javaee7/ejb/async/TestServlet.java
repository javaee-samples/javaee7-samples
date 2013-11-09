package org.javaee7.ejb.async;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Arun Gupta
 */
@WebServlet(urlPatterns = {"/TestServlet"})
public class TestServlet extends HttpServlet {
    @Inject MyAsyncBeanMethodLevel methodBean;
    @Inject MyAsyncBeanClassLevel classBean;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Asynchronous EJB</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Asynchronous EJB</h1>");
            out.println("<h2>Method-level annotation</h2>");
            Future<Integer> result = methodBean.addNumbers(2, 4);
            while (true) {
                out.println("<br>Waiting ...");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(TestServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (result.isDone()) {
                    out.println("<br>Result is now ready...");
                    try {
                        out.println("<br>Got the result: " + result.get());
                    } catch (InterruptedException | ExecutionException ex) {
                        Logger.getLogger(TestServlet.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                }
            }
            
            out.println("<h2>Class-level annotation</h2>");
            result = methodBean.addNumbers(2, 4);
            while (true) {
                out.println("<br>Waiting ...");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(TestServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (result.isDone()) {
                    out.println("<br>Result is now ready...");
                    try {
                        out.println("<br>Got the result: " + result.get());
                    } catch (InterruptedException | ExecutionException ex) {
                        Logger.getLogger(TestServlet.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                }
            }
            out.println("</body>");
            out.println("</html>");
        }
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
        processRequest(request, response);
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
