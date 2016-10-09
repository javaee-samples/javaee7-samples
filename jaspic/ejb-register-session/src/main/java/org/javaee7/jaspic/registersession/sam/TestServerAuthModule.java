package org.javaee7.jaspic.registersession.sam;

import static java.lang.Boolean.TRUE;
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
import javax.security.auth.message.callback.GroupPrincipalCallback;
import javax.security.auth.message.module.ServerAuthModule;
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

    @SuppressWarnings("unchecked")
    @Override
    public AuthStatus validateRequest(MessageInfo messageInfo, Subject clientSubject, Subject serviceSubject)
        throws AuthException {

        HttpServletRequest request = (HttpServletRequest) messageInfo.getRequestMessage();
        Callback[] callbacks;

        Principal userPrincipal = request.getUserPrincipal();
        if (userPrincipal != null && request.getParameter("continueSession") != null) {

            // ### If already authenticated before, continue this session

            // Execute protocol to signal container registered authentication session be used.
            callbacks = new Callback[] { new CallerPrincipalCallback(clientSubject, userPrincipal) };

        } else if (request.getParameter("doLogin") != null) {

            // ### If not authenticated before, do a new login if so requested

            // For the test perform a login by directly "returning" the details of the authenticated user.
            // Normally credentials would be checked and the details fetched from some repository

            callbacks = new Callback[] {
                // The name of the authenticated user
                
                    request.getParameter("customPrincipal") == null?
                        // Name based Callback 
                        new CallerPrincipalCallback(clientSubject, "test") :
                        
                        // Custom principal based Callback
                        new CallerPrincipalCallback(clientSubject, new MyPrincipal("test")),
                
                
                // the roles of the authenticated user
                new GroupPrincipalCallback(clientSubject, new String[] { "architect" }) };

            // Tell container to register an authentication session.
            messageInfo.getMap().put("javax.servlet.http.registerSession", TRUE.toString());
        } else {

            // ### If no registered session and no login request "do nothing"

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
        return AuthStatus.SEND_SUCCESS;
    }

    @Override
    public void cleanSubject(MessageInfo messageInfo, Subject subject) throws AuthException {

    }
}