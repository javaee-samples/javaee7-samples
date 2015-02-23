package org.javaee7.jaspic.common;

import java.util.Collections;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.AuthStatus;
import javax.security.auth.message.MessageInfo;
import javax.security.auth.message.ServerAuth;
import javax.security.auth.message.config.ServerAuthContext;
import javax.security.auth.message.module.ServerAuthModule;

/**
 * The Server Authentication Context is an extra (required) indirection between the Application Server and the actual Server
 * Authentication Module (SAM). This can be used to encapsulate any number of SAMs and either select one at run-time, invoke
 * them all in order, etc.
 * <p>
 * Since this simple example only has a single SAM, we delegate directly to that one. Note that this {@link ServerAuthContext}
 * and the {@link ServerAuthModule} (SAM) share a common base interface: {@link ServerAuth}.
 * 
 * @author Arjan Tijms
 */
public class TestServerAuthContext implements ServerAuthContext {

    private final ServerAuthModule serverAuthModule;

    public TestServerAuthContext(CallbackHandler handler, ServerAuthModule serverAuthModule) throws AuthException {
        this.serverAuthModule = serverAuthModule;
        serverAuthModule.initialize(null, null, handler, Collections.<String, String> emptyMap());
    }

    @Override
    public AuthStatus validateRequest(MessageInfo messageInfo, Subject clientSubject, Subject serviceSubject)
        throws AuthException {
        return serverAuthModule.validateRequest(messageInfo, clientSubject, serviceSubject);
    }

    @Override
    public AuthStatus secureResponse(MessageInfo messageInfo, Subject serviceSubject) throws AuthException {
        return serverAuthModule.secureResponse(messageInfo, serviceSubject);
    }

    @Override
    public void cleanSubject(MessageInfo messageInfo, Subject subject) throws AuthException {
        serverAuthModule.cleanSubject(messageInfo, subject);
    }

}
