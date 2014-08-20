/* controllo che l'ora di inizio sia inferiore a quella di fine 
 * e compresa nella durata del film [NON COMPLETA].
 */
var form = document.getElementById('showForm');
form.addEventListener('submit', function(evt) {
    var inizio = this.ora_inizio.value;
    var fine = this.ora_fine.value;
    var durataFilm = document.getElementById('durata').innerHTML;

    inizio = Date.parse('01/01/2001 ' + inizio);
    fine = Date.parse('01/01/2001 ' + fine);
    //durataFilm = Date.parse('01/01/2001 ' + durataFilm); PROBLEMA FORMATTAZIONE TEMPO
    durata = fine - inizio;

    if (durata > 0 /*&& durata > durataFilm*/) {
        // submit form
        return true;
    } else {
        // blocco form
        formBlock(evt);
    }
});

/*
 * se l'ora di inizio è superiore all'ora di fine, aggiungo la classe .error altrimenti
 * la elimino
 */
var inizio = form.ora_inizio;
inizio.addEventListener('change', function() {
    controlTime();
});
var fine = form.ora_fine;
fine.addEventListener('change', function() {
    controlTime();
});

function controlTime() {
    var form = document.getElementById('showForm');
    var inizio = form.ora_inizio.value;
    var fine = form.ora_fine.value;

    inizio = Date.parse('01/01/2001 ' + inizio);
    fine = Date.parse('01/01/2001 ' + fine);

    if (inizio > fine) {
        form.ora_inizio.className = 'form-control error';
    } else {
        form.ora_inizio.className = 'form-control';
    }
}