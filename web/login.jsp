<%@page info="Star Cinema" %>
<%@page contentType="text/html" %>
<%@page session="false" %>
<%@page errorPage="errorpage.jsp" %>

<%
    String status = request.getParameter("status");
    Boolean registrato = false;

    /* da cambiare con una che prenda la pagina da cui si arriva */
    String pageName = "home.jsp";

    if (status != null && status.equals("registrato")) {
        registrato = true;
    }
%>

<!DOCTYPE html>
<html>
    <head>
        <title>Sign In</title>
        <%@include file="default.html" %>
        <link href="css/custom-login.css" rel="stylesheet" type="text/css"/>
    </head>
    <body>
        <div class="container">
            <% if (registrato) {%>
            <h2>La registrazione è andata a buon fine, effettua il login per essere reindirizzato alla homepage</h2>
            <%
                    pageName = "home.jsp";
                }
            %>
            <!-- Form di Login -->
            <form class="form-signin" action="<%=pageName%>" method="post">
                <h2 class="form-signin-heading">Login</h2>
                <input type="hidden" name="status" value="login"/>
                <input type="text" class="form-control" name="username" placeholder="Username" required="required"/>
                <input type="password" class="form-control" name="password" placeholder="Password" required="required"/>
                <!--<label class="checkbox">
                  <input type="checkbox" value="Ricordami"/>
                </label>-->
                <button class="btn btn-lg btn-primary btn-block" type="submit">Accedi</button>
                <a href="registration.jsp" class="btn btn-lg btn-warning btn-block">Registrati</a>
            </form>
        </div> 
    </body>
</html>
