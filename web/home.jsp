<%@include file="login_navbar.jsp" %>

<jsp:useBean id="ticketBean" scope="page" class="bflows.TicketManagement" />

<%
    ticketBean.index();
    int[] id_film = ticketBean.FilmDate_IdFilm();
    String[] week = ticketBean.getWeek();
%>

<div class="jumbotron">
    <div class="container">
        <h1>Star(k) Cinema Home</h1>
    </div>
</div>
<div class="container">
    <!-- Acquisto Biglietti -->
    <div>
        <form id="ticketfilm" class="form-inline" action="addtickettime.jsp" method="get">
            <!-- Selezione film -->
            <div class="col-lg-4 col-md-4">
                <select name="id_film" class="form-control" required="required">
                    <option selected="selected" disabled="true">Acquista Biglietto per un Film...</option>
                    <%
                        for (int j = 0; j < id_film.length; j++) {
                            String titolo = ticketBean.FilmDate_Titolo(id_film[j]);
                    %>
                    <option value="<%=id_film[j]%>"><%=titolo%></option>
                    <%}%>
                </select>
            </div>
            <!-- Selezione data -->
            <div class="col-lg-3 col-md-3">
                <select name="data" class="form-control" required="required" disabled="disabled">
                    <option selected="selected" disabled="true">Seleziona una data...</option>
                    <%
                        for (int j = 0; j < week.length; j++) {
                    %>
                    <option value="<%=week[j]%>"><%=week[j]%></option>
                    <%}%>
                </select>
            </div>
            <button type="submit" form="ticketfilm" class="form-group btn btn-primary">Conferma</button>
        </form>
        <!-- Creazione hidden -->
        <%
            for (int j = 0; j < id_film.length; j++) {
                String[] data = ticketBean.FilmDate_Date(id_film[j]);
                String hidden = "";
                for (int p = 0; p < week.length; p++) {
                    Boolean check = true;
                    for (String tmp : data) {
                        if (tmp.equals(week[p])) {
                            check = false;
                            break;
                        }
                    }
                    if (check) {
                        hidden += (p + 1) + ":";
                    }
                }
        %>
        <input type="hidden" id="<%=id_film[j]%>" value="<%=hidden%>"/>
        <%}%>
    </div>
    <br/>
    <!-- Film in Programmazione -->
    <%
        int elements = 5;
        int list = id_film.length;
        int num_page = list % elements == 0 ? list / elements : list / elements + 1;
        if (num_page == 0) {
            num_page = 1;
        }
        String[] pages = new String[num_page];
        pages[0] = "page1";
        for (int j = 1; j < num_page; j++) {
            pages[j] = "page" + (j + 1);
        }
    %>
    <legend>Questa Settimana</legend>
    <!-- Tab panes -->
    <div class="tab-content">
        <%
            String active = "";
            for (int j = 0; j < num_page; j++) {
                if (j == 0) {
                    active = "active";
                } else {
                    active = "";
                }
        %>
        <div class="tab-pane <%=active%>" id="<%=pages[j]%>">
            <%
                int count;
                if (list <= elements) {
                    count = list;
                } else {
                    count = elements;
                    list -= elements;
                }
                for (int i = 0; i < count; i++) {
                    String locandina = ticketBean.FilmDate_Locandina(id_film[i + (j * elements)]);
                    String href = "film.jsp?id_film=" + id_film[i + (j * elements)];
            %>
            <a href="<%=href%>"><img src="images/<%=locandina%>"/></a>
                <%}%>
        </div>
        <%}%>
    </div>
    <div class="row">
        <!-- Pagination -->
        <ul class="pagination">
            <li class="active">
                <a href="#page1" data-toggle="tab">1 <span class="sr-only">(current)</span></a>
            </li>
            <% for (int i = 1; i < num_page; i++) {%>
            <li>
                <a href="#<%=pages[i]%>" data-toggle="tab"><%=i + 1%> <span class="sr-only">(current)</span></a>
            </li>
            <%}%>
        </ul>
    </div>
    <br/>
    <br/>
    <div>
        <legend>Ricerca Film</legend>
        <!-- Ricerca Film per Titolo -->
        <form id="filmsearch" action="search.jsp" class="col-lg-4 col-md-4">
            <div class="form-group">
                <label class="control-label">Ricerca per Titolo</label>
                <div class="controls">
                    <input type="text" name="searchString" class="form-control" placeholder="Titolo" required="required"/>
                </div>
            </div>
            <button type="submit" form="filmsearch" class="form-group btn btn-default">Cerca</button>
        </form>
        <!-- Ricerca Film per Data -->
        <form id="datasearch" action="search.jsp" class="col-lg-4 col-md-4">
            <div class="form-group">
                <label class="control-label">Ricerca per Data</label>
                <div class="controls">
                    <input type="date" name="searchString" class="form-control" placeholder="Data" required="required"/>
                </div>
            </div>
            <button type="submit" form="datasearch" class="btn btn-default">Cerca</button>
        </form>
    </div>
</div>
<script src="scripts/ticket.js"></script>
</body>
</html>