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

import org.javaee7.jca.filewatch.adapter.FileSystemWatcher;
import org.javaee7.jca.filewatch.event.Created;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.ResourceAdapterArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.enterprise.event.Observes;
import java.io.File;
import java.util.concurrent.Callable;

import static com.jayway.awaitility.Awaitility.await;
import static com.jayway.awaitility.Duration.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.javaee7.jca.filewatch.FileEvent.Type.DELETED;

/**
 * @author Robert Panzer (robert.panzer@me.com)
 * @author Bartosz Majsak (bartosz.majsak@gmail.com)
 */
@RunWith(Arquillian.class)
public class FileWatcherTest {

    @Deployment
    public static EnterpriseArchive deploy() throws Exception {

        final JavaArchive fsWatcherFileAdapter = ShrinkWrap.create(JavaArchive.class, "rar.jar")
            .addPackages(true, Created.class.getPackage(), FileSystemWatcher.class.getPackage());

        final ResourceAdapterArchive rar = ShrinkWrap.create(ResourceAdapterArchive.class, "fswatcher.rar")
            .addAsLibrary(fsWatcherFileAdapter);

        final JavaArchive fileWatcher = ShrinkWrap.create(JavaArchive.class, "mdb.jar")
            .addClasses(FileEvent.class, FileWatchingMDB.class)
            // appropriate descriptor will be only picked up by the target container
            .addAsManifestResource("glassfish-ejb-jar.xml")
            .addAsManifestResource("jboss-ejb3.xml")
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");

        final JavaArchive test = ShrinkWrap.create(JavaArchive.class, "test.jar")
            .addClasses(FileWatcherTest.class)
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");

        final JavaArchive[] testArchives = Maven.resolver()
            .loadPomFromFile("pom.xml")
            .resolve("org.assertj:assertj-core", "com.jayway.awaitility:awaitility")
            .withTransitivity()
            .as(JavaArchive.class);

        return ShrinkWrap.create(EnterpriseArchive.class, "test.ear")
            .addAsModules(rar, fileWatcher)
            .addAsLibraries(testArchives)
            .addAsLibrary(test);

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

        // when
        await().atMost(TEN_SECONDS).with().pollInterval(FIVE_HUNDRED_MILLISECONDS)
            .until(fileEventObserved());

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

        // when
        await().atMost(TEN_SECONDS).with().pollInterval(FIVE_HUNDRED_MILLISECONDS)
            .until(fileEventObserved());

        // then
        assertThat(tempFile.getName()).isEqualTo(observedFileEvent.getFile().getName());
        assertThat(FileEvent.Type.CREATED).isEqualTo(observedFileEvent.getType());
    }

    @Test
    @InSequence(3)
    public void should_react_on_deletion_of_existing_text_file() throws Exception {
        // given
        File tempFile = new File("/tmp", "test.txt");
        tempFile.delete();

        // when
        await().atMost(TEN_SECONDS).with().pollInterval(FIVE_HUNDRED_MILLISECONDS)
            .until(fileEventObserved());
        // then
        assertThat(tempFile.getName()).isEqualTo(observedFileEvent.getFile().getName());
        assertThat(DELETED).isEqualTo(observedFileEvent.getType());
    }

    // CDI Observer

    private Callable<Boolean> fileEventObserved() {
        return new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return observedFileEvent != null;
            }
        };
    }

    // -- Helper methods

    public void notifyFileEvent(@Observes FileEvent fileEvent) {
        observedFileEvent = fileEvent;
    }

}
