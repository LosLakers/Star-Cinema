/* controllo che l'ora di inizio sia inferiore a quella di fine 
 * e compresa nella durata del film [NON COMPLETA].
 */
var form = document.getElementById('showForm');
form.addEventListener('submit', function(evt) {
    var inizio = document.getElementById('inizio').value;
    var fine = document.getElementById('fine').value;
    var durataFilm = document.getElementById('durata').innerHTML;
    
    inizio = Date.parse('01/01/2001 ' + inizio);
    fine = Date.parse('01/01/2001 ' + fine);
    //durataFilm = Date.parse('01/01/2001 ' + durataFilm); PROBLEMA FORMATTAZIONE TEMPO
    durata = fine - inizio;

    if (durata > 0 /*&& durata > durataFilm*/ ) {
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
var inizio = document.getElementById('inizio');
inizio.addEventListener('change', function() {
   controlTime();
});
var fine = document.getElementById('fine');
fine.addEventListener('change', function() {
   controlTime(); 
});

function controlTime() {
   var inizio = document.getElementById('inizio').value;
   var fine = document.getElementById('fine').value;
   
   inizio = Date.parse('01/01/2001 ' + inizio);
   fine = Date.parse('01/01/2001 ' + fine);
   
   if (inizio > fine) {
       document.getElementById('inizio').className = 'form-control error';
   } else {
       document.getElementById('inizio').className = 'form-control';
   }
}