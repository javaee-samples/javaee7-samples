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
package org.javaee7.jca.filewatch.adapter;

import javax.resource.ResourceException;
import javax.resource.spi.*;
import javax.resource.spi.endpoint.MessageEndpointFactory;
import javax.transaction.xa.XAResource;
import java.io.IOException;
import java.nio.file.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Robert Panzer (robert.panzer@me.com)
 * @author Bartosz Majsak (bartosz.majsak@gmail.com)
 */
@Connector
public class FileSystemWatcherResourceAdapter implements ResourceAdapter {

    FileSystem fileSystem;

    WatchService watchService;

    Map<WatchKey, MessageEndpointFactory> listeners = new ConcurrentHashMap<>();

    Map<MessageEndpointFactory, Class<?>> endpointFactoryToBeanClass = new ConcurrentHashMap<>();

    private BootstrapContext bootstrapContext;

    @Override
    public void endpointActivation(MessageEndpointFactory endpointFactory, ActivationSpec activationSpec) throws ResourceException {
        FileSystemWatcherActivationSpec fsWatcherAS = (FileSystemWatcherActivationSpec) activationSpec;

        try {
            WatchKey watchKey = fileSystem.getPath(fsWatcherAS.getDir())
                .register(watchService, StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_DELETE,
                    StandardWatchEventKinds.ENTRY_MODIFY);

            listeners.put(watchKey, endpointFactory);

            endpointFactoryToBeanClass.put(endpointFactory, endpointFactory.getEndpointClass());
        } catch (IOException e) {
            throw new ResourceException(e);
        }
    }

    @Override
    public void endpointDeactivation(MessageEndpointFactory endpointFactory, ActivationSpec activationSpec) {
        for (WatchKey watchKey : listeners.keySet()) {
            if (listeners.get(watchKey) == endpointFactory) {
                listeners.remove(watchKey);
                break;
            }
        }
        endpointFactoryToBeanClass.remove(endpointFactory);
    }

    @Override
    public XAResource[] getXAResources(ActivationSpec[] arg0) throws ResourceException {
        return new XAResource[0];
    }

    @Override
    public void start(BootstrapContext bootstrapContext) throws ResourceAdapterInternalException {
        this.bootstrapContext = bootstrapContext;

        try {
            fileSystem = FileSystems.getDefault();
            watchService = fileSystem.newWatchService();
        } catch (IOException e) {
            throw new ResourceAdapterInternalException(e);
        }

        new WatchingThread(watchService, this).start();
    }

    @Override
    public void stop() {
        try {
            watchService.close();
        } catch (IOException e) {
            throw new RuntimeException("Failed stopping file watcher.", e);
        }
    }

    public MessageEndpointFactory getListener(WatchKey watchKey) {
        return listeners.get(watchKey);
    }

    public BootstrapContext getBootstrapContext() {
        return bootstrapContext;
    }

    public Class<?> getBeanClass(MessageEndpointFactory endpointFactory) {
        return endpointFactoryToBeanClass.get(endpointFactory);
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
