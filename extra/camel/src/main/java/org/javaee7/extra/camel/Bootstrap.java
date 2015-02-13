package org.javaee7.extra.camel;

import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.cdi.CdiCamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Arun Gupta
 */
@Singleton
@Startup
public class Bootstrap {

    @Inject
    CdiCamelContext context;

    Logger logger = LoggerFactory.getLogger(Bootstrap.class);

    @PostConstruct
    public void init() {
        logger.info(">> Create CamelContext and register Camel Route.");

        try {
            context.addRoutes(new RouteBuilder() {
                @Override
                public void configure() {
                    //                    from("test-jms:queue:test.queue").to("file://test");
                    from("timer://timer1?period=1000")
                        .process(new Processor() {
                            @Override
                            public void process(Exchange message) throws Exception {
                                logger.info("Processing {}", message);
                            }
                        });
                }
            });
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(Bootstrap.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Define Timer URI
        //        simpleRoute.setTimerUri("timer://simple?fixedRate=true&period=10s");
        // Start Camel Context
        context.start();

        logger.info(">> CamelContext created and camel route started.");
    }
}
