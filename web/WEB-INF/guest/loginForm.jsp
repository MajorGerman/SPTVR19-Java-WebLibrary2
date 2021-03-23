<%@ page pageEncoding="UTF-8" %>
<div class="row justify-content-md-center">
    <h2 class="display-5"> Авторизация </h2>
    <form action="login" method="POST">
        <div class="mb-3">
            <label for="exampleInputEmail1" class="form-label">Логин</label>
            <input name="login" type="text" class="form-control" id="exampleInputEmail1">
        </div>
        <div class="mb-3">
            <label for="exampleInputPassword1" class="form-label">Пароль</label>
            <input name="password" type="password" class="form-control" id="exampleInputPassword1">
        </div>
        <button type="submit" class="btn btn-primary"> Готово </button>
    </form>
</div>
