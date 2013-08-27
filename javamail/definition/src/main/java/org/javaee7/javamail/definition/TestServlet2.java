/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.javaee7.javamail.definition;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;
import javax.annotation.Resource;
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
 *
 * @author arungup
 */
@WebServlet(urlPatterns = {"/TestServlet2"})
@MailSessionDefinition(name = "java:comp/myMailSession",
        properties = {
            "mail.smtp.host=smtp.gmail.com",
            "mail.smtp.ssl.enable=true",
            "mail.smtp.auth=true",
            "mail.transport.protocol=smtp",
            "mail.debug=true"
        })
public class TestServlet2 extends HttpServlet {

    @Resource(lookup = "java:comp/myMailSession")
    Session session;

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
            out.println("<title>Servlet TestServlet2</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Sending email using @MailSessionDefinition</h1>");

            final Properties creds = new Properties();
            creds.load(new FileInputStream(System.getProperty("user.home")
                    + System.getProperty("file.separator")
                    + ".javamail"));
            
            try {
                out.println("Sending message using gmail...<br>");
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(creds.getProperty("username")));
                message.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(creds.getProperty("to")));
                message.setSubject("Sending message using JavaMail");
                message.setText("Java EE 7 is cool!");

//                Transport t = session.getTransport("smtp");
//                t.connect(creds.getProperty("username"), creds.getProperty("password"));
//                t.send(message);

                Transport.send(message);

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
