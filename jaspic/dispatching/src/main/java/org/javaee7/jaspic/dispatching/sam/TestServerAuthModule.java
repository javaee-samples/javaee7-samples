package org.javaee7.jaspic.dispatching.sam;

import static javax.security.auth.message.AuthStatus.SEND_CONTINUE;
import static javax.security.auth.message.AuthStatus.SEND_SUCCESS;
import static javax.security.auth.message.AuthStatus.SUCCESS;

import java.io.IOException;
import java.security.Principal;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.AuthStatus;
import javax.security.auth.message.MessageInfo;
import javax.security.auth.message.MessagePolicy;
import javax.security.auth.message.callback.CallerPrincipalCallback;
import javax.security.auth.message.module.ServerAuthModule;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author Arjan Tijms
 * 
 */
public class TestServerAuthModule implements ServerAuthModule {

    private CallbackHandler handler;
    private Class<?>[] supportedMessageTypes = new Class[] { HttpServletRequest.class, HttpServletResponse.class };

    @Override
    public void initialize(MessagePolicy requestPolicy, MessagePolicy responsePolicy, CallbackHandler handler,
        @SuppressWarnings("rawtypes") Map options) throws AuthException {
        this.handler = handler;
    }

    @Override
    public AuthStatus validateRequest(MessageInfo messageInfo, Subject clientSubject, Subject serviceSubject) throws AuthException {
        try {
            HttpServletRequest request = (HttpServletRequest) messageInfo.getRequestMessage();
            HttpServletResponse response = (HttpServletResponse) messageInfo.getResponseMessage();

            if ("include".equals(request.getParameter("dispatch"))) {
                request.getRequestDispatcher("/includedServlet")
                       .include(request, response);

                // "Do nothing", required protocol when returning SUCCESS
                handler.handle(new Callback[] { new CallerPrincipalCallback(clientSubject, (Principal) null) });

                // When using includes, the response stays open and the main
                // resource can also write to the response
                return SUCCESS;

            } else {
                request.getRequestDispatcher("/forwardedServlet")
                       .forward(request, response);

                // MUST NOT invoke the resource, so CAN NOT return SUCCESS here.
                return SEND_CONTINUE;
            }
            
        } catch (IOException | ServletException | UnsupportedCallbackException e) {
            throw (AuthException) new AuthException().initCause(e);
        }
    }

    @Override
    public Class<?>[] getSupportedMessageTypes() {
        return supportedMessageTypes;
    }

    @Override
    public AuthStatus secureResponse(MessageInfo messageInfo, Subject serviceSubject) throws AuthException {
        return SEND_SUCCESS;
    }

    @Override
    public void cleanSubject(MessageInfo messageInfo, Subject subject) throws AuthException {

    }
}