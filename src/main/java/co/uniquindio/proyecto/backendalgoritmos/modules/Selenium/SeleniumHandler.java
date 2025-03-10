package co.uniquindio.proyecto.backendalgoritmos.modules.Selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class SeleniumHandler {

    private static final String CHROME_DRIVER_PATH = "/Users/mvalencia/Downloads/chromedriver-mac-x64/chromedriver";
    private static final String BASE_URL = "https://library.uniquindio.edu.co/databases";
    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(10);
    private static final String SEARCH_KEY = "computational thinking";

    private WebDriver driver;
    private WebDriverWait wait;

    public SeleniumHandler() {
        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_PATH);
        driver = new ChromeDriver();
        driver.get(BASE_URL);
        wait = new WebDriverWait(driver, DEFAULT_TIMEOUT);
    }

    public void ejecute() {
        try {
            expandFacultadIngenieria();
            clickACMLink();
            searchComputational();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void expandFacultadIngenieria() {
        WebElement detailsElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//details/summary/div[contains(text(), 'Fac. Ingeniería')]")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", detailsElement);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("onload-background")));
        detailsElement.click();
        System.out.println("Elemento 'Fac. Ingeniería' encontrado y expandido.");
    }

    private void clickACMLink() {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//details/div/article[1]/div/div/h3/a")));
        WebElement acmLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//h3[@class='result-title']/a[contains(.,'ACM Digital Library')]")));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("onload-background")));
        acmLink.click();
        System.out.println("Enlace 'ACM Digital Library' clicado.");
    }

    private void searchComputational() {
        WebElement searchInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@type='search']")));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("onload-background")));
        searchInput.sendKeys(SEARCH_KEY);
        WebElement searchButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='submit' and @title='Search']")));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("onload-background")));
        searchButton.click();
        System.out.println("Búsqueda: " + SEARCH_KEY + " realizada.");
    }
}