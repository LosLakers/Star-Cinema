// ad ogni sala associo l'evento per sbloccare le date
var sala = document.getElementById('sala');
sala.addEventListener('change', function() {
    disableDate();
});
sala.addEventListener('load', function() {
    disableDate();
});

// ad ogni data associo l'evento per sbloccare gli orari
var data = document.getElementById('data');
data.addEventListener('change', function() {
   var radio = document.getElementsByName('orario');
   for (var j = 0; j < radio.length; j++) {
       radio[j].disabled = false;
   }
});

// funzione per disabilitare date
function disableDate() {
    num_sala = document.getElementById('sala').value;
    document.getElementById('data').disabled = false;
    var date = document.getElementById('data').getElementsByTagName('option');
    var day = Date.today();
    for (var i = 1; i < date.length; i++) {
        // controllo sugli hidden
        day = day.addDays(1);
        var hidden = document.getElementById(day.toString('yyyy-MM-dd'));
        if (hidden !== null) {
            var hiddenvalue = hidden.value;
            hiddenvalue = hiddenvalue.split(':');
            for (var j = 0; j < hiddenvalue.length; j++) {
                if (num_sala === hiddenvalue[j]) {
                    date[i].disabled = true;
                    break;
                } else {
                    date[i].disabled = false;
                }
            }
        } else {
            date[i].disabled = false;
        }
    }
}