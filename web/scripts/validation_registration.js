// javascript per la registrazione
var form = document.getElementById('registration');
form.addEventListener('submit', function(evt) {
    var pass1 = this.password.value;
    var pass2 = this.conf_password.value;
    if (pass1 !== pass2) {
        passwordCheck();
        // blocco submit
        formBlock(evt);
    } else {
        passwordCheck();
        // submit form
        return true;
    }
});

var pass = form.password;
pass.addEventListener('change', function() {
   passwordCheck(); 
});

var conf_pass = form.conf_password;
conf_pass.addEventListener('change', function() {
    passwordCheck();
});

function passwordCheck() {
    var form = document.getElementById('registration');
    var pass1 = form.password.value;
    var pass2 = form.conf_password.value;
    if (pass1 !== pass2) {
        var div = form.conf_password.parentNode;
        div.className = 'form-group has-feedback has-error';
        var span = div.getElementsByClassName('glyphicon form-control-feedback');
        span[0].className = 'glyphicon glyphicon-remove form-control-feedback';
    } else {
        var div = form.conf_password.parentNode;
        div.className = 'form-group has-feedback has-success';
        var span = div.getElementsByClassName('glyphicon form-control-feedback');
        span[0].className = 'glyphicon glyphicon-ok form-control-feedback';
    }
}