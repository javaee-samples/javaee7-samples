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
package org.javaee7.jpa.entitygraph;

import javax.ejb.Stateless;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * @author Arun Gupta
 */
@SuppressWarnings("unchecked")
@Stateless
public class MovieBean {
    @PersistenceContext
    private EntityManager entityManager;

    public List<Movie> listMovies() {
        return entityManager.createNamedQuery("Movie.findAll")
            .getResultList();
    }

    public List<Movie> listMovies(String hint, String graphName) {
        return entityManager.createNamedQuery("Movie.findAll")
            .setHint(hint, entityManager.getEntityGraph(graphName))
            .getResultList();
    }

    public List<Movie> listMovies(String hint, EntityGraph<?> entityGraph) {
        return entityManager.createNamedQuery("Movie.findAll")
            .setHint(hint, entityGraph)
            .getResultList();
    }

    public List<Movie> listMoviesById(Integer movieId, String hint, String graphName) {
        return entityManager.createNamedQuery("Movie.findAllById")
            .setParameter("movieId", movieId)
            .setHint(hint, entityManager.getEntityGraph(graphName))
            .getResultList();
    }

    public List<Movie> listMoviesByIds(List<Integer> movieIds, String hint, String graphName) {
        return entityManager.createNamedQuery("Movie.findAllByIds")
            .setParameter("movieIds", movieIds)
            .setHint(hint, entityManager.getEntityGraph(graphName))
            .getResultList();
    }
}
