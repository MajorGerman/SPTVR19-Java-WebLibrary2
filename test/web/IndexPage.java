package web;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class IndexPage {
    protected WebDriver driver;
    private final By infoBy = By.id("info");
    public IndexPage(WebDriver driver) {
        this.driver = driver;
    }
    public String getMessageInfo(){
        return driver.findElement(infoBy).getText();
    }
}
