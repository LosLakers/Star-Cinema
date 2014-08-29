<%@page info="Star Cinema" %>
<%@page contentType="text/html" %>
<%@page session="false" %>

<jsp:useBean id="loginBean" scope="page" class="bflows.LoginManagement" />
<jsp:setProperty name="loginBean" property="*" />

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
        loginBean.login();
        if (loginBean.getCookies() != null) {
            for (i = 0; i < loginBean.getCookies().length; i++) {
                response.addCookie(loginBean.getCookie(i));
            }
            cookies = loginBean.getCookies();
            loggedIn = true;
            username = loginBean.getCookieValue("username");
            password = loginBean.getCookieValue("password");
            isAdmin = loginBean.getCookieValue("admin").equals("true") ? true : false;
        }
    }

    if (status.equals("logout")) {
        if (loggedIn) {
            loginBean.setCookies(cookies);
            loginBean.logout();
            for (i = 0; i < loginBean.getCookies().length; i++) {
                response.addCookie(loginBean.getCookie(i));
            }

            loggedIn = false;
        }
    }

    /* gestione di eventuali cookie già settati o settati durante il login */
    if (cookies != null) {
        loginBean.setCookies(cookies);
        username = loginBean.getCookieValue("username");
        password = loginBean.getCookieValue("password");
        if (loginBean.getCookieValue("admin").equals("true")) {
            isAdmin = true;
            profile = "admin_page.jsp";
        } else {
            profile = "user_page.jsp";
        }
    }

    /* TODO */
    /* gestione pagina corrente */
    String uri = request.getRequestURI();
    String currentPage = uri.substring(uri.lastIndexOf("/") + 1);
    String queryString = (request.getQueryString() != null) ? "?" + request.getQueryString() : "";
%>

<!DOCTYPE html>
<html>
    <head>
        <%@include file="default.html" %>
        <title>Star Cinema</title>
        <link href="css/custom-theme.css" rel="stylesheet" type="text/css"/>
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
                    <a class="navbar-brand">Star Cinema</a>
                </div>
                <div class="navbar-collapse collapse">
                    <ul class="nav navbar-nav">
                        <%
                            if (currentPage.equals("home.jsp")) {
                        %>
                        <li class="active">
                            <%} else {%>
                        <li>
                            <%}%>
                            <a href="home.jsp">Home</a>
                        </li>
                        <%
                            if (currentPage.equals("nowshowing.jsp")) {
                        %>
                        <li class="active">
                            <%} else {%>
                        <li>
                            <%}%>
                            <a href="nowshowing.jsp">Programmazione</a>
                        </li>
                        <%
                            if (currentPage.equals("search.jsp")) {
                        %>
                        <li class="active">
                            <%} else {%>
                        <li>
                            <%}%>
                            <a href="search.jsp">Lista Film</a>
                        </li>
                    </ul>
                    <%if (!loggedIn) {%>
                    <!-- Utente non loggato -->
                    <form name="loginForm" action="home.jsp" class="navbar-form navbar-right" method="post">
                        <input type="hidden" name="status" value="login"/>
                        <div class="form-group">
                            <input type="text" name="username" placeholder="Username" class="form-control" required="required"/>
                        </div>
                        <div class="form-group">
                            <input type="password" name="password" placeholder="Password" class="form-control" required="required"/>
                        </div>
                        <button type="submit" class="btn btn-primary">Accedi</button>
                        <a href="registration.jsp" class="btn btn-default">Registrati</a>
                    </form>
                    <%} else {%>
                    <!-- Utente loggato -->
                    <form class="navbar-form navbar-right" name="logoutForm" action="/StarCinema/home.jsp" method="post">
                        <input type="hidden" name="status" value="logout"/>
                        <a href="javascript:;" class="btn btn-danger" onclick="parentNode.submit();">Disconnetti</a>
                    </form>
                    <form class="navbar-form navbar-right" name="profileForm" action="<%=profile%>" method="post">
                        <a href="javascript:;" class="btn btn-primary" onclick="parentNode.submit();"><%=username%></a>
                    </form>
                    <%}%>
                </div>
            </div>
        </div>