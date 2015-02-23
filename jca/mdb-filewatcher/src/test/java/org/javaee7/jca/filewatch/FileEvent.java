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

import java.io.File;

/**
 * @author Robert Panzer (robert.panzer@me.com)
 * @author Bartosz Majsak (bartosz.majsak@gmail.com)
 */
public class FileEvent {

    public static enum Type {
        CREATED,
        DELETED,
        MODIFIED;
    }

    private File file;

    private Type type;

    public FileEvent(Type type, File file) {
        this.type = type;
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public Type getType() {
        return type;
    }
}
