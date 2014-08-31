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
    <head></head>
    <body>
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
