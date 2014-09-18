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
        alert('Ora Inizio deve essere sempre minore di Ora Fine');
    }
});

/*
 * se l'ora di inizio è superiore all'ora di fine, aggiungo la classe .error altrimenti
 * la elimino
 */
var inizio = form.ora_inizio;
inizio.addEventListener('change', function() {
    // cambio valore di fine in base ad inizio inserito
    var form = document.getElementById('showForm');
    
    // gestione ora fine
    var durataFilm = document.getElementById('durata').innerHTML;
    durataFilm = durataFilm.split(':');
    var inizio = this.value.split(':');
    var hours = Number(durataFilm[0]);
    var minutes = Number(durataFilm[1]) + 30;
    
    if (minutes >= 60) {
        hours++;
        minutes = minutes - 60;
    }
    
    var start = Number(inizio[0]) + hours;
    var end = Number(inizio[1]) + minutes;
    if (end >= 60) {
        start++;
        end = end - 60;
    }
    start = (start < 10) ? '0' + start : start;
    end = (end < 10) ? '0' + end : end;
    form.ora_fine.value = start + ':' + end;
    
    // controllo ora fine e ora inizio
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
        var div = form.ora_inizio.parentNode;
        div.className = 'form-group has-feedback has-error';
        var span = div.getElementsByClassName('glyphicon form-control-feedback');
        span[0].className = 'glyphicon glyphicon-remove form-control-feedback';
    } else {
        var div = form.ora_inizio.parentNode;
        div.className = 'form-group has-feedback has-success';
        var span = div.getElementsByClassName('glyphicon form-control-feedback');
        span[0].className = 'glyphicon glyphicon-ok form-control-feedback';
    }
}