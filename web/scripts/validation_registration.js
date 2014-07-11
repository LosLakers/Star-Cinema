function confirmPassword() {
    var pass1 = document.getElementById("password").value;
    var pass2 = document.getElementById("conf_password").value;
    var ok = true;
    if (pass1 != pass2) {
        alert("Password e Conferma Password non combaciano");
        document.getElementById("password").style.borderColor = "#E34234";
        document.getElementById("conf_password").style.borderColor = "#E34234";
        ok = false;
    }
    return ok;
}

