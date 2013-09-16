# Java EE 7 Samples #

This workspace consists of Java EE 7 Samples. They are divided in different directories, one for each JSR.

## How to run ? ##

### How I run them ? ###

1. Open the sample in NetBeans 7.4 beta+ (Download from [http://bits.netbeans.org/dev/nightly/latest/])
2. Click on "Run" (sample is built and deployed on GlassFish 4, main page shows up)
3. Main page provides feature name, how to run the sample, and displays the output

### Cargo ###

By default, all samples are deployed on GlassFish 4. They can be deployed on Widlfly 8 by adding ``-P wildfly`` to all maven commands. Make sure to edit ``glassfish.home`` or ``wildfly.home`` property value in the top-level ``pom.xml`` to point to your local GlassFish or Wildfly directory respectively.

1. In one terminal, anywhere in the project with a ``pom.xml``: ``mvn cargo:run`` to start GlassFish server or ``mvn -P wildfly`` to start Wildfly server. They are both configured to run on port 8080 so only one can run at a given time.
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

find . -name pom.xml -depth 3 -maxdepth 3 | sed 's|\./||g' | sed 's|\/pom.xml||g'

I don't plan to write any formal documentation, let the code talk. [Java EE 7 Essentials](http://www.amazon.com/Java-EE-Essentials-Arun-Gupta/dp/1449370179/) refer to some of these samples and provide an extensive explanation.

+ batch/batchlet-simple
+ batch/chunk-checkpoint
+ batch/chunk-csv-database
+ batch/chunk-csv-database.clean
+ batch/chunk-exception
+ batch/chunk-mapper
+ batch/chunk-optional-processor
+ batch/chunk-partition
+ batch/chunk-simple
+ batch/chunk-simple-nobeans
+ batch/decision
+ batch/flow
+ batch/listeners
+ batch/multiple-steps
+ batch/split
+ cdi/bean-discovery-all
+ cdi/bean-discovery-annotated
+ cdi/bean-discovery-none
+ cdi/beanmanager
+ cdi/beansxml-noversion
+ cdi/built-in
+ cdi/decorators
+ cdi/exclude-filter
+ cdi/interceptors
+ cdi/nobeans-xml
+ cdi/pkg-level
+ cdi/vetoed
+ concurrency/dynamicproxy
+ concurrency/executor
+ concurrency/schedule
+ concurrency/threads
+ ejb/embeddable
+ ejb/lifecycle
+ ejb/singleton
+ ejb/stateful
+ ejb/stateless
+ ejb/timer
+ el/standalone
+ javamail/definition
+ jaxrs/async-client
+ jaxrs/async-server
+ jaxrs/beanvalidation
+ jaxrs/client-negotiation
+ jaxrs/dynamicfilter
+ jaxrs/filter
+ jaxrs/filter-interceptor
+ jaxrs/interceptor
+ jaxrs/invocation
+ jaxrs/invocation-async
+ jaxrs/jaxrs-client
+ jaxrs/jaxrs-endpoint
+ jaxrs/jsonp
+ jaxrs/link
+ jaxrs/mapping-exceptions
+ jaxrs/moxy
+ jaxrs/readerwriter
+ jaxrs/readerwriter-json
+ jaxrs/request-binding
+ jaxrs/resource-validation
+ jaxrs/server-negotiation
+ jaxrs/server-sent-event
+ jaxrs/singleton-annotation
+ jaxrs/singleton-application
+ jaxrs/singleton-application.old
+ jca/connector-simple
+ jms/jmscontext-cdi
+ jms/send-receive
+ jms/send-receive-simple
+ jms/temp-destination
+ jpa/criteria
+ jpa/entitygraph
+ jpa/jndi-context
+ jpa/listeners
+ jpa/locking-optimistic
+ jpa/locking-pessimistic
+ jpa/multiple-pu
+ jpa/native-sql
+ jpa/native-sql-resultset-mapping
+ jpa/pu-typesafe
+ jpa/schema-gen
+ jpa/schema-gen-scripts
+ jpa/schema-gen-scripts-external
+ jpa/schema-gen-scripts-generate
+ jpa/storedprocedure
+ jsf/ajax
+ jsf/bean-validation
+ jsf/components
+ jsf/composite-component
+ jsf/contracts
+ jsf/contracts-library
+ jsf/contracts-library-impl
+ jsf/file-upload
+ jsf/flows-declarative
+ jsf/flows-mixed
+ jsf/flows-programmatic
+ jsf/flows-simple
+ jsf/http-get
+ jsf/passthrough
+ jsf/radio-buttons
+ jsf/resource-handling
+ jsf/server-extension
+ jsf/simple-facelet
+ jsf/viewscoped
+ json/object-builder
+ json/object-reader
+ json/streaming-generate
+ json/streaming-parser
+ json/twitter-search
+ jta/transaction-scope
+ jta/transactional
+ jta/tx-exception
+ jta/user-transaction
+ servlet/async-servlet
+ servlet/cookies
+ servlet/error-mapping
+ servlet/event-listeners
+ servlet/file-upload
+ servlet/form-based-security
+ servlet/metadata-complete
+ servlet/nonblocking
+ servlet/protocol-handler
+ servlet/resource-packaging
+ servlet/servlet-filters
+ servlet/servlet-security
+ servlet/web-fragment
+ validation/custom-constraint
+ validation/methods
+ websocket/binary
+ websocket/chat
+ websocket/encoder
+ websocket/encoder-client
+ websocket/encoder-programmatic
+ websocket/endpoint
+ websocket/endpoint-async
+ websocket/endpoint-config
+ websocket/endpoint-javatypes
+ websocket/endpoint-partial
+ websocket/endpoint-programmatic
+ websocket/endpoint-programmatic-async
+ websocket/endpoint-programmatic-config
+ websocket/endpoint-programmatic-injection
+ websocket/endpoint-programmatic-partial
+ websocket/endpoint-security
+ websocket/httpsession
+ websocket/injection
+ websocket/javase-client
+ websocket/messagesize
+ websocket/parameters
+ websocket/properties
+ websocket/subprotocol
+ websocket/websocket-client
+ websocket/websocket-client-config
+ websocket/websocket-client-programmatic
+ websocket/websocket-client-programmatic-config
+ websocket/websocket-client-programmatic-encoders
+ websocket/websocket-vs-rest
+ websocket/whiteboard
