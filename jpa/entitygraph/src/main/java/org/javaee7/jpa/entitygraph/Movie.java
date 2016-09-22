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

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * @author Arun Gupta
 */
@Entity
@Table(name = "MOVIE_ENTITY_GRAPH")
@NamedQueries({
    @NamedQuery(name = "Movie.findAll", query = "SELECT m FROM Movie m"),
    @NamedQuery(name = "Movie.findAllById", query = "SELECT m FROM Movie m WHERE m.id = :movieId"),
    @NamedQuery(name = "Movie.findAllByIds", query = "SELECT m FROM Movie m WHERE m.id IN :movieIds")
})
@NamedEntityGraphs({
    @NamedEntityGraph(
        name = "movieWithActors",
        attributeNodes = {
            @NamedAttributeNode("movieActors")
        }
    ),
    @NamedEntityGraph(
        name = "movieWithActorsAndAwards",
        attributeNodes = {
            @NamedAttributeNode(value = "movieActors", subgraph = "movieActorsGraph")
        },
        subgraphs = {
            @NamedSubgraph(
                name = "movieActorsGraph",
                attributeNodes = {
                    @NamedAttributeNode("movieActorAwards")
                }
            )
        }
    )
})
public class Movie implements Serializable {
    @Id
    private Integer id;

    @NotNull
    @Size(max = 50)
    private String name;

    @OneToMany
    @JoinColumn(name = "ID")
    private Set<MovieActor> movieActors;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID")
    private Set<MovieDirector> movieDirectors;

    @OneToMany
    @JoinColumn(name = "ID")
    private Set<MovieAward> movieAwards;

    public Integer getId() {
        return id;
    }

    public Set<MovieActor> getMovieActors() {
        return movieActors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Movie movie = (Movie) o;

        return id.equals(movie.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
