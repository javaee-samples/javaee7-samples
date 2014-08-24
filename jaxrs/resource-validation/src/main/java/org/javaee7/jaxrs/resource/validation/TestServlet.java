package org.javaee7.jaxrs.resource.validation;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.Response;

/**
 * @author Arun Gupta
 */
@WebServlet(urlPatterns = {"/TestServlet"})
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
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Request Binding</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Resource Validation in JAX-RS</h1>");
        Client client = ClientBuilder.newClient();
        List<WebTarget> targets = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            targets.add(client
                    .target("http://"
                    + request.getServerName()
                    + ":"
                    + request.getServerPort()
                    + request.getContextPath()
                    + "/webresources/names" + (i + 1)));
        }

        for (WebTarget target : targets) {
            out.println("<h2>Using target: " + target.getUri() + "</h2>");
            MultivaluedHashMap<String, String> map = new MultivaluedHashMap<>();

            out.print("<br><br>POSTing with valid data ...<br>");
            map.add("firstName", "Sheldon");
            map.add("lastName", "Cooper");
            map.add("email", "random@example.com");
            Response r = target.request().post(Entity.form(map));
            printResponseStatus(out, r, 200);

            out.println();

            out.print("<br><br>POSTing with invalid (null) \"firstName\" ...<br>");
            map.putSingle("firstName", null);
            r = target.request().post(Entity.form(map));
            printResponseStatus(out, r, 200);
            out.println();

            out.print("<br><br>POSTing with invalid (null) \"lastName\" ...<br>");
            map.putSingle("firstName", "Sheldon");
            map.putSingle("lastName", null);
            r = target.request().post(Entity.form(map));
            printResponseStatus(out, r, 400);

            out.print("<br><br>POSTing with invalid (missing @) email \"email\" ...<br>");
            map.putSingle("lastName", "Cooper");
            map.putSingle("email", "randomexample.com");
            r = target.request().post(Entity.form(map));
            printResponseStatus(out, r, 400);

            out.print("<br><br>POSTing with invalid (missing .com) email \"email\" ...<br>");
            map.putSingle("email", "random@examplecom");
            r = target.request().post(Entity.form(map));
            printResponseStatus(out, r, 400);
        }

        WebTarget target = client
                .target("http://"
                + request.getServerName()
                + ":"
                + request.getServerPort()
                + request.getContextPath()
                + "/webresources/nameadd");
        out.println("<h2>Using target: " + target.getUri() + "</h2>");
        out.print("<br><br>POSTing using @Valid (all vaild data) ...<br>");
        Response r = target
                .request()
                .post(Entity.json(new Name("Sheldon", "Cooper", "sheldon@cooper.com")));
        printResponseStatus(out, r, 200);

        out.print("<br><br>POSTing using @Valid, with invalid (null) \"firstName\" ...<br>");
        r = target
                .request()
                .post(Entity.json(new Name(null, "Cooper", "sheldon@cooper.com")));
        printResponseStatus(out, r, 400);

        out.print("<br><br>POSTing using @Valid, with invalid (null) \"lastName\" ...<br>");
        r = target
                .request()
                .post(Entity.json(new Name("Sheldon", null, "sheldon@cooper.com")));
        printResponseStatus(out, r, 400);

        out.print("<br><br>POSTing using @Valid, with invalid (missing @) email \"email\" ...<br>");
        r = target
                .request()
                .post(Entity.json(new Name("Sheldon", "Cooper", "sheldoncooper.com")));
        printResponseStatus(out, r, 400);

        out.println("<br>... done.<br>");

        out.println("</body>");
        out.println("</html>");
    }

    private void printResponseStatus(PrintWriter out, Response r, int expected) {
        out.println("Received status code: " + r.getStatus() + ", reason: " + r.getStatusInfo().getReasonPhrase());
        out.println("<br>Received " + expected + " status code ?");
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
