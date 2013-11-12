# Java EE 7 Samples #

This workspace consists of Java EE 7 Samples. They are categorized in different directories, one for each JSR.

I don't plan to write any formal documentation, read the code. The [Java EE 7 Essentials](http://www.amazon.com/Java-EE-Essentials-Arun-Gupta/dp/1449370179/) book refer to some of these samples and provide an explanation. However if you are interested in adding javadocs, send a pull request.

## How to run ? ##

### Maven Plugins (WildFly) ###

#### One time ####

1. ``git clone https://github.com/wildfly/wildfly-maven-plugin``
2. ``cd wildfly-maven-plugin``
3. ``mvn install``

#### For each sample ####

1. ``mvn package -DskipTests``
2. ``mvn wildfly:start wildfly:deploy-only test -P wildfly``

### Maven Plugins (GlassFish) ###

#### For each sample ####

1. ``mvn package -DskipTests``
2. ``mvn embedded-glassfish:deploy test -P glassfish``

### NetBeans ###

1. Open the sample in [NetBeans 7.4](http://netbeans.org)
2. Click on "Run" (sample is built and deployed on GlassFish 4, main page shows up). NetBeans do not support WildFly yet.
3. Main page provides feature name, how to run the sample, and displays the output

### JBoss Tools (Eclipse) ###

### IntelliJ ###

### Arquillian ###

Samples are tested on Wildfly and GlassFish using the Arquillian ecosystem.

Only one profile can be active at a given time otherwise there will be dependency conflicts.

There are 3 available profiles:

* ``wildfly-managed-arquillian``
    The default profile and it will install a Wildfly server and start up the server pr sample.
    Useful for CI servers.

* ``wildfly-remote-arquillian``
    This profile requires you to start up a Wildfly server outside of the build. Each sample will then
    reuse this instance to run the tests.
    Useful for development to avoid the server start up cost pr sample.

* ``glassfish-embedded-arquillian``
    This profile uses the GlassFish embedded server and runs in the same JVM as the TestClass.
    Useful for development, but has the downside of server startup pr sample.

To run them in the console do:

1. In the terminal, ``mvn -Pwildfly-managed-arquillian`` at the top-level directory to start the tests

When developing and runing them from IDE, remember to activate the profile before running the test.

To learn more about Arquillian please refer to the [Arquillian Guides](http://arquillian.org/guides/)

### Manual ###

1. ``mvn clean package -DskipTests``
2. Deploy on GlassFish using ``asadmin deploy target/XXX.war`` or deploy on Wildfly using ``TBD``
3. Access http://localhost:8080/XXX/ (main page shows up)

## List of Samples ##

The following script will generate the complete list of samples.

``find . -name pom.xml -depth 3 -maxdepth 3 | sed 's|\./||g' | sed 's|\/pom.xml||g'``

There are 161 samples as of 9/16.

