<%@include file="login_navbar.jsp" %>
<jsp:useBean id="filmBean" scope="page" class="bflows.FilmManagement"/>
<jsp:setProperty name="filmBean" property="id_film"/>

<%
    Boolean firstComment = true;
    filmBean.getFilm();
    filmBean.setUser(username);

    if (status.equals("addcommento")) {
%>
<jsp:setProperty name="filmBean" property="*"/>
<%
        filmBean.addComment();
    }
    if (status.equals("updatecommento")) {
%>
<jsp:setProperty name="filmBean" property="*"/>
<%
        filmBean.updateComment();
    }

    filmBean.getComment();
    if (filmBean.getId_commento() != 0) {
        firstComment = false;
    }
%>

<% if (!filmBean.getMessage().equals("")) {%>
<!-- Gestione Errori -->
<div class="container">
    <div class="col-md-6 col-lg-6">
        <div class="alert alert-success alert-dismissable">
            <button type="button" class="close" data-dismiss="alert" aria-hidden="true">x</button>
            <p class="message-success"><%=filmBean.getMessage()%></p>
        </div>
    </div>
</div>
<%}%>

<!-- Gestione Header Film -->
<div class="jumbotron">
    <div class="row">
        <div class="col-sm-offset-1 col-lg-5">
            <h1><%=filmBean.getTitolo()%></h1>
            <% if (isAdmin) {%>
            <form id="adminForm" action="addedit_film.jsp">
                <input type="hidden" name="id_film" value="<%=filmBean.getId_film()%>"/>
                <input type="hidden" name="status" value="edit"/>
                <button form="adminForm" type="submit" class="btn btn-primary">Modifica Film</button>
            </form>
            <br/>
            <form id="showForm" action="slotsala.jsp">
                <input type="hidden" name="id_film" value="<%=filmBean.getId_film()%>"/>
                <button form="showForm" type="submit" class="btn btn-default">Aggiungi Spettacolo</button>
            </form>
            <%}%>    
        </div>
        <div class="col-lg-6">
            <h2>Programmazione da Fare</h2>
            <table class="table">
                <thead>
                    <tr>
                        <th>Data</th>
                        <th>Orari</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>Oggi</td>
                        <td>9:00 - 12:00</td>
                    </tr>
                    <tr>
                        <td>Domani</td>
                        <td>10:00 - 13:00</td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>

<div class="container">
    <!-- Gestione Film -->
    <div class="row">
        <div class="col-lg-4 col-md-4">
            <h2>Locandina</h2>
            <img src="<%=filmBean.getLocandina()%>"/>
        </div>
        <div class="col-lg-4 col-md-4">
            <h2>Descrizione</h2>
            <p><%=filmBean.getDescrizione()%></p>
        </div>
        <div class="col-lg-4 col-md-4">
            <h3>Durata</h3>
            <%=filmBean.getDurata()%>
            <br/>
            <br/>
            <a href="<%=filmBean.getTrailer()%>" class="btn btn-default">Guarda il trailer</a>
        </div>
    </div>
    <!-- Gestione Commenti -->
    <% if (!username.equals("")) {%>
    <div class="row">
        <% if (firstComment) {%>
        <!-- Primo Commento -->
        <div class="col-lg-6 col-md-6">
            <div class="row">
                <h3>Inserisci Commento</h3>
            </div>
            <br/>
            <form id="addcomment" method="post" action="film.jsp">
                <div class="inline-block">
                    <a class="glyphicon glyphicon-star btn btn-warning inline-block able" data-star="1"></a>
                    <% for (int star = 2; star <= 5; star++) {%>
                    <a class="glyphicon glyphicon-star btn btn-default inline-block able" data-star="<%=star%>"></a>
                    <%}%>
                </div>
                <br/>
                <textarea class="form-control" name="giudizio" rows="5" required="required"></textarea>
                <br/>
                <input type="hidden" id="voto" name="voto" value="1"/>
                <input type="hidden" name="id_film" value="<%=filmBean.getId_film()%>"/>
                <input type="hidden" name="status" value="addcommento"/>
                <button form="addcomment" type="submit" class="btn btn-primary">Conferma</button>
            </form>
        </div>
        <%} else {%>
        <!-- Modifica Commento -->
        <div class="col-lg-6 col-md-6">
            <div class="row">
                <h3><strong>Commento</strong></h3>
            </div>
            <br/>
            <form id="updatecomment" method="post" action="film.jsp">
                <div class="inline-block">
                    <% for (int star = 1; star <= filmBean.getVoto(); star++) {%>
                    <a class="glyphicon glyphicon-star btn btn-warning inline-block disabled able" data-star="<%=star%>"></a>
                    <%}%>
                    <% for (int star = filmBean.getVoto() + 1; star <= 5; star++) {%>
                    <a class="glyphicon glyphicon-star btn btn-default inline-block disabled able" data-star="<%=star%>"></a>
                    <%}%>
                </div>
                <br/>
                <textarea id="giudizio" class="form-control" name="giudizio" rows="5" 
                          required="required" disabled="disabled"><%=filmBean.getGiudizio()%></textarea>
                <br/>
                <a id="modify" class="btn btn-primary">Modifica Commento</a>
                <input type="hidden" name="id_film" value="<%=filmBean.getId_film()%>"/>
                <input type="hidden" name="id_commento" value="<%=filmBean.getId_commento()%>"/>
            </form>
        </div>
        <%}%>    
    </div>
    <%}%>
    <!-- Lista Commenti del Film -->
    <%
        int num_comments = filmBean.getComment_Length();
        int num_page = num_comments % 4 == 0 ? num_comments / 4 : num_comments / 4 + 1;
        if (num_page == 0) {
            num_page = 1;
        }
        String[] pages = new String[num_page];
        pages[0] = "page1";
        for (int j = 1; j < num_page; j++) {
            pages[j] = "page" + (j + 1);
        }
    %>
    <div class="row">
        <h2><strong>Commenti</strong></h2>
        <div class="col-lg-12 col-md-12">
            <!-- Tab panes -->
            <div class="row">
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
                            if (num_comments <= 4) {
                                count = num_comments;
                            } else {
                                count = 4;
                                num_comments -= 4;
                            }
                            for (int p = 0; p < count; p++) {
                                String user = filmBean.getComment_User(filmBean.getCommenti(p + (j * 4)));
                                String commento = filmBean.getComment_Giudizio(filmBean.getCommenti(p + (j * 4)));
                                int voto = filmBean.getComment_Voto(filmBean.getCommenti(p + (j * 4)));
                        %>
                        <div class="col-lg-3">
                            <div class="inline-block">
                                <h3><%=user%></h3>
                                <% for (int star = 0; star < voto; star++) {%>
                                <a class="glyphicon glyphicon-star btn btn-success inline-block disabled"></a>
                                <%}%>
                            </div>
                            <br/>
                            <textarea class="form-control" disabled="disabled"><%=commento%></textarea>
                            <br/>
                        </div>
                        <%}%>
                    </div>
                    <%}%>
                </div>
            </div>
            <div class="row">
                <!-- Pagination -->
                <ul class="pagination">
                    <li class="active">
                        <a href="#page1" data-toggle="tab">1 <span class="sr-only">(current)</span></a>
                    </li>
                    <% for (int j = 1; j < num_page; j++) {%>
                    <li>
                        <a href="#<%=pages[j]%>" data-toggle="tab"><%=j + 1%> <span class="sr-only">(current)</span></a>
                    </li>
                    <%}%>
                </ul>
            </div>
            <br/>
        </div>
    </div>
    <br/>
    <script src="../scripts/film.js"></script>
    <script src="../scripts/utility.js"></script>
</body>
</html>
