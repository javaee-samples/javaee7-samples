package org.javaee7.servlet.programmatic.registration;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author OrelGenya
 */
public class DynamicServlet extends HttpServlet {

    private static final long serialVersionUID = 8310377560908221629L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().print("dynamic GET");
    }

}