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
// Portions Copyright [2018] [Payara Foundation and/or its affiliates]
package org.javaee7.jacc.contexts.servlet;

import java.io.FilePermission;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.AccessControlException;
import java.security.AccessController;

import javax.ejb.EJB;
import javax.naming.InitialContext;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.javaee7.jacc.contexts.bean.BeanMessageInterface;
import org.javaee7.jacc.contexts.bean.BeanRootInterface;

public class TestServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @EJB
    private BeanRootInterface root;

    @EJB
    private BeanMessageInterface msg;

    private String message;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        message = msg.getMessage();
        System.out.println("servlet init: message=" + message);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        String EXPECTED_RESULT = "PostBeanRootPostBeanLeafHelloBeanLeaf";
        boolean status = false;

        try {

            String testcase = request.getParameter("tc");
            out.println("testcase = " + testcase);
            out.println("TestServlet");
            out.println("contextPath=" + request.getContextPath());

            if (testcase != null) {

                if ("InjectLookup".equals(testcase)) {
                    // EJB injection check
                    // out.println("injected root: " + root);
                    String hello = root.sayHello();
                    out.println("Hello from injected bean: " + hello);

                    // EJB lookup check
                    InitialContext initialContext = new InitialContext();

                    
                    String EJBlookupName = null;
                    if (request.getParameter("web") == null) {
                        
                        // For war inside ears:
                        
                        // "java"glabal[/<app-name>]/<module-name>/<bean-name>"
                        // app-name -- name of ear file (option)
                        // module-name -- name of war or jar file
                        // bean-name -- name of ejb
                        
                        EJBlookupName = "java:global/appperms/apppermsEJB/BeanRoot";
                    } else {
                        // For standalone war:
                        EJBlookupName = "java:module/BeanRoot";
                    }
                    
                    BeanRootInterface root2 = (BeanRootInterface) initialContext.lookup(EJBlookupName);

                    // out.println("global root: " + root2);
                    String hello2 = root2.sayHello();
                    out.println("Hello from lookup bean: " + hello2);

                    StringBuffer checkReport = new StringBuffer(" -Servlet test- ");
                    FilePermission filePermission = new FilePermission("/scratch/spei/bug/test/war.txt", "delete");
                    try {
                        if (System.getSecurityManager() != null) {
                            AccessController.checkPermission(filePermission);
                            checkReport.append("servlet - success for WAR.txt; ");
                        } else
                            checkReport.append("servlet - bypass for WAR.txt; ");

                    } catch (AccessControlException e) {
                        checkReport.append("servlet - failed for WAR.txt; ");
                    }

                    filePermission = new FilePermission("/scratch/spei/bug/test/ear.txt", "delete");
                    try {
                        if (System.getSecurityManager() != null) {
                            AccessController.checkPermission(filePermission);
                            checkReport.append("servlet - success for EAR.txt; ");
                        } else
                            checkReport.append("servlet - bypass for EAR.txt; ");
                    } catch (AccessControlException e) {
                        checkReport.append("servlet - failed for EAR.txt; ");
                    }

                    filePermission = new FilePermission("/scratch/spei/bug/test/ejb.txt", "delete");
                    try {
                        if (System.getSecurityManager() != null) {
                            AccessController.checkPermission(filePermission);
                            checkReport.append("servlet - success for EJB.txt; ");
                        } else
                            checkReport.append("servlet - bypass for EJB.txt; ");
                    } catch (AccessControlException e) {
                        checkReport.append("servlet - failed for EJB.txt; ");
                    }

                    String checkReportString = checkReport.toString();
                    out.println("test: " + checkReportString);

                    if (hello.equals(hello2) && !checkReportString.contains("failed") && !hello.contains("failed")) {
                        status = true;
                    }
                } else if ("Startup".equals(testcase)) {
                    // Deployment check for startup
                    out.println("message by deployment: " + message);
                    if (message != null && message.equals(EXPECTED_RESULT)) {
                        status = true;
                    }
                }
            }

        } catch (Throwable th) {
            th.printStackTrace(out);
        } finally {
            if (status) {
                out.println("Test:Pass");
            } else {
                out.println("Test:Fail");
            }
            out.close();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
