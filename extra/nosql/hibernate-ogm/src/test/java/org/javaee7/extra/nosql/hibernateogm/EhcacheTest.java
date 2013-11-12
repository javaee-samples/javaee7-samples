package org.javaee7.extra.nosql.hibernateogm;

import org.javaee7.extra.nosql.hibernateogm.commons.AbstractOgmTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.runner.RunWith;

/**
 * @author Guillaume Scheibel <guillaume.scheibel@gmail.com>
 */
@RunWith(Arquillian.class)
public class EhcacheTest extends AbstractOgmTest {

	@Deployment
	public static WebArchive createDeployment() {
		return AbstractOgmTest.createDeployment( "ehcache" );
	}
}
