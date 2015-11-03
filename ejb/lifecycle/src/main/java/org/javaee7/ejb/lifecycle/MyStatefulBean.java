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
package org.javaee7.ejb.lifecycle;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.PostActivate;
import javax.ejb.PrePassivate;
import javax.ejb.Stateful;
import javax.enterprise.context.Dependent;
import javax.interceptor.InvocationContext;

/**
 * @author Arun Gupta
 */
@Dependent
@MyAroundConstructInterceptorBinding
@Stateful
public class MyStatefulBean {
    private List<String> list;

    public MyStatefulBean() {
        System.out.println("MyStatefulBean.ctor");
    }

    @PostConstruct
    private void postConstruct() {
        list = new ArrayList<>();
        System.out.println("MyStatefulBean.postConstruct");
    }

    @PreDestroy
    private void preDestroy() {
        System.out.println("MyStatefulBean.preDestroy");
    }

    @PrePassivate
    private void prePassivate() {
        System.out.println("MyStatefulBean.prePassivate");
    }

    @PostActivate
    private void postActivate() {
        System.out.println("MyStatefulBean.postActivate");
    }

    public void addItem(String item) {
        list.add(item);
        System.out.println("MyBean.addItem");
    }

    public void removeItem(String item) {
        list.remove(item);
        System.out.println("MyBean.removeItem");
    }

    public List<String> items() {
        return list;
    }
}
