package org.example.utils;

import java.util.Arrays;
import java.util.List;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;

public class Driver1 {
    static public WebDriver getAutoLocalDriver() {
        WebDriverManager.chromedriver().setup();
        return new ChromeDriver();
    }

    static public WebDriver getLocalDriver() {
        System.setProperty("webdriver.chrome.driver", "E:\\ceiti\\webdriver\\chromedriver-win64\\chromedriver.exe");
        ChromeOptions o = new ChromeOptions();
        o.addArguments("--remote-allow-origins=*");
        return new ChromeDriver(o);
    }

    public static RemoteWebDriver getRemoteDriver() {
        ChromeOptions o = new ChromeOptions();
        o.addArguments("--headless=new");
        o.addArguments("--no-sandbox");
        o.addArguments("--disable-dev-shm-usage");
        o.addArguments("--disable-gpu");
        o.addArguments("--window-size=1920,1080");
        o.addArguments("--remote-allow-origins=*");
        o.addArguments("--disable-extensions");
        o.addArguments("--disable-infobars");
        o.addArguments("--disable-popup-blocking");

        o.setCapability("browserVersion", "128.0");
        o.setCapability("selenoid:options", new HashMap<String, Object>() {{
            put("name", "FormTest Remote");
            put("sessionTimeout", "15m");
            put("env", Arrays.asList("TZ=UTC"));
            put("enableVideo", true);
            put("enableVNC", true);
            put("enableLog", true);
        }});

        try {
            return new RemoteWebDriver(URI.create("http://127.0.0.1:4444/wd/hub").toURL(), o);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}