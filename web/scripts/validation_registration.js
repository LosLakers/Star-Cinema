// javascript per la registrazione
var pass = document.getElementById('password');
pass.addEventListener('change', function() {
    var pass1 = document.getElementById('password').value;
    var pass2 = document.getElementById('conf_password').value;
    if (pass1 !== pass2) {
        document.getElementById('conf_password').style.borderColor = 'red';
    } else {
        document.getElementById('conf_password').style.borderColor = 'green';
    }
});

var conf_pass = document.getElementById('conf_password');
conf_pass.addEventListener('change', function() {
    var pass1 = document.getElementById('password').value;
    var pass2 = document.getElementById('conf_password').value;
    if (pass1 !== pass2) {
        document.getElementById('conf_password').style.borderColor = 'red';
    } else {
        document.getElementById('conf_password').style.borderColor = 'green';
    }
});

var form = document.getElementById('registration');
form.addEventListener('submit', function(event) {
    var ok = true;
    var pass1 = document.getElementById('password').value;
    var pass2 = document.getElementById('conf_password').value;
    if (pass1 !== pass2) {
        event.preventDefault();
        alert('Password e Conferma Password non combaciano');
        ok = false;
    }
    return ok;
});