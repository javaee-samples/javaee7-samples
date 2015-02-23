package org.javaee7.jms.send.receive.mdb;

import org.javaee7.jms.send.receive.simple.MessageSenderAsync;
import org.javaee7.jms.send.receive.mdb.MessageReceiverAsync;
import org.javaee7.jms.send.receive.simple.MessageSenderSync;
import org.javaee7.jms.send.receive.simple.MessageReceiverSync;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;

import javax.ejb.EJB;

import org.javaee7.jms.send.receive.Resources;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.runner.RunWith;

/**
 *
 * @author Patrik Dudits
 */
@RunWith(Arquillian.class)
public class AsyncTest {

    @EJB
    MessageSenderAsync asyncSender;

    @Test
    public void testAsync() throws Exception {
        asyncSender.sendMessage("Fire!");
        ReceptionSynchronizer.waitFor(MessageReceiverAsync.class, "onMessage");
        // unless we timed out, the test passes
    }

    @Deployment
    public static WebArchive deploy() {
        return ShrinkWrap.create(WebArchive.class)
            .addClass(MessageSenderAsync.class)
            .addClass(Resources.class)
            .addClass(MessageReceiverAsync.class)
            .addClass(ReceptionSynchronizer.class)
            .addAsWebInfResource(new File("src/test/resources/WEB-INF/ejb-jar.xml"));
    }

}
