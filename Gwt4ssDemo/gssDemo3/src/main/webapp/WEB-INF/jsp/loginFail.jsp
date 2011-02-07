<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %><%
//Jetty not accept EL - ${SPRING_SECURITY_LAST_EXCEPTION.message}
Exception error = (Exception)request.getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
out.write(error==null?"Login Fail":error.getMessage());
%>