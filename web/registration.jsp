<%@page info="Registration Page" %>
<%@page contentType="text/html" %>
<%@page session="false" %>
<%@page errorPage="errorpage.jsp" %>

<jsp:useBean id="loginBean" scope="page" class="bflows.LoginManagement" />
<jsp:setProperty name="loginBean" property="*" />

<%
    String status = request.getParameter("status");

    if (status != null && status.equals("registrato")) {
        try {
            loginBean.addUser();
            String redirect = "home.jsp";
            response.sendRedirect(redirect);
        } catch (Exception ex) {
        }
    }
%>

<!DOCTYPE html>
<html>
    <head>
        <%@include file="default.html" %>
        <link href="css/custom-theme.css" rel="stylesheet">
        <title>Registrazione Star Cinema</title>
    </head>
    <body>
        <div class="container">
            <legend>Registrazione a Star Cinema</legend>
            <p class="text-info">
                Modulo di registrazione al sito Star Cinema. Ogni informazione è necessaria
                per potersi registrare al sito, eccetto la carta di credito che può essere
                inserita in un secondo momento.
                Si ricorda che per fare qualunque acquisto nel sito, è necessario aver
                registrato una carta di credito.
            </p>
        </div>
        <% if (!loginBean.getMessage().equals("")) {%>
        <!-- Gestione Errori -->
        <div class="container">
            <div class="alert alert-dismissable <%=loginBean.getMessagetype()%>">
                <button type="button" class="close" data-dismiss="alert" aria-hidden="true">x</button>
                <p class="message"><%=loginBean.getMessage()%></p>
            </div>
        </div>
        <%}%>
        <div class="container">
            <form id="registration" class="col-lg-4 col-md-4" name="registrationForm" method="post" action="registration.jsp">
                <input type="hidden" name="status" value="registrato" />
                <div class="form-group">
                    <%
                        String username = loginBean.getUsername() != null ? loginBean.getUsername() : "";
                    %>
                    <label class="control-label">Inserisci Username</label>
                    <input type="text" class="form-control" name="username" placeholder="Username" 
                           value="<%=username%>" required="required"/>
                </div>
                <br/>
                <div class="form-group">
                    <label class="control-label">Inserisci Password</label>
                    <input type="password" class="form-control" name="password" placeholder="Password" 
                           required="required"/>
                </div>
                <br/>
                <div class="form-group has-feedback">
                    <label class="control-label">Conferma Password</label>
                    <input type="password" class="form-control" name="conf_password" placeholder="Password" 
                           required="required"/>
                    <span class="glyphicon form-control-feedback"></span>
                </div>
                <br/>
                <div class="form-group">
                    <%
                    String name = loginBean.getName() != null ? loginBean.getName() : "";
                    %>
                    <label class="control-label">Inserisci Nome</label>
                    <input type="text" class="form-control" name="name" placeholder="Nome" 
                           value="<%=name%>" required="required" />
                </div>
                <br/>
                <div class="form-group">
                    <%
                        String surname = loginBean.getSurname() != null ? loginBean.getSurname() : "";
                    %>
                    <label class="control-label">Inserisci Cognome</label>
                    <input type="text" class="form-control" name="surname" placeholder="Cognome" 
                           value="<%=surname%>" required="required" />
                </div>
                <br/>
                <div class="form-group">
                    <%
                        String email = loginBean.getEmail() != null ? loginBean.getEmail() : "";
                    %>
                    <label class="control-label">Inserisci E-mail</label>
                    <input type="email" class="form-control" name="email" placeholder="E-mail"  
                           value="<%=email%>" required="required"/>
                </div>
                <br/>
                <div class="form-group">
                    <%
                        String creditcard = loginBean.getCreditcard() != null ? loginBean.getCreditcard() : "";
                        %>
                    <label class="control-label">Inserisci Numero Carta di Credito</label>
                    <input type="number" class="form-control" name="creditcard" placeholder="Carta di Credito"
                           value="<%=creditcard%>"/>
                </div>
                <br/>
                <input type="submit" class="btn btn-primary" value="Registrami"></input>
                <a href="home.jsp" class="btn btn-warning">Annulla</a>
                <br/>
                <br/>
            </form>
        </div>
        <script src="scripts/validation_registration.js"></script>
        <script src="scripts/utility.js"></script>
    </body>
</html>
