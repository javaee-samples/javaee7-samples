/** Copyright Payara Services Limited **/
package org.javaee7;

import javax.naming.Context;

public interface RemoteEJBContextProvider {
    Context getContextWithCredentialsSet(String username, String password);
    void releaseContext();
}
