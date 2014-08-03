<jsp:useBean id="filmBean" scope="page" class="bflows.FilmManagement"/>
<jsp:setProperty name="filmBean" property="*"/>

<%
    // controllo validità admin
    String status = request.getParameter("status");
    String submit = "Aggiungi";
    String hidden = "aggiungi";
    int id = 0;

    boolean delete = false;

    if (status == null) {
        hidden = "aggiungi";
        submit = "Aggiungi";
    } else if (status.equals("aggiungi")) {
        hidden = "aggiornato";
        submit = "Aggiorna";
        filmBean.addFilm();
        delete = true;
        id = filmBean.getId_film();
    } else if (status.equals("aggiornato")) {
        hidden = "aggiornato";
        submit = "Aggiorna";
        delete = true;
        filmBean.updateFilm();
        id = filmBean.getId_film();
    } else if (status.equals("edit")) {
        submit = "Aggiorna";
        hidden = "aggiornato";
        delete = true;
        id = filmBean.getId_film();
    } else if (status.equals("delete")) {
        hidden = "aggiungi";
        submit = "Aggiungi";
        filmBean.deleteFilm();
    }

    filmBean.getFilm();

%>

<!doctype html>
<html>
    <head>
        <title></title>
        <%@include file="default.html" %>
    </head>
    <body>
        <div class="jumbotron">
            <div class="container">
                <h1>Film</h1>
            </div>
        </div>  
        <div class="container">
            <form id="filmForm" class="inline-form" method="post" action="addedit_film.jsp">
                <div class="row">
                    <div class="col-xs-4">
                        <span class="label label-info">Inserisci Titolo Film</span>
                        <input type="text" class="form-control" name="titolo" placeholder="Titolo" 
                               required="required" value="<%=filmBean.getTitolo()%>" />
                    </div>
                    <div class="col-xs-4">
                        <span class="label label-info">Inserisci Durata Film</span>
                        <input type="time" class="form-control" name="durata" required="required" 
                               value="<%=filmBean.getDurata()%>" />
                    </div>
                </div>  
                <br/>
                <div class="row">  
                    <div class="form-group col-xs-4">
                        <span class="label label-info">Inserisci URL Trailer</span>
                        <input type="url" class="form-control" name="trailer" placeholder="URL Trailer" 
                               required="required" value="<%=filmBean.getTrailer()%>" />
                    </div>
                    <div class="form-group col-xs-4">
                        <span class="label label-info">Inserisci URL Locandina</span>
                        <input type="url" class="form-control" name="locandina" placeholder="URL Locandina" 
                               required="required" value="<%=filmBean.getLocandina()%>" />
                    </div>
                </div>  
                <div class="row">
                    <div class="form-group col-xs-8">
                        <span class="label label-info">Inserisci Descrizione</span>
                        <textarea form="filmForm" class="form-control" name="descrizione" 
                                  required="required"><%=filmBean.getDescrizione()%></textarea>
                    </div>
                </div>
                <input type="submit" form="filmForm" class="btn btn-primary" value="<%=submit%>"/>
                <a href="#" class="btn btn-warning">Torna al Profilo</a>
                <input type="hidden" name="status" value="<%=hidden%>"/>
                <input type="hidden" name="id_film" value="<%=id%>"/>
            </form>
            <br/>
            <% if (delete) {%>
            <form id="deleteFilm" class="inline-form" method="post" action="addedit_film.jsp">
                <input type="submit" form="deleteFilm" class="btn btn-danger" value="Elimina Film"/>
                <input type="hidden" name="status" value="delete"/>
                <input type="hidden" name="id_film" value="<%=filmBean.getId_film()%>"/>
            </form>
            <%}%>
        </div>
    </body>
</html>