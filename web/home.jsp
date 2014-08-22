<%@include file="login_navbar.jsp" %>

<div class="jumbotron">
    <div class="container">
        <h1>Star(k) Cinema Home</h1>
        <br/>
        <!-- Acquisto Biglietti -->
        <div class="col-lg-6 col-md-6">
            <form id="ticketfilm" class="form-inline">
                <div class="form-group col-lg-7 col-md-7">
                    <select name="id_film" class="form-control" required="required">
                        <option selected="selected" disabled="true">Acquista Biglietto per un Film...</option>
                        <option>Film 1</option>
                        <option>Film 2</option>
                    </select>
                </div>
                <button type="submit" form="ticketfilm" class="form-group btn btn-default">Conferma</button>
            </form>
        </div>
    </div>
</div>
<div class="container">
    <br/>
    <div class="col-lg-12 col-md-12">
        <!-- Ricerca Film per Titolo -->
        <form id="filmsearch" action="search.jsp" class="col-lg-6 col-md-6">
            <div class="form-group">
                <label class="control-label">Ricerca Film per Titolo</label>
                <div class="controls">
                    <input type="text" name="searchString" class="form-control" placeholder="Titolo" required="required"/>
                </div>
            </div>
            <button type="submit" form="filmsearch" class="form-group btn btn-default">Cerca</button>
        </form>
        <!-- Ricerca Film per Data -->
        <form id="datasearch" action="search.jsp" class="col-lg-6">
            <div class="form-group">
                <label class="control-label">Ricerca Film per Data</label>
                <div class="controls">
                    <input type="date" name="searchString" class="form-control" placeholder="Data" required="required"/>
                </div>
            </div>
            <button type="submit" form="datasearch" class="btn btn-default">Cerca</button>
        </form>
    </div>
</div>
</body>
</html>