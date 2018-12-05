/** Copyright Payara Services Limited **/
package org.javaee7.ejb.remote.ssl;

import javax.ejb.Remote;

@Remote
public interface BeanRemote {
    String method();
}
