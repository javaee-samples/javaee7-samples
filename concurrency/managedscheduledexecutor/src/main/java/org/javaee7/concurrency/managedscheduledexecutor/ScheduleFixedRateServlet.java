package org.javaee7.concurrency.managedscheduledexecutor;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedScheduledExecutorService;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Arun Gupta
 */
@WebServlet(urlPatterns = { "/ScheduleFixedRateServlet" })
public class ScheduleFixedRateServlet extends HttpServlet {

    //    @Resource(name = "concurrent/myScheduledExecutor2")
    @Resource(name = "DefaultManagedScheduledExecutorService")
    ManagedScheduledExecutorService executor;

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
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Schedule at fixed rate</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Schedule at fixed rate</h1>");
            ScheduledFuture<?> f = executor.scheduleAtFixedRate(new MyRunnableTask(5), 2, 3, TimeUnit.SECONDS);
            ////            try {
            ////                Thread.sleep(1000);
            ////            } catch (InterruptedException ex) {
            ////                Logger.getLogger(TestScheduleFixedRateServlet.class.getName()).log(Level.SEVERE, null, ex);
            ////            }
            ////            f.cancel(true);
            //            
            //            executor.scheduleWithFixedDelay(new MyRunnableTask(5), 2, 3, TimeUnit.SECONDS);
            System.out.println("Runnable Task completed");
            out.println("<br><br>Check server.log for output");
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
