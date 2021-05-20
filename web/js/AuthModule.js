import {userModule} from './UserModule.js';

class AuthModule{
    
    printLoginForm(){
       document.getElementById('context').innerHTML =
        `
        <div hidden id="infobox" style="width: 100%;" class="alert alert-info alert-dismissible fade show" data-bs-dismiss="alert" role="alert">
            <strong id="info"></strong>
        </div> 
        <div class="text-center justify-content-center m-2">
            <h2 class="display-5"> Авторизация </h2>
            <br>
            <div class="row">
                <div class="col-md-4 mb-3 mx-auto">
                    <input placeholder="Логин" name="login" type="text" class="form-control" id="login">
                </div>
            </div>
            <div class="row mx-auto">
                <div class="col-md-4 mb-3 mx-auto">
                    <input placeholder="Пароль" name="password" type="password" class="form-control" id="password">
                </div>
            </div>
            <button id="btn1" type="button" class="btn btn-primary mb-2"> Готово </button>
            <br>
            <a id="registration-link" href="#reg">Нет учетной записи?</a>
        </div>`
        ;

        document.getElementById('btn1').addEventListener('click', authModule.auth); 
        document.getElementById('registration-link').addEventListener('click',userModule.registration); 
        
    }
    async auth(){
       const login = document.getElementById('login').value;
       const password = document.getElementById('password').value;
       const credential = {
         "login": login,
         "password": password
       };
       const response = await fetch('loginJson', {
         method: 'POST',
         headers: {
          'Content-Type': 'application/json;charset:utf8'
         },
         body: JSON.stringify(credential)
       });
       if(response.ok){
        const result = await response.json();
        document.getElementById('info').innerHTML = result.info;
        console.log("Request status: "+result.requestStatus);
        document.getElementById('context').innerHTML='';
        if(result.requestStatus){
          sessionStorage.setItem('token',JSON.stringify(result.token));
          sessionStorage.setItem('role',JSON.stringify(result.role));
        }else{
          if(sessionStorage.getItem(token) !== null){
            sessionStorage.removeItem('token');
            sessionStorage.removeItem('role');
          }
        }
      }else{
        console.log("Ошибка получения данных");
      }
      authModule.toogleMenu();
      // console.log('Auth: token'+sessionStorage.getItem('token'));
      // console.log('Auth: role'+sessionStorage.getItem('role'));
    }
    
    async logout(){
      const response = await fetch('logoutJson',{
        method: 'GET',
        headers:{
          'Content-Type': 'application/json;charset:utf8'
        }
      });
      if(response.ok){
        const result = await response.json();
        authModule.printLoginForm();
        if(result.requestStatus){
          sessionStorage.removeItem('token');
          sessionStorage.removeItem('role');
        }
      }
      authModule.toogleMenu();
      

    }
    toogleMenu(){
      let role = null;
      if(sessionStorage.getItem('role') !== null){
        role = JSON.parse(sessionStorage.getItem('role'));
      }
      console.log('Auth: token - '+sessionStorage.getItem('token'));
      console.log('Auth: role - '+sessionStorage.getItem('role'));
      
      if(role === null){
        document.getElementById("buyProductForm").style.display = 'block';
        document.getElementById("loginForm").style.display = 'block';
        document.getElementById("logout").style.display = 'none';
        document.getElementById("regForm").style.display = 'block'
        document.getElementById("addProductForm").style.display = 'none';
        document.getElementById("editProductForm").style.display = 'none';
        document.getElementById("discountForm").style.display = 'none';
        document.getElementById("listPersons").style.display = 'none';
        document.getElementById("editPersonForm").style.display = 'none';
        document.getElementById("showProfile").style.display = 'none';
        
      }else if(role==="customer"){
        document.getElementById("buyProductForm").style.display = 'block';
        document.getElementById("loginForm").style.display = 'none';
        document.getElementById("logout").style.display = 'block';
        document.getElementById("regForm").style.display = 'none'
        document.getElementById("addProductForm").style.display = 'none';
        document.getElementById("editProductForm").style.display = 'none';
        document.getElementById("discountForm").style.display = 'none';
        document.getElementById("listPersons").style.display = 'none';
        document.getElementById("editPersonForm").style.display = 'block';
        document.getElementById("showProfile").style.display = 'block';
        
      }else if(role === "manager"){
        document.getElementById("buyProductForm").style.display = 'block';
        document.getElementById("loginForm").style.display = 'none';
        document.getElementById("logout").style.display = 'block';
        document.getElementById("regForm").style.display = 'none'
        document.getElementById("addProductForm").style.display = 'block';
        document.getElementById("editProductForm").style.display = 'block';
        document.getElementById("discountForm").style.display = 'block';
        document.getElementById("listPersons").style.display = 'none';
        document.getElementById("editPersonForm").style.display = 'block';
        document.getElementById("showProfile").style.display = 'block';
        
      }else if(role === "admin"){
        document.getElementById("buyProductForm").style.display = 'block';
        document.getElementById("loginForm").style.display = 'none';
        document.getElementById("logout").style.display = 'block';
        document.getElementById("regForm").style.display = 'none'
        document.getElementById("addProductForm").style.display = 'block';
        document.getElementById("editProductForm").style.display = 'block';
        document.getElementById("discountForm").style.display = 'block';
        document.getElementById("listPersons").style.display = 'block';
        document.getElementById("editPersonForm").style.display = 'block';
        document.getElementById("showProfile").style.display = 'block';
      }
    
    }
   
}
const authModule = new AuthModule();
export {authModule};