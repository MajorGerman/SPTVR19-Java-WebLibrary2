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
                <div class="col-md-3 mb-3 mx-auto">
                    <input placeholder="Логин" name="login" type="text" class="form-control" id="login">
                </div>
            </div>
            <div class="row mx-auto">
                <div class="col-md-3 mb-3 mx-auto">
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
       var result = await response.json();
       if(response.ok){
        document.getElementById('info').innerHTML = result.info;
        document.getElementById('infobox').style.display = 'block';
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
        document.getElementById('infobox').style.display = 'none';
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
      const result = await response.json();
      if(response.ok){
        authModule.printLoginForm();
        if(result.requestStatus){
          sessionStorage.removeItem('token');
          sessionStorage.removeItem('role');
        }
      }
      authModule.toogleMenu();
      
    }
    
    async printIndex() {
        document.getElementById('context').innerHTML = `
            <div id='index-body'>  
                <h2 class="display-5 text-center mx-auto">Добро пожаловать в кухонный магазин Георга Лаабе!</h2>  
                <h4 id="cool" class="display-6 text-center mx-auto" style="color: grey;">У нас есть самые крутые товары!</h4> 
            </div>

            <div class="row justify-content-md-center text-center mx-auto">

                <div id="carouselExampleControls" class="carousel slide" data-bs-ride="carousel">
                  <div class="carousel-inner">
                    <div class="carousel-item active">
                      <img src="insertFile/E:\\UploadFolder\\spoons1.jpg" class="w-50" alt="spoons">
                    </div>
                    <div class="carousel-item">
                      <img src="insertFile/E:\\UploadFolder\\pan1.jpg" class="w-50" alt="pan">
                    </div>
                    <div class="carousel-item">
                      <img src="insertFile/E:\\UploadFolder\\spatula1.jpg" class="w-50" alt="spatula">
                    </div>
                  </div>
                  <button class="carousel-control-prev" type="button" data-bs-target="#carouselExampleControls"  data-bs-slide="prev">
                    <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                    <span class="visually-hidden"> Назад </span>
                  </button>
                  <button class="carousel-control-next" type="button" data-bs-target="#carouselExampleControls"  data-bs-slide="next">
                    <span class="carousel-control-next-icon" aria-hidden="true"></span>
                    <span class="visually-hidden"> Вперед </span>
                  </button>
                </div>

            </div>`; 
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
        document.getElementById("showProfile").style.display = 'none';
        document.getElementById("printIndex").style.display = 'block';
        
      }else if(role === "customer"){
        document.getElementById("buyProductForm").style.display = 'block';
        document.getElementById("loginForm").style.display = 'none';
        document.getElementById("logout").style.display = 'block';
        document.getElementById("regForm").style.display = 'none'
        document.getElementById("addProductForm").style.display = 'none';
        document.getElementById("editProductForm").style.display = 'none';
        document.getElementById("discountForm").style.display = 'none';
        document.getElementById("listPersons").style.display = 'none';
        document.getElementById("showProfile").style.display = 'block';
        document.getElementById("printIndex").style.display = 'block';
        
      }else if(role === "manager"){
        document.getElementById("buyProductForm").style.display = 'block';
        document.getElementById("loginForm").style.display = 'none';
        document.getElementById("logout").style.display = 'block';
        document.getElementById("regForm").style.display = 'none'
        document.getElementById("addProductForm").style.display = 'block';
        document.getElementById("editProductForm").style.display = 'block';
        document.getElementById("discountForm").style.display = 'block';
        document.getElementById("listPersons").style.display = 'none';
        document.getElementById("showProfile").style.display = 'block';
        document.getElementById("printIndex").style.display = 'block';
        
      }else if(role === "admin"){
        document.getElementById("buyProductForm").style.display = 'block';
        document.getElementById("loginForm").style.display = 'none';
        document.getElementById("logout").style.display = 'block';
        document.getElementById("regForm").style.display = 'none'
        document.getElementById("addProductForm").style.display = 'block';
        document.getElementById("editProductForm").style.display = 'block';
        document.getElementById("discountForm").style.display = 'block';
        document.getElementById("listPersons").style.display = 'block';
        document.getElementById("showProfile").style.display = 'block';
        document.getElementById("printIndex").style.display = 'block';
      }
    
    }
   
}
const authModule = new AuthModule();
export {authModule};