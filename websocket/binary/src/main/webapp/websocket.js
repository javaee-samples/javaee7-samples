/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

var wsByteArrayUri = "ws://" + document.location.host + document.location.pathname + "bytearray";
var wsByteBufferUri = "ws://" + document.location.host + document.location.pathname + "bytebuffer";
var wsInputStreamUri = "ws://" + document.location.host + document.location.pathname + "inputstream";

console.log("Connecting to " + wsByteArrayUri);

var websocketByteArray = new WebSocket(wsByteArrayUri);
var websocketByteBuffer = new WebSocket(wsByteBufferUri);
var websocketInputStream = new WebSocket(wsInputStreamUri);

websocketByteArray.binaryType = "arraybuffer";
websocketByteBuffer.binaryType = "arraybuffer";
websocketInputStream.binaryType = "arraybuffer";

websocketByteArray.onopen = function(evt) { onOpenByteArray(evt); };
websocketByteArray.onmessage = function(evt) { onMessageByteArray(evt); };
websocketByteArray.onerror = function(evt) { onError(evt); };

websocketByteBuffer.onopen = function(evt) { onOpenByteBuffer(evt); };
websocketByteBuffer.onmessage = function(evt) { onMessageByteBuffer(evt); };
websocketByteBuffer.onerror = function(evt) { onError(evt); };

websocketInputStream.onopen = function(evt) { onOpenInputStream(evt); };
websocketInputStream.onmessage = function(evt) { onMessageInputStream(evt); };
websocketInputStream.onerror = function(evt) { onError(evt); };

var output = document.getElementById("output");

function onOpenByteArray() {
    console.log("onOpen (byte])");
}

function onOpenByteBuffer() {
    console.log("onOpen (ByteBuffer)");
}

function onOpenInputStream() {
    console.log("onOpen (InputStream)");
}

function echoBinaryByteArray() {
    var buffer = new ArrayBuffer(myField.value.length);
    var bytes = new Uint8Array(buffer);
    for (var i=0; i<bytes.length; i++) {
        bytes[i] = i;
    }
    websocketByteArray.send(buffer);
    writeToScreen("SENT (byte[]): " + buffer.byteLength + " bytes");
}

function echoBinaryByteBuffer() {
    var buffer = new ArrayBuffer(myField.value.length);
    var bytes = new Uint8Array(buffer);
    for (var i=0; i<bytes.length; i++) {
        bytes[i] = i;
    }
    websocketByteBuffer.send(buffer);
    writeToScreen("SENT (ByteBuffer): " + buffer.byteLength + " bytes");
}

function echoBinaryInputStream() {
    var buffer = new ArrayBuffer(myField.value.length);
    var bytes = new Uint8Array(buffer);
    for (var i=0; i<bytes.length; i++) {
        bytes[i] = i;
    }
    websocketInputStream.send(buffer);
    writeToScreen("SENT (InputStream): " + buffer.byteLength + " bytes");
}

function onMessageByteArray(evt) {
    writeToScreen("RECEIVED (byte[]): " + evt.data);
}

function onMessageByteBuffer(evt) {
    writeToScreen("RECEIVED (ByteBuffer): " + evt.data);
}

function onMessageInputStream(evt) {
    writeToScreen("RECEIVED (InputStream): " + evt.data);
}

function onError(evt) {
    writeToScreen('<span style="color: red;">ERROR:</span> ' + evt.data);
}

function writeToScreen(message) {
    var pre = document.createElement("p");
    pre.style.wordWrap = "break-word";
    pre.innerHTML = message;
    output.appendChild(pre);
}
