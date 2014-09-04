<%@include file="login_navbar.jsp" %>

<jsp:useBean id="nowShowingBean" class="bflows.NowShowingManagement" />

<%
    if (isAdmin) {
        nowShowingBean.populateTheater();
%>
<div class="jumbotron">
    <div class="container">
        <h1>Situazione Sale</h1>
    </div>
</div>
<div class="container">
    <!-- Navbar -->
    <ul class="nav nav-tabs">
        <%
            int sale = nowShowingBean.TheaterDate_Length();
            for (int j = 1; j <= sale; j++) {
                String href = "#" + "Sala" + j;
                if (j == 1) {
        %>
        <li class="active">
            <a href="<%=href%>" data-toggle="tab">Sala #<%=j%></a>
        </li>
        <%
        } else {
        %>
        <li>
            <a href="<%=href%>" data-toggle="tab">Sala #<%=j%></a>
        </li>
        <%
                }
            }
        %>
    </ul>
    <!-- Tab Content -->
    <div class="tab-content">
        <%
            for (int j = 0; j < sale; j++) {
                String active = "";
                String id = "Sala" + (j + 1);
                if (j == 0) {
                    active = "active";
                }
        %>
        <div class="tab-pane <%=active%>" id="<%=id%>">
            <table class="table table-striped table-bordered">
                <thead>
                    <tr class="success">
                        <th class="col-lg-2 col-md-2">Data</th>
                        <th>Film - Orario - Posti Disponibili</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        String[] week = nowShowingBean.getWeek();
                        String[] date = nowShowingBean.TheaterDate_Date(j);
                        String[] film = nowShowingBean.TheaterDate_Film(j);
                        String[] orario = nowShowingBean.TheaterDate_Orario(j);
                        int[] posti_disp = nowShowingBean.TheaterDate_Posti(j);
                        for (int p = 0; p < week.length; p++) {
                    %>
                    <tr>
                        <td class="col-lg-2 col-md-2"><%=week[p]%></td>
                        <td>
                            <%
                                for (int k = 0; k < date.length; k++) {
                                    if (week[p].equals(date[k])) {
                            %>
                            <strong><%=film[k]%></strong> | <%=orario[k]%> | Posti <%=posti_disp[k]%> <->
                            <%
                                    }
                                }
                            %>
                        </td>
                    </tr>
                    <%
                        }
                    %>
                </tbody>
            </table>
        </div>
        <%
            }
        %>
    </div>
</div>
<%    } else {
        String redirect = "home.jsp";
        response.sendRedirect(redirect);
    }
%>

</body>
</html>