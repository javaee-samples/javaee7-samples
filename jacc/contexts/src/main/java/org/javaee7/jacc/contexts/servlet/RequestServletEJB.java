package org.javaee7.jacc.contexts.servlet;

import java.io.IOException;

import javax.inject.Inject;
import javax.security.jacc.PolicyContextException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.javaee7.jacc.contexts.bean.JaccRequestBean;

/**
 * 
 * @author Arjan Tijms
 * 
 */
@WebServlet(urlPatterns = "/requestServletEJB")
public class RequestServletEJB extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Inject
    private JaccRequestBean jaccRequestBean;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setAttribute("jaccTest", "true");

        try {
            if (jaccRequestBean.getRequest() != null) {
                response.getWriter().print("Obtained request from context.");

                if (jaccRequestBean.hasAttribute()) {
                    response.getWriter().print("Attribute present in request from context.");
                }

                if (jaccRequestBean.hasParameter()) {
                    response.getWriter().print("Request parameter present in request from context.");
                }

            }
        } catch (PolicyContextException e) {
            e.printStackTrace(response.getWriter());
        }

    }

}
