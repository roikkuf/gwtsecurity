<%@page contentType="text/html"%>
<%
response.setHeader("Pragma","No-cache");
response.setHeader("Cache-Control","no-cache");
response.setDateHeader("Expires", 0);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <title>Secured Document for ROLE_USER</title>
        <meta http-equiv="Expires" content="-1" />
        <meta http-equiv="Pragma" content="no-cache" />
        <meta http-equiv="Cache-Control" content="no-cache" />
    </head>
    <body>
      <center>
        <h1>Hello,This is a secured Document!</h1>
        <h1>You must be a ROLE_STAFF user to visit here.</h1>
        <a href="../j_spring_security_logout">logout</a>
      </center>
    </body>
</html>
