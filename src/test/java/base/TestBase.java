package base;

import org.apache.log4j.PropertyConfigurator;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.locators.RelativeLocator;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;
import org.testng.log4testng.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class TestBase {
    public static WebDriver driver;
    private static Properties or = new Properties();
    private static Properties config = new Properties();
    private static FileInputStream fis;
    private static Logger log = Logger.getLogger(TestBase.class);
    //public static ExcelReader excel = new ExcelReader(".//src//test//resources//excel//testdata.xlsx");
    public static WebDriverWait wait;
    public static WebElement dropdown;
    public static List<WebElement> searchResult;

    public static FileInputStream file ;
    public static XSSFWorkbook workbook ;
    public static Sheet sheet ;
    @Test
    public void test()
    {
        System.out.print("DDd");
    }
    @BeforeSuite
    public void setUp() throws IOException {
        // loading the log file
        PropertyConfigurator.configure("./src/main/resources/properties/log4j.properties");

        // loading the OR and Config properties
        try {
            fis = new FileInputStream("./src/main/resources/properties/Config.properties");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            config.load(fis);
            log.info("Config properties loaded !!!");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            fis = new FileInputStream("./src/main/resources/properties/OR.properties");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            or.load(fis);
            log.info("OR properties loaded !!!");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (config.getProperty("browser").equals("chrome")) {

            ChromeOptions options = new ChromeOptions();
            options.addArguments("--remote-allow-origins=*");
            driver = new ChromeDriver(options);
            log.info("Launching Chrome !!!");

        }

        driver.navigate().to(config.getProperty("testsiteurl"));
        log.info("Navigated to : " + config.getProperty("testsiteurl"));
        driver.manage().window().maximize();
        driver.manage().timeouts()
                .implicitlyWait(Duration.ofSeconds(Integer.parseInt(config.getProperty("implicit.wait"))));
        wait = new WebDriverWait(driver, Duration.ofSeconds(Integer.parseInt(config.getProperty("explicit.wait"))));

    }
    public void click(String locator) {
        driver.findElement(getElement(locator)).click();
       // CustomListeners.testReport.get().log(Status.INFO, "Clicking on : " + locator);
    }

    public void type(String locator, String value) {
        driver.findElement(getElement(locator)).sendKeys(value);
    }
    public WebElement getWebElement(String locator){
        return driver.findElement(getElement(locator));
    }


    public void select(String locator, String value) {

        dropdown=driver.findElement(getElement(locator));
        Select select = new Select(dropdown);
        select.selectByVisibleText(value);
        //CustomListeners.testReport.get().log(Status.INFO, "Selecting from dropdown : " + locator + " value as " + value);
    }
    public By getElement(String locator){
        By by=null;
        if (locator.endsWith("_CSS")) {
            by =By.cssSelector(or.getProperty(locator));
        } else if (locator.endsWith("_XPATH")) {
            by = By.xpath(or.getProperty(locator));
        } else if (locator.endsWith("_ID")) {
            by= By.id(or.getProperty(locator));
        }else if (locator.endsWith("_TAG")){
            by= By.tagName(or.getProperty(locator));
        }
        return by;
    }
    public List<WebElement> listOfElement(String relativeElement,String baseElement) {
        return driver.findElements(RelativeLocator.with(getElement(relativeElement)).toRightOf(getElement(baseElement)));
    }
    public List<WebElement> listOfElement(String locator) {
        return driver.findElements(getElement(locator));
    }

    public String getDomainName(String domain){
        String domainName=config.getProperty(domain);
        return domainName;
    }
    public static String getTestCaseID(String methodName){
        return config.getProperty(methodName);
    }
    public boolean isElementPresent(By by) {

        try {

            driver.findElement(by);
            return true;

        } catch (NoSuchElementException e) {

            return false;

        }

    }
    @BeforeMethod
    public void before(){
        System.out.println("before");
    }
    @AfterMethod
    public void after(){
        System.out.println("after");
        driver.navigate().to(config.getProperty("testsiteurl"));
    }

    @AfterSuite
    public void tearDown() {

        driver.quit();
        log.info("Test Execution completed !!!");
    }
}
