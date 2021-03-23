<h2 class="display-5"> Список пользователей </h2>
<div class="container-fluid">
    <div class="row justify-content-md-center">
        <ul class="list-group" name="persId" multiple="true">
        <c:forEach var="user" items="${listUsers}">
            <li class="list-group-item" value="${user.id}">${user.login} (${user.person.name} ${user.person.surname}, ${user.person.money}$)
                
                <br>
                <form action="makeManager" method="POST">
                <input name="userId" value="${user.id}" hidden>                
                <button style="min-width: 200px; margin: 5px;" type="submit" class="btn btn-primary"> Сделать менеджером </button>
                </form>
                
                <form action="makeAdmin" method="POST">
                <input name="userId" value="${user.id}" hidden>                
                <button style="min-width: 200px;margin: 5px;" type="submit" class="btn btn-primary"> Сделать админом </button>
                </form>
                
                <form action="makeNobody" method="POST">
                <input name="userId" value="${user.id}" hidden>                
                <button style="min-width: 200px;margin: 5px;" type="submit" class="btn btn-danger"> Разжаловать </button>
                </form>
                
            </li>
            <br>
        </c:forEach>
    </ul>
    </div>
</div>
    