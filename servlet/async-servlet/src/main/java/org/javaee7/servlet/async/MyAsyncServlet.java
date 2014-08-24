package org.javaee7.servlet.async;

import java.io.IOException;
import java.io.PrintWriter;
import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Arun Gupta
 */
@WebServlet(urlPatterns = "/MyAsyncServlet", asyncSupported = true)
public class MyAsyncServlet extends HttpServlet {
    
//    @Resource(lookup="java:comp/DefaultManagedExecutorService")
    @Resource
    ManagedExecutorService executor;

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
        try (final PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Async Servlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Async Servlet</h1>");
            AsyncContext ac = request.startAsync();

            ac.addListener(new AsyncListener() {
                @Override
                public void onComplete(AsyncEvent event) throws IOException {
                    System.out.println("onComplete");
                }

                @Override
                public void onTimeout(AsyncEvent event) throws IOException {
                    System.out.println("onTimeout");
                }

                @Override
                public void onError(AsyncEvent event) throws IOException {
                    System.out.println("onError");
                }

                @Override
                public void onStartAsync(AsyncEvent event) throws IOException {
                    System.out.println("onStartAsync");
                }
            });
            executor.submit(new MyAsyncService(ac));
            out.println("Check \"server.log\" for output from Async Servlet");
            out.println("</body>");
            out.println("</html>");
        }
    }

    class MyAsyncService implements Runnable {

        AsyncContext ac;

        public MyAsyncService(AsyncContext ac) {
            this.ac = ac;
        }

        @Override
        public void run() {
            System.out.println("Running inside MyAsyncService");
            ac.complete();
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
