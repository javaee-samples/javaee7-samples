<%@page 
    contentType="text/html; charset=UTF-8"
    language="java"
    pageEncoding="UTF-8"
    import="java.nio.charset.StandardCharsets"
%><%

String param = request.getParameter("name");
if (param == null) {
	param = "";
}
int paramLength = param.length();
byte[] paramData = param.getBytes(StandardCharsets.UTF_8);
int paramDataLength = paramData.length;
%>
<!DOCTYPE html>
<html>
<head>
<title>Receiving parameters via post</title>
</head>
<body>

<p>Here you should see the data you entered in the previous form.</p>
<p>the bug makes the last parameter to contain a lot of junk (zeros) at the end</p>


Hello <span id="param"><%= param %></span><br/>
paramLegnth <span id="paramLength"><%= paramLength %></span><br/>
paramDataLength <span id="arrayLength"><%= paramDataLength %></span>


</body>
</html>
