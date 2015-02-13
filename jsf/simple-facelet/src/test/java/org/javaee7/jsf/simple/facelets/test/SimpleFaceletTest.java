package org.javaee7.jsf.simple.facelets.test;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import org.javaee7.jsf.simple.facelets.CustomerSessionBean;
import org.javaee7.jsf.simple.facelets.Name;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.InitialPage;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

/**
 *
 * @author Juraj Huska
 */
@RunWith(Arquillian.class)
@RunAsClient
public class SimpleFaceletTest {

    private static final String WEBAPP_SRC = "src/main/webapp/";

    private static final List<String> EXPECTED_TABLE_NAMES = Arrays.asList("Penny", "Sheldon",
        "Amy", "Leonard", "Bernadette", "Raj", "Priya", "Howard");

    @Drone
    private WebDriver browser;

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
            .addClass(CustomerSessionBean.class)
            .addClass(Name.class)
            .addAsWebResource(new File(WEBAPP_SRC, "index.xhtml"))
            .addAsWebResource(new File(WEBAPP_SRC + "resources/css/cssLayout.css"), "resources/css/cssLayout.css")
            .addAsWebResource(new File(WEBAPP_SRC + "resources/css/default.css"), "resources/css/default.css")
            .addAsWebInfResource(new File(WEBAPP_SRC, "/WEB-INF/template.xhtml"))
            .addAsWebInfResource(new File(WEBAPP_SRC + "/WEB-INF", "web.xml"));
    }

    @Test
    public void testDataTableRendered(@InitialPage SimpleFaceletPage simpleFaceletPage) {
        Assert.assertEquals(
            "The simple facelet was not rendered correctly!",
            EXPECTED_TABLE_NAMES, simpleFaceletPage.getNames());
    }
}
