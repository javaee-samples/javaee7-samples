/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013 Oracle and/or its affiliates. All rights reserved.
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
// Portions Copyright [2018] [Payara Foundation and/or its affiliates]
package org.javaee7.jacc.contexts.bean;

import java.io.FilePermission;
import java.security.AccessControlException;
import java.security.AccessController;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

@Singleton
@Startup
public class BeanRoot implements BeanRootInterface {

    @EJB
    private BeanLeaf bl;

    @EJB
    private BeanMessageInterface msg;

    String MESSAGE_POST = "PostBeanRoot";
    String MESSAGE_HELLO = "HelloBeanRoot";

    @Override
    @PostConstruct
    public void afterConstruct() {
        if (msg != null && !msg.getMessage().contains(MESSAGE_POST)) {
            msg.appendMessage(MESSAGE_POST);
        }
        String h = bl.sayHello();
        System.out.println("** BeanRoot: Hello from beanLeaf: " + h);
    }

    @Override
    public String sayHello() {
        if (msg != null && !msg.getMessage().contains(MESSAGE_HELLO)) {
            msg.appendMessage(MESSAGE_HELLO);
        }

        StringBuffer check = new StringBuffer(" -EJB test-");

        FilePermission fp = new FilePermission("/scratch/spei/bug/test/war.txt", "delete");
        try {
            if (System.getSecurityManager() != null) {
                AccessController.checkPermission(fp);
                check.append("BeanRoot - success for WAR.txt; ");
            } else
                check.append("BeanRoot - bypass for WAR.txt; ");
        } catch (AccessControlException e) {
            check.append("BeanRoot - failed for WAR.txt; ");
        }

        fp = new FilePermission("/scratch/spei/bug/test/ear.txt", "delete");
        try {
            if (System.getSecurityManager() != null) {
                AccessController.checkPermission(fp);
                check.append("BeanRoot - success for EAR.txt; ");
            } else
                check.append("BeanRoot - bypass for EAR.txt; ");
        } catch (AccessControlException e) {
            check.append("BeanRoot - failed for EAR.txt; ");
        }

        fp = new FilePermission("/scratch/spei/bug/test/ejb.txt", "delete");
        final FilePermission p1 = fp;
        try {
            if (System.getSecurityManager() != null) {
                AccessController.checkPermission(p1);
                check.append("BeanRoot - success for EJB.txt; ");
            } else
                check.append("BeanRoot - bypass for EJB.txt; ");
        } catch (AccessControlException e) {
            check.append("BeanRoot - failed for EJB.txt; " + e.getMessage());
        }

        return "Hello from: " + this.getClass().getName() + "; "
                + check.toString() + " , code= "
                + System.identityHashCode(this);
    }

}
