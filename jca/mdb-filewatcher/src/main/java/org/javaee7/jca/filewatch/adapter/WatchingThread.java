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

import static java.lang.System.out;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

import java.lang.reflect.Method;
import java.nio.file.ClosedWatchServiceException;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;

import javax.resource.spi.endpoint.MessageEndpoint;
import javax.resource.spi.endpoint.MessageEndpointFactory;
import javax.resource.spi.work.Work;
import javax.resource.spi.work.WorkException;

import org.javaee7.jca.filewatch.event.Created;
import org.javaee7.jca.filewatch.event.Deleted;
import org.javaee7.jca.filewatch.event.Modified;

/**
 * @author Robert Panzer (robert.panzer@me.com)
 * @author Bartosz Majsak (bartosz.majsak@gmail.com)
 */
final class WatchingThread extends Thread {

    private WatchService watchService;

    private FileSystemWatcherResourceAdapter resourceAdapter;

    WatchingThread(WatchService watchService, FileSystemWatcherResourceAdapter ra) {
        this.watchService = watchService;
        this.resourceAdapter = ra;
    }

    public void run() {
        while (true) {
            try {
                WatchKey watchKey = watchService.take();
                if (watchKey != null) {
                    dispatchEvents(watchKey.pollEvents(), resourceAdapter.getListener(watchKey));
                    watchKey.reset();
                }
            } catch (ClosedWatchServiceException e) {
                return;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void dispatchEvents(List<WatchEvent<?>> events, MessageEndpointFactory messageEndpointFactory) {
        for (WatchEvent<?> event : events) {
            Path path = (Path) event.context();
            
            out.println("Watch thread received event of kind: " + event.kind() + " for " + path.getFileName());

            try {
                MessageEndpoint endpoint = messageEndpointFactory.createEndpoint(null);
                Class<?> beanClass = resourceAdapter.getBeanClass(messageEndpointFactory);
                
                for (Method beanClassMethod : beanClass.getMethods()) {
                    if (methodIsForEvent(path.toString(), beanClassMethod, event.kind())) {
                        invoke(endpoint, beanClassMethod, path);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void invoke(final MessageEndpoint endpoint, final Method beanClassMethod, final Path path) throws WorkException {
        
        out.println("Watch thread scheduling endpoint call via workmanager for method: " + beanClassMethod.getName() + " and file" + path.getFileName());
        
        resourceAdapter.getBootstrapContext().getWorkManager().scheduleWork(new Work() {

            @Override
            public void run() {
                try {
                    try {
                        endpoint.beforeDelivery(beanClassMethod);
    
                        beanClassMethod.invoke(endpoint, path.toFile());
                    
                    } finally {
                        endpoint.afterDelivery();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void release() {
            }
        });
    }
    
    private boolean methodIsForEvent(String path, Method method, Kind<?> eventKind) {
        return
            (
                ENTRY_CREATE.equals(eventKind) && 
                method.isAnnotationPresent(Created.class) && 
                path.matches(method.getAnnotation(Created.class).value())
            ) 
            
            ||
            
            (
                ENTRY_DELETE.equals(eventKind) && 
                method.isAnnotationPresent(Deleted.class) && 
                path.matches(method.getAnnotation(Deleted.class).value())
            )
            
            ||
            
            (
                ENTRY_MODIFY.equals(eventKind) && 
                method.isAnnotationPresent(Modified.class) && 
                path.matches(method.getAnnotation(Modified.class).value())
            )
            
            ;
    }
}