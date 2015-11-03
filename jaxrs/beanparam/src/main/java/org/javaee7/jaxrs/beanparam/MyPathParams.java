package org.javaee7.jaxrs.beanparam;

import javax.ws.rs.PathParam;

/**
 * @author xcoulon
 *
 */
public class MyPathParams {

    @PathParam("id1")
    private String id1;

    private String id2;

    public String getId1() {
        return id1;
    }

    public void setId1(String id1) {
        this.id1 = id1;
    }

    public String getId2() {
        return id2;
    }

    @PathParam("id2")
    public void setId2(String id2) {
        this.id2 = id2;
    }

}
