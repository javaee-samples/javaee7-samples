package org.javaee7.ejb.async;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.io.File;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static com.jayway.awaitility.Awaitility.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

/**
 * @author Jakub Marchwicki
 */
@RunWith(Arquillian.class)
public class AsyncClassBeanTest {

    @Inject
    MyAsyncBeanClassLevel bean;

    @Deployment
    public static WebArchive createDeployment() {
        File[] jars = Maven.resolver().loadPomFromFile("pom.xml")
            .resolve("com.jayway.awaitility:awaitility")
            .withTransitivity().asFile();

        return ShrinkWrap.create(WebArchive.class)
            .addAsLibraries(jars)
            .addClasses(MyAsyncBeanClassLevel.class)
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Test
    public void should_return_async_sum() throws ExecutionException, InterruptedException {
        final Integer numberOne = 5;
        final Integer numberTwo = 10;

        long start = System.currentTimeMillis();
        final Future<Integer> resultFuture = bean.addNumbers(numberOne, numberTwo);

        assertThat(resultFuture.isDone(), is(equalTo(false)));
        assertThat(System.currentTimeMillis() - start, is(lessThan(MyAsyncBeanMethodLevel.AWAIT)));

        await().until(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return resultFuture.isDone();
            }
        });

        assertThat(resultFuture.get(), is(equalTo(numberOne + numberTwo)));
    }

}
