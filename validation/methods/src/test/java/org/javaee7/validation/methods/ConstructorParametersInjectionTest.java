package org.javaee7.validation.methods;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.weld.exceptions.UnsatisfiedResolutionException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

/**
 * @author Jakub Marchwicki
 */
@RunWith(Arquillian.class)
public class ConstructorParametersInjectionTest {

	@Inject
	Instance<MyBean2> bean;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Deployment
	public static Archive<?> deployment() {
		return ShrinkWrap.create(JavaArchive.class).addClasses(MyBean2.class)
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
	}

	@Test
	public void constructorViolationsWhenNullParameters() throws NoSuchMethodException, SecurityException {
		thrown.expect(UnsatisfiedResolutionException.class);
		bean.get();
	}

}
