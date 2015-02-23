package org.javaee7.jpa.unsynchronized.pc;

import java.io.IOException;
import java.io.PrintWriter;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Arun Gupta
 */
@WebServlet(urlPatterns = { "/TestServlet" })
public class TestServlet extends HttpServlet {

    @Inject
    EmployeeBean bean;

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
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Unsynchronized Persistence Context</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Unsynchronized Persistence Context</h1>");
        listEmployees(out, "Initial list", "Total == 7?");
        Employee e = new Employee(8, "Priya");
        bean.persistWithoutJoin(e);
        listEmployees(out, "PC has not joined a transaction", "Total == 7?");
        bean.persistWithJoin(e);
        listEmployees(out, "PC has joined a transaction", "Total == 8?");
        out.println("</body>");
        out.println("</html>");
    }

    private void listEmployees(PrintWriter out, String title, String result) {
        out.println("<h3>" + title + " (" + bean.get().size() + ") " + result + "</h3>");
        for (Employee e : bean.get()) {
            out.println(e.getName() + "<br>");
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
