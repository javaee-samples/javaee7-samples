package org.javaee7.javamail.definition;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Properties;
import javax.inject.Inject;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
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
@WebServlet(urlPatterns = { "/ProgrammaticEmailServlet" })
public class ProgrammaticEmailServlet extends HttpServlet {

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
            out.println("<title>Sending email using JavaMail API</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Sending email using JavaMail API</h1>");

            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.ssl.enable", "true");
            props.put("mail.smtp.auth", "true");
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.debug", "true");

            Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(creds.getFrom(), creds.getPassword());
                    }
                });
            //            Session session = Session.getInstance(props);

            try {

                out.println("Sending message from \""
                    + creds.getFrom()
                    + "\" to \""
                    + creds.getTo()
                    + "\"...<br>");

                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(creds.getFrom()));
                message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(creds.getTo()));
                message.setSubject("Sending message using Programmatic JavaMail " + Calendar.getInstance().getTime());
                message.setText("Java EE 7 is cool!");

                //                Transport t = session.getTransport();
                //                t.connect(creds.getFrom(), creds.getTo());
                //                t.sendMessage(message, message.getAllRecipients());
                Transport.send(message, message.getAllRecipients());

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
