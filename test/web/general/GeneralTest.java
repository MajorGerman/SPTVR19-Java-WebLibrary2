package web.general;

import java.util.concurrent.TimeUnit;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.MoveTargetOutOfBoundsException;
import web.webinf.manager.AddProductPage;
import web.webinf.menu.MenuPage;

public class GeneralTest {
    static private WebDriver driver;
    private final MenuPage menuPage = new MenuPage(driver);
    private final AddProductPage addProductPage = new AddProductPage(driver);
    
    public GeneralTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        System.setProperty("webdriver.chrome.driver", "lib/chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);       
        driver.get("http://localhost:8080/SPTVR19-Java-WebLibrary2");
        driver.manage().window().maximize();
    }
    
    @AfterClass
    public static void tearDownClass() throws InterruptedException {
        Thread.sleep(3000);
        driver.quit();
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void loginUserTest(){
        System.out.println("loginUserTest: ");
        String result = menuPage.getLoginFormPage().loginValidUser("admin", "12345").getMessageInfo();
        String expected = "Успешный вход!";
        System.out.println(" Expected: "+ expected);
        System.out.println(" Result: " + result);
        Assert.assertEquals(result, expected);
    }
    
    @Test
    public void addProductTest(){
        System.out.println("addProductTest: ");
        //menuPage.getLoginFormPage().loginValidUser("admin", "12345");
        String result = menuPage.getAddProductPage().addProduct("Pizza", "25", "Food", "Very tasty!", "E://pizza3.jpg").getMessageInfo();
        String expected = "Товар успешно добавлен!";
        System.out.println(" Expected: "+ expected);
        System.out.println(" Result: " + result);
        Assert.assertEquals(result, expected);
    }    
    
    @Test
    public void logoutTest(){
        //menuPage.getLoginFormPage().loginValidUser("admin", "12345");
        System.out.println("logoutTest: ");
        menuPage.logout();
        String result = menuPage.getMessageInfo();
        String expected = "Успешный выход!";
        System.out.println(" Expected: "+ expected);
        System.out.println(" Result: " + result);
        Assert.assertEquals(result, expected);
    }
      
}
