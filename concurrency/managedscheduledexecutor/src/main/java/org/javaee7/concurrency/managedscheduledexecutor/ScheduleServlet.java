package org.javaee7.concurrency.managedscheduledexecutor;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedScheduledExecutorService;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author arungup
 */
@WebServlet(urlPatterns = {"/ScheduleServlet"})
public class ScheduleServlet extends HttpServlet {

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
            out.println("<title>Schedule using Callable after 5 seconds</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Schedule using Callable after 5 seconds</h1>");
            out.println("<h2>Scheduling tasks using Callable</h2>");
            ScheduledFuture<Product> future = executor.schedule(new MyCallableTask(5), 5, TimeUnit.SECONDS);
            while (true) {
                if (future.isDone()) {
                    break;
                } else {
                    System.out.println("Checking Callable Future, waiting for 1 sec");
                    Thread.sleep(1000);
                }
            }
            out.println("Callable Task completed: " + future.get().getId());
            
            out.println("<h2>Scheduling tasks using Runnable</h2>");
            ScheduledFuture<?> f = executor.schedule(new MyRunnableTask(10), 5, TimeUnit.SECONDS);
            while (true) {
                if (f.isDone()) {
                    break;
                } else {
                    System.out.println("Checking Runnable Future, waiting for 1 sec");
                    Thread.sleep(1000);
                }
            }
            out.println("Runnable Task completed: " + future.get().getId());
            out.println("<br><br>Check server.log for output");
            out.println("</body>");
            out.println("</html>");
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(ScheduleServlet.class.getName()).log(Level.SEVERE, null, ex);
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
