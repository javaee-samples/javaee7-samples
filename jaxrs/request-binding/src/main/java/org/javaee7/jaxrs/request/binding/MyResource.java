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

    @Context Application app;
    @Context UriInfo uri;
    @Context HttpHeaders headers;
    @Context Request request;
    @Context SecurityContext security;
    @Context Providers providers;

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
