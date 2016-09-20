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
package org.javaee7.jaxrs.request.binding;

import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.MatrixParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Providers;

/**
 * @author Arun Gupta
 */
@Path("persons")
public class MyResource {

    @Context
    Application app;
    @Context
    UriInfo uri;
    @Context
    HttpHeaders headers;
    @Context
    Request request;
    @Context
    SecurityContext security;
    @Context
    Providers providers;

    @GET
    @Produces("text/plain")
    public String getList(@CookieParam("JSESSIONID") String sessionId,
        @HeaderParam("Accept") String acceptHeader) {
        StringBuilder builder = new StringBuilder();
        builder
            .append("JSESSIONID: ")
            .append(sessionId)
            .append("<br>Accept: ")
            .append(acceptHeader);
        return builder.toString();
    }

    @GET
    @Path("matrix")
    @Produces("text/plain")
    public String getList(@MatrixParam("start") int start, @MatrixParam("end") int end) {
        StringBuilder builder = new StringBuilder();
        builder
            .append("start: ")
            .append(start)
            .append("<br>end: ")
            .append(end);
        return builder.toString();
    }

    @GET
    @Path("context")
    @Produces("text/plain")
    public String getList() {
        StringBuilder builder = new StringBuilder();
        builder.append("Application.classes: ")
            .append(app.getClasses())
            .append("<br>Path: ")
            .append(uri.getPath());
        for (String header : headers.getRequestHeaders().keySet()) {
            builder
                .append("<br>Http header: ")
                .append(headers.getRequestHeader(header));
        }
        builder.append("<br>Headers.cookies: ")
            .append(headers.getCookies())
            .append("<br>Request.method: ")
            .append(request.getMethod())
            .append("<br>Security.isSecure: ")
            .append(security.isSecure());
        return builder.toString();
    }

}
