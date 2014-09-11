// back button
try {
    var back = document.getElementById('backbutton');
    back.addEventListener('click', function() {
        backButton();
    });
} catch (err) {
}

// link che effettuano il submit
var a_link = document.getElementsByClassName('submit-link');
for (var i = 0; i < a_link.length; i++) {
    a_link[i].addEventListener('click', function() {
        parentSubmit(this);
    });
}