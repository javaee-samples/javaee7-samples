<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Servlet : Security</title>
    </head>
    <body>
        <h1>Servlet : Security</h1>
        
        Make sure to create a user:<br><br>
        
        For WildFly: Invoke "./bin/add-user.sh -a -u u1 -p p1 -g g1"<br>
        For GlassFish: Invoke "./bin/asadmin create-file-user --groups g1 u1" and use the password "p1" when prompted.<br><br>
        Then call the <a href="${pageContext.request.contextPath}/SecureServlet">GET</a> method.<br/>
    </body>
</html>
