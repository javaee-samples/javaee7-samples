package org.javaee7.jpa.listeners;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.javaee7.Parameter;
import org.javaee7.ParameterRule;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;

@RunWith(Arquillian.class)
public class JpaListenerInjectionTest {

    @Deployment
    public static WebArchive deployment() {
        return ShrinkWrap.create(WebArchive.class)
                .addPackage("org.javaee7.jpa.listeners")
                .addAsResource("META-INF/persistence.xml")
                .addAsResource("META-INF/create.sql")
                .addAsResource("META-INF/drop.sql")
                .addAsResource("META-INF/load.sql");
    }

    public static final List<ImmutablePair<String, Integer>> movies = Arrays.asList(
            new ImmutablePair<>("The Matrix", 60),
            new ImmutablePair<>("The Lord of The Rings", 70),
            new ImmutablePair<>("Inception", 80),
            new ImmutablePair<>("The Shining", 90)
    );

    @Rule
    public ParameterRule<ImmutablePair<String, Integer>> rule = new ParameterRule<>(movies);

    @Parameter
    ImmutablePair<String, Integer> expectedMovie;

    @Inject
    MovieBean bean;

    @Test
    public void should_provide_movie_rating_via_jpa_listener_injection() {
        //given
        Movie movie = bean.getMovieByName(expectedMovie.getLeft());

        assertThat(movie.getRating(), is(equalTo(expectedMovie.getRight())));
    }
}
