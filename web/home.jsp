<%@include file="login_navbar.jsp" %>

<div class="jumbotron">
    <br/>
    <div class="container">
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
</body>
</html>