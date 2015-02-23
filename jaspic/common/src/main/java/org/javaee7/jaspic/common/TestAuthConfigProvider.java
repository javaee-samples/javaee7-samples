package org.javaee7.jaspic.common;

import java.util.Map;

import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.config.AuthConfigFactory;
import javax.security.auth.message.config.AuthConfigProvider;
import javax.security.auth.message.config.ClientAuthConfig;
import javax.security.auth.message.config.ServerAuthConfig;
import javax.security.auth.message.config.ServerAuthContext;
import javax.security.auth.message.module.ServerAuthModule;

/**
 * This class functions as a kind of factory-factory for {@link ServerAuthConfig} instances, which are by themselves factories
 * for {@link ServerAuthContext} instances, which are delegates for the actual {@link ServerAuthModule} (SAM) that we're after.
 * 
 * @author Arjan Tijms
 */
public class TestAuthConfigProvider implements AuthConfigProvider {

    private static final String CALLBACK_HANDLER_PROPERTY_NAME = "authconfigprovider.client.callbackhandler";

    private Map<String, String> providerProperties;
    private ServerAuthModule serverAuthModule;

    public TestAuthConfigProvider(ServerAuthModule serverAuthModule) {
        this.serverAuthModule = serverAuthModule;
    }

    /**
     * Constructor with signature and implementation that's required by API.
     * 
     * @param properties
     * @param factory
     */
    public TestAuthConfigProvider(Map<String, String> properties, AuthConfigFactory factory) {
        this.providerProperties = properties;

        // API requires self registration if factory is provided. Not clear
        // where the "layer" (2nd parameter)
        // and especially "appContext" (3rd parameter) values have to come from
        // at this place.
        if (factory != null) {
            factory.registerConfigProvider(this, null, null, "Auto registration");
        }
    }

    /**
     * The actual factory method that creates the factory used to eventually obtain the delegate for a SAM.
     */
    @Override
    public ServerAuthConfig getServerAuthConfig(String layer, String appContext, CallbackHandler handler) throws AuthException,
        SecurityException {
        return new TestServerAuthConfig(layer, appContext, handler == null ? createDefaultCallbackHandler() : handler,
            providerProperties, serverAuthModule);
    }

    @Override
    public ClientAuthConfig getClientAuthConfig(String layer, String appContext, CallbackHandler handler) throws AuthException,
        SecurityException {
        return null;
    }

    @Override
    public void refresh() {
    }

    /**
     * Creates a default callback handler via the system property "authconfigprovider.client.callbackhandler", as seemingly
     * required by the API (API uses wording "may" create default handler). TODO: Isn't
     * "authconfigprovider.client.callbackhandler" JBoss specific?
     * 
     * @return
     * @throws AuthException
     */
    private CallbackHandler createDefaultCallbackHandler() throws AuthException {
        String callBackClassName = System.getProperty(CALLBACK_HANDLER_PROPERTY_NAME);

        if (callBackClassName == null) {
            throw new AuthException("No default handler set via system property: " + CALLBACK_HANDLER_PROPERTY_NAME);
        }

        try {
            return (CallbackHandler) Thread.currentThread().getContextClassLoader().loadClass(callBackClassName).newInstance();
        } catch (Exception e) {
            throw new AuthException(e.getMessage());
        }
    }

}
