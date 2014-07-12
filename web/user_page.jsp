<%@include file="login_navbar.jsp" %>

<%
    boolean authorized = false;

    if (loginManagement.getCookieValue("username").equals(loginManagement.getUsername()) && status.equals("profile")) {
        authorized = true;
        loginManagement.getUser();
    }

    if (status.equals("edit")) {
        authorized = true;
        loginManagement.updateUser();
    }
    
    username = loginManagement.getUsername();
    password = loginManagement.getPassword();
%>

<% if (authorized) {%>
<form action="user_page.jsp" method="post">
    <input type="text" name="name" value="<%=loginManagement.getName()%>" placeholder="Nome" required/>
    <input type="text" name="surname" value="<%=loginManagement.getSurname()%>" placeholder="Cognome" required/>
    <input type="email" name="email" value="<%=loginManagement.getEmail()%>" placeholder="Email" required/>
    <input type="text" name="creditcard" value="<%=loginManagement.getCreditcard()%>" placeholder="CreditCard" required/>
    <input type="hidden" name="status" value="edit"/>
    <input type="hidden" name="username" value="<%=username%>"/>
    <input type="hidden" name="password" value="<%=password%>"/>
    <button type="submit">Modifica</button>
</form>

<a href="#">Lista Biglietti Acquistati</a>
#Abbonamento
#ingressi disponibili
<%}%>
</body>
</html>