import {authModule} from './AuthModule.js';
class UserModule{
    
    registration(){
        document.getElementById('context').innerHTML=
        `       
            <div hidden id="infobox" style="width: 100%;" class="alert alert-info alert-dismissible fade show" data-bs-dismiss="alert" role="alert">
                <strong id="info"></strong>
            </div> 
            <div class="text-center justify-content-center mx-auto m-2">
            <h2 class="display-5"> Регистрация </h2>
            <br>
            <form action="createUser" method="POST" onsubmit="false">
                <div class="row">
                <div class="col-md-3 mb-3 mx-auto"></div>
                  <div class="col-md-3 mb-3 mx-auto">
                    <input type="text" id="firstname" class="form-control" placeholder="Имя" aria-label="Имя">
                  </div>
                  <div class="col-md-3 mb-3 mx-auto">
                    <input type="text" id="lastname" class="form-control" placeholder="Фамилия" aria-label="Фамилия">
                  </div>
                <div class="col-md-3 mb-3 mx-auto"></div>
                </div>
                <div class="row">
                <div class="col-md-3 mb-3 mx-auto"></div>
                  <div class="col-md-3 mb-3">
                    <input type="text" id="phone" class="form-control" placeholder="Телефон" aria-label="Телефон">
                  </div>
                  <div class="col-md-3 mb-3">
                    <input type="text" id="money" class="form-control" placeholder="Деньги" aria-label="Деньги">
                  </div>
                <div class="col-md-3 mb-3 mx-auto"></div>
                </div>
                <div class="row">
                  <div class="col-md-3 mb-3 mx-auto"></div>
                  <div class="col-md-3 mb-3">
                    <input type="text" id="login" class="form-control" placeholder="Логин" aria-label="Логин">
                  </div>
                  <div class="col-md-3 mb-3">
                    <input type="text" id="password" class="form-control" placeholder="Пароль" aria-label="Пароль">
                  </div>
                  <div class="col-md-3 mb-3 mx-auto"></div>
                </div>
                <div class="row">
                  <div class="col-md-auto mb-3 mx-auto">
                    <input type="button" id="btnRegistration" class="form-control btn btn-primary" value="Готово">
                  </div>
                </div>
          
          </form>
      </div>`;

      document.getElementById('info').innerHTML='';
      document.getElementById('btnRegistration').addEventListener('click', userModule.createUser); 
    }
    
    async showProfile(){
        document.getElementById('context').innerHTML=
           `<div class="text-center justify-content-md-center m-auto">  
                <h2 class="display-5"> Профиль </h2> 
                <div class="col-md mb-3 mx-auto"></div>
                <div class="col-md-4 justify-content-md-center mx-auto ml-5">
                    <ul class="list-group list-group-horizontal">            
                        <li class="list-group-item disabled">Логин</li>
                        <li id='login' class="list-group-item "></li>
                    </ul>
                    <ul class="list-group list-group-horizontal">            
                        <li class="list-group-item disabled">Деньги</li>
                        <li id='money' class="list-group-item"></li>
                    </ul>
                    <ul class="list-group list-group-horizontal">            
                        <li class="list-group-item disabled">Имя</li>
                        <li id='name' class="list-group-item "></li>
                    </ul>
                    <ul class="list-group list-group-horizontal">            
                        <li class="list-group-item disabled">Фамилия</li>
                        <li id='surname' class="list-group-item"></li>
                    </ul>
                    <ul class="list-group list-group-horizontal">            
                        <li class="list-group-item disabled">Телефон</li>
                        <li id='phone' class="list-group-item "></li>
                    </ul>
                </div> 
                <div class="col-md-auto mb-3 mx-auto"></div>
            </div>`;
    
            const response = await fetch('showProfileJson',{
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json;charset:utf8'
                }});
            
            var result = await response.json();
            if (response.ok){
              console.log("Request status: "+result.requestStatus);
            } else {
              console.log("Ошибка получения данных");
            }
    
            document.getElementById('name').innerHTML = result.name;
            document.getElementById('surname').innerHTML = result.surname;
            document.getElementById('login').innerHTML = result.login;
            document.getElementById('phone').innerHTML = result.phone;
            document.getElementById('money').innerHTML = result.money + '$';
    
    }

    async createUser(){
        document.getElementById('info').innerHTML='';
        const firstname = document.getElementById("firstname").value;
        const lastname = document.getElementById("lastname").value;
        const phone = document.getElementById("phone").value;
        const money = document.getElementById("money").value;
        const login = document.getElementById("login").value;
        const password = document.getElementById("password").value;
        const user = {
            "firstname": firstname,
            "lastname": lastname,
            "phone": phone,
            "money": money,
            "login": login,
            "password": password
        };

       const response = await fetch('createUserJson',{
            method: 'POST',
            headers: {
                'Content-Type': 'application/json;charset:utf8'
            },
            body: JSON.stringify(user)
        })
        if (response.ok){
          const result = await response.json();
          document.getElementById('info').innerHTML = result.info;
          console.log("Request status: "+result.requestStatus);
          authModule.printLoginForm();
        } else {
          console.log("Ошибка получения данных");
        }
        
    }
}
const userModule = new UserModule();
export {userModule};