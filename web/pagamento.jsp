<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<jsp:useBean id="ticketBean" scope="page" class="bflows.TicketManagement" />
<jsp:setProperty name="ticketBean" property="*" />
<%
    String status = request.getParameter("status");
    if (status != null && status.equals("addticket")) {
        try {
            ticketBean.addTicket();
        } catch (Exception ex) {
        }
    }
    int topay = ticketBean.getTopay();
%>

<html>
    <head>
        <%@include file="default.html" %>
        <title>Acquisto Ticket Star Cinema</title>
        <link href="css/custom-theme.css" rel="stylesheet" type="text/css"/>
    </head>
    <body>
        <% if (!ticketBean.getMessage().equals("")) {%>
        <!-- Gestione Errori -->
        <div class="container">
            <div class="alert alert-dismissable <%=ticketBean.getMessagetype()%>">
                <button type="button" class="close" data-dismiss="alert" aria-hidden="true">x</button>
                <p class="message"><%=ticketBean.getMessage()%></p>
            </div>
        </div>
        <%}%>
        <p>
            Pagina di pagamento non implementata per mancanze di conoscenze a livello
            finanziario. Vengono solo mostrati i biglietti da pagare e si viene
            reindirizzati alla lista dei ticket acquistati
        </p>
        <p>Totale biglietti da pagare = <%=topay%></p>
        <a href="ticketlist.jsp" class="btn btn-primary">Conferma</a>
    </body>
</html>
