## JPA 2.1 Custom Converter

This project demonstrates following new additions to JPA 2.1
* [Custom converter](http://en.wikibooks.org/wiki/Java_Persistence/Basic_Attributes#Converters_.28JPA_2.1.29) for `CreditCard` object which belongs to Employee
* [JPA 2.1 unified schema generation / db seeding options](https://blogs.oracle.com/arungupta/entry/jpa_2_1_schema_generation) (see `persistence.xml`)

In addition to [Arquillian testing platform](http://arquillian.org) following tools has been used:
* [AssertJ](http://assertj.org) fluent assertions to verify the result of retrieving all objects from the datastore.
* [ShrinkWrap Maven Resolver](https://github.com/shrinkwrap/resolver/blob/master/README.asciidoc) to bundle aforementioned AssertJ together with the test deployment.

