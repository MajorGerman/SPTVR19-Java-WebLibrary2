package web.webinf.manager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.MoveTargetOutOfBoundsException;
import web.IndexPage;

public class AddProductPage {
    protected WebDriver driver;
    private final By nameBy = By.id("name");
    private final By priceBy = By.id("price");
    private final By tagBy = By.id("tag");
    private final By descriptionBy = By.id("description");
    private final By photoBy = By.id("photo");
    private final By addButtonBy = By.id("addButton");
    
    public AddProductPage(WebDriver driver) {
        this.driver = driver;
    }

    public IndexPage addProduct(String name, String price, String tag, String description, String photo) throws MoveTargetOutOfBoundsException{
        driver.findElement(nameBy).sendKeys(name);
        driver.findElement(priceBy).sendKeys(price);
        driver.findElement(tagBy).sendKeys(tag);
        driver.findElement(descriptionBy).sendKeys(description);
        driver.findElement(photoBy).sendKeys(photo);
        WebElement element = driver.findElement(addButtonBy);
        Actions actions = new Actions(driver);
        actions.moveToElement(element).click().perform();
        return new IndexPage(driver);
    }
    
}