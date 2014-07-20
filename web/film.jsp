<%@include file="login_navbar.jsp" %>
<jsp:useBean id="filmManagement" scope="page" class="bflows.FilmManagement"/>
<jsp:setProperty name="filmManagement" property="id_film"/>

<% 
    filmManagement.getFilm();
    
    if (status.equals("commento")) {
%>
<jsp:setProperty name="filmManagement" property="*"/>
<%
        filmManagement.addComment();
    }
%>

<div class="jumbotron">
    <div class="row">
        <div class="col-sm-offset-1 col-lg-5">
            <h1><%=filmManagement.getTitolo()%></h1>
            <% if (isAdmin) {%>
            <form id="adminForm" action="addedit_film.jsp">
                <input type="hidden" name="id_film" value="<%=filmManagement.getId_film()%>"/>
                <input type="hidden" name="status" value="edit"/>
                <button form="adminForm" type="submit" class="btn btn-success">Modifica</button>
            </form>
            <%}%>    
        </div>
        <div class="col-lg-6">
            <h2>Programmazione</h2>
            <table class="table">
                <thead>
                    <tr>
                        <th>Data</th>
                        <th>Orari</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>Oggi</td>
                        <td>9:00 - 12:00</td>
                    </tr>
                    <tr>
                        <td>Domani</td>
                        <td>10:00 - 13:00</td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>
<div class="container">
    <!-- Example row of columns -->
    <div class="row">
        <div class="col-lg-4">
            <h2>Locandina</h2>
            <img src="<%=filmManagement.getLocandina()%>"/>
        </div>
        <div class="col-lg-4">
            <h2>Descrizione</h2>
            <p><%=filmManagement.getDescrizione()%></p>
        </div>
        <div class="col-lg-4">
            <h3>Durata</h3>
            <%=filmManagement.getDurata()%>
            <br/>
            <br/>
            <a href="<%=filmManagement.getTrailer()%>" class="btn btn-default">Guarda il trailer</a>
        </div>
    </div>
    <% if (!username.equals("")) {%>    
    <div class="row">
        <form id="commento" method="post" action="film.jsp" class="col-lg-6">
            <h3>Inserisci Commento</h3>
            <br/>
            <div class="inline-block">
                <div class="glyphicon glyphicon-star btn btn-success inline-block"></div>
                <div class="glyphicon glyphicon-star btn btn-default inline-block"></div>
                <div class="glyphicon glyphicon-star btn btn-default inline-block"></div>
                <div class="glyphicon glyphicon-star btn btn-default inline-block"></div>
                <div class="glyphicon glyphicon-star btn btn-default inline-block"></div>
            </div>
            <br/>
            <textarea class="form-control" name="giudizio" rows="5" required="required"></textarea>
            <br/>
            <input type="hidden" name="voto" value="1"/>
            <input type="hidden" name="id_film" value="<%=filmManagement.getId_film()%>"/>
            <input type="hidden" name="user" value="<%=username%>"/>
            <input type="hidden" name="status" value="commento"/>
            <button form="commento" type="submit" class="btn btn-success">Conferma</button>
        </form>
    </div>
    <%}%>
    <br/>
</div>
</body>
</html>
