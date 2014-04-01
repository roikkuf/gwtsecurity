<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <title>Login Page</title>                                                          
        <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/openid/css/openid.css" />
        <!--link type="text/css" rel="stylesheet" href="css/openid.css" /-->
        <script src="http://code.jquery.com/jquery.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/openid/js/openid-jquery.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/openid/js/openid-en.js"></script>
        <!--script type="text/javascript" src="js/openid-jquery.js"></script>
        <script type="text/javascript" src="js/openid-en.js"></script-->
        <script type="text/javascript"><!--
            $(function() {
                openid.img_path="${pageContext.request.contextPath}/openid/images/"
                //openid.img_path="/openid/images/"
                openid.init('openid_identifier');
                //openid.setDemoMode(true); //Stops form submission for client javascript-only test purposes
             });
        --></script> 
        <!-- /Simple OpenID Selector -->
        <style type="text/css">
            /* Basic page formatting */
            body {
                font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
            }
        </style>
    </head>
    <body>
        <center>
            <form action="${pageContext.request.contextPath}/j_spring_openid_security_check" method="post" id="openid_form">
            <!--form action="/j_spring_openid_security_check" method="post" id="openid_form"-->
                <c:if test="${not empty param.login_error}">
                    <span style="color:red">
                        Your login attempt was not successful, try again.<br/><br/>
                        Reason: <c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}"/>.
                    </span>
                </c:if>
                <input type="hidden" name="action" value="verify" />
                <fieldset>
                    <legend>Sign-in or Create New Account</legend>

                    <div id="openid_choice">
                        <p>Please click your account provider:</p>
                        <div id="openid_btns"></div>

                    </div>

                    <div id="openid_input_area">
                        <input id="openid_identifier" name="openid_identifier" type="text" value="http://" />
                        <input id="openid_submit" type="submit" value="Sign-In"/>
                    </div>
                    <noscript>
                        <p>OpenID is a service that allows you to log-on to many different websites using a single identity.
                            Find out <a href="http://openid.net/what/">more about OpenID</a> and <a href="http://openid.net/get/">how to get an OpenID enabled account</a>.</p>
                    </noscript>
                </fieldset>
            </form>   
            <div>&nbsp;</div>
            <table border="1" align="center">
                <thead>
                    <tr>
                        <th>OpenId provider</th>
                        <th>Authorities</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>Google</td>
                        <td nowrap>ROLE_ADMIN,ROLE_STAFF</td>
                    </tr>
                    <tr>
                        <td>Other Provider</td>                       
                        <td>ROLE_STAFF</td>
                    </tr>
                </tbody>
            </table>
        </center>
    </body>
</html>
