<%@page info="Star Cinema" %>
<%@page contentType="text/html" %>
<%@page session="false" %>

<jsp:useBean id="loginManagement" scope="page" class="bflows.LoginManagement" />
<jsp:setProperty name="loginManagement" property="*" />

<%
    Cookie[] cookies = null;
    cookies = request.getCookies();
    boolean isAdmin = false;
    String profile = "#";
    String username = "";
    String password = "";

    boolean loggedIn = (cookies != null);

    int i;
    String status;
    // da usare in futuro per mantenere la sessione anche alla chiusura del browser
    //String ricordami = request.getParameter("ricordami");

    /* gestione dello status con cui arrivo nella pagina */
    status = request.getParameter("status");
    if (status == null) {
        status = "view";
    }

    if (status.equals("login")) {
        loginManagement.login();
        if (loginManagement.getCookies() != null) {
            for (i = 0; i < loginManagement.getCookies().length; i++) {
                response.addCookie(loginManagement.getCookie(i));
            }
            cookies = loginManagement.getCookies();
            loggedIn = true;
            username = loginManagement.getCookieValue("username");
            password = loginManagement.getCookieValue("password");
            isAdmin = loginManagement.getCookieValue("admin").equals("true") ? true : false;
        }
    }

    if (status.equals("logout")) {
        if (loggedIn) {
            loginManagement.setCookies(cookies);
            loginManagement.logout();
            for (i = 0; i < loginManagement.getCookies().length; i++) {
                response.addCookie(loginManagement.getCookie(i));
            }

            loggedIn = false;
        }
    }
    
    /* gestione di eventuali cookie già settati o settati durante il login */
    if (cookies != null) {
        loginManagement.setCookies(cookies);
        username = loginManagement.getCookieValue("username");
        password = loginManagement.getCookieValue("password");
        if (loginManagement.getCookieValue("admin").equals("true")) {
            isAdmin = true;
            profile = "admin_page.jsp?" + "username=" + username
                    + "&password=" + password;
        } else {
            profile = "user_page.jsp?" + "username=" + username
                    + "&password=" + password;
        }
    }

    /* TODO */
    /* gestione pagina corrente */
    String uri = request.getRequestURI();
    String pageName = uri.substring(uri.lastIndexOf("/") + 1);
    String queryString = (request.getQueryString() != null) ? "?" + request.getQueryString() : "";
    pageName = pageName + queryString;
%>

<!DOCTYPE html>
<html>
    <head>
        <%@include file="default.html" %>
        <title>Star Cinema</title>
        <link href="theme.css" rel="stylesheet" type="text/css"/>
        <script src="utility.js"></script>
    </head>
    <body>
        <div class="navbar navbar-inverse navbar-fixed-top">
            <div class="container">
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </button>
                    <a class="navbar-brand" href="#">Star Cinema</a>
                </div>
                <div class="navbar-collapse collapse">
                    <ul class="nav navbar-nav">
                        <li class="active">
                            <a href="home.jsp">Home</a>
                        </li>
                        <li>
                            <a href="#">Programmazione</a>
                        </li>
                    </ul>
                    <%if (!loggedIn) {%>
                    <form name="loginForm" action="<%=pageName%>" class="navbar-form navbar-right" method="post">
                        <input type="hidden" name="status" value="login"/>
                        <div class="form-group">
                            <input type="text" name="username" placeholder="Username" class="form-control" required="required"/>
                        </div>
                        <div class="form-group">
                            <input type="password" name="password" placeholder="Password" class="form-control" required="required"/>
                        </div>
                        <button type="submit" class="btn btn-success">Accedi</button>
                        <a href="registration.jsp" class="btn btn-primary">Registrati</a>
                    </form>
                    <%} else {%>
                    <form class="navbar-form navbar-right" name="logoutForm" action="home.jsp" method="post">
                        <input type="hidden" name="status" value="logout"/>
                        <a href="javascript:;" class="btn btn-warning" onclick="parentNode.submit();">Disconnetti</a>
                    </form>
                    <form class="navbar-form navbar-right" name="profileForm" action="<%=profile%>" method="post">
                        <a href="javascript:;" class="btn btn-primary" onclick="parentNode.submit();"><%=username%></a>
                        <input type="hidden" name="status" value="profile"/>
                        <input type="hidden" name="username" value="<%=username%>"/>
                        <input type="hidden" name="password" value="<%=password%>"/>
                    </form>
                    <%}%>
                </div>
            </div>
        </div>
