package org.javaee7.jaxrs.beanparam;

import javax.ws.rs.QueryParam;

/**
 * @author xcoulon
 *
 */
public class MyQueryParams {

    @QueryParam("param1")
    private String param1;

    @QueryParam("param2")
    private String param2;

    @QueryParam("param3")
    private String param3;

    public String getParam1() {
        return param1;
    }

    public String getParam2() {
        return param2;
    }

    public String getParam3() {
        return param3;
    }

}
