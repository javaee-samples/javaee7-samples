package org.javaee7.jaspic.programmaticauthentication.sam;

import static javax.security.auth.message.AuthStatus.SEND_SUCCESS;
import static javax.security.auth.message.AuthStatus.SUCCESS;
import static javax.security.auth.message.AuthStatus.SEND_CONTINUE;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

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
import javax.security.auth.message.callback.GroupPrincipalCallback;
import javax.security.auth.message.module.ServerAuthModule;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Very basic SAM that returns a single hardcoded user named "test" with role "architect" when the request *attribute*
 * <code>doLogin</code> is present.
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
    public AuthStatus validateRequest(MessageInfo messageInfo, Subject clientSubject, Subject serviceSubject)
        throws AuthException {

        HttpServletRequest request = (HttpServletRequest) messageInfo.getRequestMessage();
        HttpServletResponse response = (HttpServletResponse) messageInfo.getResponseMessage();

        Callback[] callbacks;

        if (request.getAttribute("doLogin") != null) { // notice "getAttribute" here, this is set by the Servlet

            // For the test perform a login by directly "returning" the details of the authenticated user.
            // Normally credentials would be checked and the details fetched from some repository

            callbacks = new Callback[] {
                // The name of the authenticated user
                new CallerPrincipalCallback(clientSubject, "test"),
                // the roles of the authenticated user
                new GroupPrincipalCallback(clientSubject, new String[] { "architect" })
            };
        } else if(messageInfo.getMap().get("javax.security.auth.message.MessagePolicy.isMandatory").equals("true")) {
        	
        	// Must set error code if authentication is mandatory, but unsuccessful
        	response.setStatus(SC_UNAUTHORIZED);
        	return SEND_CONTINUE;
        } else {

            // The JASPIC protocol for "do nothing"
            callbacks = new Callback[] { new CallerPrincipalCallback(clientSubject, (Principal) null) };
        }

        try {

            // Communicate the details of the authenticated user to the container. In many
            // cases the handler will just store the details and the container will actually handle
            // the login after we return from this method.
            handler.handle(callbacks);

        } catch (IOException | UnsupportedCallbackException e) {
            throw (AuthException) new AuthException().initCause(e);
        }

        return SUCCESS;
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