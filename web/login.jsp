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
        <style type="text/css">
            body {
                padding-top: 40px;
                padding-bottom: 40px;
                background-color: #eee;
            }

            .form-signin {
                max-width: 330px;
                padding: 15px;
                margin: 0 auto;
            }
            .form-signin .form-signin-heading,
            .form-signin .checkbox {
                margin-bottom: 10px;
            }
            .form-signin .checkbox {
                font-weight: normal;
            }
            .form-signin .form-control {
                position: relative;
                font-size: 16px;
                height: auto;
                padding: 10px;
                -webkit-box-sizing: border-box;
                -moz-box-sizing: border-box;
                box-sizing: border-box;
            }
            .form-signin .form-control:focus {
                z-index: 2;
            }
            .form-signin input[type="text"] {
                margin-bottom: -1px;
                border-bottom-left-radius: 0;
                border-bottom-right-radius: 0;
            }
            .form-signin input[type="password"] {
                margin-bottom: 10px;
                border-top-left-radius: 0;
                border-top-right-radius: 0;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <% if (registrato) {%>
            <h2>La registrazione è andata a buon fine, effettua il login per essere reindirizzato alla homepage</h2>
            <% pageName = "home.jsp"; }%>
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

        </div> <!-- /container -->

    </body>
</html>
