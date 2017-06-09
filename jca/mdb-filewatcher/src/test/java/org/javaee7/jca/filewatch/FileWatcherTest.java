/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.javaee7.jca.filewatch;

import static com.jayway.awaitility.Awaitility.await;
import static com.jayway.awaitility.Duration.FIVE_HUNDRED_MILLISECONDS;
import static com.jayway.awaitility.Duration.ONE_MINUTE;
import static java.lang.System.out;
import static org.assertj.core.api.Assertions.assertThat;
import static org.javaee7.jca.filewatch.event.FileEvent.Type.CREATED;
import static org.javaee7.jca.filewatch.event.FileEvent.Type.DELETED;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.jboss.shrinkwrap.api.asset.EmptyAsset.INSTANCE;

import java.io.File;
import java.util.concurrent.Callable;

import javax.enterprise.event.Observes;

import org.javaee7.jca.filewatch.adapter.FileSystemWatcher;
import org.javaee7.jca.filewatch.event.Created;
import org.javaee7.jca.filewatch.event.FileEvent;
import org.javaee7.jca.filewatch.mdb.FileWatchingMDB;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.ResourceAdapterArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Robert Panzer (robert.panzer@me.com)
 * @author Bartosz Majsak (bartosz.majsak@gmail.com)
 */
@RunWith(Arquillian.class)
public class FileWatcherTest {

    @Deployment
    public static EnterpriseArchive deploy() throws Exception {

        return ShrinkWrap.create(EnterpriseArchive.class, "test.ear")
            .addAsModules(
                create(ResourceAdapterArchive.class, "fswatcher.rar")
                    .addAsLibrary(
                        create(JavaArchive.class, "rar.jar")
                            .addPackages(true, 
                                Created.class.getPackage(), 
                                FileSystemWatcher.class.getPackage())),
                    
                create(JavaArchive.class, "mdb.jar")
                    .addClasses(FileWatchingMDB.class)
                    // appropriate descriptor will be only picked up by the target container
                    .addAsManifestResource("glassfish-ejb-jar.xml")
                    .addAsManifestResource("jboss-ejb3.xml")
                    .addAsManifestResource(INSTANCE, "beans.xml"))
            
            .addAsLibrary( 
                create(JavaArchive.class, "test.jar")
                    .addClasses(FileWatcherTest.class, FileEvent.class)
                    .addAsManifestResource(INSTANCE, "beans.xml"))
            
            .addAsLibraries(
                Maven.resolver()
                    .loadPomFromFile("pom.xml")
                    .resolve("org.assertj:assertj-core", "com.jayway.awaitility:awaitility")
                    .withTransitivity()
                    .as(JavaArchive.class))
            ;

    }

    private static FileEvent observedFileEvent;

    @Before
    public void init() throws Exception {
        observedFileEvent = null;
    }

    @Test
    @InSequence(1)
    public void should_react_on_new_text_file_arriving_in_the_folder() throws Exception {
        // given
        File tempFile = new File("/tmp", "test.txt");
        tempFile.createNewFile();
        tempFile.deleteOnExit();
        
        System.out.println("Test created text file: " + tempFile.getName());

        // when
        await().atMost(ONE_MINUTE)
               .with().pollInterval(FIVE_HUNDRED_MILLISECONDS)
               .until(fileEventObserved());
        
        out.println("Test received CDI event " + observedFileEvent);

        // then
        assertThat(tempFile.getName()).isEqualTo(observedFileEvent.getFile().getName());
        assertThat(FileEvent.Type.CREATED).isEqualTo(observedFileEvent.getType());
    }

    @Test
    @InSequence(2)
    public void should_react_on_new_pdf_file_arriving_in_the_folder() throws Exception {
        
        // given
        File tempFile = new File("/tmp", "pdf-test-creation" + System.currentTimeMillis() + ".pdf");
        tempFile.createNewFile();
        tempFile.deleteOnExit();
        
        out.println("Test created PDF file: " + tempFile.getName());

        // when
        await().atMost(ONE_MINUTE)
               .with().pollInterval(FIVE_HUNDRED_MILLISECONDS)
               .until(fileEventObserved());
        
        out.println("Test received CDI event " + observedFileEvent);

        // then
        assertThat(tempFile.getName()).isEqualTo(observedFileEvent.getFile().getName());
        assertThat(CREATED).isEqualTo(observedFileEvent.getType());
    }

    @Test
    @InSequence(3)
    public void should_react_on_deletion_of_existing_text_file() throws Exception {
        // given
        File tempFile = new File("/tmp", "test.txt");
        tempFile.delete();
        
        System.out.println("Test deleted text file: " + tempFile.getName());

        // when
        await().atMost(ONE_MINUTE)
               .with().pollInterval(FIVE_HUNDRED_MILLISECONDS)
               .until(fileEventObserved());
        
        out.println("Test received CDI event " + observedFileEvent);
        
        // then
        assertThat(tempFile.getName()).isEqualTo(observedFileEvent.getFile().getName());
        assertThat(DELETED).isEqualTo(observedFileEvent.getType());
    }

    // CDI Observer

    private Callable<Boolean> fileEventObserved() {
        return new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (observedFileEvent == null) {
                    return false;
                }
                
                out.println("Watchable received CDI event " + observedFileEvent);
                
                return true;
            }
        };
    }

    // -- Helper methods

    public void notifyFileEvent(@Observes FileEvent fileEvent) {
        observedFileEvent = fileEvent;
    }

}
