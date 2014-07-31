<%@page info="Registration Page" %>
<%@page contentType="text/html" %>
<%@page session="false" %>

<jsp:useBean id="registrationManagement" scope="page" class="bflows.RegistrationManagement" />
<jsp:setProperty name="registrationManagement" property="*" />

<%
    Boolean registration = false;
    String status = request.getParameter("status");

    if (status != null && status.equals("registrato")) {
        registration = true;
        registrationManagement.registration();
        String redirect = new String("home.jsp");
        response.sendRedirect(redirect);
    }

%>

<!DOCTYPE html>
<html>
    <head>
        <%@include file="default.html" %>
        <title>Registrazione Star Cinema</title>
    </head>
    <body>
        <div class="jumbotron">
            <div class="container">
                <% if (!registration) {%>
                <h1>Registrazione</h1>
            </div>
        </div>
        <div class="container">
            <form id="registration" name="registrationForm" method="post" action="registration.jsp">
                <input type="hidden" name="status" value="registrato" />
                <div class="input-group">
                    <span class="label label-info">Inserisci Username</span>
                    <input type="text" class="form-control" name="username" placeholder="Username" required="required"/>
                </div>
                <br/>
                <div class="input-group">
                    <span class="label label-info">Inserisci Password</span>
                    <input type="password" id="password" class="form-control" name="password" placeholder="Password" 
                           required="required"/>
                </div>
                <br/>
                <div class="input-group">
                    <span class="label label-info">Conferma Password</span>
                    <input type="password" id="conf_password" class="form-control" placeholder="Password" 
                           required="required"/>
                </div>
                <br/>
                <div class="input-group">
                    <span class="label label-info">Inserisci Nome</span>
                    <input type="text" class="form-control" name="name" placeholder="Nome" required="required" />
                </div>
                <br/>
                <div class="input-group">
                    <span class="label label-info">Inserisci Cognome</span>
                    <input type="text" class="form-control" name="surname" placeholder="Cognome" required="required" />
                </div>
                <br/>
                <div class="input-group">
                    <span class="label label-info">Inserisci E-mail</span>
                    <input type="email" class="form-control" name="email" placeholder="E-mail" />
                </div>
                <br/>
                <div class="input-group">
                    <span class="label label-info">Inserisci Numero Carta di Credito</span>
                    <input type="number" class="form-control" name="creditcard" placeholder="Carta di Credito"/>
                </div>
                <br/>
                <input type="submit" class="btn btn-primary" value="Registrami"></input>
                <a href="home.jsp" class="btn btn-warning">Annulla</a>
                <br/>
                <br/>
            </form>
            <%}%>
        </div>
<script src="scripts/validation_registration.js"></script>
</body>
</html>
