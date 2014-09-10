<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page isErrorPage="true"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Pagina di Errore</title>
    </head>
    <body>
        <div class="container">
            <p>
                Si è verificato un errore. Si prega di provare a ricaricare la pagina.
                <br/>
                Se il problema persiste contattare un amministratore segnalando il codice seguente
            </p>
            <br/>
            <code>
                <%=exception.getMessage()%>
            </code>
        </div>
    </body>
</html>
