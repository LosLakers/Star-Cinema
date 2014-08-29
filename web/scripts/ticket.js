var form = document.forms[id = 'ticketfilm'];
var film = form.id_film;

film.addEventListener('change', function() {
    var form = document.forms[id = 'ticketfilm'];
    var data = form.data;
    data.disabled = false;
    controlData(form, data);
});

// cambio il modifica per sbloccare la nuova selezione film e data
var link = document.getElementById('unlock');
if (link != null) {
    link.addEventListener('click', function() {
        // sblocco elementi della form
        var form = document.forms[id = 'ticketfilm'];
        var film = form.id_film;
        var data = form.data;
        film.disabled = false;
        data.disabled = false;
        addSubmit(form, 'ticketfilm', 'Modifica');

        // elimino il link
        removeElement(this);

        // blocco date
        controlData(form, data);
    });
}

function controlData(form, data) {
    var film = form.id_film;
    // sblocco select data alla selezione di un film e mi posiziono nel valore di default
    data.selectedIndex = 0;

    // disabilito le date in cui il film non è in programmazione
    var id_film = film.value;
    var hidden = document.getElementById(id_film);
    if (hidden !== null) {
        var value = hidden.value;
        value = value.split(':');
        var options = data.options;
        // salto la prima perchè è quella di default
        for (i = 1; i < options.length; i++) {
            for (j = 0; j < value.length - 1; j++) { // l'ultimo value è un ""
                if (value[j] == i) {
                    options[i].disabled = true;
                    break;
                } else {
                    options[i].disabled = false;
                }
            }
        }
    }
}

// back button
var back = document.getElementById('backbutton');
back.addEventListener('click', function() {
    backButton();
});