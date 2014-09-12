<%@page isErrorPage="true" import="java.io.*" contentType="text/html"%>
<%@page session="false" %>

<html>
    <head>
        <%@include file="default.html" %>
        <title>Pagina di Errore</title>
    </head>
    <body>
        <div class="jumbotron">
            <div class="container">
                <p>
                    Si è verificato un errore. Si prega di provare a ricaricare la pagina.
                    <br/>
                    Se il problema persiste contattare un amministratore segnalando il codice seguente
                </p>
            </div>
        </div>
        <div class="container">
            <br/>
            <legend>StackTrace</legend>
            <br/>
            <%
                StringWriter stringWriter = new StringWriter();
                PrintWriter printWriter = new PrintWriter(stringWriter);
                exception.printStackTrace(printWriter);
                out.println(stringWriter);
                printWriter.close();
                stringWriter.close();
            %>
        </div>
    </body>
</html>
