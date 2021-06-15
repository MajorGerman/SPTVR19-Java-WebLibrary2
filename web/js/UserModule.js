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
                    <br><button id="btn2" type="button" class="btn btn-danger mb-2"> Уничтожить сайт! </button>                
                </div>
            </div>`;
    
            document.getElementById("btn1").addEventListener("click", userModule.printEditPersonForm);
            document.getElementById("btn2").addEventListener("click", () => {document.getElementById("html").innerHTML = "";} );
            
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
    
    async printEditPersonForm() {
    
        const response = await fetch('printEditPersonFormJson',{
            method: 'GET',
            headers: {'Content-Type': 'application/json;charset:utf8'},});        
        
            var result = await response.json();
                if (response.ok) {
                    console.log("Request status: " + result.requestStatus);
                    
                    document.getElementById("context").innerHTML = `
                        <div class='text-center justify-content-center'>
                            <h2 class="display-5"> Изменить данные </h2>
                            <form id='form3' method="POST" enctype="multipart/form-data"> 
                                <input type="hidden" id="idforedit"  value="${result.persId}" name="persId">
                                <div class="mb-3">
                                    <label for="exampleInputEmail1" class="form-label">Имя</label>
                                    <input name="name" type="text" class="form-control" id="name" required>
                                </div>
                                <div class="mb-3">
                                    <label for="exampleInputEmail1" class="form-label">Фамилия</label>
                                    <input name="surname" type="text" class="form-control" id="surName" required>
                                </div>    
                                <div class="mb-3">
                                    <label for="exampleInputEmail1" class="form-label">Телефон</label>
                                    <input name="phone" type="text" class="form-control" id="Phone" required>
                                </div>    
                                <div class="mb-3">
                                    <label for="exampleInputEmail1" class="form-label">Деньги</label>
                                    <input name="money" min='0' type="number" class="form-control" id="Money" required>
                                </div> 
                                <div class="mb-3">
                                    <label for="exampleInputEmail1" class="form-label">Пароль</label>
                                    <input name="password" type="password" class="form-control" id="Password" required>
                                </div> 
                                <div class="col-md-4 mb-3 mx-auto">
                                    <label for="exampleInputEmail1" class="form-label">Аватар</label>
                                    <input name="file" type="file" class="form-control" id="Photo" required>
                                </div>   
                                <button type="submit" id="btn1" class="btn btn-primary"> Готово </button>
                            </form>
                        </div>`;
                    
                } else {
                    document.getElementById('infobox').style.display = 'none';
                    console.log("Ошибка получения данных");
                }      
                
                document.getElementById("name").value = result.name;
                document.getElementById("surName").value = result.surname;
                document.getElementById("Money").value = result.money;
                document.getElementById("Phone").value = result.phone;
                
                document.getElementById("btn1").addEventListener("click", userModule.editPerson);
    }
    
    async editPerson() {
        let form = new FormData(document.getElementById('form3'));
        form.append("persId", document.getElementById("idforedit").value);
        form.append("surname", document.getElementById("surName").value);
        form.append("money", document.getElementById("Money").value);
        form.append("phone", document.getElementById("Phone").value);
        form.append("password", document.getElementById("Password").value);
        let files = document.getElementById("Photo").files
        console.log(files[0])
        let fileName = files[0].name
        console.log(fileName)
        form.append("file", files[0], fileName);      
        
        let response = await fetch('editPersonJson',{
            method: 'POST',
            body: form,
            });     
        let result = await response.json();
           if (response.ok) {
               console.log("Request status: " + result.requestStatus);
               document.getElementById('infobox').innerHTML = result.info;
               document.getElementById('infobox').style.display = 'block';
               sessionStorage.setItem('token',JSON.stringify(result.token));
               sessionStorage.setItem('role',JSON.stringify(result.role));
               userModule.showProfile();
           } else {
               document.getElementById('infobox').style.display = 'none';
               console.log("Ошибка получения данных");                
           }
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
    
    // <a class="dropdown-item" id="btn${user.id}a"> Сделать админом  </a>
    
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
                                
                                <img src="insertFile/${user.coverpath}" style="width: 60px;">
                                <strong>${user.login}</strong> (${user.money}$)<br>
                                <p style="color: #909090">${user.name} ${user.surname}
                                <br>${user.phone}</p>

                                <div class="dropdown show">
                                    <a class="btn btn-secondary dropdown-toggle" href="#" role="button" id="dropdownMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                      Dropdown link
                                    </a>

                                    <div class="dropdown-menu" aria-labelledby="dropdownMenuLink">
                                      <a class="dropdown-item" href="#">Action</a>
                                      <a class="dropdown-item" href="#">Another action</a>
                                      <a class="dropdown-item" href="#">Something else here</a>
                                    </div>
                                  </div>
                      
                `;
                

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