# Java EE 7 Samples #

This workspace consists of Java EE 7 Samples and unit tests. They are categorized in different directories, one for each Technology/JSR.

Some samples/tests have documentation otherwise read the code. The [Java EE 7 Essentials](http://www.amazon.com/Java-EE-Essentials-Arun-Gupta/dp/1449370179/) book refer to most these samples and provide an explanation. Feel free to add docs and send a pull request.

## How to run ? ##

Samples are tested on Wildfly and GlassFish using the Arquillian ecosystem.

A brief instruction how to clone, build, import and run the samples on your local machine @radcortez provides in this sample video https://www.youtube.com/watch?v=BB4b-Yz9cF0

Only one container profile and one profile for browser can be active at a given time otherwise there will be dependency conflicts.

There are 8 available container profiles, for 5 different servers:

* ``wildfly-managed-arquillian``
    
    This profile will install a Wildfly server and start up the server per sample.
    Useful for CI servers.

* ``wildfly-remote-arquillian``
    
    This profile requires you to start up a Wildfly server outside of the build. Each sample will then
    reuse this instance to run the tests.
    Useful for development to avoid the server start up cost per sample. This is the default profile.

* ``glassfish-embedded-arquillian``
    
    This profile uses the GlassFish embedded server and runs in the same JVM as the TestClass.
    Useful for development, but has the downside of server startup per sample.

* ``glassfish-remote-arquillian``
    
    This profile requires you to start up a GlassFish server outside of the build. Each sample will then
    reuse this instance to run the tests.
    Useful for development to avoid the server start up cost per sample.
    
* ``tomee-managed-arquillian``

    This profile will install a TomEE server and start up that server per sample.
    Useful for CI servers. This profile cannot connect to a running server.
    
    Note that the version of TomEE to be used has to be present in an
    available maven repository. The defaults in this profile assume that the arquillian adapter and
    the TomEE server have the same version. E.g both 7.0.0.
    
    To use a TomEE server that's not available in maven central, one way to use it for the samples is to
    install it in a local .m2 as follows:
    
    Clone TomEE repo:
    
    ``git clone https://github.com/apache/tomee``
    ``cd tomee``
    
    Switch to the desired version if needed, then build and install in .m2:
    
    ``mvn clean install -pl tomee/apache-tomee -am -Dmaven.test.skip=true``
    
    ``mvn clean install -pl arquillian -amd -Dmaven.test.skip=true``
    
    Make sure the version that's installed (see pom.xml in TomEE project) matches the ``tomee.version`` in the
    properties section in the root pom.xml of the samples project.
    
* ``tomee-embedded-arquillian``

    This profile uses the TomEE embedded server and runs in the same JVM as the TestClass.
    
* ``liberty-managed-arquillian``

    This profile will start up the Liberty server per sample, and optionally connects to a running server that you
    can start up outside of the build (with the restriction that this server has to run on the host as where
    the tests are run using the same user).
    
    To connect to a running server the ``org.jboss.arquillian.container.was.wlp_managed_8_5.allowConnectingToRunningServer`` 
    system property has to be set to true. E.g.
    
    ``-Dorg.jboss.arquillian.container.was.wlp_managed_8_5.allowConnectingToRunningServer=true``
    
    This profile requires you to set the location where Liberty is installed via the ``libertyManagedArquillian_wlpHome``
    system property. E.g.
    
    ``-DlibertyManagedArquillian_wlpHome=/opt/wlp``
    
    This profile also requires the localConnector feature to be configured in server.xml, and if all tests are to be run the
    javaee-7.0 feature E.g.
    
    ```xml
    <featureManager>
        <feature>javaee-7.0</feature>
        <feature>localConnector-1.0</feature>
    </featureManager>
    ```
    
    For the JASPIC tests to even be attempted to be executed a cheat is needed that creates a group in Liberty's internal user registry:
    
    ```xml
    <basicRegistry id="basic">
        <group name="architect"/>
    </basicRegistry>
    ```
    
* ``weblogic-remote-arquillian``
    
    This profile requires you to start up a WebLogic server outside of the build. Each sample will then
    reuse this instance to run the tests. NOTE: this has been tested on WebLogic 12.1.3, which is a Java EE 6 implementation,
    but it has some Java EE 7 features which can be optionally activated.
    
    This profile requires you to set the location where WebLogic is installed via the ``weblogicRemoteArquillian_wlHome``
    system property. E.g.
    
    ``-DweblogicRemoteArquillian_wlHome=/opt/wls12130``
    
    The default username/password are assumed to be "admin" and "admin007" respectively. This can be changed using the
    ``weblogicRemoteArquillian_adminUserName`` and ``weblogicRemoteArquillian_adminPassword`` system properties. E.g.
    
    ``-DweblogicRemoteArquillian_adminUserName=myuser``
    ``-DweblogicRemoteArquillian_adminPassword=mypassword``
    
Some of the containers allow you to override the version used

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

## How to contribute ##

With your help we can improve this set of samples, learn from each other and grow the community full of passionate people who care about the technology, innovation and code quality. Every contribution matters!

There is just a bunch of things you should keep in mind before sending a pull request, so we can easily get all the new things incorporated into the master branch.

Standard tests are jUnit based - for example [this commit](servlet/servlet-filters/src/test/java/org/javaee7/servlet/filters/FilterServletTest.java). Test classes naming must comply with surefire naming standards `**/*Test.java`, `**/*Test*.java` or `**/*TestCase.java`.

However, if you fancy something new, hip and fashionable it is perfectly legal to write  Spock specifications as standard JavaEE integration test. For the sake of clarity and consistency, to minimize the upfront complexity, in this project we prefare standard jUnit test. However, some Spock example are provided in the `extra/spock-tests` folder  - [like here](extra/spock-tests/src/test/java/org/javaee7/servlet/filters/FilterServletSpecification.groovy). The `spock-tests` project also showcases the Maven configuration. In this particular case the Groovy Specification files are included in the maven test phase if and only if you follow Spock naming convention and give your `Specification` suffix the magic will happen.

The extras folder is not included by default, to limit Groovy dependency. If you want to import the extra samples in an Eclipse workspace (including the Spock tests), please install the [Groovy plugins for your Eclipse version](http://groovy.codehaus.org/Eclipse+Plugin) first, then import the sample projects you want using File>Import>Existing Maven Projects. 

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

* Install Docker client from http://boot2docker.io/
* Build the sample that you want to run as
  
  ``mvn clean package -DskipTests``

  For example:

  ``mvn -f jaxrs/jaxrs-client/pom.xml clean package -DskipTests``

* Change the second line in ``Dockerfile`` to specify the location of the generated WAR file
* Run boot2docker and give the command

  ``docker build -it -p 80:8080 javaee7-sample``

* In a different shell, find out the IP address of the running container as:

  ``boot2docker ip``

* Access the sample as http://IP_ADDRESS:80/jaxrs-client/webresources/persons. The exact URL would differ based upon the sample.

