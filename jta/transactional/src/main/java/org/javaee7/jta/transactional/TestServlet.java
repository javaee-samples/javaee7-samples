/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.javaee7.jta.transactional;

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
@WebServlet(urlPatterns = {"/TestServlet"})
public class TestServlet extends HttpServlet {
    
    @Inject MyBean bean;

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
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet TestCDIServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet TestCDIServlet at " + request.getContextPath() + "</h1>");
            out.println("Transactional.TxType.REQUIRED<br/>");
            try {
                bean.required();
            } catch (Exception e) {
                out.println("Exception: " + e.getMessage() + "<br/>");
            }
            out.println("No stack trace, right ?<p/>");
            
            out.println("Transactional.TxType.REQUIRES_NEW<br/>");
            try {
                bean.requiresNew();
            } catch (Exception e) {
                out.println("Exception: " + e.getMessage() + "<br/>");
            }
            out.println("No stack trace, right ?<p/>");
            
            out.println("Transactional.TxType.MANDATORY<br/>");
            try {
                bean.mandatory();
            } catch (Exception e) {
                out.println("Exception: " + e.getMessage() + "<br/>");
            }
            out.println("Got the expected exception, right ?<p/>");
            
            out.println("Transactional.TxType.SUPPORTS<br/>");
            try {
                bean.supports();
            } catch (Exception e) {
                out.println("Exception: " + e.getMessage() + "<br/>");
            }
            out.println("No stack trace, right ?<p/>");
            
            out.println("Transactional.TxType.NOT_SUPPORTED<br/>");
            try {
                bean.notSupported();
            } catch (Exception e) {
                out.println("Exception: " + e.getMessage() + "<br/>");
            }
            out.println("No stack trace, right ?<p/>");
            
            out.println("Transactional.TxType.NEVER<br/>");
            try {
                bean.never();
            } catch (Exception e) {
                out.println("Exception: " + e.getMessage() + "<br/>");
            }
            out.println("No stack trace, right ?<p/>");
            
            out.println("</body>");
            out.println("</html>");
        } finally {            
            out.close();
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
