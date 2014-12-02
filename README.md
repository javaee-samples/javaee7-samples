# Java EE 7 Samples #

This workspace consists of Java EE 7 Samples and unit tests. They are categorized in different directories, one for each Technology/JSR.

Some samples/tests have documentation otherwise read the code. The [Java EE 7 Essentials](http://www.amazon.com/Java-EE-Essentials-Arun-Gupta/dp/1449370179/) book refer to most these samples and provide an explanation. Feel free to add docs and send a pull request.

## How to run ? ##

Samples are tested on Wildfly and GlassFish using the Arquillian ecosystem.

Only one container profile and one profile for browser can be active at a given time otherwise there will be dependency conflicts.

There are 4 available container profiles:

* ``wildfly-managed-arquillian``
    
    The default profile and it will install a Wildfly server and start up the server per sample.
    Useful for CI servers.

* ``wildfly-remote-arquillian``
    
    This profile requires you to start up a Wildfly server outside of the build. Each sample will then
    reuse this instance to run the tests.
    Useful for development to avoid the server start up cost per sample.

* ``glassfish-embedded-arquillian``
    
    This profile uses the GlassFish embedded server and runs in the same JVM as the TestClass.
    Useful for development, but has the downside of server startup per sample.

* ``glassfish-remote-arquillian``
    
    This profile requires you to start up a GlassFish server outside of the build. Each sample will then
    reuse this instance to run the tests.
    Useful for development to avoid the server start up cost per sample.

Each of the containers allow you to override the version used

* `-Dorg.wildfly=8.1.0.Final`

    This will change the version from 8.0.0 to 8.1.0.Final for WildFly.

* `-Dglassfish.version=4.1`

    This will change the version from 4.0 to 4.1 for GlassFish testing purposes.

Similarly, there are 6 profiles to choose a browser to test on:

* ``browser-firefox``
    
    To run tests on Mozilla Firefox. If its binary is installed in the usual place, no additional information is         required.

* ``browser-chrome``
    
    To run tests on Google Chrome. Need to pass a ``-Darq.extension.webdriver.chromeDriverBinary`` property
    pointing to a ``chromedriver`` binary.

* ``browser-ie``
    
    To run tests on Internet Explorer. Need to pass a ``-Darq.extension.webdriver.ieDriverBinary`` property
    pointing to a ``IEDriverServer.exe``.

* ``browser-safari``
    
    To run tests on Safari. If its binary is installed in the usual place, no additional information is required.

* ``browser-opera``
    
    To run tests on Opera. Need to pass a ``-Darq.extension.webdriver.opera.binary`` property pointing to a Opera        executable.

* ``browser-phantomjs``
    
    To run tests on headless browser PhantomJS. If you do not specify the path of phantomjs binary via 
    ``-Dphantomjs.binary.path`` property, it will be downloaded automatically.

To run them in the console do:

1. In the terminal, ``mvn -Pwildfly-managed-arquillian,browser-firefox test`` at the top-level directory to start the tests

When developing and runing them from IDE, remember to activate the profile before running the test.

To learn more about Arquillian please refer to the [Arquillian Guides](http://arquillian.org/guides/)

### Importing in Eclipse ###

To import the samples in an Eclipse workspace, please install the [Groovy plugins for your Eclipse version](http://groovy.codehaus.org/Eclipse+Plugin) first, then import the sample projects you want using File>Import>Existing Maven Projects. 

## How to contribute ##

With your help we can improve this set of samples, learn from each other and grow the community full of passionate people who care about the technology, innovation and code quality. Every contribution matters!

There is just a bunch of things you should keep in mind before sending a pull request, so we can easily get all the new things incorporated into the master branch.

Standard tests are jUnit based - for example [this commit](servlet/servlet-filters/src/test/java/org/javaee7/servlet/filters/FilterServletTest.java). Test classes naming must comply with surefire naming standards `**/*Test.java`, `**/*Test*.java` or `**/*TestCase.java`.

However, if you fancy something new, hip and fashionable we also accept Spock specifications - [like here](/servlet/servlet-filters/src/test/groovy/org/javaee7/servlet/filters/FilterServletSpecification.groovy). The spec files are included in the maven test phase if and only if you follow Spock naming convention and give your `Specification` suffix the magic will happen.

### Some coding principles ###

* When creating new source file do not put (or copy) any license header, as we use top-level license (MIT) for each and every file in this repository.
* Please follow JBoss Community code formatting profile as defined in the [jboss/ide-config](https://github.com/jboss/ide-config#readme) repository. The details are explained there, as well as configurations for Eclipse, IntelliJ and NetBeans.

### Small Git tips ###

* Make sure your [fork](https://help.github.com/articles/fork-a-repo) is always up-to-date. Simply run ``git pull upstream master`` and you are ready to hack.
* When developing new features please create a feature branch so that we incorporate your changes smoothly. It's also convenient for you as you could work on few things in parallel ;) In order to create a feature branch and switch to it in one swoop you can use ``git checkout -b my_new_cool_feature``

That's it! Welcome in the community!

## CI Job ##

* [WildFly](https://javaee-support.ci.cloudbees.com/job/javaee7-samples-wildfly-8.1/)
* [GlassFish](https://javaee-support.ci.cloudbees.com/job/javaee7-samples-glassfish-4.1/)
* [TomEE](https://javaee-support.ci.cloudbees.com/job/javaee7-samples-tomee-2.0/)

## Run each sample in Docker

. Install Docker client from http://boot2docker.io/
. Build the sample that you want to run as
+
``mvn clean package -DskipTests``
+
For example:
+
``mvn -f jaxrs/jaxrs-client/pom.xml clean package -DskipTests``
+
. Change the second line in ``Dockerfile`` to specify the location of the generated WAR file
. Run boot2docker and give the command
+
``docker build -it -p 80:8080 javaee7-sample``
+
. In a different shell, find out the IP address of the running container as:
+
``boot2docker ip``
+
. Access the sample as http://<IP>:80/jaxrs-client/webresources/persons

