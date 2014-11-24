package org.javaee7.servlet.async;

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
import java.io.IOException;

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
        AsyncContext ac = request.startAsync();

        ac.addListener(new AsyncListener() {
            @Override
            public void onComplete(AsyncEvent event) throws IOException {
                event.getSuppliedResponse().getWriter().println("onComplete");
            }

            @Override
            public void onTimeout(AsyncEvent event) throws IOException {
                event.getSuppliedResponse().getWriter().println("onTimeout");
                event.getAsyncContext().complete();
            }

            @Override
            public void onError(AsyncEvent event) throws IOException {
                event.getSuppliedResponse().getWriter().println("onError");
            }

            @Override
            public void onStartAsync(AsyncEvent event) throws IOException {
                event.getSuppliedResponse().getWriter().println("onStartAsync");
            }
        });
        executor.submit(new MyAsyncService(ac));
    }

    class MyAsyncService implements Runnable {

        AsyncContext ac;

        public MyAsyncService(AsyncContext ac) {
            this.ac = ac;
        }

        @Override
        public void run() {
            try {
                ac.getResponse().getWriter().println("Running inside MyAsyncService");
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
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
