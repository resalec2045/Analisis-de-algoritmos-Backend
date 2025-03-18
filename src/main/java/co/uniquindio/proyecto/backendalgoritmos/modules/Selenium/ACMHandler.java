package co.uniquindio.proyecto.backendalgoritmos.modules.Selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.Duration;
import java.util.Random;

public class ACMHandler {

    private static final String directorioActual = System.getProperty("user.dir");
    private static final String CHROME_DRIVER_PATH = directorioActual + "/src/main/resources/drivers/chromedriver-win64/chromedriver.exe";
    private static final String CHROME_DRIVER_PATH_MAC = "/src/main/resources/drivers/chromedriver-mac-x64/chromedriver";
    private static final String BASE_URL = "https://library.uniquindio.edu.co/databases";
    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(10);
    private static final String SEARCH_KEY = "computational thinking";
    private static final String BIB_FOLDER_PATH = directorioActual+ "/src/main/resources/co.uniquindio.proyecto.backendalgoritmos/";;

    private WebDriver driver;
    private WebDriverWait wait;

    public ACMHandler() {
        File downloadDir = new File(BIB_FOLDER_PATH);
        if (!downloadDir.exists()) {
            downloadDir.mkdirs();
        }

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--start-maximized");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-infobars");
        options.addArguments("--no-sandbox");
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        options.setExperimentalOption("useAutomationExtension", false);
        options.setExperimentalOption("prefs", new java.util.HashMap<String, Object>() {{
            put("download.default_directory", BIB_FOLDER_PATH);
            put("download.prompt_for_download", false);
            put("download.directory_upgrade", true);
            put("safebrowsing.enabled", true);
        }});
        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_PATH);
        driver = new ChromeDriver(options);
        driver.get(BASE_URL);
        wait = new WebDriverWait(driver, DEFAULT_TIMEOUT);
    }

    public void ejecute() {
        try {
            expandFacultadIngenieria();
            clickACMLink();
            searchComputational();
            Thread.sleep(20000);
            dowloandDocuments();
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
        System.out.println("Enlace 'ACM Digital Library' clickeado.");
    }

    private void searchComputational() throws InterruptedException {
        WebElement searchInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@type='search']")));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("onload-background")));
        searchInput.sendKeys(SEARCH_KEY);
        Thread.sleep(new Random().nextInt(3000) + 1000);
        WebElement searchButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='submit' and @title='Search']")));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("onload-background")));
        searchButton.click();
        System.out.println("Búsqueda: " + SEARCH_KEY + " realizada.");
    }

    private void dowloandDocuments() throws InterruptedException {
        for (int pagina = 1; pagina <= 50; pagina++) {
            try {

                WebElement checkbox = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='item-results__checkbox']/label[@class='checkbox--primary']/input[@type='checkbox' and @name='markall']")));
                JavascriptExecutor executor = (JavascriptExecutor) driver;
                executor.executeScript("arguments[0].click();", checkbox);
                System.out.println("Checkbox clicked successfully using JavascriptExecutor.");
                Thread.sleep(2000);

                // Click Export
                WebElement exportButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@href='#'][@title='Export Citations'][@data-target='#exportCitation'][@data-toggle='modal'][@class='btn light export-citation']")));
                executor.executeScript("arguments[0].click();", exportButton);
                Thread.sleep(2000);

                // Click Export citation to BibTeX
                WebElement downloadButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//li[@style='cursor: pointer;']/a[@href='javascript:void(0)'][@role='menuitem'][@title='Download citation'][@class='download__btn']")));
                executor.executeScript("arguments[0].click();", downloadButton);
                Thread.sleep(2000);
                WebElement closeButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='button'][@data-dismiss='modal'][@class='close']")));
                executor.executeScript("arguments[0].click();", closeButton);
                Thread.sleep(2000);

                // Deselect all results
                checkbox = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='item-results__checkbox']/label[@class='checkbox--primary']/input[@type='checkbox' and @name='markall']")));
                executor.executeScript("arguments[0].click();", checkbox);
                Thread.sleep(2000);

                WebElement nextPageButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span/a[@title='Next Page'][@aria-label='Next Page'][@class='pagination__btn--next']")));
                executor.executeScript("arguments[0].click();", nextPageButton);
                Thread.sleep(7000);

            } catch (Exception e) {
                System.out.println("⚠ Error on page " + pagina + ": " + e.getMessage());
            }
        }

        System.out.println("Download completed up to page 38.");
    }
}