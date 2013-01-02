<!doctype html>
<!-- The DOCTYPE declaration above will set the    -->
<!-- browser's rendering engine into               -->
<!-- "Standards Mode". Replacing this declaration  -->
<!-- with a "Quirks Mode" doctype may lead to some -->
<!-- differences in layout.                        -->

<html>
    <head>
        <meta http-equiv="content-type" content="text/html; charset=UTF-8">
        <meta name="gwt:property" content="locale=<%=request.getLocale()%>">

        <!--                                                               -->
        <!-- Consider inlining CSS to reduce the number of requested files -->
        <!--                                                               -->
        <link type="text/css" rel="stylesheet" href="demo3.css">

        <!--                                           -->
        <!-- Any title is fine                         -->
        <!--                                           -->
        <title>Gwt for Spring Security Demo</title>

        <!--                                           -->
        <!-- This script loads your compiled module.   -->
        <!-- If you add any GWT meta tags, they must   -->
        <!-- be added before this line.                -->
        <!--                                           -->
        <script type="text/javascript"><!--
            var _gaq = _gaq || [];
            _gaq.push(['_setAccount', 'UA-22086822-5']);
            _gaq.push(['_trackPageview']);

            (function() {
                var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
                ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
                var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
            })();
            --></script>
        <script type="text/javascript" language="javascript" src="demo3/demo3.nocache.js"></script>
    </head>

    <!--                                           -->
    <!-- The body can have arbitrary html, or      -->
    <!-- you can leave the body empty if you want  -->
    <!-- to create a completely dynamic UI.        -->
    <!--                                           -->
    <body>

        <!-- OPTIONAL: include this if you want history support -->
        <iframe src="javascript:''" id="__gwt_historyFrame" tabIndex='-1' style="position:absolute;width:0;height:0;border:0"></iframe>

        <!-- RECOMMENDED if your web app will not function without JavaScript enabled -->
        <noscript>
            <div style="width: 22em; position: absolute; left: 50%; margin-left: -11em; color: red; background-color: white; border: 1px solid red; padding: 4px; font-family: sans-serif">
                Your web browser must have JavaScript enabled
                in order for this application to display correctly.
            </div>
        </noscript>

        <h1>Gwt for Spring Security <a href="http://code.google.com/p/spring4gwt/">spring4gwt</a> Demo</h1>
        <h2 style="text-align:center;color:red">10 concurrency-control sessions per account loing</h2>
        <center id="showarea"></center>
        <div>&nbsp;</div>
        <table border="1" align="center">
            <thead>
                <tr>
                    <th>Account</th>
                    <th>Password</th>
                    <th>Authorities</th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td>staff</td>
                    <td>only</td>
                    <td>ROLE_STAFF</td>
                </tr>
                <tr>
                    <td>admin</td>
                    <td>user</td>
                    <td nowrap>ROLE_ADMIN,ROLE_STAFF</td>
                </tr>
            </tbody>
            <tfoot>
                <tr>
                    <td colSpan="3"><a href="./secured/SecuredDocument.jsp" target="_blank">Try another secured resource.</a></td>
                </tr>
            </tfoot>
        </table>
    </body>
</html>
