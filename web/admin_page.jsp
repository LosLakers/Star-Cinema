<%@include file="login_navbar.jsp" %>

<%
    boolean authorized = loginBean.authenticate(username, password);

    if (status.equals("edit") && authorized) {
        loginBean.updateUser();
    }
%>

<%
    if (authorized) {
        loginBean.setUsername(username);
        loginBean.setPassword(password);
        loginBean.getUser();
%>
<div class="jumbotron">
    <div class="container">
        <h1>Profilo Admin</h1>
    </div>
</div>
<div class="container">
    <br/>
    <div class="row">
        <!-- Modifica Profilo -->
        <form class="col-lg-4 col-md-4">
            <div class="form-group">
                <label class="control-label">Nome</label>
                <div class="controls">
                    <input type="text" class="form-control" name="name" required="required" 
                           value="<%=loginBean.getName()%>"/>
                </div>
                <br/>
                <label class="control-label">Cognome</label>
                <div class="controls">
                    <input type="text" class="form-control" name="surname" required="required"
                           value="<%=loginBean.getSurname()%>"/>
                </div>
                <br/>
                <label class="control-label">Email</label>
                <div class="controls">
                    <input type="email" class="form-control" name="email" required="required" 
                           value="<%=loginBean.getEmail()%>"/>
                </div>
                <br/>
                <label class="control-label">Carta di Credito</label>
                <div class="controls">
                    <input type="number" class="form-control" name="creditcard" placeholder="CreditCard" required="required" 
                           value="<%=loginBean.getCreditcard()%>"/>
                </div>
                <br/>
                <input type="hidden" name="status" value="edit"/>
                <input type="submit" class="btn btn-primary" value="Aggiorna Profilo">
            </div>
        </form>
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
            <!-- Link alla lista ticket -->
            <a href="ticketlist.jsp" class="btn btn-default">Lista Ingressi</a>
        </div>
        <!-- Opzioni Admin -->
        <div class="col-lg-4 col-md-4 ">
            <table class="table">
                <thead>
                    <tr>
                        <th>Opzioni Admin</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>
                            <a href="addfilm.jsp" class="btn btn-default">Aggiungi Film</a>  
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <a href="situationtheater.jsp" class="btn btn-default">Situazione Sale</a>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <a href="situationdate.jsp" class="btn btn-default">Situazione Date</a>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
    <br/>
</div>
<%
    } else {
        String redirect = "home.jsp";
        response.sendRedirect(redirect);
    }
%>
</body>
</html>
