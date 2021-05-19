/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.webinf.menu;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import web.webinf.guest.LoginFormPage;
import web.webinf.manager.AddProductPage;

/**
 *
 * @author jvm
 */
public class MenuPage {
    protected WebDriver driver;
    private final By infoBy = By.id("info");
    private final By loginformBy = By.id("loginForm");
    private final By addProductFormBy = By.id("addProductFormPage");
    private final By adminformBy = By.id("adminForm");
    private final By listReadersBy = By.id("adminForm");
    private final By logoutBy = By.id("logout");
    public MenuPage(WebDriver driver) {
        this.driver = driver;
    }
    
    public String getMessageInfo(){
        return driver.findElement(infoBy).getText();
    }
    
    public LoginFormPage getLoginFormPage(){
        driver.findElement(loginformBy).click();
        return new LoginFormPage(driver);
    }

    public AddProductPage getAddProductPage(){
        driver.findElement(addProductFormBy).click();
        return new AddProductPage(driver);
    }

    public void logout() {
        driver.findElement(logoutBy).click();
    }
}
