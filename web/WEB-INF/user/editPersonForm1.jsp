<h2 class="display-5"> Изменить данные пользователя </h2> 
<form action="editPersonForm2" method="POST">
    <select style="width: 55%;margin: auto;" name="personId" class="form-select form-select-lg mb-3" aria-label=".form-select-lg example">
        <c:forEach var="pers" items="${listPersons}">
            <option value="${pers.id}">${user.login} (${pers.name} ${pers.surname} | ${pers.money}$)</option>
        </c:forEach>       
    </select>
    <button type="submit" class="btn btn-primary"> Изменить </button>
</form>

