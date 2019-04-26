package org.javaee7.ejb.timer;

import static com.jayway.awaitility.Awaitility.await;
import static com.jayway.awaitility.Awaitility.to;
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
public class ProgrammaticTimerBeanTest {

    private static final long TIMEOUT = 5000l;
    private static final long TOLERANCE = 1000l;

    @Inject
    private PingsListener pings;

    @Deployment
    public static WebArchive deploy() {
        return create(WebArchive.class)
                .addAsLibraries(
                    Maven.resolver().loadPomFromFile("pom.xml")
                        .resolve("com.jayway.awaitility:awaitility")
                        .withTransitivity().asFile())
                .addClasses(
                    WithinWindowMatcher.class, 
                    Ping.class, 
                    PingsListener.class, 
                    ProgrammaticTimerBean.class)
                .addAsWebInfResource(new File("src/main/webapp/WEB-INF/jboss-deployment-structure.xml"));
                
    }

    @Test
    public void should_receive_two_pings() {
        await()
            .untilCall(
                to(pings.getPings()).size(), greaterThanOrEqualTo(2));

        Ping firstPing = pings.getPings().get(0);
        Ping secondPing = pings.getPings().get(1);

        long delay = secondPing.getTime() - firstPing.getTime();
        System.out.println("Actual timeout = " + delay);
        
        assertThat(delay, is(withinWindow(TIMEOUT, TOLERANCE)));
    }
}