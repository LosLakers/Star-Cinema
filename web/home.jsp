<%@include file="login_navbar.jsp" %>

<div class="jumbotron">
    <div class="container">
        <h1>Star(k) Cinema Home</h1>
        <div class="col-lg-6">
            <!-- Nav tabs -->
            <ul class="nav nav-tabs">
                <li class="active">
                    <a href="#oggi" data-toggle="tab">Oggi</a>
                </li>
                <li>
                    <a href="#domani" data-toggle="tab">Domani</a>
                </li>
                <li>
                    <a href="#dopodomani" data-toggle="tab">Dopodomani</a>
                </li>
            </ul>
        </div>
        <!-- Tab panes -->        
        <div class="tab-content">
            <div class="tab-pane active" id="oggi">
                <table class="table table-bordered table-hover">
                    <thead>
                        <tr class="info">
                            <th>
                                Titolo
                            </th>
                            <th>
                                Orario
                            </th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>
                                <a href="#">Film1</a>
                            </td>
                            <td>
                                Orario1 - Orario2
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <a href="#">Film2</a>
                            </td>
                            <td>
                                Orario1 - Orario2
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
            <div class="tab-pane" id="domani">
                <table class="table">
                    <thead>
                        <tr>
                            <th>
                                Titolo
                            </th>
                            <th>
                                Orario
                            </th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>
                                <a href="#">Film3</a>
                            </td>
                            <td>
                                Orario1 - Orario2
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <a href="#">Film4</a>
                            </td>
                            <td>
                                Orario1 - Orario2
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
            <div class="tab-pane" id="dopodomani">
                <table class="table">
                    <thead>
                        <tr>
                            <th>
                                Titolo
                            </th>
                            <th>
                                Orario
                            </th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>
                                <a href="#">Film5</a>
                            </td>
                            <td>
                                Orario1 - Orario2
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <a href="#">Film6</a>
                            </td>
                            <td>
                                Orario1 - Orario2
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
<div class="container">
    <form id="filmsearch" action="search.jsp" class="col-lg-6 col-md-6">
        <div class="form-group">
            <label class="control-label">Ricerca Film per Titolo</label>
            <div class="controls">
                <input type="text" name="searchString" class="form-control" placeholder="Titolo" required="required"/>
            </div>
        </div>
        <button type="submit" form="filmsearch" class="form-group btn btn-default">Cerca</button>
    </form>
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
</body>
</html>