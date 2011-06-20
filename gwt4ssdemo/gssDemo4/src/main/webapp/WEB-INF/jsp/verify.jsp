<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<html>                                                 
    <head>
        <title>Verify Success Page</title>
        <script language="javascript" type="text/javascript"><!--
            function doload(){
                if(window.opener&&window.opener.loginResult){
                    window.opener.loginResult(true);
                }
                window.close();
            }
        --></script>
    </head>
    <body onload="doload()">
        <h1 style="text-align:center">Verify Success</h1>
        <div align="center"><input type="button" onclick="doload()" value="Close Window &amp; return to main page"/></div>
    </body>
</html>
