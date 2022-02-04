<!-- 
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
-->

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>REST vs WebSocket</title>

    </head>
    <body>
        <link rel="stylesheet" href="stylesheet.css" type="text/css">
        <h1>REST vs WebSocket</h1>

        <div style="text-align: center;">
            <form action=""> 
                Payload size: <input id="size" value="1" type="text"><br>
                How many times ?: <input id="times" value="10" type="text"><br>
                <form name="inputForm">
                Protocol: <input type="checkbox" name="protocol" id="rest" checked="true">REST
                            <input type="checkbox" name="protocol" id="websocket" checked="true">WebSocket
                </form><br/>
                <input onclick="echoText();" value="Echo" type="button">
                <input onclick="clearProgressBar();" value="Clear" type="button">
            </form>
            <table>
                <tr>
                    <th>
                        REST Endpoint
                    </th>
                    <th>
                        WebSocket
                    </th>
                </tr>
                <tr>
                    <td align="left">
                        Sending messages:<br/>
                        <progress id="restSendProgressBar" max="100" value="0">
                        </progress>
                        Receiving messages:<br/>
                        <progress id="restReceiveProgressBar" max="100" value="0">
                        </progress>
                        <div id="restOutput"></div>
                    </td>
                    <td align="left">
                        Sending messages:<br/>
                        <progress id="wsSendProgressBar" max="100" value="0">
                        </progress>
                        Receiving messages:<br/>
                        <progress id="wsReceiveProgressBar" max="100" value="0">
                        </progress>
                        <div id="wsOutput"></div>
                    </td>
                </tr>
            </table>
        </div>
        
        <script language="javascript" type="text/javascript" src="websocket.js"> </script>
        <script language="javascript" type="text/javascript" src="rest.js"> </script>
    </body>
</html>
