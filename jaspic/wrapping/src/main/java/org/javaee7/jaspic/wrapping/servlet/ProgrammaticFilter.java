package org.javaee7.jaspic.wrapping.servlet;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * This Filter tests that the request and response objects it receives are the ones marked as wrapped by the SAM that executed
 * before the Servlet was called.
 * 
 * @author Arjan Tijms
 * 
 */
public class ProgrammaticFilter implements Filter {
    
    public void init(FilterConfig fConfig) throws ServletException {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        
        Writer writer = response.getWriter();

        writer.write("programmatic filter request isWrapped: " + request.getAttribute("isWrapped"));
        writer.write("\n");
        writer.write("programmatic filter response isWrapped: " + ((HttpServletResponse)response).getHeader("isWrapped"));
        writer.write("\n");
        
        chain.doFilter(request, response);
    }

	public void destroy() {
	}

}
