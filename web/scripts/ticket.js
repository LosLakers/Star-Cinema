var form = document.getElementById('ticketfilm');
var film = form.id_film;
var data = form.data;

film.addEventListener('change', function() {
    var form = document.getElementById('ticketfilm');
    var data = form.data;
    // sblocco select data alla selezione di un film e mi posiziono nel valore di default
    data.disabled = false;
    data.selectedIndex = 0;
    
    // disabilito le date in cui il film non è in programmazione
    var id_film = this.value;
    var hidden = document.getElementById(id_film);
    if (hidden !== null) {
        var value = hidden.value;
        value = value.split(':');
        var options = data.options;
        // salto la prima perchè è quella di default
        for (i = 1; i < options.length; i++) {
            for (j = 0; j < value.length-1; j++) { // l'ultimo value è un ""
                if (value[j] == i) {
                    options[i].disabled = true;
                    break;
                } else {
                    options[i].disabled = false;
                }
            }
        }
    }
});