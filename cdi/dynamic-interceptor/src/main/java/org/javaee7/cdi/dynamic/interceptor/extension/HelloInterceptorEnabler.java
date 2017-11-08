package org.javaee7.cdi.dynamic.interceptor.extension;

import javax.annotation.Priority;
import javax.interceptor.Interceptor;

/**
 * Class used to enable (activate) the dynamic interceptor and sets its priority
 * 
 * @author Arjan Tijms
 *
 */
@Interceptor
@Priority(200)
public class HelloInterceptorEnabler {

}