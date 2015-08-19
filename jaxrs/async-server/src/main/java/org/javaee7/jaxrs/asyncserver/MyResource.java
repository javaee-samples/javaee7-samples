/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2012 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package org.javaee7.jaxrs.asyncserver;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.enterprise.concurrent.ManagedThreadFactory;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.CompletionCallback;
import javax.ws.rs.container.ConnectionCallback;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.container.TimeoutHandler;

/**
 * @author Arun Gupta
 */
@Path("fruits")
public class MyResource {
    private final String[] response = { "apple", "banana", "mango" };

    //    @Resource(name = "DefaultManagedThreadFactory")
    //    ManagedThreadFactory threadFactory;

    @GET
    public void getList(@Suspended final AsyncResponse ar) throws NamingException {
        ar.setTimeoutHandler(new TimeoutHandler() {

            @Override
            public void handleTimeout(AsyncResponse ar) {
                ar.resume("Operation timed out");
            }
        });
        ar.setTimeout(4000, TimeUnit.MILLISECONDS);

        ar.register(new MyCompletionCallback());
        ar.register(new MyConnectionCallback());

        ManagedThreadFactory threadFactory = (ManagedThreadFactory) new InitialContext()
            .lookup("java:comp/DefaultManagedThreadFactory");

        Executors.newSingleThreadExecutor(threadFactory).submit(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    ar.resume(response[0]);
                } catch (InterruptedException ex) {

                }
            }

        });
    }

    class MyCompletionCallback implements CompletionCallback {

        @Override
        public void onComplete(Throwable t) {
            System.out.println("onComplete");
        }

    }

    class MyConnectionCallback implements ConnectionCallback {

        @Override
        public void onDisconnect(AsyncResponse ar) {
            System.out.println("onDisconnect");
        }

    }

}
