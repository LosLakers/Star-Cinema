// eliminazione multipla
var checkbox = document.getElementsByName('deleteFilm');
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

// blocco form se non ci sono selezioni
var form = document.forms[id = 'multideletefilm'];
form.addEventListener('submit', function(evt) {
   var inputs = this.getElementsByTagName('input');
   if (inputs.length > 1) { // c'è da considerare l'hidden
       return true;
   } else {
       // blocco il submit
       formBlock(evt);
       alert('Selezionare almeno un film');
   }
});
