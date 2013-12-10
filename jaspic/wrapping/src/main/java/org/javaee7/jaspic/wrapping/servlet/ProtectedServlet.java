package org.javaee7.jaspic.wrapping.servlet;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This Servlet tests that the request and response objects it receives are the ones marked as wrapped by the SAM that executed
 * before the Servlet was called.
 * 
 * @author Arjan Tijms
 * 
 */
@WebServlet(urlPatterns = "/protected/servlet")
public class ProtectedServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Writer writer = response.getWriter();

        writer.write("request isWrapped: " + request.getAttribute("isWrapped"));
        writer.write("\n");
        writer.write("response isWrapped: " + response.getHeader("isWrapped"));
    }

}
