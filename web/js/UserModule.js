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
      document.getElementById('infobox').style.display = 'none';
      document.getElementById('btnRegistration').addEventListener('click', userModule.createUser); 
    }
    
    async showProfile(){
        document.getElementById('context').innerHTML =
           `<div class="row text-center m-2">     
                <h2 class="display-5"> Профиль </h2> 
                <div class="col-md">
                    <div class="text-center mx-auto">
                      <img id="avatar" src="" class="rounded" alt="Аватар">
                    </div>                
                </div>
            </div>
            <div class="row text-center">    
                <div class="col-md text-center d-flex justify-content-center">
                    <div class="text-center justify-content-center">
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
                </div> 
            </div>
            <div class="row text-center">
                <div class="col-md m-2">
                    <button id="btn1" type="button" class="btn btn-primary mb-2"> Изменить данные </button>
                </div>
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
              document.getElementById('infobox').style.display = 'none';
              console.log("Ошибка получения данных");
            }
    
            document.getElementById('name').innerHTML = result.name;
            document.getElementById('surname').innerHTML = result.surname;
            document.getElementById('login').innerHTML = result.login;
            document.getElementById('phone').innerHTML = result.phone;
            document.getElementById('money').innerHTML = result.money + '$';
            document.getElementById('avatar').src = "insertFile/" + result.coverpath;
    
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
        var result = await response.json();
        if (response.ok){
          document.getElementById('info').innerHTML = result.info;
          document.getElementById('infobox').style.display = 'block';
          console.log("Request status: "+result.requestStatus);
          authModule.printLoginForm();
        } else {
          document.getElementById('infobox').style.display = 'none';
          console.log("Ошибка получения данных");
        }
        
    }
    
    async listPersons() {
        const response = await fetch('listPersonsJson',{
        method: 'GET',
        headers: {
                'Content-Type': 'application/json;charset:utf8'
                }});
        var user_ids = [];
        var result = await response.json();
        if (response.ok){
            document.getElementById('context').innerHTML = `
                <div id="list" class="row text-center">     
                    <h2 class="display-5"> Список покупателей </h2> 
                </div>`;  
           for (let user of result) {
                document.getElementById('list').innerHTML += `
                        <div class="col-md mx-auto">
                            <ul class="list-group text-center mx-auto">
                            <li class="list-group-item" value="${user.id}">
                                
                                <img src="insertFile/${user.coverpath}" style="width: 50px;">
                                <strong>${user.login}</strong> (${user.money}$)<br>
                                <p style="color: #909090">${user.name} ${user.surname}
                                <br>${user.phone}</p>
                                
                                <hr>
                                <form>
                                <input name="userId" value="${user.id}" hidden>                
                                <button style="min-width: 200px; margin: 2px;" type="button" class="btn btn-primary" id="btn${user.id}a"> Сделать админом </button>
                                </form>

                                <form>
                                <input name="userId" value="${user.id}" hidden>                
                                <button style="min-width: 200px;margin: 2px;" type="button" class="btn btn-primary" id="btn${user.id}b"> Сделать менеджером </button>
                                </form>

                                <form>
                                <input name="userId" value="${user.id}" hidden>                
                                <button style="min-width: 200px;margin: 2px;" type="button" class="btn btn-danger" id="btn${user.id}c"> Разжаловать </button>
                                </form>
                
                            </li>
                            </ul>
                        </div>`;
                
                user_ids.push(user.id.toString());
            }
            
            console.log(user_ids)
            for (let j of user_ids) {
                console.log(j)
                document.getElementById('btn' + j + "a").addEventListener('click', () => { userModule.makeAdmin(j) } );
                document.getElementById('btn' + j + "b").addEventListener('click', () => { userModule.makeManager(j) } );
                document.getElementById('btn' + j + "c").addEventListener('click', () => { userModule.makeNobody(j) } );
            }
        } else {
          document.getElementById('infobox').style.display = 'none';
          console.log("Ошибка получения данных");
        }
    }
    
    async makeAdmin(id) {
       const user_id = id;
       const data = {"userId": user_id};
       const response = await fetch('makeAdminJson',{
            method: 'POST',
            headers: {
                'Content-Type': 'application/json;charset:utf8'
            },
            body: JSON.stringify(data)
        });
        
        var result = await response.json();
        if (response.ok){
            document.getElementById('info').innerHTML = result.info;
            document.getElementById('infobox').style.display = 'block';
            console.log("Request status: " + result.requestStatus);
            userModule.listPersons();      
        } else {
           document.getElementById('infobox').style.display = 'none';
           console.log("Ошибка получения данных");                
        }     
    }
    
    async makeManager(id) {
     const user_id = id;
     const data = {"userId": user_id};
       const response = await fetch('makeManagerJson',{
            method: 'POST',
            headers: {
                'Content-Type': 'application/json;charset:utf8'
            },
            body: JSON.stringify(data)
        });
        
        var result = await response.json();
        if (response.ok){
            document.getElementById('info').innerHTML = result.info;
            document.getElementById('infobox').style.display = 'block';
            console.log("Request status: " + result.requestStatus);
            userModule.listPersons();            
        } else {
           document.getElementById('infobox').style.display = 'none';
           console.log("Ошибка получения данных");                
        }     
    }
    
    async makeNobody(id) {
       const user_id = id;
       const data = {"userId": user_id};
       const response = await fetch('makeNobodyJson',{
            method: 'POST',
            headers: {
                'Content-Type': 'application/json;charset:utf8'
            },
            body: JSON.stringify(data)
        });
        
        var result = await response.json();
        if (response.ok){
            document.getElementById('info').innerHTML = result.info;
            document.getElementById('infobox').style.display = 'block';
            console.log("Request status: " + result.requestStatus);
            userModule.listPersons();            
        } else {
           document.getElementById('infobox').style.display = 'none';
           console.log("Ошибка получения данных");                
        }     
    }
    
}
const userModule = new UserModule();
export {userModule};