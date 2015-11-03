package org.javaee7.jaspic.common;

import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.MessageInfo;
import javax.security.auth.message.config.ServerAuthConfig;
import javax.security.auth.message.config.ServerAuthContext;
import javax.security.auth.message.module.ServerAuthModule;

/**
 * This class functions as a kind of factory for {@link ServerAuthContext} instances, which are delegates for the actual
 * {@link ServerAuthModule} (SAM) that we're after.
 * 
 * @author Arjan Tijms
 */
public class TestServerAuthConfig implements ServerAuthConfig {

    private String layer;
    private String appContext;
    private CallbackHandler handler;
    private Map<String, String> providerProperties;
    private ServerAuthModule serverAuthModule;

    public TestServerAuthConfig(String layer, String appContext, CallbackHandler handler,
        Map<String, String> providerProperties, ServerAuthModule serverAuthModule) {
        this.layer = layer;
        this.appContext = appContext;
        this.handler = handler;
        this.providerProperties = providerProperties;
        this.serverAuthModule = serverAuthModule;
    }

    @Override
    public ServerAuthContext getAuthContext(String authContextID, Subject serviceSubject,
        @SuppressWarnings("rawtypes") Map properties) throws AuthException {
        return new TestServerAuthContext(handler, serverAuthModule);
    }

    // ### The methods below mostly just return what has been passed into the
    // constructor.
    // ### In practice they don't seem to be called

    @Override
    public String getMessageLayer() {
        return layer;
    }

    /**
     * It's not entirely clear what the difference is between the "application context identifier" (appContext) and the
     * "authentication context identifier" (authContext). In early iterations of the specification, authContext was called
     * "operation" and instead of the MessageInfo it was obtained by something called an "authParam".
     */
    @Override
    public String getAuthContextID(MessageInfo messageInfo) {
        return appContext;
    }

    @Override
    public String getAppContext() {
        return appContext;
    }

    @Override
    public void refresh() {
    }

    @Override
    public boolean isProtected() {
        return false;
    }

    public Map<String, String> getProviderProperties() {
        return providerProperties;
    }

}
