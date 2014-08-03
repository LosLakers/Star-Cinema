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
%>

<div class="jumbotron">
    <div class="container">
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
    <table class="table">
        <thead>
            <tr>
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
                    String href = "film.jsp?id_film=" + filmBean.filmList_idfilm(j);
                    String modify = "addedit_film.jsp?status=edit&id_film=" + filmBean.filmList_idfilm(j);
                    String delete = "addedit_film.jsp?status=delete&id_film=" + filmBean.filmList_idfilm(j);
                    String show = "slotsala.jsp?id_film=" + filmBean.filmList_idfilm(j);
            %>
            <tr>
                <td>
                    <a href="<%=href%>"><%=film_tit%></a>
                </td>
                <% if (isAdmin) {%>
                <td>
                    <a href="<%=modify%>">Modifica</a> -
                    <a href="<%=delete%>">Elimina</a> -
                    <a href="<%=show%>">Aggiungi in Programmazione</a>
                </td>
                <%}%>
            </tr>
            <%}%>
        </tbody>
    </table>
</div>
</body>
</html>