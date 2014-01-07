package org.javaee7.jaspic.common;

import java.io.File;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;

/**
 * 
 * @author Arjan Tijms
 * 
 */
public class ArquillianBase {

    private static final String WEBAPP_SRC = "src/main/webapp";

    public static WebArchive defaultArchive() {
        return ShrinkWrap.create(WebArchive.class).addPackages(true, "org.javaee7.jaspic")
                .addAsWebInfResource(resource("web.xml")).addAsWebInfResource(resource("glassfish-web.xml"))
                .addAsWebInfResource(resource("jboss-web.xml"));
    }

    private static File resource(String name) {
        return new File(WEBAPP_SRC + "/WEB-INF", name);
    }

}
