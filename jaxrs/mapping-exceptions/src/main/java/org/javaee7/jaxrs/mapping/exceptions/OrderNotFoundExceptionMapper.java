package org.javaee7.jaxrs.mapping.exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * @author Arun Gupta
 */
@Provider
public class OrderNotFoundExceptionMapper
        implements ExceptionMapper<OrderNotFoundException> {

    @Override
    public Response toResponse(OrderNotFoundException exception) {
        return Response
                .status(Response.Status.PRECONDITION_FAILED)
                .entity("Response not found")
                .build();
    }
}
