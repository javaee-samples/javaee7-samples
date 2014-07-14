package org.javaee7.javamail.definition;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.mail.MailSessionDefinition;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Arun Gupta
 */
@WebServlet(urlPatterns = {"/AnnotatedEmailServlet"})
@MailSessionDefinition(name = "java:comp/myMailSession",
                host = "smtp.gmail.com",
                transportProtocol = "smtps",
        properties = {
            "mail.debug=true"
        })
public class AnnotatedEmailServlet extends HttpServlet {

    @Resource(lookup = "java:comp/myMailSession")
    Session session;

    @Inject
    Credentials creds;

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
            out.println("<title>Sending email using @MailSessionDefinition</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Sending email using @MailSessionDefinition</h1>");

            try {
                out.println("Sending message from \""
                        + creds.getFrom()
                        + "\" to \""
                        + creds.getTo()
                        + "\"...<br>");

                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(creds.getFrom()));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(creds.getTo()));
                message.setSubject("Sending message using Annotated JavaMail "
                        + Calendar.getInstance().getTime());
                message.setText("Java EE 7 is cool!");

                Transport t = session.getTransport();
                t.connect(creds.getFrom(), creds.getPassword());
                t.sendMessage(message, message.getAllRecipients());

                out.println("message sent!");

            } catch (MessagingException e) {
                throw new RuntimeException(e);
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
