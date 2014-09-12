<%@include file="login_navbar.jsp" %>

<jsp:useBean id="subscriptionBean" scope="page" class="bflows.SubscriptionManagement" />
<jsp:setProperty name="subscriptionBean" property="*" />

<%
    if (loggedIn) {
        loginBean.getUser();
        if (loginBean.getCreditcard().equals("0")) {
            String redirect = profile + "?status=creditcarderror";
            response.sendRedirect(redirect);
        } else {
            subscriptionBean.getSubscription(username);
%>
<div class="jumbotron">
    <div class="container"></div>
</div>
<div class="container">
    <!-- Nessun abbonamento -->
    <% if (subscriptionBean.getId_abbonamento() == 0) {%>
    <legend>Acquista un nuovo abbonamento</legend>
    <table class="table">
        <tbody>
            <tr>
                <td>Nuovo abbonamento (10 ingressi)</td>
                <td>Prezzo</td>
                <td>
                    <form id="addsubscriptionform" action="pagamento_abb.jsp">
                        <input type="hidden" name="status" value="addsubscription" />
                        <input type="hidden" name="username" value="<%=username%>" />
                        <button type="submit" class="btn btn-default">Acquista</button>
                    </form>
                </td>
            </tr>
        </tbody>
    </table>
    <%} else if (subscriptionBean.getNum_ticket() == 0) {%>
    <!-- Abbonamento senza ingressi -->
    <legend>Rinnova abbonamento</legend>
    <p><strong>Nota:</strong> il tuo abbonamento ha finito gli ingressi disponibili</p>
    <table class="table">
        <tbody>
            <tr>
                <td>Rinnovo 10 ingressi</td>
                <td>Prezzo</td>
                <td>
                    <form id="updatesubscriptionform" action="pagamento_abb.jsp">
                        <input type="hidden" name="status" value="updatesubscription" />
                        <input type="hidden" name="username" value="<%=username%>" />
                        <button type="submit" class="btn btn-default">Rinnova</button>
                    </form>
                </td>
            </tr>
        </tbody>
    </table>
    <%} else {%>
    <!-- Abbonamento con ingressi -->
    <legend>Riepilogo abbonamento</legend>
    <p><strong>Nota:</strong> l'abbonamento sarà rinnovabile quando avrai finito tutti gli ingressi a disposizione</p>
    <table class="table">
        <tbody>
            <tr>
                <td>Il tuo abbonamento</td>
                <td>Ingressi disponibili = <%=subscriptionBean.getNum_ticket()%></td>
            </tr>
        </tbody>
    </table>
    <%}%>
</div>
<%
        }
    } else {
        String redirect = "login.jsp";
        response.sendRedirect(redirect);
    }
%>
</body>
</html>
