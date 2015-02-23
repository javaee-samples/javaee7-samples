package org.javaee7.concurrency.dynamicproxy;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javax.annotation.Resource;
import javax.enterprise.concurrent.ContextService;
import javax.enterprise.concurrent.ManagedThreadFactory;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Arun Gupta
 */
@WebServlet(urlPatterns = { "/TestMultipleInterfaceServlet" })
public class TestMultipleInterfaceServlet extends HttpServlet {

    //    @Resource(name = "java:comp/DefaultManagedThreadFactory")
    @Resource
    ManagedThreadFactory factory;

    //    @Resource(name = "java:comp/DefaultContextService")
    @Resource
    ContextService service;

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
        try (PrintWriter out = response.getWriter()) {
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Creating contextual proxy (with multiple interfaces)</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Creating contextual proxy (with multiple interfaces)</h1>");

            Object proxy = service.createContextualProxy(new MyRunnableWork(), Runnable.class, MyWork.class);
            out.println("Calling MyWork interface<br>");
            ((MyWork) proxy).myWork();
            out.println("Creating Java SE style ExecutorService<br>");
            ExecutorService executor = Executors.newFixedThreadPool(10, factory);
            out.println("Submitting the task<br>");
            Future f = executor.submit((Runnable) proxy);
            out.println("done<br><br>");
            out.println("Check server.log for output from the task.");
            out.println("</body>");
            out.println("</html>");
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
