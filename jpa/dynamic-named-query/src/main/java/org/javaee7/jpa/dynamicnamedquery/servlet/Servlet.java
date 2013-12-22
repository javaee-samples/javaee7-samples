package org.javaee7.jpa.dynamicnamedquery.servlet;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.javaee7.jpa.dynamicnamedquery.entity.TestEntity;
import org.javaee7.jpa.dynamicnamedquery.service.TestService;

/**
 * This servlet is not part of the normal test flow, but is an extra artifact to use when deploying the WAR created by this
 * Maven project standalone.
 * 
 * @author Arjan Tijms
 * 
 */
@WebServlet("/servlet")
public class Servlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Inject
    private TestService testService;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        TestEntity testEntity = new TestEntity();
        testEntity.setValue("myValue");
        testService.save(testEntity);

        response.getWriter().write("#Entities after insert = " + testService.getAll().size() + "\n");
        response.getWriter().write("Value found = " + (testService.getByValue("myValue").size() > 0));
    }

}
