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
import org.javaee7.jca.filewatch.event.Deleted;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import java.io.File;

import static org.javaee7.jca.filewatch.FileEvent.Type.CREATED;
import static org.javaee7.jca.filewatch.FileEvent.Type.DELETED;

/**
 * @author Robert Panzer (robert.panzer@me.com)
 * @author Bartosz Majsak (bartosz.majsak@gmail.com)
 */
@MessageDriven(activationConfig = { @ActivationConfigProperty(propertyName = "dir", propertyValue = "/tmp") })
public class FileWatchingMDB implements FileSystemWatcher {

    @Inject
    private Event<FileEvent> fileEvent;

    @Created(".*\\.txt")
    public void onNewTextFile(final File f) {
        fileEvent.fire(new FileEvent(CREATED, f));
    }

    @Created(".*\\.pdf")
    public void onNewPdfFile(final File f) {
        fileEvent.fire(new FileEvent(CREATED, f));
    }

    @Deleted(".*\\.txt")
    public void onDeleteTextFile(final File f) {
        fileEvent.fire(new FileEvent(DELETED, f));
    }
}
