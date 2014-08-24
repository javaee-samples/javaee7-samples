package org.javaee7.jaxrs.resource.validation;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * @author Arun Gupta
 */
@ApplicationPath(MyApplication.PATH)
public class MyApplication extends Application {
    static final String PATH = "webresources";
}
