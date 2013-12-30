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

* [WildFly](https://arungupta.ci.cloudbees.com/job/Java%20EE%207%20Samples%20on%20WildFly-cb/)
* [GlassFish](https://arungupta.ci.cloudbees.com/job/Java%20EE%207%20Samples%20on%20GlassFish-cb/)
