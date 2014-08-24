<%@include file="login_navbar.jsp" %>

<% if (isAdmin) {%>

<jsp:useBean id="filmBean" scope="page" class="bflows.FilmManagement"/>
<jsp:setProperty name="filmBean" property="*"/>

<%
    // controllo validità admin
    status = request.getParameter("status");
    String id = request.getParameter("id_film");

    if (id == null || id.equals("0")) {
        String redirect = "home.jsp";
        response.sendRedirect(redirect);
    }
    if (status.equals("updatefilm")) {
        filmBean.updateFilm();
    } else if (status.equals("deletefilm")) {
        filmBean.deleteFilm();
        String redirect = "search.jsp";
        response.sendRedirect(redirect);
    }

    filmBean.getFilm();

%>

<div class="jumbotron">
    <div class="container">
        <h1>Aggiorna Film</h1>
    </div>
</div>  
<div class="container">
    <form id="filmForm" class="inline-form" method="post" action="updatefilm.jsp">
        <div class="row">
            <div class="col-lg-4 col-md-4">
                <span class="label label-info">Inserisci Titolo Film</span>
                <input type="text" class="form-control" name="titolo" placeholder="Titolo" 
                       required="required" value="<%=filmBean.getTitolo()%>" />
            </div>
            <div class="col-lg-4 col-md-4">
                <span class="label label-info">Inserisci Durata Film</span>
                <input type="time" class="form-control" name="durata" required="required" 
                       value="<%=filmBean.getDurata()%>" />
            </div>
        </div>  
        <br/>
        <div class="row">  
            <div class="form-group col-lg-4 col-md-4">
                <span class="label label-info">Inserisci URL Trailer</span>
                <input type="url" class="form-control" name="trailer" placeholder="URL Trailer" 
                       required="required" value="<%=filmBean.getTrailer()%>" />
            </div>
            <div class="form-group col-lg-4 col-md-4">
                <span class="label label-info">Inserisci URL Locandina</span>
                <input type="url" class="form-control" name="locandina" placeholder="URL Locandina" 
                       required="required" value="<%=filmBean.getLocandina()%>" />
            </div>
        </div>  
        <div class="row">
            <div class="form-group col-lg-8 col-md-8">
                <span class="label label-info">Inserisci Descrizione</span>
                <textarea form="filmForm" class="form-control" name="descrizione" 
                          required="required"><%=filmBean.getDescrizione()%></textarea>
            </div>
        </div>
        <input type="submit" form="filmForm" class="btn btn-primary" value="Aggiorna"/>
        <a href="<%=profile%>" class="btn btn-warning">Torna al Profilo</a>
        <input type="hidden" name="status" value="updatefilm"/>
        <input type="hidden" name="id_film" value="<%=id%>"/>
    </form>
    <br/>
    <form id="deleteFilm" class="inline-form" method="post" action="update.jsp">
        <input type="submit" form="deleteFilm" class="btn btn-danger" value="Elimina"/>
        <input type="hidden" name="status" value="deletefilm"/>
        <input type="hidden" name="id_film" value="<%=filmBean.getId_film()%>"/>
    </form>
</div>
<%
    } else {// redirect per evitare che un non admin abbia accesso
        String redirect = "home.jsp";
        response.sendRedirect(redirect);
    }
%>
</body>
</html>