<%@include file="login_navbar.jsp" %>

<jsp:useBean id="ticketBean" scope="page" class="bflows.TicketManagement" />

<%
    ticketBean.index();
    int[] id_film = ticketBean.ticketId_film();
    String[] week = ticketBean.getWeek();
%>

<div class="jumbotron">
    <div class="container">
        <h1>Star(k) Cinema Home</h1>
        <br/>
        <!-- Acquisto Biglietti -->
        <div class="col-lg-12 col-md-12">
            <form id="ticketfilm" class="form-inline" action="addtickettime.jsp" method="get">
                <!-- Selezione film -->
                <div class="col-lg-4 col-md-4">
                    <select name="id_film" class="form-control" required="required">
                        <option selected="selected" disabled="true">Acquista Biglietto per un Film...</option>
                        <%
                            for (int j = 0; j < id_film.length; j++) {
                                String titolo = ticketBean.ticketTitolo(id_film[j]);
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
                <button type="submit" form="ticketfilm" class="form-group btn btn-default">Conferma</button>
            </form>
            <!-- Creazione hidden -->
            <%
                for (int j = 0; j < id_film.length; j++) {
                    String[] data = ticketBean.ticketDate(id_film[j]);
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
    </div>
</div>
<div class="container">
    <br/>
    <div class="col-lg-12 col-md-12">
        <!-- Ricerca Film per Titolo -->
        <form id="filmsearch" action="search.jsp" class="col-lg-6 col-md-6">
            <div class="form-group">
                <label class="control-label">Ricerca Film per Titolo</label>
                <div class="controls">
                    <input type="text" name="searchString" class="form-control" placeholder="Titolo" required="required"/>
                </div>
            </div>
            <button type="submit" form="filmsearch" class="form-group btn btn-default">Cerca</button>
        </form>
        <!-- Ricerca Film per Data -->
        <form id="datasearch" action="search.jsp" class="col-lg-6">
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
<script src="scripts/ticket.js"></script>
</body>
</html>