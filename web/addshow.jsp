<%@include file="login_navbar.jsp" %>

<% if (isAdmin) {%>

<jsp:useBean id="nowShowingBean" scope="page" class="bflows.NowShowingManagement" />
<jsp:setProperty name="nowShowingBean" property="*" />

<%
    String id_film = request.getParameter("id_film");
    if (id_film == null) {
        String redirect = "home.jsp";
        response.sendRedirect(redirect);
    }
    if (status.equals("addShow")) {
        try {
            nowShowingBean.addShow();
        } catch (Exception ex) {
        }
    }
    nowShowingBean.populateTheater();
    int num_sale = nowShowingBean.TheaterDate_Length();
    String[] week = nowShowingBean.getWeek();
%>

<!-- Jumbotron -->
<div class="jumbotron">
    <div class="container">
        <h2>Sala - Data - Ora</h2>
        <p>
            Seleziona la sala, la data, l'ora di inizio e l'ora di fine del film che vuoi inserire.
            <br/>
            Il film è <strong><%=nowShowingBean.getTitolo_film()%></strong> con durata <strong id="durata"><%=nowShowingBean.getDurata_film()%></strong>
        </p>
    </div>
</div>

<% if (!nowShowingBean.getMessage().equals("")) {%>
<!-- Gestione Errori -->
<div class="container">
    <div class="alert alert-dismissable <%=nowShowingBean.getMessagetype()%>">
        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">x</button>
        <p class="message"><%=nowShowingBean.getMessage()%></p>
    </div>
</div>
<%}%>

<div class="container">
    <div class="row">
        <form id="showForm" method="post" action="addshow.jsp">
            <!-- Gestione Sale -->
            <div class="col-lg-12 col-md-12">
                <div class="form-group col-lg-4 col-md-4">
                    <label class="control-label">Sala</label>
                    <select id="sala" name="sala" class="form-control" required="required">
                        <option selected="selected" disabled="true">Seleziona una Sala...</option>
                        <%for (int j = 1; j <= num_sale; j++) {%>
                        <option value="<%=j%>">Sala <%=j%></option>
                        <%}%>
                    </select>
                </div>
                <!-- Gestione Date -->
                <div class="form-group col-lg-4 col-md-4">
                    <label class="control-label">Data</label>
                    <select id="data" name="data" class="form-control" required="required">
                        <option selected="selected" disabled="true">Seleziona una Data...</option>
                        <% for (int j = 0; j < week.length; j++) {%>
                        <option value="<%=week[j]%>"><%=week[j]%></option>
                        <%}%>
                    </select>
                </div>
            </div>
            <br/>
            <!-- Gestione Orario -->
            <div class="col-lg-12 col-md-12">
                <div class="form-group col-lg-4 col-md-4">
                    <label class="control-label">Ora Inizio</label>
                    <div class="controls">
                        <input type="time" name="ora_inizio" class="form-control" required="required"/>
                    </div>
                </div>
                <div class="form-group col-lg-4 col-md-4">
                    <label class="control-label">Ora Fine</label>
                    <div class="controls">
                        <input type="time" name="ora_fine" class="form-control" required="required"/>
                    </div>
                </div>
            </div>
            <input type="hidden" name="id_film" value="<%=id_film%>" />
            <input type="hidden" name="status" value="addShow" />
            <br/>
            <div class="col-lg-12 col-md-12">
                <button type="submit" class="btn btn-primary">Conferma</button>
                <a href="#" class="btn btn-warning">Annulla</a>
            </div>
        </form>
    </div>
</div>
<br/>
<div class="jumbotron">
    <!-- Gestione Occupazione Sale nella Settimana -->
    <div class="container">
        <%
            // creo gli href per la tabella
            String[] href = new String[week.length];
            for (int j = 0; j < href.length; j++) {
                href[j] = "#" + week[j];
            }
        %>
        <legend>Controlla per ogni sala e per ogni data, gli orari di occupazione.</legend>
        <br/>
        <div class="col-lg-10 col-md-10">
            <!-- Nav tabs -->
            <ul class="nav nav-tabs">
                <li class="active">
                    <a href="<%=href[0]%>" data-toggle="tab"><%=week[0]%></a>
                </li>
                <% for (int j = 1; j < week.length; j++) {%>
                <li>
                    <a href="<%=href[j]%>" data-toggle="tab"><%=week[j]%></a>
                </li>
                <%}%>
            </ul>
        </div>
        <!-- Tab panes -->  
        <div class="col-lg-10 col-md-10">
            <div class="tab-content">
                <div class="tab-pane active" id="<%=week[0]%>">
                    <table class="table table-bordered table-striped">
                        <thead>
                            <tr class="info">
                                <th>
                                    Numero Sala
                                </th>
                                <th>
                                    Orari in cui è occupata
                                </th>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                for (int j = 0; j < num_sale; j++) {
                                    String[] inizio = nowShowingBean.TheaterDate_OraInizio(j + 1, week[0]);
                                    String[] fine = nowShowingBean.TheaterDate_OraFine(j + 1, week[0]);
                            %>
                            <tr>
                                <td>
                                    Sala <%=j + 1%>
                                </td>
                                <td>
                                    <% for (int p = 0; p < inizio.length; p++) {%>
                                    <%=inizio[p]%> - <%=fine[p]%> |
                                    <%}%>
                                </td>
                            </tr>
                            <%}%>
                        </tbody>
                    </table>
                </div>
                <% for (int j = 1; j < week.length; j++) {%>        
                <div class="tab-pane" id="<%=week[j]%>">
                    <table class="table table-bordered table-hover">
                        <thead>
                            <tr class="info">
                                <th>
                                    Numero Sala
                                </th>
                                <th>
                                    Orari in cui è occupata
                                </th>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                for (int m = 0; m < num_sale; m++) {
                                    String[] inizio = nowShowingBean.TheaterDate_OraInizio(m + 1, week[j]);
                                    String[] fine = nowShowingBean.TheaterDate_OraFine(m + 1, week[j]);
                            %>
                            <tr>
                                <td>
                                    Sala <%=m + 1%>
                                </td>
                                <td>
                                    <% for (int p = 0; p < inizio.length; p++) {%>
                                    <%=inizio[p]%> - <%=fine[p]%> |
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
    </div>
</div>
<script src="scripts/nowshowing_script.js"></script>
<script src="scripts/utility.js"></script>
<%
    } else { // redirect per evitare che un non admin abbia accesso
        String redirect = "home.jsp";
        response.sendRedirect(redirect);
    }
%>    
</body>
</html>
