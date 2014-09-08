// eliminazione multipla
var checkbox = document.getElementsByName('film_delete');
for (var i = 0; i < checkbox.length; i++) {
    checkbox[i].addEventListener('change', function() {
        var form = document.forms[id = 'multideletefilm'];
        if (this.checked) {
            // inserisco input
            addHidden(form, this.name, this.value, this.value);
        } else {
            // elimino input
            var input = document.getElementById(this.value);
            removeElement(input);
        }
    });
}
