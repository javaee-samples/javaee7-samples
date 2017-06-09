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
package org.javaee7.jca.filewatch.mdb;

import static java.lang.System.out;
import static org.javaee7.jca.filewatch.event.FileEvent.Type.CREATED;
import static org.javaee7.jca.filewatch.event.FileEvent.Type.DELETED;

import java.io.File;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.javaee7.jca.filewatch.adapter.FileSystemWatcher;
import org.javaee7.jca.filewatch.event.Created;
import org.javaee7.jca.filewatch.event.Deleted;
import org.javaee7.jca.filewatch.event.FileEvent;

/**
 * @author Robert Panzer (robert.panzer@me.com)
 * @author Bartosz Majsak (bartosz.majsak@gmail.com)
 */
@MessageDriven(activationConfig = { @ActivationConfigProperty(propertyName = "dir", propertyValue = "/tmp") })
public class FileWatchingMDB implements FileSystemWatcher {

    @Inject
    private Event<FileEvent> fileEvent;

    @Created(".*\\.txt")
    public void onNewTextFile(File file) {
        out.println("MDB called for new text file: " + file.getName());
        fileEvent.fire(new FileEvent(CREATED, file));
    }

    @Created(".*\\.pdf")
    public void onNewPdfFile(File file) {
        out.println("MDB called for new PDF file: " + file.getName());
        fileEvent.fire(new FileEvent(CREATED, file));
    }

    @Deleted(".*\\.txt")
    public void onDeleteTextFile(File file) {
        out.println("MDB called for text file deleted: " + file.getName());
        fileEvent.fire(new FileEvent(DELETED, file));
    }
}
