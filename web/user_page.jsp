<%@include file="login_navbar.jsp" %>

<%
    boolean authorized = false;

    if (loginManagement.getCookieValue("username").equals(loginManagement.getUsername()) && status.equals("profile")) {
        authorized = true;
        loginManagement.getUser();
    }

    if (status.equals("edit")) {
        authorized = true;
        loginManagement.updateUser();
    }
%>

<% if (authorized) {%>
<div class="jumbotron">
    <div class="container">
        <h1>Profilo Utente</h1>
    </div>
</div>
<div class="container">
    <br/>
    <div class="row">
        <form class="col-xs-4" action="user_page.jsp" method="post">
            <div class="form-group">
                <label class="control-label">Nome</label>
                <div class="controls">
                    <input type="text" class="form-control" name="name" value="<%=loginManagement.getName()%>" placeholder="Nome" required="required"/>
                </div>
                <br/>
                <label class="control-label">Cognome</label>
                <div class="controls">
                    <input type="text" class="form-control" name="surname" value="<%=loginManagement.getSurname()%>" placeholder="Cognome" required="required"/>
                </div>
                <br/>
                <label class="control-label">Email</label>
                <div class="controls">
                    <input type="email" class="form-control" name="email" value="<%=loginManagement.getEmail()%>" placeholder="Email" required="required"/>
                </div>
                <br/>
                <label class="control-label">Carta di Credito</label>
                <div class="controls">
                    <input type="text" class="form-control" name="creditcard" value="<%=loginManagement.getCreditcard()%>" placeholder="CreditCard" required="required"/>
                </div>
                <br/>
                <input type="hidden" name="status" value="edit"/>
                <input type="hidden" name="username" value="<%=username%>"/>
                <input type="hidden" name="password" value="<%=password%>"/>
                <button type="submit" class="btn btn-primary">Modifica</button>
        </form>
    </div>

    <a href="#">Lista Biglietti Acquistati</a>
    #Abbonamento
    #ingressi disponibili
</div>
<%}%>
</body>
</html>