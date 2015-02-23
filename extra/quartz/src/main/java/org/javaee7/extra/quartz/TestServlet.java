package org.javaee7.extra.quartz;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

/**
 * @author Arun Gupta
 */
@WebServlet(urlPatterns = { "/TestServlet" })
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
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Quartz Scheduler</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Quartz Scheduler</h1>");

            JobDetail simpleJob = JobBuilder.newJob(MySimpleJob.class).build();
            JobDetail cronJob = JobBuilder.newJob(MyCronJob.class).build();

            Trigger simpleTrigger = TriggerBuilder
                .newTrigger()
                .withSchedule(
                    SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(1).repeatForever()
                )
                .build();

            Trigger cronTrigger = TriggerBuilder
                .newTrigger()
                .withSchedule(
                    CronScheduleBuilder.cronSchedule("0/3 * * * * ?")
                )
                .build();

            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

            out.println("Starting the scheduler");
            scheduler.start();

            out.println("<h2>Starting Simple Trigger - every 1 second</h2>");
            scheduler.scheduleJob(simpleJob, simpleTrigger);
            out.println("<h2>Starting Cron Trigger - every 3 seconds</h2>");
            scheduler.scheduleJob(cronJob, cronTrigger);

            out.println("Sleeping for 7 seconds");
            Thread.sleep(7000);

            out.println("<br>Shutting down the scheduler");
            scheduler.shutdown();

            out.println("<br><br>Check \"server.log\" for output - 8 outputs from simple trigger, 3 from cron trigger");
            out.println("</body>");
            out.println("</html>");
        } catch (SchedulerException | InterruptedException ex) {
            Logger.getLogger(TestServlet.class.getName()).log(Level.SEVERE, null, ex);
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
