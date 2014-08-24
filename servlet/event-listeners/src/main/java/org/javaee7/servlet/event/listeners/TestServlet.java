package org.javaee7.servlet.event.listeners;

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
@WebServlet(urlPatterns = "/TestServlet")
public class TestServlet extends HttpServlet {

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
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Servlet Event Listeners</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Servlet Event Listeners</h1>");
        out.println("<h2>Setting, updating, and removing ServletContext Attributes</h2>");
        request.getServletContext().setAttribute("attribute1", "attribute-value1");
        request.getServletContext().setAttribute("attribute1", "attribute-updated-value1");
        request.getServletContext().removeAttribute("attribute1");
        out.println("done");
        out.println("<h2>Setting, updating, and removing HttpSession Attributes</h2>");
        request.getSession(true).setAttribute("attribute1", "attribute-value1");
        request.getSession().setAttribute("attribute1", "attribute-updated-value1");
        request.getSession().removeAttribute("attribute1");
        out.println("done");
        out.println("<h2>Setting, updating, and removing ServletRequest Attributes</h2>");
        request.setAttribute("attribute1", "attribute-value1");
        request.setAttribute("attribute1", "attribute-updated-value1");
        request.removeAttribute("attribute1");
        out.println("done");
        out.println("<h2>Invalidating session</h2>");
        request.getSession().invalidate();
        out.println("done");
        out.println("<br><br>Check output in server log");
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
