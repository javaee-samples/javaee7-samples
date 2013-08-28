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
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Collaborative White Board</title>
    </head>
    <body>
        <h1>Collaborative White Board</h1>
        <table>
            <tr>
                <td>
            <canvas id="myCanvas" width="150" height="150" style="border:1px solid #000000;"></canvas>
        </td>
        <td>
            <form name="inputForm">
                <table cellpadding="2" cellspacing="2">

                    <tr>
                        <th align="left">Color</th>
                        <td>
                            <input type="radio" name="color" value="#FF0000" checked="true">Red
                        </td>
                        <td>
                            <input type="radio" name="color" value="#0000FF">Blue
                        </td>
                        <td>
                            <input type="radio" name="color" value="#FF9900">Orange
                        </td>
                        <td>
                            <input type="radio" name="color" value="#33CC33">Green
                        </td>
                    </tr>

                    <tr>
                        <th align="left">Shape</th>
                        <td>
                            <input type="radio" name="shape" value="square" checked="true">Square
                        </td>
                        <td>
                            <input type="radio" name="shape" value="circle">Circle
                        </td>
                    </tr>

                    <tr>
                        <th align="left">Transfer</th>
                        <td>
                            <input type="checkbox" id="instant" value="instant" checked="true">Online
                        </td>
                        <td>
                            <input type="submit" value="Snapshot" onclick="defineImageBinary(); return false;"></input>
                        </td>
                    </tr>
                </table>

            </form>
        </td>
    </tr>
</table>


<div id="output"></div>
<script type="text/javascript" src="websocket.js"></script>
<script type="text/javascript" src="whiteboard.js"></script>

</body>
</html>
