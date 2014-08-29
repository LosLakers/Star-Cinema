// controllo ogni volta che una checkbox viene cambiata
var form = document.forms[id = 'seatform'];
for (var i = 0; i < form.seat.length; i++) {
    form.seat[i].addEventListener('change', function() {
        var form = document.forms[id = 'seatform'];
        var checkbox = form.seat.length;
        var user_checkable = Number(document.getElementById('ticketcount').value);
        var checked = 0;
        for (var i = 0; i < checkbox; i++) {
            if (form.seat[i].checked && !form.seat[i].disabled) {
                checked++;
            }
        }
        if (checked > user_checkable) {
            this.checked = false;
            alert("Numero massimo di posti selezionabili per l'utente = " + user_checkable);
        }
    });
}

// controllo nel submit che non siano stati selezionati più posti di quelli possibili
form.addEventListener('submit', function(evt) {
    var checkbox = this.seat.length;
    var user_checkable = Number(document.getElementById('ticketcount').value);
    var checked = 0;
    for (var i = 0; i < checkbox; i++) {
        if (this.seat[i].checked && !form.seat[i].disabled) {
            checked++;
        }
    }
    if (checked > user_checkable || checked === 0) {
        // blocco submit
        formBlock(evt);
        alert("Impossibile completare l'operazione");
    } else {
        return true;
    }
});

// back button
var back = document.getElementById('backbutton');
back.addEventListener('click', function() {
    backButton();
});
