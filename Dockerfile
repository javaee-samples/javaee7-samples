FROM centos/wildfly
ADD jaxrs/jaxrs-client/target/jaxrs-client.war /opt/wildfly/standalone/deployments/
