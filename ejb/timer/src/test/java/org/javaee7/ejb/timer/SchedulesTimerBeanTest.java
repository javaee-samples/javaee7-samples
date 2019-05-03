package org.javaee7.ejb.timer;

import static com.jayway.awaitility.Awaitility.await;
import static com.jayway.awaitility.Awaitility.to;
import static java.lang.Math.min;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.javaee7.ejb.timer.WithinWindowMatcher.withinWindow;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;

import java.io.File;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * author: Jacek Jackowiak
 */
@RunWith(Arquillian.class)
public class SchedulesTimerBeanTest {

    private static final long TIMEOUT = 0l;
    private static final long TOLERANCE = 1000l;

    @Inject
    private PingsListener pings;

    @Deployment
    public static WebArchive deploy() {
        return create(WebArchive.class)
                .addAsLibraries(Maven.resolver().loadPomFromFile("pom.xml")
                        .resolve("com.jayway.awaitility:awaitility")
                        .withTransitivity().asFile())
                .addClasses(WithinWindowMatcher.class, Ping.class, PingsListener.class, SchedulesTimerBean.class)
                .addAsWebInfResource(new File("src/main/webapp/WEB-INF/jboss-deployment-structure.xml"));
    }

    @Test
    public void should_receive_three_pings() {
        await().untilCall(to(pings.getPings()).size(), greaterThanOrEqualTo(3));

        Ping firstPing = pings.getPings().get(0);
        Ping secondPing = pings.getPings().get(1);
        Ping thirdPing = pings.getPings().get(2);

        long delay = secondPing.getTime() - firstPing.getTime();
        System.out.println("Actual timeout = " + delay);
        
        long delay2 = thirdPing.getTime() - secondPing.getTime();
        System.out.println("Actual timeout = " + delay2);
        
        long smallerDelay = min(delay, delay2);
        
        assertThat(smallerDelay, is(withinWindow(TIMEOUT, TOLERANCE)));
    }
}
