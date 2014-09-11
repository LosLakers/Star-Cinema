<%@include file="login_navbar.jsp" %>

<%
    boolean authorized = loginBean.authenticate(username, password);

    if (authorized) {
        if (status.equals("edit")) {
            try {
                loginBean.updateUser();
            } catch (Exception ex) {
            }
        }
        loginBean.setUsername(username);
        loginBean.setPassword(password);
        loginBean.getUser();
%>
<div class="jumbotron">
    <div class="container">
        <h1>Profilo Utente</h1>
    </div>
</div>
<div class="container">
    <!-- Gestione Errori Carta di Credito -->
    <% if (status.equals("creditcarderror")) {%>
    <div class="container">
        <div class="alert alert-dismissable alert-danger">
            <button type="button" class="close" data-dismiss="alert" aria-hidden="true">x</button>
            <p class="message">Inserire una carta di credito per effettuare acquisti nel sito</p>
        </div>
    </div>
    <%}%>
    <% if (!loginBean.getMessage().equals("")) {%>
    <!-- Gestione Errori -->
    <div class="container">
        <div class="alert alert-dismissable <%=loginBean.getMessagetype()%>">
            <button type="button" class="close" data-dismiss="alert" aria-hidden="true">x</button>
            <p class="message"><%=loginBean.getMessage()%></p>
        </div>
    </div>
    <%}%>
    <br/>
    <div class="row">
        <!-- Informazioni utente -->
        <form class="col-lg-4 col-md-4" action="user_page.jsp" method="post">
            <div class="form-group">
                <label class="control-label">Nome</label>
                <div class="controls">
                    <input type="text" class="form-control" name="name" placeholder="Nome" required="required" 
                           value="<%=loginBean.getName()%>"/>
                </div>
                <br/>
                <label class="control-label">Cognome</label>
                <div class="controls">
                    <input type="text" class="form-control" name="surname" placeholder="Cognome" required="required" 
                           value="<%=loginBean.getSurname()%>"/>
                </div>
                <br/>
                <label class="control-label">Email</label>
                <div class="controls">
                    <input type="email" class="form-control" name="email" placeholder="Email" required="required" 
                           value="<%=loginBean.getEmail()%>"/>
                </div>
                <br/>
                <label class="control-label">Carta di Credito</label>
                <div class="controls">
                    <input type="number" class="form-control" name="creditcard" placeholder="CreditCard" 
                           value="<%=loginBean.getCreditcard()%>"/>
                </div>
                <br/>
                <input type="hidden" name="status" value="edit"/>
                <button type="submit" class="btn btn-primary">Modifica</button>
            </div>
        </form>
        <div class="col-lg-4 col-md-4">
            <!-- Gestione biglietti -->
            <a href="ticketlist.jsp" class="btn btn-default">Lista Ingressi</a>
        </div>
        <div class="col-lg-4">
            <!-- Gestione abbonamento -->
            <table class="table">
                <thead>
                    <tr>
                        <th>Abbonamento</th>
                    </tr>
                </thead>
                <tbody>
                    <% if (loginBean.getSubscriptionticket() == -1) {%>
                    <tr>
                        <td>
                            <a href="addsubscription.jsp" class="btn btn-default">Acquista</a>
                        </td>
                    </tr>
                    <%} else if (loginBean.getSubscriptionticket() == 0) {%>
                    <tr>
                        <td>0 Ingressi Disponibili</td>
                        <td>
                            <a href="addsubscription.jsp" class="btn btn-default">Rinnova</a>
                        </td>
                    </tr>
                    <%} else {%>
                    <tr>
                        <td><%=loginBean.getSubscriptionticket()%> Ingressi Disponibili</td>
                    </tr>
                    <%}%>
                </tbody>
            </table>
        </div>
    </div>
</div>
<%
    } else {
        String redirect = "home.jsp";
        response.sendRedirect(redirect);
    }
%>
</body>
</html>