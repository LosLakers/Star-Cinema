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
        <div class="jumbotron">
            <div class="container">
                <h1>Registrazione</h1>
            </div>
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
            <form id="registration" name="registrationForm" method="post" action="registration.jsp">
                <input type="hidden" name="status" value="registrato" />
                <div class="input-group">
                    <%
                        String username = loginBean.getUsername() != null ? loginBean.getUsername() : "";
                    %>
                    <span class="label label-info">Inserisci Username</span>
                    <input type="text" class="form-control" name="username" placeholder="Username" 
                           value="<%=username%>" required="required"/>
                </div>
                <br/>
                <div class="input-group">
                    <span class="label label-info">Inserisci Password</span>
                    <input type="password" class="form-control" name="password" placeholder="Password" 
                           required="required"/>
                </div>
                <br/>
                <div class="input-group">
                    <span class="label label-info">Conferma Password</span>
                    <input type="password" class="form-control" name="conf_password" placeholder="Password" 
                           required="required"/>
                </div>
                <br/>
                <div class="input-group">
                    <%
                    String name = loginBean.getName() != null ? loginBean.getName() : "";
                    %>
                    <span class="label label-info">Inserisci Nome</span>
                    <input type="text" class="form-control" name="name" placeholder="Nome" 
                           value="<%=name%>" required="required" />
                </div>
                <br/>
                <div class="input-group">
                    <%
                        String surname = loginBean.getSurname() != null ? loginBean.getSurname() : "";
                    %>
                    <span class="label label-info">Inserisci Cognome</span>
                    <input type="text" class="form-control" name="surname" placeholder="Cognome" 
                           value="<%=surname%>" required="required" />
                </div>
                <br/>
                <div class="input-group">
                    <%
                        String email = loginBean.getEmail() != null ? loginBean.getEmail() : "";
                    %>
                    <span class="label label-info">Inserisci E-mail</span>
                    <input type="email" class="form-control" name="email" placeholder="E-mail"  
                           value="<%=email%>" required="required"/>
                </div>
                <br/>
                <div class="input-group">
                    <%
                        String creditcard = loginBean.getCreditcard() != null ? loginBean.getCreditcard() : "";
                        %>
                    <span class="label label-info">Inserisci Numero Carta di Credito</span>
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
