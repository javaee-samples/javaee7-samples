# Java EE 7 Samples #

This workspace consists of Java EE 7 Samples. They are categorized in different directories, one for each JSR.

I don't plan to write any formal documentation, read the code. The [Java EE 7 Essentials](http://www.amazon.com/Java-EE-Essentials-Arun-Gupta/dp/1449370179/) book refer to some of these samples and provide an explanation. However if you are interested in adding javadocs, send a pull request.

## How to run ? ##

### Maven Plugins (WildFly) ###

#### One time ####

1. ``git clone https://github.com/wildfly/wildfly-maven-plugin``
2. ``cd wildfly-maven-plugin``
3. ``mvn install``

#### Deploy the app, run the tests ####

1. For each sample: ``mvn wildfly:start wildfly:deploy-only test -P wildfly``

### Maven Plugins (GlassFish) ###

TBD

### NetBeans ###

1. Open the sample in [NetBeans 7.4](http://netbeans.org)
2. Click on "Run" (sample is built and deployed on GlassFish 4, main page shows up). NetBeans do not support WildFly yet.
3. Main page provides feature name, how to run the sample, and displays the output

### JBoss Tools ###

### IntelliJ ###

### Cargo ###

Samples can be deployed and run on WildFly and GlassFish 4. 

Make sure to edit ``glassfish.home`` or ``wildfly.home`` property value in the top-level ``pom.xml`` to point to your local GlassFish or Wildfly directory respectively. This is achieved using Maven profiles. Include ``-P wildfly`` on mvn CLI to run the samples on WildFly and ``-P glassfish`` fo GlassFish.

Only one profile can be active at a given time otherwise there will be port conflicts.

1. In one terminal, ``mvn cargo:run`` at the top-level directory to start container
2. In the sample directory
    1. ``mvn package cargo:deploy`` to deploy for the first time
    2. ``mvn package cargo:redeploy`` to redeploy subsequently
    3. ``mvn cargo:undeploy`` to undeploy 
3. Check for application name printed by Cargo output. Access the application at http://localhost:8080/<APP-NAME>

### Manual ###

1. ``mvn clean package -DskipTests``
2. Deploy on GlassFish using ``asadmin deploy target/XXX.war`` or deploy on Wildfly using ``TBD``
3. Access http://localhost:8080/XXX/ (main page shows up)


## List of Samples ##

The following script will generate the complete list of samples.

``find . -name pom.xml -depth 3 -maxdepth 3 | sed 's|\./||g' | sed 's|\/pom.xml||g'``

There are 161 samples as of 9/16.

