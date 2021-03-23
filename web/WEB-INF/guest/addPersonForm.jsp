<%@page contentType="text/html" pageEncoding="UTF-8"%>
<div class="row justify-content-md-center">
    <h2 class="display-5"> Регистрация </h2>
    <form action="addPerson" method="POST">    
        <div class="mb-3">
            <label for="exampleInputEmail1" class="form-label">Имя</label>
            <input name="name" type="text" value="${name}" class="form-control" id="exampleInputEmail1">
        </div>
        <div class="mb-3">
            <label for="exampleInputEmail1" class="form-label">Фамилия</label>
            <input name="surname" type="text" value="${surname}" class="form-control" id="exampleInputPassword1">
        </div>
        <div class="mb-3">
            <label for="exampleInputEmail1" class="form-label">Телефон</label>
            <input name="phone" type="text" value="${phone}" class="form-control" id="exampleInputPassword1">
        </div>
        <div class="mb-3">
            <label for="exampleInputEmail1" class="form-label">Деньги</label>
            <input name="money" min="0" type="number" value="${money}" class="form-control" id="exampleInputPassword1">
        </div>
        <div class="mb-3">
            <label for="exampleInputEmail1" class="form-label">Логин</label>
            <input name="login" type="text" value="${login}" class="form-control" id="exampleInputPassword1">
        </div>  
        <div class="mb-3">
            <label for="exampleInputPassword1" class="form-label">Пароль</label>
            <input name="password" type="password" value="" class="form-control" id="exampleInputPassword1">
        </div>
        <button type="submit" class="btn btn-primary"> Готово </button>
    </form>
</div>