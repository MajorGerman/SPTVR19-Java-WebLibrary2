<%@page contentType="text/html" pageEncoding="UTF-8"%> 
<nav id="navbar" class="navbar navbar-expand-md navbar-light bg-light">
  <div class="container-fluid">
    <div class="collapse navbar-collapse" id="navbarNavAltMarkup">
      <div class="navbar-nav">
        <a class="nav-link" href="buyProductForm">Каталог</a>
        <a class="nav-link" href="addProductForm" ${managerhidden}hidden>  Добавить товар  </a>
        <a class="nav-link" href="editProductForm1" ${managerhidden}hidden>  Изменить товар  </a>
        <a class="nav-link" href="listPersons" ${adminhidden}hidden>  Список покупателей  </a> 
        <a class="nav-link" href="editPersonForm1" ${userhidden}hidden>  Изменить данные пользователя  </a>
        <a class="nav-link" href="showProfile" ${userhidden}hidden>  Профиль  </a> 
      </div>
    </div>
  </div>
</nav>