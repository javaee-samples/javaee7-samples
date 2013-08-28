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
package org.javaee7.json.streaming.parser;

import java.io.IOException;
import java.io.PrintWriter;
import javax.json.Json;
import javax.json.stream.JsonParser;
import static javax.json.stream.JsonParser.Event.END_ARRAY;
import static javax.json.stream.JsonParser.Event.END_OBJECT;
import static javax.json.stream.JsonParser.Event.KEY_NAME;
import static javax.json.stream.JsonParser.Event.START_ARRAY;
import static javax.json.stream.JsonParser.Event.START_OBJECT;
import static javax.json.stream.JsonParser.Event.VALUE_FALSE;
import static javax.json.stream.JsonParser.Event.VALUE_NULL;
import static javax.json.stream.JsonParser.Event.VALUE_NUMBER;
import static javax.json.stream.JsonParser.Event.VALUE_STRING;
import static javax.json.stream.JsonParser.Event.VALUE_TRUE;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Arun Gupta
 */
@WebServlet(urlPatterns = {"/JsonParserFromStream"})
public class JsonParserFromStream extends HttpServlet {

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
            out.println("<title>Streaming Reading of JSON from Stream</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Streaming Reading of JSON from Stream</h1>");

            ServletContext servletContext = request.getServletContext();
            out.println("Reading an empty object<br>");
            JsonParser jsonParser = Json.createParser(servletContext.getResourceAsStream("/1.json"));
            parseEvents(jsonParser, out);

            out.println("<br><br>Reading an object with two elements<br>");
            jsonParser = Json.createParser(servletContext.getResourceAsStream("/2.json"));
            parseEvents(jsonParser, out);

            out.println("<br><br>Reading an array with two objects<br>");
            jsonParser = Json.createParser(servletContext.getResourceAsStream("/3.json"));
            parseEvents(jsonParser, out);

            out.println("<br><br>Reading a nested structure<br>");
            jsonParser = Json.createParser(servletContext.getResourceAsStream("/4.json"));
            parseEvents(jsonParser, out);

            out.println("</body>");
            out.println("</html>");
        }
    }

    private void parseEvents(JsonParser parser, PrintWriter out) {
        while (parser.hasNext()) {
            switch (parser.next()) {
                case START_ARRAY:
                    out.println("Starting an array<br>");
                    break;
                case START_OBJECT:
                    out.println("Starting an object<br>");
                    break;
                case END_ARRAY:
                    out.println("Ending an array<br>");
                    break;
                case END_OBJECT:
                    out.println("Ending an object<br>");
                    break;
                case KEY_NAME:
                    out.format("Found key: <b>%1$s</b><br>", parser.getString());
                    break;
                case VALUE_STRING:
                    out.format("Found value: <b>%1$s</b><br>", parser.getString());
                    break;

                case VALUE_NUMBER:
                    if (parser.isIntegralNumber()) {
                        out.format("Found value: <b>%1$d</b><br>", parser.getInt());
//                        out.format("Found value: <b>%1$d</b><br>", parser.getLong());
                    } else {
                        out.format("Found value: <b>%1$f</b><br>", parser.getBigDecimal());
                    }
                    break;
                case VALUE_TRUE:
                case VALUE_FALSE:
                    out.format("Found boolean value: <b>%1$b</b><br>", parser.getString());
                    break;
                case VALUE_NULL:
                    out.format("Found null value");
                    break;
                default:
                    out.format("What did you find ?");
                    break;
            }
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
