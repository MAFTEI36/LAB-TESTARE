package org.example.tests;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class FormTest {
    WebDriver driver;
    WebDriverWait wait;

    // Test data (un singur set, fără duplicate)
    static public String URL = "https://demoqa.com/automation-practice-form";
    static public String FIRST_NAME = "Mihai";
    static public String LAST_NAME = "Maftei";
    static public String GENDER = "Male"; // folosim label pentru click
    static public String EMAIL = "mamihai50@gmail.com";
    static public String NUMBER = "068385135";
    static public String DATE = "15 Jun 2006"; // format folosit doar pentru logging
    static public String SUBJECTS = "Civics";
    static public String HOBBY = "Reading"; // vom folosi checkbox 'Reading'
    static public String PICTURE = "cat.png";
    static public String ADDRESS = "Str. Exemplu 1";

    @BeforeClass
    public void setUp() throws MalformedURLException {
        // Selenium Grid remote address - permite suprascriere prin variabila de mediu
        String seleniumUrl = System.getenv().getOrDefault("SELENIUM_REMOTE_URL", "http://localhost:4444");
        ChromeOptions options = new ChromeOptions();
        // Headless în CI dacă vrei (dezactivează dacă ai nevoie de debugging vizual)
        // options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1200,900");

        driver = new RemoteWebDriver(new URL(seleniumUrl), options);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(30));
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    @Test
    public void formTest() {
        try {
            // 1) deschide pagina
            driver.get(URL);

            // 2) asteapta formularul principal (id ul form-ului sau element vizibil)
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("userForm")));

            // 3) completeaza nume si prenume
            wait.until(ExpectedConditions.elementToBeClickable(By.id("firstName"))).sendKeys(FIRST_NAME);
            driver.findElement(By.id("lastName")).sendKeys(LAST_NAME);

            // 4) email
            driver.findElement(By.id("userEmail")).sendKeys(EMAIL);

            // 5) gender - click pe label (folk: Male/Female/Other)
            // gasim label-ul care contine textul gender
            WebElement genderLabel = driver.findElement(By.xpath("//label[text()='" + GENDER + "']"));
            genderLabel.click();

            // 6) mobile number
            driver.findElement(By.id("userNumber")).sendKeys(NUMBER);

            // 7) date of birth - demoqa folosește un date picker; setăm valoarea via JS pentru stabilitate
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("document.getElementById('dateOfBirthInput').value = '15 Jun 2006';");

            // 8) subjects - input dinamic
            WebElement subjectsInput = driver.findElement(By.id("subjectsInput"));
            subjectsInput.sendKeys(SUBJECTS);
            // asteapta dropdown si apasa Enter
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".subjects-auto-complete__menu")));
            subjectsInput.sendKeys(Keys.ENTER);

            // 9) hobbies - facem click pe Reading (exemplu)
            WebElement hobbyLabel = driver.findElement(By.xpath("//label[text()='" + HOBBY + "']"));
            hobbyLabel.click();

            // 10) upload picture (daca ai path local in proiect)
            // uncomment si adapteaza calea daca ai fișier în proiect
            // WebElement upload = driver.findElement(By.id("uploadPicture"));
            // upload.sendKeys("/path/to/" + PICTURE);

            // 11) current address
            driver.findElement(By.id("currentAddress")).sendKeys(ADDRESS);

            // 12) scroll la butonul Submit daca e nevoie si submit
            WebElement submitBtn = driver.findElement(By.id("submit"));
            js.executeScript("arguments[0].scrollIntoView(true);", submitBtn);
            wait.until(ExpectedConditions.elementToBeClickable(submitBtn)).click();

            // 13) asteapta modalul de confirmare (id: example-modal-sizes-title-lg)
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("example-modal-sizes-title-lg")));

            // 14) verificare simpla: modalul are titlu "Thanks for submitting the form"
            String modalTitle = driver.findElement(By.id("example-modal-sizes-title-lg")).getText();
            Assert.assertTrue(modalTitle.toLowerCase().contains("thanks"), "Modal title nu contine 'Thanks'");

        } catch (Exception e) {
            // dump page source in logs pentru debugging (va apărea în output Maven)
            try {
                System.err.println("=== PAGE SOURCE START ===");
                System.err.println(driver.getPageSource());
                System.err.println("=== PAGE SOURCE END ===");
            } catch (Exception ignore) {}

            // re-throw pentru ca surefire sa marcheze testul ca failed si sa vedem stacktrace
            throw e;
        }
    }

    @AfterClass
    public void closeDriver() {
        if (driver != null) {
            try {
                driver.quit();
            } catch (Exception ignored) {}
        }
    }
}
