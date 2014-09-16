<%@include file="login_navbar.jsp" %>

<jsp:useBean id="nowShowingBean" scope="page" class="bflows.NowShowingManagement"/>

<%
    nowShowingBean.populate();
%>

<div class="jumbotron">
    <div class="container">
        <% if (isAdmin) {%>
        <p><strong>Admin</strong> cliccando sull'orario, è possibile modificarne la programmazione dello spettacolo</p>
        <%}%>
    </div>
</div>
<div class="container">
    <legend>Programmazione Settimanale Star Cinema</legend>
    <!-- Navbar -->
    <ul class="nav nav-tabs nav-justified">
        <%
            String week[] = nowShowingBean.getWeek();
            for (int j = 0; j < week.length; j++) {
                String href = "#" + week[j];
                if (j == 0) {
        %>
        <li class="active">
            <a href="<%=href%>" data-toggle="tab"><%=week[j]%></a>    
        </li>
        <%
        } else {
        %>
        <li>
            <a href="<%=href%>" data-toggle="tab"><%=week[j]%></a>
        </li>
        <%
                }
            }
        %>
    </ul>
    <!-- Tab Content -->
    <div class="tab-content">
        <%
            for (int j = 0; j < week.length; j++) {
                String active = "";
                if (j == 0) {
                    active = "active";
                }
        %>
        <div class="tab-pane <%=active%>" id="<%=week[j]%>">
            <table class="table table-striped table-hover">
                <thead>
                    <tr class="success">
                        <th>Film</th>
                        <th>Orari</th>
                        <th>Sala - Posti Disponibili</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        int num_film = nowShowingBean.FilmTheaterDate_Films();
                        for (int p = 0; p < num_film; p++) {
                            if (nowShowingBean.FilmTheaterDate_Show(p, week[j])) {
                                String titolo = nowShowingBean.FilmTheaterDate_Titolo(p);
                                String href = "film.jsp?id_film=" + nowShowingBean.FilmTheaterDate_IdFilm(p);
                    %>
                    <tr>
                        <td>
                            <a href="<%=href%>"><%=titolo%></a>
                        </td>
                        <td>
                            <%
                                String[] oraInizio = nowShowingBean.FilmTheaterDate_OraInizio(p, week[j]);
                                String[] oraFine = nowShowingBean.FilmTheaterDate_OraFine(p, week[j]);
                                int[] id_tabella = nowShowingBean.FilmTheaterDate_IdTabella(p, week[j]);
                                for (int q = 0; q < oraInizio.length; q++) {
                                    if (isAdmin) {
                            %>
                            <form class="col-lg-4 col-md-4" action="updateshow.jsp" method="post">
                                <input type="hidden" name="id_film" value="<%=nowShowingBean.FilmTheaterDate_IdFilm(p)%>" />
                                <input type="hidden" name="id_tabella" value="<%=id_tabella[q]%>" />
                                <a href="#" class="submit-link"><%=oraInizio[q]%>-<%=oraFine[q]%></a>
                                ||
                            </form>
                            <%
                            } else {
                            %>
                            <%=oraInizio[q]%>-<%=oraFine[q]%> <->
                            <%
                                    }
                                }
                            %>
                        </td>
                        <td>
                            <%
                                Integer[] num_sale = nowShowingBean.FilmTheaterDate_Sale(p, week[j]);
                                Integer[] posti = nowShowingBean.FilmTheaterDate_Posti(p, week[j]);
                                for (int q = 0; q < num_sale.length; q++) {
                            %>
                            Sala <%=num_sale[q]%> - <%=posti[q]%> || 
                            <%}%>
                        </td>
                    </tr>
                    <%
                            }
                        }
                    %>
                </tbody>
            </table>
        </div>
        <%}%>
    </div>
    <% if (isAdmin) {%>
    <br/>
    <div class="col-lg-8 col-md-8">
        <!-- Aggiunta in programmazione se Admin-->
        <%
            nowShowingBean.index();
            String[] films = nowShowingBean.getFilms();
        %>
        <form id="addshowform" class="form-inline" action="addshow.jsp" method="post">
            <select name="id_film" class="form-control" required="required">
                <option selected="selected" disabled="true">Scegli un Film per Programmazione...</option>
                <%
                    for (int j = 0; j < films.length; j++) {
                        String[] film = films[j].split("_");
                %>
                <option value="<%=film[0]%>"><%=film[1]%></option>
                <%}%>
            </select>
            <button form="addshowform" class="btn btn-primary">Conferma</button>
        </form>
    </div>
    <%}%>
</div>
<% if (isAdmin) {%>
<script src="scripts/starcinema_utility.js"></script>
<script src="scripts/utility.js"></script>
<%}%>
</body>
</html>