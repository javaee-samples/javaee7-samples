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

var wsUri = "ws://" + document.location.host + document.location.pathname + "websocket";
console.log("Connecting to " + wsUri);
var websocket = new WebSocket(wsUri);
websocket.onopen = function(evt) { onOpen(evt) };
websocket.onmessage = function(evt) { onMessage(evt) };
websocket.onerror = function(evt) { onError(evt) };

var wsSendBar = document.getElementById("wsSendProgressBar");
var wsRxBar = document.getElementById("wsReceiveProgressBar");
var wsStartTime;
var wsEndTime;
var payload = "";

var wsOutput = document.getElementById("wsOutput");

function echoText() {
    clearProgressBar();
    if (document.getElementById("rest").checked) {
        restEchoText();
    }
    if (document.getElementById("websocket").checked) {
        wsEchoText();
    }
}

function wsEchoText() {
    wsClearProgressBar();

    if ((times.value % 5) != 0) {
        var oldTimes = times.value;
        times.value -= times.value % 5;
        writeToScreen(wsOutput, "Resetting the value " + oldTimes + " to " + times.value);
    }
    writeToScreen(wsOutput, "Sending " + times.value + " messages of \"" + size.value + "\" byte(s)");
    payload = "";
    for (var i = 0; i < size.value; i++) {
        payload += "x";
    }
    wsStartTime = new Date().getTime();
    for (var i=0; i<times.value; i++) {
        websocket.send(payload);
        wsSendBar.value += 100 / times.value;
    }
}

function wsClearProgressBar() {
    wsSendBar.value = 0;
    wsRxBar.value = 0;
}

function clearLog() {
    clearProgressBar();
    wsOutput.innerHTML = '';
}

function onOpen() {
//    writeToScreen(wsOutput, "Connected to "+ wsUri + "<hr>");
}

function onMessage(evt) {
    if (evt.data == payload) {
        wsRxBar.value += 100 / times.value;
        if (wsRxBar.value == "100") {
            wsEndTime = new Date().getTime();
            writeToScreen(wsOutput, "Total execution time: " + (wsEndTime - wsStartTime) + " ms");
            writeToScreen(wsOutput, "<hr>");
        }
    } else {
        writeToScreen(evt.data);
    }
}

function onError(evt) {
    writeToScreen(wsOutput, '<span style="color: red;">ERROR:</span> ' + evt.data);
}

function writeToScreen(div, message) {
    div.innerHTML += message + "<br/>";
}
