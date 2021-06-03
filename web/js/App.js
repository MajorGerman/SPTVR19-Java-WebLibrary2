import {authModule} from './AuthModule.js';
import {userModule} from './UserModule.js';
import {productModule} from './ProductModule.js';

document.getElementById("printIndex").onclick = function (){
    toogleMenuActive("printIndex");
    authModule.printIndex();
};
document.getElementById("buyProductForm").onclick = function (){
    toogleMenuActive("buyProductForm");
    productModule.printBuyProductForm();
};
document.getElementById("addProductForm").onclick = function (){
    toogleMenuActive("addProductForm");
    productModule.printAddProductForm();
};
document.getElementById("editProductForm").onclick = function (){
    toogleMenuActive("editProductForm");
    productModule.printEditProductForm();
};
document.getElementById("discountForm").onclick = function (){
    toogleMenuActive("discountForm");
};
document.getElementById("listPersons").onclick = function (){
    toogleMenuActive("listPersons");
    userModule.listPersons();
};
document.getElementById("showProfile").onclick = function (){
    toogleMenuActive("showProfile");
    userModule.showProfile();
};
document.getElementById("loginForm").onclick = function (){
    toogleMenuActive("loginForm");
    authModule.printLoginForm();
};
document.getElementById("regForm").onclick = function (){
    toogleMenuActive("regForm");
    userModule.registration();
};
document.getElementById("logout").onclick = function (){
    toogleMenuActive("logout");
    authModule.logout();
};

authModule.toogleMenu();
document.getElementById("body").onload = function() {
    authModule.printIndex();
};



function toogleMenuActive(elementId){
   const activeElement = document.getElementById(elementId);
   const passiveElements = document.getElementsByClassName("nav-link");
    for (let i = 0; i < passiveElements.length; i++) {
        if (activeElement === passiveElements[i]){
            passiveElements[i].classList.add("active-menu");
        } else {
            if (passiveElements[i].classList.contains("active-menu")){
                passiveElements[i].classList.remove("active-menu");
            }
        }
    }
}
