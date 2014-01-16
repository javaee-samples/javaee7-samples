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
package org.javaee7.jpa.listeners;

import javax.persistence.*;
import java.util.concurrent.CountDownLatch;

/**
 * @author Arun Gupta
 */
public class MovieListener {
    public static CountDownLatch entityListenersCountDownLatch = new CountDownLatch(26);

    public static boolean postLoadInvoked;
    public static boolean prePersistInvoked;
    public static boolean postPersistInvoked;
    public static boolean preUpdateInvoked;
    public static boolean postUpdateInvoked;
    public static boolean preRemoveInvoked;
    public static boolean postRemoveInvoked;

    @PostLoad
    public void newMovieLoad(Movie movie) {
        postLoadInvoked = true;
        entityListenersCountDownLatch.countDown();
        System.out.println("## Movie loaded: " + movie.getName());
    }

    @PrePersist
    public void newMovieAlertBefore(Movie movie) {
        prePersistInvoked = true;
        entityListenersCountDownLatch.countDown();
        System.out.println("## Ready to create new movie: " + movie.getName());
    }

    @PostPersist
    public void newMovieAlertAfter(Movie movie) {
        postPersistInvoked = true;
        entityListenersCountDownLatch.countDown();
        System.out.println("## New movie created: " + movie.getName());
    }

    @PreUpdate
    public void updateMovieAlertBefore(Movie movie) {
        preUpdateInvoked = true;
        entityListenersCountDownLatch.countDown();
        System.out.println("## Ready to update movie: " + movie.getName());
    }

    @PostUpdate
    public void updateMovieAlertAfter(Movie movie) {
        postUpdateInvoked = true;
        entityListenersCountDownLatch.countDown();
        System.out.println("## Movie updated: " + movie.getName());
    }

    @PreRemove
    public void deleteMovieAlertBefore(Movie movie) {
        preRemoveInvoked = true;
        entityListenersCountDownLatch.countDown();
        System.out.println("## Ready to delete movie: " + movie.getName());
    }

    @PostRemove
    public void deleteMovieAlertAfter(Movie movie) {
        postRemoveInvoked = true;
        entityListenersCountDownLatch.countDown();
        System.out.println("## Movie deleted: " + movie.getName());
    }
}
