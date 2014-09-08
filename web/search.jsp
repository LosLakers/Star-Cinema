<%@include file="login_navbar.jsp" %>

<jsp:useBean id="filmBean" scope="page" class="bflows.FilmManagement"/>
<jsp:setProperty name="filmBean" property="searchString"/>

<%
    String searchString = request.getParameter("searchString");
    if (searchString == null) {
        filmBean.index();
    } else {
        filmBean.search();
    }
    if (status.equals("deletefilm") && isAdmin) {
%>
<jsp:setProperty name="filmBean" property="id_film"/>
<%
        filmBean.deleteFilm();
        String redirect = "search.jsp";
        response.sendRedirect(redirect);
    }
%>

<div class="jumbotron">
    <div class="container">
        <!-- Gestione Ricerca -->
        <form id="filmsearch" class="col-lg-6">
            <div class="form-group">
                <label class="control-label">Ricerca Film per Titolo</label>
                <div class="controls">
                    <input type="text" name="searchString" class="form-control" placeholder="Titolo" required="required"/>
                </div>
            </div>
            <button type="submit" form="filmsearch" class="btn btn-default">Cerca</button>
        </form>
        <form id="datasearch" class="col-lg-6">
            <div class="form-group">
                <label class="control-label">Ricerca Film per Data</label>
                <div class="controls">
                    <input type="date" name="searchString" class="form-control" placeholder="Data" required="required"/>
                </div>
            </div>
            <button type="submit" form="datasearch" class="btn btn-default">Cerca</button>
        </form>
    </div>
</div>
<div class="container">
    <% if (searchString != null) {%>
    <h4>Risultati per ricerca: <strong><%=searchString%></strong></h4>
    <br/>
    <%}%>
    <!-- Tabella Risultati -->
    <table class="table table-striped">
        <thead>
            <tr>
                <% if (isAdmin) {%>
                <th class="col-lg-1 col-md-1">Elimina</th>
                    <%}%>
                <th>Titolo</th>
                    <% if (isAdmin) {%>
                <th>Azioni disponibili</th>
                    <%}%>
            </tr>
        </thead>
        <tbody>
            <%
                int film_num = filmBean.filmList_length();
                for (int j = 0; j < film_num; j++) {
                    String film_tit = filmBean.filmList_titolo(j);
                    int id_film = filmBean.filmList_idfilm(j);
                    String href = "film.jsp?id_film=" + filmBean.filmList_idfilm(j);
                    String show = "addshow.jsp?id_film=" + filmBean.filmList_idfilm(j);
            %>
            <tr>
                <% if (isAdmin) {%>
                <td class="col-lg-1 col-md-1">
                    <input type="checkbox" name="film_delete" value="<%=id_film%>" />
                </td>
                <%}%>
                <td>
                    <a href="<%=href%>"><%=film_tit%></a>
                </td>
                <% if (isAdmin) {%>
                <td>
                    <div class="row">
                        <form class="col-lg-2 col-md-2" action="updatefilm.jsp" method="get">
                            <input type="hidden" name="id_film" value="<%=id_film%>" />
                            <button type="submit" class="btn btn-primary">Modifica</button>
                        </form>
                        <form class="col-lg-2 col-md-2" action="search.jsp" method="get">
                            <input type="hidden" name="id_film" value="<%=id_film%>" />
                            <input type="hidden" name="status" value="deletefilm" />
                            <button type="submit" class="btn btn-danger">Elimina</button>
                        </form>
                        <a href="<%=show%>" class="btn btn-default">Aggiungi in Programmazione</a>
                    </div>
                </td>
                <%}%>
            </tr>
            <%}%>
        </tbody>
    </table>
    <% if (isAdmin) {%>
    <form id="multideletefilm">
        <button type="submit" form="multideletefilm" class="btn btn-danger">Elimina</button>
    </form>
    <script src="scripts/utility.js"></script>
    <script src="scripts/searchfilm_script.js"></script>
    <%}%>
</div>
</body>
</html>