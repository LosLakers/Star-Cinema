// javascript per la registrazione
var form = document.getElementById('registration');
form.addEventListener('submit', function(evt) {
    var pass1 = this.password.value;
    var pass2 = this.conf_password.value;
    if (pass1 !== pass2) {
        // blocco submit
        formBlock(evt);
    } else {
        // submit form
        return true;
    }
});

var pass = form.password;
pass.addEventListener('change', function() {
    passwordCheck();
});

var conf_pass = form.conf_pass;
conf_pass.addEventListener('change', function() {
    passwordCheck();
});

function passwordCheck() {
    var form = document.getElementById('registration');
    var pass1 = form.password.value;
    var pass2 = form.conf_password.value;
    if (pass1 !== pass2) {
        form.conf_password.className = 'form-control error';
    } else {
        form.conf_password.className = 'form-control';
    }
}