package org.javaee7.jaspic.common;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.AuthStatus;
import javax.security.auth.message.MessageInfo;
import javax.security.auth.message.MessagePolicy;
import javax.security.auth.message.MessagePolicy.TargetPolicy;
import javax.security.auth.message.MessagePolicy.ProtectionPolicy;
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
	
	private static TargetPolicy[] targetPolicyArr = { new TargetPolicy(null, new ProtectionPolicy() {
		public String getID() {
			return ProtectionPolicy.AUTHENTICATE_SENDER;
		}
	}) };
	
	private static MessagePolicy mandatoryRequestPolicy = new MessagePolicy(targetPolicyArr, true);
	private static MessagePolicy optionalRequestPolicy = new MessagePolicy(targetPolicyArr, false);

	private ServerAuthModule mandatoryServerAuthModule; 
	private ServerAuthModule optionalServerAuthModule; 
	
	private ServerAuthModule chooseModule(MessageInfo messageInfo){
		if("true".equals(messageInfo.getMap().get("javax.security.auth.message.MessagePolicy.isMandatory"))) {
			return mandatoryServerAuthModule;
		} else {
			return optionalServerAuthModule;
		}
	}
	
    public TestServerAuthContext(CallbackHandler handler, Class<? extends ServerAuthModule> serverAuthModuleClass) throws AuthException {

    	//The spec requires that the mandatory authentication parameter can be accessed from the requestPolicy,
    	//even though it is not really useful, as the same information is available from the messageInfo map.
    	//To satisfy this requirement two SAM objects are constructed, and they are initialized with the appropriate requestPolicies. 
    	
    	try {
			mandatoryServerAuthModule = serverAuthModuleClass.getConstructor().newInstance();
			mandatoryServerAuthModule.initialize(mandatoryRequestPolicy, null, handler, Collections.<String, String> emptyMap());
	    	
	    	optionalServerAuthModule = serverAuthModuleClass.getConstructor().newInstance();
	    	optionalServerAuthModule.initialize(optionalRequestPolicy, null, handler, Collections.<String, String> emptyMap());
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException | SecurityException e) {
			throw new AuthException();
		}

    }

    @Override
    public AuthStatus validateRequest(MessageInfo messageInfo, Subject clientSubject, Subject serviceSubject)
        throws AuthException {
        return chooseModule(messageInfo).validateRequest(messageInfo, clientSubject, serviceSubject);
    }

    @Override
    public AuthStatus secureResponse(MessageInfo messageInfo, Subject serviceSubject) throws AuthException {
        return chooseModule(messageInfo).secureResponse(messageInfo, serviceSubject);
    }

    @Override
    public void cleanSubject(MessageInfo messageInfo, Subject subject) throws AuthException {
    	chooseModule(messageInfo).cleanSubject(messageInfo, subject);
    }

}
