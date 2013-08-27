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
package org.javaee7.json.streaming.generate;

import java.io.IOException;
import java.io.PrintWriter;
import javax.json.Json;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonGeneratorFactory;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Arun Gupta
 */
@WebServlet(urlPatterns = {"/StreamingGeneratorServlet"})
public class StreamingGeneratorServlet extends HttpServlet {

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
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Create JSON structures</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet TestServlet at " + request.getContextPath() + "</h1>");
//            JsonGeneratorFactory factory = Json.createGeneratorFactory(new JsonConfiguration().withPrettyPrinting());
            JsonGeneratorFactory factory = Json.createGeneratorFactory(null);
//            JsonGenerator gen = factory.createGenerator(System.out);

            out.println("Creating an empty object ...<br>");
            JsonGenerator gen = factory.createGenerator(out);
//            JsonGenerator gen = Json.createGenerator(out);
            gen.writeStartObject().writeEnd();
            gen.flush();
            out.println("<br>...done<br>");

            out.println("<br>Creating a simple object ...<br>");
            gen = factory.createGenerator(out);
            gen
                    .writeStartObject()
                    .write("apple", "red")
                    .write("banana", "yellow")
                    .writeEnd();
            gen.flush();
            out.println("<br>...done<br>");

            out.println("<br>Creating a simple array ...<br>");
            gen = factory.createGenerator(out);
            gen
                    .writeStartArray()
                    .writeStartObject()
                    .write("apple", "red")
                    .writeEnd()
                    .writeStartObject()
                    .write("banana", "yellow")
                    .writeEnd()
                    .writeEnd();
            gen.flush();
            out.println("<br>...done<br>");

            out.println("<br>Creating a nested structure ...<br>");
            gen = factory.createGenerator(out);
            gen
                    .writeStartObject()
                    .write("title", "The Matrix")
                    .write("year", 1999)
                    .writeStartArray("cast")
                    .write("Keanu Reaves")
                    .write("Laurence Fishburne")
                    .write("Carrie-Anne Moss")
                    .writeEnd()
                    .writeEnd();
            gen.flush();
            out.println("<br>...done<br>");

            out.println("<br>...done");
            out.println("</body>");
            out.println("</html>");
            gen.close();
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
