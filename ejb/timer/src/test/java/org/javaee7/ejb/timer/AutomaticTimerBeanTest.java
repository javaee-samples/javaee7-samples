package org.javaee7.ejb.timer;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.io.File;

import static com.jayway.awaitility.Awaitility.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.javaee7.ejb.timer.WithinWindowMatcher.withinWindow;

/**
 * author: Jakub Marchwicki
 */
@RunWith(Arquillian.class)
public class AutomaticTimerBeanTest {

    final static long TIMEOUT = 5000l;
    final static long TOLERANCE = 1000l;

    @Inject
    PingsListener pings;

    @Deployment
    public static WebArchive deploy() {
        File[] jars = Maven.resolver().loadPomFromFile("pom.xml")
            .resolve("com.jayway.awaitility:awaitility")
            .withTransitivity().asFile();

        return ShrinkWrap.create(WebArchive.class)
            .addAsLibraries(jars)
            .addClasses(WithinWindowMatcher.class, Ping.class, PingsListener.class, AutomaticTimerBean.class);
    }

    @Test
    public void should_receive_two_pings() {
        await().untilCall(to(pings.getPings()).size(), equalTo(2));

        Ping firstPing = pings.getPings().get(0);
        Ping secondPing = pings.getPings().get(1);

        long delay = secondPing.getTime() - firstPing.getTime();
        System.out.println("Actual timeout = " + delay);
        assertThat(delay, is(withinWindow(TIMEOUT, TOLERANCE)));
    }
}
