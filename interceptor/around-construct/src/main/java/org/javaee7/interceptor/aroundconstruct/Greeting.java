package org.javaee7.interceptor.aroundconstruct;

/**
 * Class description here.
 *
 * @author <a href="mailto:radim.hanus@apksoft.eu">Radim Hanus</a>
 */
public interface Greeting {
	boolean isConstructed();
	boolean isInitialized();

	Param getParam();
}
