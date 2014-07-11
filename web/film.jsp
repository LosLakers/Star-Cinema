<%@include file="login_navbar.jsp" %>
<jsp:useBean id="filmManagement" scope="page" class="bflows.FilmManagement"/>
<jsp:setProperty name="filmManagement" property="titolo"/>

<% filmManagement.getFilm(); %>

<div class="jumbotron">
    <div class="row">
        <div class="col-sm-offset-1 col-lg-5">
            <h1><%=filmManagement.getTitolo()%></h1>
            <% if (isAdmin) {%>
            <form id="adminForm" action="addedit_film.jsp">
                <input type="hidden" name="titolo" value="<%=filmManagement.getTitolo()%>"/>
                <input type="hidden" name="status" value="edit"/>
                <button type="submit" class="btn btn-success">Modifica</button>
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
</div>
</body>
</html>
