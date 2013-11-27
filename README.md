# Java EE 7 Samples #

This workspace consists of Java EE 7 Samples and unit tests. They are categorized in different directories, one for each Technology/JSR.

Some samples/tests have documentataion otherwise read the code. The [Java EE 7 Essentials](http://www.amazon.com/Java-EE-Essentials-Arun-Gupta/dp/1449370179/) book refer to most these samples and provide an explanation. Feel free to add docs and send a pull request.

## How to run ? ##

Samples are tested on Wildfly and GlassFish using the Arquillian ecosystem.

Only one profile can be active at a given time otherwise there will be dependency conflicts.

There are 4 available profiles:

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

To run them in the console do:

1. In the terminal, ``mvn -Pwildfly-managed-arquillian test`` at the top-level directory to start the tests

When developing and runing them from IDE, remember to activate the profile before running the test.

To learn more about Arquillian please refer to the [Arquillian Guides](http://arquillian.org/guides/)

## CI Job ##

* [WildFly](https://arungupta.ci.cloudbees.com/job/Java%20EE%207%20Samples%20on%20WildFly-cb/)
* [GlassFish](https://arungupta.ci.cloudbees.com/job/Java%20EE%207%20Samples%20on%20GlassFish-cb/)
