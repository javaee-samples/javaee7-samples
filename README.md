# Java EE 7 Samples #

This workspace consists of Java EE 7 Samples. They are categorized in different directories, one for each JSR.

## How to run ? ##

### How I run them ? ###

1. Open the sample in NetBeans 7.4 beta+ (Download from http://bits.netbeans.org/dev/nightly/latest)
2. Click on "Run" (sample is built and deployed on GlassFish 4, main page shows up)
3. Main page provides feature name, how to run the sample, and displays the output

### Cargo ###

By default, all samples are deployed on GlassFish 4. They can be deployed on Widlfly 8 by adding ``-P wildfly`` to all maven commands. Make sure to edit ``glassfish.home`` or ``wildfly.home`` property value in the top-level ``pom.xml`` to point to your local GlassFish or Wildfly directory respectively. Only one profile can be active at a given time otherwise there will be port conflicts.

1. In one terminal, anywhere in the project with a ``pom.xml``: ``mvn cargo:run`` to start GlassFish server or ``mvn -P wildfly`` to start Wildfly server. 
2. In another terminal, in the actual sample directory
    1. ``mvn package cargo:deploy`` to deploy for the first time
    2. ``mvn package cargo:redeploy`` to redeploy subsequently
    3. ``mvn cargo:undeploy`` to undeploy 
3. Check for application name printed by Cargo output. Access the application at http://localhost:8080/<APP-NAME>
4. Same as 3 in the first one

### Manual ###

1. ``mvn clean package``
2. Deploy on GlassFish using ``asadmin deploy target/XXX.war`` or deploy on Wildfly using ``TBD``
3. Access http://localhost:8080/XXX/ (main page shows up)
4. Same as 3 in the first one

## List of Samples ##

The following script will generate the complete list of samples.

``find . -name pom.xml -depth 3 -maxdepth 3 | sed 's|\./||g' | sed 's|\/pom.xml||g'``

There are 161 samples as of 9/16.

I don't plan to write any formal documentation, let the code talk. [Java EE 7 Essentials](http://www.amazon.com/Java-EE-Essentials-Arun-Gupta/dp/1449370179/) refer to some of these samples and provide an extensive explanation.

