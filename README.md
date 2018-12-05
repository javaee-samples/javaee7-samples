# Java EE 7 Samples #


This workspace consists of Java EE 7 Samples and unit tests. They are categorized in different directories, one for each Technology/JSR.

Some samples/tests have documentation, otherwise read the code. The [Java EE 7 Essentials](http://www.amazon.com/Java-EE-Essentials-Arun-Gupta/dp/1449370179/) book refers to most of these samples and provides an explanation. Feel free to add docs and send a pull request.


## How to run? ##

Samples are tested on Payara, GlassFish, Wildfly and more using the Arquillian ecosystem.

A brief instruction how to clone, build, import and run the samples on your local machine @radcortez provides in this sample video https://www.youtube.com/watch?v=BB4b-Yz9cF0

Only one container profile can be active at a given time otherwise there will be dependency conflicts.

There are 16 available container profiles, for 6 different servers:

* Payara and GlassFish
  * ``payara-ci-managed``
    
      This profile will install a Payara server and start up the server per sample.
      Useful for CI servers. The Payara version that's used can be set via the ``payara.version`` property.
      This is the default profile and does not have to be specified explicitly.

  * ``payara-embedded``
    
      This profile uses the Payara embedded server and runs in the same JVM as the TestClass.
      Useful for development, but has the downside of server startup per sample.

  * ``payara-remote``
    
      This profile requires you to start up a Payara server outside of the build. Each sample will then
      reuse this instance to run the tests.
      Useful for development to avoid the server start up cost per sample.
      
      This profile supports for some tests to set the location where Payara is installed via the ``glassfishRemote_gfHome``
      system property. E.g.
    
      ``-DglassfishRemote_gfHome=/opt/payara171``
      
      This is used for sending asadmin commands to create container resources, such as users in an identity store.

  * ``glassfish-embedded``
    
      This profile uses the GlassFish embedded server and runs in the same JVM as the TestClass.
      Useful for development, but has the downside of server startup per sample.

  * ``glassfish-remote``
    
      This profile requires you to start up a GlassFish server outside of the build. Each sample will then
      reuse this instance to run the tests.
      Useful for development to avoid the server start up cost per sample.
      
      This profile supports for some tests to set the location where GlassFish is installed via the ``glassfishRemote_gfHome``
      system property. E.g.
    
      ``-DglassfishRemote_gfHome=/opt/glassfish41``
      
      This is used for sending asadmin commands to create container resources, such as users in an identity store.

* WildFly

  * ``wildfly-ci-managed``
    
      This profile will install a Wildfly server and start up the server per sample.
      Useful for CI servers. The WildFly version that's used can be set via the ``wildfly.version`` property.
    
  * ``wildfly-embedded``
    
      This profile is almost identical to wildfly-ci-managed. It will install the same Wildfly server and start up 
      that server per sample again, but instead uses the Arquillian embedded connector to run it in the same JVM. 
      Useful for CI servers. The WildFly version that's used can be set via the ``wildfly.version`` property.

  * ``wildfly-remote``
    
      This profile requires you to start up a Wildfly server outside of the build. Each sample will then
      reuse this instance to run the tests.
      Useful for development to avoid the server start up cost per sample.
      
  * ``wildfly-swarm``

      This profile uses WildFly Swarm, which allows building uberjars that contain just enough of the WildFly
      application server. Here, the parts of WildFly that are included are selected based on inspecting the application
      and looking for the Java EE APIs that are actually used. The WildFly Swarm version that's used can be set via
      the ``wildfly.swarm.version`` property.

* TomEE
    
  * ``tomee-ci-managed``

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
    
  * ``tomee-embedded``

      This profile uses the TomEE embedded server and runs in the same JVM as the TestClass.
      
* Liberty
    
  * ``liberty-managed``

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
    
      For older versions of Liberty (pre 16.0.0.0) for the JASPIC tests to even be attempted to be executed a cheat is needed that creates a group in Liberty's internal user registry:
    
      ```xml
      <basicRegistry id="basic">
          <group name="architect"/>
      </basicRegistry>
      ```
    
      This cheat is not needed for the latest versions of Liberty (16.0.0.0/2016.7 and later)
        
  * ``liberty-ci-managed``
    
      This profile will download and install a Liberty server and start up the server per sample.
      Useful for CI servers. Note, this is not a real embedded server, but a regular server. It's now
      called "embedded" because no separate install is needed as it's downloaded automatically. 
      
* Weblogic
    
  * ``weblogic-remote``
    
      This profile requires you to start up a WebLogic server outside of the build. Each sample will then
      reuse this instance to run the tests.
    
      This profile requires you to set the location where WebLogic is installed via the ``weblogicRemoteArquillian_wlHome``
      system property. E.g.
    
    ``-DweblogicRemoteArquillian_wlHome=/opt/wls12210``
    
      The default username/password are assumed to be "admin" and "admin007" respectively. This can be changed using the
      ``weblogicRemoteArquillian_adminUserName`` and ``weblogicRemoteArquillian_adminPassword`` system properties. E.g.
    
      ``-DweblogicRemoteArquillian_adminUserName=myuser``
      ``-DweblogicRemoteArquillian_adminPassword=mypassword``
      
* Tomcat
    
  * ``tomcat-remote``

      This profile requires you to start up a plain Tomcat (8.5 or 9) server outside of the build. Each sample will then
      reuse this instance to run the tests.
    
      Tomcat supports samples that make use of Servlet, JSP, Expression Language (EL), WebSocket and JASPIC.
    
      This profile requires you to enable JMX in Tomcat. This can be done by adding the following to ``[tomcat home]/bin/catalina.sh``:
    
      ```
      JAVA_OPTS="$JAVA_OPTS -Dcom.sun.management.jmxremote.port=8089 -Dcom.sun.management.jmxremote=true "
      JAVA_OPTS="$JAVA_OPTS -Dcom.sun.management.jmxremote.ssl=false "
      JAVA_OPTS="$JAVA_OPTS -Dcom.sun.management.jmxremote.authenticate=false"
      JAVA_OPTS="$JAVA_OPTS -Djava.rmi.server.hostname=localhost "
      ```
    
      This profile also requires you to set a username (``tomcat``) and password (``manager``) for the management application in 
      ``tomcat-users.xml``. See the file ``test-utils/src/main/resources/tomcat-users.xml`` in this repository for a full example.
    
      Be aware that this should *only* be done for a Tomcat instance that's used exclusively for testing, as the above will make
      the Tomcat installation **totally insecure!**
    
  * ``tomcat-ci-managed``

      This profile will install a Tomcat server and start up the server per sample.
      Useful for CI servers. The Tomcat version that's used can be set via the ``tomcat.version`` property.
      
   
    
The containers that download and install a server (the \*-ci-managed profiles) allow you to override the version used, e.g.:

* `-Dpayara.version=4.1.1.163`

    This will change the version from the current one (e.g 4.1.1.171.1) to 4.1.1.163 for Payara testing purposes.

* `-Dglassfish.version=4.1`

    This will change the version from the current one (e.g 4.1.1) to 4.1 for GlassFish testing purposes.

* `-Dwildfly.version=8.1.0.Final`

    This will change the version from the current one (e.g. 10.1.0.Final) to 8.1.0.Final for WildFly.




**To run them in the console do**:

1. In the terminal, ``mvn test -fae`` at the top-level directory to start the tests for the default profile.

When developing and runing them from IDE, remember to activate the profile before running the test.

To learn more about Arquillian please refer to the [Arquillian Guides](http://arquillian.org/guides/)

**To run only a subset of the tests do at the top-level directory**:

1. Install top level dependencies: ``mvn clean install -pl "test-utils,util" -am``
1. cd into desired module, e.g.: ``cd jaspic``
1. Run tests against desired server, e.g.: ``mvn clean test -P liberty-ci-managed``


## How to contribute ##

With your help we can improve this set of samples, learn from each other and grow the community full of passionate people who care about the technology, innovation and code quality. Every contribution matters!

There is just a bunch of things you should keep in mind before sending a pull request, so we can easily get all the new things incorporated into the master branch.

Standard tests are jUnit based - for example [this commit](servlet/servlet-filters/src/test/java/org/javaee7/servlet/filters/FilterServletTest.java). Test classes naming must comply with surefire naming standards `**/*Test.java`, `**/*Test*.java` or `**/*TestCase.java`.

For the sake of clarity and consistency, and to minimize the upfront complexity, we prefer standard jUnit tests using Java, with as additional helpers HtmlUnit, Hamcrest and of course Arquillian. Please don't use alternatives for these technologies. If any new dependency has to be introduced into this project it should provide something that's not covered by these existing dependencies.


### Some coding principles ###

* When creating new source file do not put (or copy) any license header, as we use top-level license (MIT) for each and every file in this repository.
* Please follow JBoss Community code formatting profile as defined in the [jboss/ide-config](https://github.com/jboss/ide-config#readme) repository. The details are explained there, as well as configurations for Eclipse, IntelliJ and NetBeans.


### Small Git tips ###

* Make sure your [fork](https://help.github.com/articles/fork-a-repo) is always up-to-date. Simply run ``git pull upstream master`` and you are ready to hack.
* When developing new features please create a feature branch so that we incorporate your changes smoothly. It's also convenient for you as you could work on few things in parallel ;) In order to create a feature branch and switch to it in one swoop you can use ``git checkout -b my_new_cool_feature``

That's it! Welcome in the community!

## CI Job ##

CI jobs are executed by [Travis](https://travis-ci.org/javaee-samples/javaee7-samples). Note that by the very nature of the samples provided here it's perfectly normal that not all tests pass. This normally would indicate a bug in the server on which the samples are executed. If you think it's really the test that's faulty, then please submit an issue or provide a PR with a fix.


## Run each sample in Docker

* Install Docker client from http://boot2docker.io
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

