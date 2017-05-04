package org.javaee7.jpa.listeners;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import javax.inject.Inject;

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

@RunWith(Arquillian.class)
public class JpaListenerInjectionTest {

    @Deployment
    public static WebArchive deployment() {
        return ShrinkWrap.create(WebArchive.class)
                .addPackage("org.javaee7.jpa.listeners")
                .addPackage(ParameterRule.class.getPackage())
                .addPackages(true, "org.apache.commons.lang3")
                .addAsResource("META-INF/persistence.xml")
                .addAsResource("META-INF/create.sql")
                .addAsResource("META-INF/drop.sql")
                .addAsResource("META-INF/load.sql");
    }

    public static final List<ImmutablePair<String, Integer>> movies = asList(
            new ImmutablePair<>("The Matrix", 60),
            new ImmutablePair<>("The Lord of The Rings", 70),
            new ImmutablePair<>("Inception", 80),
            new ImmutablePair<>("The Shining", 90)
    );

    @Rule
    public ParameterRule<ImmutablePair<String, Integer>> rule = new ParameterRule<>(movies);

    @Parameter
    private ImmutablePair<String, Integer> expectedMovie;

    @Inject
    private MovieBean bean;

    @Test
    public void should_provide_movie_rating_via_jpa_listener_injection() {
        // Given
        Movie movie = bean.getMovieByName(expectedMovie.getLeft());

        assertThat(movie.getRating(), is(equalTo(expectedMovie.getRight())));
    }
}
