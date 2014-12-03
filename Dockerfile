FROM centos/wildfly
RUN /opt/wildfly/bin/add-user.sh -up mgmt-users.properties admin Admin#70365 --silent
CMD ["/opt/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]

#ADD jaxrs/jaxrs-client/target/jaxrs-client.war /opt/wildfly/standalone/deployments/
