<%@include file="login_navbar.jsp" %>

<jsp:useBean id="nowshowingManagement" scope="page" class="bflows.NowShowingManagement"/>

<%
    nowshowingManagement.setFilms();
%>

<div class="jumbotron">
    <div class="container">
        <ul class="nav nav-tabs">
            <li class="active">
                <a href="#oggi" data-toggle="tab"><%=nowshowingManagement.getDate(0)%></a>    
            </li>
            <% for (int day = 1; day < 7; day++) {%>
            <li>
                <a href="#<%=day%>" data-toggle="tab"><%=nowshowingManagement.getDate(day)%></a>
            </li>
            <%}%>
        </ul>
        <div class="tab-content">
            <div class="tab-pane active" id="oggi">
                <table class="table table-bordered table-hover">
                    <thead>
                        <tr class="info">
                            <th>
                                Titolo
                            </th>
                            <th>
                                Orario
                            </th>
                            <th>
                                Sala
                            </th>
                        </tr>
                    </thead>
                    <tbody>
                        <%for (int film = 0; film < 6; film++) {%>
                        <tr>
                            <td>
                                <a href="#">Film1</a>
                            </td>
                            <td>
                                Orario1 - Orario2
                            </td>
                            <td>
                                Numero Sala
                            </td>
                        </tr>
                        <%}%>
                    </tbody>
                </table>
            </div>
            <% for (int day = 0; day < 6; day++) {%>
            <div class="tab-pane" id="<%=day%>">
                <table class="table table-bordered table-hover">
                    <thead>
                        <tr class="info">
                            <th>
                                Titolo
                            </th>
                            <th>
                                Orario
                            </th>
                            <th>
                                Sala
                            </th>
                        </tr>
                    </thead>
                    <tbody>
                        <%for (int film = 0; film < 6; film++) {%>
                        <tr>
                            <td>
                                <a href="#">Film3</a>
                            </td>
                            <td>
                                Orario1 - Orario2
                            </td>
                            <td>
                                Numero Sala
                            </td>
                        </tr>
                        <%}%>
                    </tbody>
                </table>
            </div>
            <%}%>
        </div>
    </div>
</div>
</body>
</html>