<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>WebSocket Security</title>

    </head>
    <body>
        <h1>WebSocket Security</h1>

        <div style="text-align: center;">
            <form action=""> 
                <input id="myField" value="WebSocket" type="text"><br>
                <input onclick="echo();" value="Echo" type="button"> 
            </form>
        </div>
        <div id="output"></div>
        <script language="javascript" type="text/javascript" src="websocket.js">
        </script>
    </body>
</html>