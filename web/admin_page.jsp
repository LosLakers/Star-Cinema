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
        <h1>Profilo Admin</h1>
    </div>
</div>
<div class="container">
    <br/>
    <div class="row">
        <form class="col-xs-4">
            <div class="form-group">
                <label class="control-label">Nome</label>
                <div class="controls">
                    <input type="text" class="form-control" name="name" required="required" 
                           value="<%=loginManagement.getName()%>"/>
                </div>
                <br/>
                <label class="control-label">Cognome</label>
                <div class="controls">
                    <input type="text" class="form-control" name="surname" required="required"
                           value="<%=loginManagement.getSurname()%>"/>
                </div>
                <br/>
                <label class="control-label">Email</label>
                <div class="controls">
                    <input type="email" class="form-control" name="email" required="required" 
                           value="<%=loginManagement.getEmail()%>"/>
                </div>
                <br/>
                <label class="control-label">Carta di Credito</label>
                <div class="controls">
                    <input type="number" class="form-control" name="creditcard" placeholder="CreditCard" required="required" 
                           value="<%=loginManagement.getCreditcard()%>"/>
                </div>
                <br/>
                <input type="hidden" name="status" value="edit"/>
                <input type="hidden" name="username" value="<%=username%>"/>
                <input type="hidden" name="password" value="<%=password%>"/>
                <input type="submit" class="btn btn-primary" value="Aggiorna Profilo">
            </div>
        </form>
        <div class="col-xs-4 col-xs-offset-4">
            <table class="table table-hover">
                <thead>
                    <tr>
                        <th>Titolo</th>
                        <th>Azioni</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>
                            <a href="film.jsp?id_film=1">Iron Man 3</a>
                        </td>
                        <td>
                            <a href="addedit_film.jsp?id_film=1&status=edit" class="badge">Modifica</a>
                            <a href="#" class="badge">Elimina</a> 
                        </td>
                    </tr>
                    <tr>
                        <td>2</td>
                        <td>Jake</td>
                    </tr>
                </tbody>
            </table>
            <a href="addedit_film.jsp" class="btn btn-primary">Aggiungi Film</a>
        </div>
    </div><br>
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
<%}%>
</body>
</html>
