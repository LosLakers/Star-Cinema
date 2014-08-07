<%@include file="login_navbar.jsp" %>

<% if (isAdmin) {%>

<jsp:useBean id="filmBean" scope="page" class="bflows.FilmManagement"/>
<jsp:setProperty name="filmBean" property="*"/>

<%
    // controllo validità admin
    status = request.getParameter("status");

    if (status != null && status.equals("addfilm")) {
        filmBean.addFilm();
        String redirect = "film.jsp?id_film=" + filmBean.getId_film();
        response.sendRedirect(redirect);
    }
%>

<div class="jumbotron">
    <div class="container">
        <h1>Film</h1>
    </div>
</div>  
<div class="container">
    <form id="filmForm" class="inline-form" method="post" action="addfilm.jsp">
        <div class="row">
            <div class="col-lg-4 col-md-4">
                <span class="label label-info">Inserisci Titolo Film</span>
                <input type="text" class="form-control" name="titolo" placeholder="Titolo" 
                       required="required"/>
            </div>
            <div class="col-lg-4 col-md-4">
                <span class="label label-info">Inserisci Durata Film</span>
                <input type="time" class="form-control" name="durata" required="required"/>
            </div>
        </div>  
        <br/>
        <div class="row">  
            <div class="form-group col-lg-4 col-md-4">
                <span class="label label-info">Inserisci URL Trailer</span>
                <input type="url" class="form-control" name="trailer" placeholder="URL Trailer" 
                       required="required"/>
            </div>
            <div class="form-group col-lg-4 col-md-4">
                <span class="label label-info">Inserisci URL Locandina</span>
                <input type="url" class="form-control" name="locandina" placeholder="URL Locandina" 
                       required="required"/>
            </div>
        </div>  
        <div class="row">
            <div class="form-group col-lg-8 col-md-8">
                <span class="label label-info">Inserisci Descrizione</span>
                <textarea form="filmForm" class="form-control" name="descrizione" 
                          required="required"></textarea>
            </div>
        </div>
        <input type="submit" form="filmForm" class="btn btn-primary" value="Aggiungi"/>
        <a href="<%=profile%>" class="btn btn-warning">Annulla</a>
        <input type="hidden" name="status" value="addfilm"/>
    </form>
    <br/>
</div>
<%
    } else {// redirect per evitare che un non admin abbia accesso
        String redirect = "home.jsp";
        response.sendRedirect(redirect);
    }
%>
</body>
</html>