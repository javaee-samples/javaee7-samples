package org.javaee7.servlet.programmatic.registration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author OrelGenya
 */
public class DynamicServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().print("dynamic GET");
    }

    @Override
    public String getServletInfo() {
        return "My dynamic awesome servlet!";
    }
}