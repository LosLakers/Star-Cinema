<%@include file="login_navbar.jsp" %>

<jsp:useBean id="nowShowingBean" class="bflows.NowShowingManagement" />

<%
    if (isAdmin) {
        nowShowingBean.populateTheater();
        int num_sale = nowShowingBean.TheaterDate_Length();
        String[] week = nowShowingBean.getWeek();
%>
<div class="jumbotron">
    <div class="container">
        <h1>Situazione Date</h1>
    </div>
</div>
<div class="container">
    <%
        // creo gli href per la tabella
        String[] href = new String[week.length];
        for (int j = 0; j < href.length; j++) {
            href[j] = "#" + week[j];
        }
    %>
    <!-- Nav tabs -->
    <ul class="nav nav-tabs nav-justified">
        <li class="active">
            <a href="<%=href[0]%>" data-toggle="tab"><%=week[0]%></a>
        </li>
        <% for (int j = 1; j < week.length; j++) {%>
        <li>
            <a href="<%=href[j]%>" data-toggle="tab"><%=week[j]%></a>
        </li>
        <%}%>
    </ul>
    <!-- Tab panes -->  
    <div class="tab-content">
        <%
            for (int k = 0; k < week.length; k++) {
                String active = "";
                if (k == 0) {
                    active = "active";
                }
        %> 
        <div class="tab-pane <%=active%>" id="<%=week[k]%>">
            <table class="table table-bordered table-striped">
                <thead>
                    <tr class="success">
                        <th class="col-lg-2 col-md-2">Sala</th>
                        <th>Film - Orario - Posti Disponibili</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        for (int j = 0; j < num_sale; j++) {
                            String[] inizio = nowShowingBean.TheaterDate_OraInizio(j + 1, week[k]);
                            String[] fine = nowShowingBean.TheaterDate_OraFine(j + 1, week[k]);
                            String[] film = nowShowingBean.TheaterDate_Film(j + 1, week[k]);
                            int[] posti_disp = nowShowingBean.TheaterDate_Posti(j + 1, week[k]);
                    %>
                    <tr>
                        <td class="col-lg-2 col-md-2">Sala <%=j + 1%></td>
                        <td>
                            <% for (int p = 0; p < inizio.length; p++) {%>
                            <strong><%=film[p]%></strong> | <%=inizio[p]%> - <%=fine[p]%> | Posti <%=posti_disp[p]%>
                            <br/>
                            <%}%>
                        </td>
                    </tr>
                    <%}%>
                </tbody>
            </table>
        </div>
        <%}%>
    </div>
</div>
<%
    } else {
        String redirect = "home.jsp";
        response.sendRedirect(redirect);
    }
%>
</body>
</html>
