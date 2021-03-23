<h2 class="display-5"> Изменить данные пользователя </h2> 
<form action="editPerson" method="POST">
    <input type="hidden" name="persId" value="${pers.id}">
    <div class="mb-3">
        <label for="exampleInputEmail1" class="form-label">Имя</label>
        <input name="name" type="text" value="${pers.name}" class="form-control" id="exampleInputPassword1">
    </div>   
    <div class="mb-3">
        <label for="exampleInputEmail1" class="form-label">Фамилия</label>
        <input name="surname" type="text" value="${pers.surname}" class="form-control" id="exampleInputPassword1">
    </div>    
    <div class="mb-3">
        <label for="exampleInputEmail1" class="form-label">Телефон</label>
        <input name="phone" type="text" value="${pers.phone}" class="form-control" id="exampleInputPassword1">
    </div>    
    <div class="mb-3">
        <label for="exampleInputEmail1" class="form-label">Деньги</label>
        <input name="money" min='0' value="${pers.money}" type="number" class="form-control" id="exampleInputPassword1">
    </div> 
    <div class="mb-3">
        <label for="exampleInputEmail1" class="form-label">Пароль</label>
        <input name="password" type="password" value="${user.password}" class="form-control" id="exampleInputPassword1">
    </div>    
    <button type="submit" class="btn btn-primary"> Готово </button>
</form>
        