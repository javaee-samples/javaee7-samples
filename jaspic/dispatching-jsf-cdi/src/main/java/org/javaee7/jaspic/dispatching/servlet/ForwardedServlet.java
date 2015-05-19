package org.javaee7.jaspic.dispatching.servlet;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.javaee7.jaspic.dispatching.bean.MyBean;

/**
 * 
 * @author Arjan Tijms
 * 
 */
@WebServlet(urlPatterns = "/forwardedServlet")
public class ForwardedServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    
    @Inject
    private MyBean myBean;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().write("response from forwardedServlet - " + myBean.getText());
    }

}