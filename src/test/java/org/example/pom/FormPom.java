package org.example.pom;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class FormPom {
    WebDriver driver;
    JavascriptExecutor js;
    WebDriverWait wait;

    @FindBy(xpath = "//*[@id='firstName']")
    WebElement firstName;

    @FindBy(xpath = "//*[@id='lastName']")
    WebElement lastName;

    @FindBy(xpath = "//*[@id='userEmail']")
    WebElement userEmail;

    @FindBy(xpath = "//*[@id='userNumber']")
    WebElement userNumber;

    @FindBy(xpath = "//*[@id='dateOfBirthInput']")
    WebElement dateOfBirthInput;

    @FindBy(xpath = "//*[@id='subjectsInput']")
    WebElement subjectsInput;

    @FindBy(xpath = "//*[@id='state']")
    WebElement state;

    @FindBy(xpath = "//*[@id='city']")
    WebElement city;

    @FindBy(xpath = "//*[@id='submit']")
    WebElement submit;

    public FormPom(WebDriver driver) {
        this.driver = driver;
        this.js = (JavascriptExecutor) driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    // ---- Helper retry method ----
    private void retry(Runnable action, int maxAttempts, int delayMillis) {
        int attempts = 0;
        while (attempts < maxAttempts) {
            try {
                action.run();
                return;
            } catch (TimeoutException | ElementClickInterceptedException | StaleElementReferenceException e) {
                attempts++;
                try { Thread.sleep(delayMillis); } catch (InterruptedException ignored) {}
            }
        }
        throw new TimeoutException("Element nu a fost accesibil după multiple încercări!");
    }

    public void setFirstName(String firstNameParam) {
        retry(() -> {
            WebElement input = wait.until(ExpectedConditions.visibilityOf(firstName));
            input.clear();
            input.sendKeys(firstNameParam);
        }, 5, 300);
    }

    public void setLastName(String lastNameParam) {
        retry(() -> {
            WebElement input = wait.until(ExpectedConditions.visibilityOf(lastName));
            input.clear();
            input.sendKeys(lastNameParam);
        }, 5, 300);
    }

    public void setEmail(String emailParam) {
        retry(() -> {
            WebElement input = wait.until(ExpectedConditions.visibilityOf(userEmail));
            input.clear();
            input.sendKeys(emailParam);
        }, 5, 300);
    }

    public void setUserNumber(String numberParam) {
        retry(() -> {
            WebElement input = wait.until(ExpectedConditions.visibilityOf(userNumber));
            input.clear();
            input.sendKeys(numberParam);
        }, 5, 300);
    }

    public void setDateOfBirth(String dateOfBirthParam) {
        retry(() -> {
            js.executeScript("arguments[0].scrollIntoView(true);", dateOfBirthInput);
            js.executeScript("window.scrollBy(0, -100);");
            WebElement input = wait.until(ExpectedConditions.elementToBeClickable(dateOfBirthInput));
            input.click();
            input.sendKeys(Keys.CONTROL, "a");
            input.sendKeys(dateOfBirthParam);
            input.sendKeys(Keys.ENTER);
        }, 5, 500);
    }

    public void setSubjects(String subjectParam) {
        retry(() -> {
            js.executeScript("arguments[0].scrollIntoView(true);", subjectsInput);
            js.executeScript("window.scrollBy(0, -100);");
            WebElement input = wait.until(ExpectedConditions.elementToBeClickable(subjectsInput));
            input.click();
            input.sendKeys(subjectParam);
            input.sendKeys(Keys.ENTER);
        }, 5, 500);
    }

    public void setGender(String genderParam) {
        retry(() -> {
            WebElement gender = wait.until(ExpectedConditions.elementToBeClickable(
                    driver.findElement(By.xpath("//*[text()='" + genderParam + "']"))));
            gender.click();
        }, 5, 300);
    }

    public void setHobby(String hobbyParam1, String hobbyParam2, String hobbyParam3) {
        String[] hobbies = {hobbyParam1, hobbyParam2, hobbyParam3};
        for (String hobby : hobbies) {
            if (!hobby.isEmpty() && (hobby.equals("Sports") || hobby.equals("Reading") || hobby.equals("Music"))) {
                retry(() -> {
                    WebElement el = wait.until(ExpectedConditions.elementToBeClickable(
                            driver.findElement(By.xpath("//*[text()='" + hobby + "']"))));
                    el.click();
                }, 5, 300);
            }
        }
    }

    public void setState(String stateParam) {
        retry(() -> {
            js.executeScript("arguments[0].scrollIntoView(true);", state);
            js.executeScript("window.scrollBy(0, -150);");
            WebElement stateInput = wait.until(ExpectedConditions.elementToBeClickable(state));
            try { stateInput.click(); } catch (ElementClickInterceptedException e) {
                js.executeScript("arguments[0].click();", stateInput);
            }
            WebElement stateOption = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[contains(@id,'react-select') and text()='" + stateParam + "']")));
            stateOption.click();
        }, 5, 500);
    }

    public void setCity(String cityParam) {
        retry(() -> {
            js.executeScript("arguments[0].scrollIntoView(true);", city);
            js.executeScript("window.scrollBy(0, -150);");
            WebElement cityInput = wait.until(ExpectedConditions.elementToBeClickable(city));
            try { cityInput.click(); } catch (ElementClickInterceptedException e) {
                js.executeScript("arguments[0].click();", cityInput);
            }
            WebElement cityOption = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[contains(@id,'react-select') and text()='" + cityParam + "']")));
            cityOption.click();
        }, 5, 500);
    }

    public void setSubmit() {
        retry(() -> {
            js.executeScript("arguments[0].scrollIntoView(true);", submit);
            WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(submit));
            try { btn.click(); } catch (ElementClickInterceptedException e) {
                js.executeScript("arguments[0].click();", btn);
            }
        }, 5, 500);
    }

    public void closeAdvert() {
        try {
            js.executeScript(
                    "var elem = document.evaluate(\"//*[@id='adplus-anchor']\", document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue;" +
                            "if(elem) elem.parentNode.removeChild(elem);");
        } catch (Exception ignored) {}

        try {
            js.executeScript(
                    "var elem = document.evaluate(\"//footer\", document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue;" +
                            "if(elem) elem.parentNode.removeChild(elem);");
        } catch (Exception ignored) {}
    }
}
