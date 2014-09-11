<%@include file="login_navbar.jsp" %>

<jsp:useBean id="ticketBean" scope="page" class="bflows.TicketManagement"/>
<jsp:setProperty name="ticketBean" property="*"/>

<%
    boolean authorized = loginBean.authenticate(username, password);

    if (authorized) {
        ticketBean.setUsername(username);

        if (status == null || !status.equals("updateticket")) {
            ticketBean.populateUpdate();
        }
        if (status.equals("updateticket")) {
            try {
                ticketBean.updateTicket();
                String redirect = "ticketlist.jsp";
                response.sendRedirect(redirect);
            } catch (Exception ex) {
            }
%>
<%
    }
%>
<div class="jumbotron">
    <div class="container">
        <p>
            <b>Nota:</b> Lo schermo si trova di fronte alla fila A con la fila J che corrisponde
            all'ultima fila della sala nel punto più alto
        </p>
    </div>
</div>
<% if (!ticketBean.getMessage().equals("")) {%>
<!-- Gestione Errori -->
<div class="container">
    <div class="alert alert-dismissable <%=ticketBean.getMessagetype()%>">
        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">x</button>
        <p class="message"><%=ticketBean.getMessage()%></p>
    </div>
</div>
<%}%>
<div class="container">
    <legend>Seleziona Posti a Sedere</legend>
    <form id="seatform" action="updateticket.jsp" method="post">
        <!-- Selezione posti nella sala -->
        <table class="table">
            <thead>
                <tr>
                    <th>Fila</th>
                        <% for (int j = 1; j <= 20; j++) {%>
                    <th><%=j%></th>
                        <%}%>
                </tr>
            </thead>
            <tbody>
                <%
                    String fila = "A";
                    int num_posto = 1;
                    int cont = 0;
                    for (int j = 0; j < 200; j++) {
                        if (num_posto == 1) {
                %>
                <tr><td><%=fila%></td>
                    <%
                        }
                        String posto = fila + "-" + num_posto;
                        String[] reserved = ticketBean.getReserved();
                        int reserv = reserved != null ? reserved.length : 0;
                        String checkbox = "";
                        for (int k = cont; k < reserv; k++) {
                            if (posto.equals(reserved[k])) {
                                checkbox = "checked=\"checked\" disabled=\"disabled\"";
                                cont = k;
                                break;
                            }
                        }
                    %>
                    <td><input type="checkbox" name="seat" value="<%=posto%>" <%=checkbox%> /></td>
                        <%
                            if (num_posto == 20) {
                                num_posto = 1;
                                int num_fila = fila.charAt(0);
                                fila = String.valueOf((char) (num_fila + 1)); // vado alla lettera successiva
                        %>
                </tr>
                <%
                        } else {
                            num_posto++;
                        }
                    }
                %>
            </tbody>
        </table>
        <input type="hidden" name="status" value="updateticket" />
        <input type="hidden" name="id_tabella" value="<%=ticketBean.getId_tabella()%>" />
        <input type="hidden" name="id_ingresso" value="<%=ticketBean.getId_ingresso()%>" />
        <br/>
        <button type="submit" form="seatform" class="btn btn-primary">Conferma</button>
        <a href="#" id="backbutton" class="btn btn-warning">Indietro</a>
    </form>
</div>
<input type="hidden" id="ticketcount" value="<%=ticketBean.getTicketCounter()%>"/>
<script src="scripts/utility.js"></script>
<script src="scripts/ticket_validation.js"></script>
<script src="scripts/starcinema_utility.js"></script>
<%
    } else {
        String redirect = "home.jsp";
        response.sendRedirect(redirect);
    }
%>
</body>
</html>
