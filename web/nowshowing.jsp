<%@include file="login_navbar.jsp" %>

<jsp:useBean id="nowShowingBean" scope="page" class="bflows.NowShowingManagement"/>

<%
    nowShowingBean.populate();
%>

<div class="jumbotron">
    <div class="container">
    </div>
</div>
<div class="container">
    <!-- Navbar -->
    <ul class="nav nav-tabs">
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
            <table class="table table-bordered table-hover">
                <thead>
                    <tr class="info">
                        <th>
                            Film
                        </th>
                        <th>
                            Orari
                        </th>
                        <th>
                            Sala - Posti Disponibili
                        </th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        int num_film = nowShowingBean.showFilms();
                        for (int p = 0; p < num_film; p++) {
                            if (nowShowingBean.show(p, week[j])) {
                                String titolo = nowShowingBean.showTitolo(p);
                                String href = "film.jsp?id_film=" + nowShowingBean.showIdFilm(p);
                    %>
                    <tr>
                        <td>
                            <a href="<%=href%>"><%=titolo%></a>
                        </td>
                        <td>
                            <%
                                String[] oraInizio = nowShowingBean.showOraInizio(p, week[j]);
                                String[] oraFine = nowShowingBean.showOraFine(p, week[j]);
                                for (int q = 0; q < oraInizio.length; q++) {
                            %>
                            <%=oraInizio[q]%>-<%=oraFine[q]%> ||
                            <%}%>
                        </td>
                        <td>
                            <%
                                Integer[] num_sale = nowShowingBean.showSale(p, week[j]);
                                Integer[] posti = nowShowingBean.showPosti(p, week[j]);
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
</div>
</body>
</html>