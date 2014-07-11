<jsp:useBean id="filmManagement" scope="page" class="bflows.FilmManagement"/>
<jsp:setProperty name="filmManagement" property="*"/>

<%
    // controllo validità admin
    String status = request.getParameter("status");
    String submit = "Aggiungi";
    String hidden = "aggiungi";

    if (status == null) {
        hidden = "aggiungi";
        submit = "Aggiungi";
    } else if (status.equals("aggiungi")) {
        submit = "Aggiorna";
        hidden = "aggiornato";
        filmManagement.addFilm();
    } else if(status.equals("aggiornato")) {
        hidden ="aggiornato";
        submit = "Aggiorna";
        filmManagement.updateFilm();
    }

    String titolo = filmManagement.getTitolo() != null ? filmManagement.getTitolo() : "";
    filmManagement.getFilm();

%>

<!doctype html>
<html>
    <head>
        <title></title>
        <%@include file="default.html" %>
    </head>
    <body>
        <div class="jumbotron">
            <h1>Film</h1>
        </div>  
        <div class="container">
            <form id="filmForm" class="inline-form" method="post" action="addedit_film.jsp">
                <div class="row">
                    <div class="col-xs-4">
                        <span class="label label-info">Inserisci Titolo Film</span>
                        <input type="text" class="form-control" name="titolo" placeholder="Titolo" 
                               required="required" value="<%=titolo%>" />
                    </div>
                    <div class="col-xs-4">
                        <span class="label label-info">Inserisci Durata Film</span>
                        <input type="time" class="form-control" name="durata" placeholder="Durata" 
                               required="required" value="<%=filmManagement.getDurata()%>" />
                    </div>
                </div>  
                <br/>
                <div class="row">  
                    <div class="form-group col-xs-4">
                        <span class="label label-info">Inserisci URL Trailer</span>
                        <input type="url" class="form-control" name="trailer" placeholder="URL Trailer" 
                               required="required" value="<%=filmManagement.getTrailer()%>" />
                    </div>
                    <div class="form-group col-xs-4">
                        <span class="label label-info">Inserisci URL Locandina</span>
                        <input type="url" class="form-control" name="locandina" placeholder="URL Locandina" 
                               required="required" value="<%=filmManagement.getLocandina()%>" />
                    </div>
                </div>  
                <div class="row">
                    <div class="form-group col-xs-8">
                        <span class="label label-info">Inserisci Descrizione</span>
                        <textarea form="filmForm" class="form-control" name="descrizione" required="required"><%=filmManagement.getDescrizione()%></textarea>
                    </div>
                </div>
                <input type="submit" class="btn btn-primary" value="<%=submit%>"></input>
                <a href="#" class="btn btn-warning">Torna al Profilo</a>
                <input type="hidden" name="status" value="<%=hidden%>"/>
            </form>
        </div>
    </div>
</div>
</body>
</html>