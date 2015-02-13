package org.javaee7.servlet.protocolhandler;

import java.io.IOException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpUpgradeHandler;
import javax.servlet.http.WebConnection;

/**
 * @author Arun Gupta
 */
public class MyProtocolHandler implements HttpUpgradeHandler {

    //    public MyProtocolHandler(ServletInputStream in, ServletOutputStream out) {
    //    }

    @Override
    public void init(WebConnection wc) {
        try (ServletInputStream input = wc.getInputStream();
            ServletOutputStream output = wc.getOutputStream();) {
        } catch (IOException ex) {
        }
    }

    @Override
    public void destroy() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
