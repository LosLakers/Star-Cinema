<jsp:useBean id="subscriptionBean" scope="page" class="bflows.SubscriptionManagement" />
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    String status = request.getParameter("status");
    boolean ok = false;
    if (status != null && status.equals("addsubscription")) {
        String username = request.getParameter("username");
        subscriptionBean.addSubscription(username);
        ok = true;
    } else if (status != null && status.equals("updatesubscription")) {
        String username = request.getParameter("username");
        subscriptionBean.updateSubscription(username);
        ok = true;
    }
    
    if (ok) {
%>

<!DOCTYPE html>
<html>
    <head>
        <head>
        <%@include file="default.html" %>
        <title>Abbonamento Star Cinema</title>
        <link href="css/custom-theme.css" rel="stylesheet" type="text/css"/>
    </head>
    <body>
        <% if (!subscriptionBean.getMessage().equals("")) {%>
        <!-- Gestione Errori -->
        <div class="container">
            <div class="alert alert-dismissable <%=subscriptionBean.getMessagetype()%>">
                <button type="button" class="close" data-dismiss="alert" aria-hidden="true">x</button>
                <p class="message"><%=subscriptionBean.getMessage()%></p>
            </div>
        </div>
        <%}%>
        <% if (status.equals("addsubscription")) {%>
        <strong>Acquisto abbonamento avvenuto</strong>
        <a href="home.jsp">Home</a>
        <%} else if (status.equals("updatesubscription")) {%>
        <strong>Aggiornamento avvenuto</strong>
        <a href="home.jsp">Home</a>
        <%}%>
    </body>
</html>
<%}%>
