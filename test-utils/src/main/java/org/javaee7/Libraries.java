package org.javaee7;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;

public class Libraries {
    
    public static JavaArchive[] awaitability() {
        return Maven.resolver()
                .loadPomFromFile("pom.xml")
                .resolve("org.assertj:assertj-core", "com.jayway.awaitility:awaitility")
                .withTransitivity()
                .as(JavaArchive.class);
    }

}
