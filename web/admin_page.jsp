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
    <div class="row">
        <div class="col-md-4">
            <label>Situazione Sale in Data Odierna</label><br>
            <ul class="nav nav-tabs">
                <li class="active">
                    <a href="#">Sala 1</a>
                </li>
                <li>
                    <a href="#">Sala 2</a>
                </li>
                <li>
                    <a href="#">Sala 3</a>
                </li>
                <li>
                    <a href="#">Sala 4</a>
                </li>
            </ul>
            <table class="table table-hover">
                <thead>
                    <tr>
                        <th>Orario</th>
                        <th>Posti Disponibili</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>9:00</td>
                        <td>200</td>
                    </tr>
                    <tr>
                        <td>11:00</td>
                        <td>200</td>
                    </tr>
                </tbody>
            </table>
        </div>
        <div class="col-xs-push-2 col-md-8">
            <label>Situazione Date della Settimana</label><br>
            <ul class="nav nav-tabs">
                <li class="active">
                    <a href="#">Data 1</a>
                </li>
                <li>
                    <a href="#">Data 2</a>
                </li>
                <li>
                    <a href="#">Data 3</a>
                </li>
                <li>
                    <a href="#">Data 4</a>
                </li>
                <li>
                    <a href="#">Data 5</a>
                </li>
                <li>
                    <a href="#">Data 6</a>
                </li>
                <li>
                    <a href="#">Data 7</a>
                </li>
            </ul>
            <table class="table table-hover">
                <thead>
                    <tr>
                        <th>Orario</th>
                        <th>Sala 1</th>
                        <th>Sala 2</th>
                        <th>Sala 3</th>
                        <th>Sala 4</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>9:00</td>
                        <td>
                            <a href="#">Film1</a>
                        </td>
                        <td>
                            <a href="#">Film2</a>
                        </td>
                        <td>
                            <a href="#">Film3</a>
                        </td>
                        <td>
                            <a href="#">Film4</a>
                        </td>
                    </tr>
                    <tr>
                        <td>2</td>
                        <td>Jake</td>
                    </tr>
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
