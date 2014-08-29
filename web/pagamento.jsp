<%-- 
    Document   : pagamento
    Created on : 29-ago-2014, 17.38.06
    Author     : Guido Pio
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<jsp:useBean id="ticketBean" scope="page" class="bflows.TicketManagement" />
<jsp:setProperty name="ticketBean" property="*" />
<%
    String status = request.getParameter("status");
    Boolean errore = true;
    if (status != null && status.equals("addticket")) {
        try {
            ticketBean.addTicket();
            errore = false;
        } catch (Exception ex) {
            // gestione errore
        }
    }
    int topay = ticketBean.getTopay();
%>

<html>
    <head></head>
    <body>
        <% if (errore) {%>
        <!-- Gestione Errori -->
        <div class="container">
            <div class="col-md-12 col-lg-12">
                <div class="alert alert-danger alert-dismissable">
                    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">x</button>
                    <p>Errore inserimento acquisto</p>
                </div>
            </div>
        </div>
        <%}%>
        <p>
            Pagina di pagamento non implementata per mancanze di conoscenze a livello
            finanziario. Vengono solo mostrati i biglietti da pagare e si viene
            reindirizzati alla home del sito
        </p>
        <p>Totale biglietti da pagare = <%=topay%></p>
        <a href="home.jsp" class="btn btn-primary">Conferma</a>
    </body>
</html>
