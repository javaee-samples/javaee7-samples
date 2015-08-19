/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
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
