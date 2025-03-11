package co.uniquindio.proyecto.backendalgoritmos.modules.Selenium;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.Duration;
import java.util.Random;

public class SageHandler {

    private static final String CHROME_DRIVER_PATH = "/Users/mvalencia/Downloads/chromedriver-mac-x64/chromedriver";
    private static final String BASE_URL = "https://login.crai.referencistas.com/login?url=https://journals.sagepub.com";
    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(10);
    private static final String SEARCH_KEY = "computational thinking";
    private static final String directorioActual = System.getProperty("user.dir");
    private static final String BIB_FOLDER_PATH = directorioActual+ "/src/main/resources/co.uniquindio.proyecto.backendalgoritmos/";

    public static void ejectue() {
        String downloadPath = BIB_FOLDER_PATH; // Replace with your desired download path

        // Create the download directory if it doesn't exist
        File downloadDir = new File(downloadPath);
        if (!downloadDir.exists()) {
            downloadDir.mkdirs();
        }

        // Configure Chrome options for automatic downloads and anti-detection
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--start-maximized");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-infobars");
        options.addArguments("--no-sandbox");
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        options.setExperimentalOption("useAutomationExtension", false);
        options.setExperimentalOption("prefs", new java.util.HashMap<String, Object>() {{
            put("download.default_directory", downloadPath);
            put("download.prompt_for_download", false);
            put("download.directory_upgrade", true);
            put("safebrowsing.enabled", true);
        }});

        // Initialize WebDriver

        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_PATH);

        ChromeDriverService service = ChromeDriverService.createDefaultService();
        WebDriver driver = new ChromeDriver(service, options);

        try {
            // Access ScienceDirect
            driver.get(BASE_URL);

            // Wait for and click the Google login button
            WebDriverWait wait = new WebDriverWait(driver, DEFAULT_TIMEOUT);
            WebElement googleButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("btn-google")));
            googleButton.click();
            System.out.println("Clicked Google login button.");

            // Enter email
            WebElement username = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("identifierId")));
            escribirComoHumano(driver, username, "mariana.valenciae1@uqvirtual.edu.co"); // Replace with your email
            username.sendKeys(Keys.RETURN);
            Thread.sleep(3000);

            // Enter password
            WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("Passwd")));
            escribirComoHumano(driver, passwordField, "Lindaraja11,."); // Replace with your password
            passwordField.sendKeys(Keys.RETURN);
            Thread.sleep(10000);

            // Perform search

            WebElement searchInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@type='search']")));
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("onload-background")));
            searchInput.sendKeys(SEARCH_KEY);
            wait = new WebDriverWait(driver, DEFAULT_TIMEOUT);
            WebElement searchButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[data-id='global-homepage-quick-search']")));
            searchButton.click();
            System.out.println("Clicked search button");

            Thread.sleep(2000); // Es
            // Iterate through pages

            for (int pagina = 1; pagina <= 50; pagina++) {
                try {
                    // Show 100 results per page
                    WebElement allSelector = wait.until(ExpectedConditions.elementToBeClickable(By.id("action-bar-select-all")));
                    allSelector.click();
                    Thread.sleep(2000);

                    WebElement exportCitationsButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[data-id='srp-export-citations']")));
                    // Hacer clic en el botón
                    exportCitationsButton.click();
                    Thread.sleep(2000);

                    WebElement selectElement = wait.until(ExpectedConditions.elementToBeClickable(By.id("citation-format")));
                    selectElement.click();
                    Thread.sleep(2000);

                    Select select = new Select(selectElement);

                    select.selectByValue("bibtex");

                    WebElement downloadButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a.btn.btn-secondary.download__btn")));
                    // Hacer clic en el botón de descarga
                    downloadButton.click();
                    System.out.println("Download button clicked successfully.");
                    Thread.sleep(2000);

                    // Esperar a que el modal esté visible
                    wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.modal__content")));
                    // Esperar a que el botón de cierre esté presente y sea clickable
                    WebElement closeButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(@alt,'close')]")));
                    closeButton.click();
                    System.out.println("Close button clicked successfully.");

                    allSelector = wait.until(ExpectedConditions.elementToBeClickable(By.id("action-bar-select-all")));
                    allSelector.click();
                    Thread.sleep(2000);

                    WebElement nextButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a.next.hvr-forward.pagination__link")));
                    nextButton.click();
                    Thread.sleep(2000);
                } catch (Exception e) {
                    System.out.println("⚠ Error on page " + pagina + ": " + e.getMessage());
                }
            }

            System.out.println("Download completed up to page 38.");

        } catch (Exception e) {
            System.out.println("General error: " + e.getMessage());
        } finally {
            driver.quit();
        }
    }

    private static void escribirComoHumano(WebDriver driver, WebElement element, String texto) throws InterruptedException {
        Random random = new Random();
        for (char caracter : texto.toCharArray()) {
            element.sendKeys(String.valueOf(caracter));
            Thread.sleep((long) (random.nextDouble() * 200 + 100));
        }
    }
}