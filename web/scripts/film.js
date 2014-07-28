// javascript per voto di un film
var i,j;
var stars = document.getElementsByClassName('glyphicon-star able');
for (i = 0; i < stars.length; i++) {
    stars[i].addEventListener('click', function() {
        var value = this.getAttribute('data-star');
        var starlist = document.getElementsByClassName('glyphicon-star able');
        for (j = 0; j < starlist.length; j++) {
            if (starlist[j].getAttribute('data-star') <= value) {
                starlist[j].className = starlist[j].className.replace('btn-default','');
                starlist[j].className = starlist[j].className.replace('btn-warning','');
                starlist[j].className += ' btn-warning';
            } else {
                starlist[j].className = starlist[j].className.replace('btn-warning','');
                starlist[j].className = starlist[j].className.replace('btn-default','');
                starlist[j].className += ' btn-default';
            }
        }
        document.getElementById('voto').value = value;
    });
}
var modify = document.getElementById('modify');
modify.addEventListener('click', function() {
    // sblocco i voti e la textarea
    var stars = document.getElementsByClassName('glyphicon-star able');
    for (i = 0; i < stars.length; i++) {
        stars[i].className = stars[i].className.replace('disabled','');
    }
    document.getElementById('giudizio').disabled = false;
    
    // aggiungo elementi alla form
    var form = document.forms[id = 'updatecomment'];
    stars = document.getElementsByClassName('glyphicon-star able');
    addHidden(form, 'voto', stars.length, 'voto');
    addHidden(form, 'status', 'updatecommento', '#');
    addSubmit(form, 'updatecomment', 'Aggiorna');
    
    // elimino il bottone
    removeElement(this);
});
