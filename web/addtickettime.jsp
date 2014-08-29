<%@include file="login_navbar.jsp" %>

<%
    boolean authorized = loginBean.authenticate(username, password);
    if (authorized) {
%>

<jsp:useBean id="ticketBean" scope="page" class="bflows.TicketManagement"/>
<jsp:setProperty name="ticketBean" property="*"/>

<%
    ticketBean.index();
    int[] id_film = ticketBean.ticketId_film();
    String[] week = ticketBean.getWeek();
%>
<div class="jumbotron">
    <div class="container">
        <!-- Form per cambio film e/o data -->
        <form id="ticketfilm" class="form-inline" action="addtickettime.jsp" method="post">
            <!-- Selezione film -->
            <div class="col-lg-4 col-md-4">
                <select name="id_film" class="form-control" required="required" disabled="disabled">
                    <%
                        for (int j = 0; j < id_film.length; j++) {
                            String titolo = ticketBean.ticketTitolo(id_film[j]);
                            String selected = "";
                            if (id_film[j] == ticketBean.getId_film()) {
                                selected = "selected=\"selected\"";
                            }
                    %>
                    <option value="<%=id_film[j]%>" <%=selected%>><%=titolo%></option>
                    <%}%>
                </select>
            </div>
            <!-- Selezione data -->
            <div class="col-lg-3 col-md-3">
                <select name="data" class="form-control" required="required" disabled="disabled">
                    <option disabled="true">Seleziona una data...</option>
                    <%
                        for (int j = 0; j < week.length; j++) {
                            String selected = "";
                            if (week[j].equals(ticketBean.getData())) {
                                selected = "selected=\"selected\"";
                            }
                    %>
                    <option value="<%=week[j]%>" <%=selected%>><%=week[j]%></option>
                    <%}%>
                </select>
            </div>
            <a id="unlock" class="btn btn-default">Modifica</a>
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
<div class="container">
    <%
        String[] orari = new String[0];
        try {
            orari = ticketBean.populateTime();
        } catch (Exception ex) {
            String redirect = "home.jsp";
            response.sendRedirect(redirect);
        }
        int[] id_tabella = ticketBean.getId_tab();
    %>
    <!-- Form per la selezione di orario e sala -->
    <form id="timeform" action="addticket.jsp" method="post">
        <legend>Seleziona una fascia oraria</legend>
        <%
            for (int j = 0; j < orari.length; j++) {
                String disabled = "";
                int occupied = orari[j].lastIndexOf("Posti 0");
                if (occupied != -1) {
                    disabled = "disabled=\"disabled\"";
                }
        %>
        <div class="radio">
            <label>
                <input type="radio" name="id_tabella" value="<%=id_tabella[j]%>" required="required" <%=disabled%>/>
                <%=orari[j]%>
            </label>
        </div>
        <%}%>
        <br/>
        <input type="hidden" name="id_film" value="<%=ticketBean.getId_film()%>"/>
        <input type="hidden" name="data" value="<%=ticketBean.getData()%>"/>
        <button type="submit" form="timeform" class="btn btn-primary">Conferma</button>
        <a href="#" id="backbutton" class="btn btn-warning">Indietro</a>
    </form>
</div>
<script src="scripts/ticket.js"></script>
<script src="scripts/utility.js"></script>
<%
    } else {
        String redirect = "login.jsp";
        response.sendRedirect(redirect);
    }
%>
</body>
</html>
