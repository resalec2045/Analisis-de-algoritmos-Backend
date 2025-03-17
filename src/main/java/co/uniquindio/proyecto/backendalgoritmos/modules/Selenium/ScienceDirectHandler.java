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
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.Duration;
import java.util.Random;

public class ScienceDirectHandler {

    private static final String directorioActual = System.getProperty("user.dir");
    private static final String CHROME_DRIVER_PATH = directorioActual + "/src/main/resources/drivers/chromedriver-mac-x64/chromedriver";
    private static final String BASE_URL = "https://www-sciencedirect-com.crai.referencistas.com/";
    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(10);
    private static final String SEARCH_KEY = "computational thinking";
    private static final String PDF_FOLDER_PATH = directorioActual+ "/src/main/resources/co.uniquindio.proyecto.backendalgoritmos/";

    public static void ejectue() {
        String downloadPath = PDF_FOLDER_PATH; // Replace with your desired download path

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
            WebElement searchBox = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("qs")));
            escribirComoHumano(driver, searchBox, SEARCH_KEY);
            searchBox.sendKeys(Keys.RETURN);
            Thread.sleep(3000);

            // Show 100 results per page
            WebElement resultsPerPage100 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//li[@aria-current='false']/a[@data-aa-name='srp-100-results-per-page']")));
            resultsPerPage100.click();
            Thread.sleep(2000);

            // Iterate through pages
            for (int pagina = 1; pagina <= 60; pagina++) {
                try {
                    // Select all results
                    WebElement selectAllCheckbox = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//label[@for='select-all-results']")));
                    selectAllCheckbox.click();
                    Thread.sleep(2000);

                    // Click Export
                    WebElement exportButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(@class, 'export-all-link-button')]")));
                    exportButton.click();
                    Thread.sleep(2000);

                    // Click Export citation to BibTeX
                    WebElement exportBibtexButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[.//span[contains(text(), 'Export citation to BibTeX')]]")));
                    exportBibtexButton.click();
                    Thread.sleep(2000);

                    // Deselect all results
                    selectAllCheckbox = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//label[@for='select-all-results']")));
                    selectAllCheckbox.click();
                    Thread.sleep(2000);

                    // Go to the next page
                    if (pagina < 37) {
                        WebElement nextPageButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@data-aa-name='srp-next-page']")));
                        JavascriptExecutor js = (JavascriptExecutor) driver;
                        js.executeScript("arguments[0].scrollIntoView();", nextPageButton);
                        Thread.sleep(1000);
                        js.executeScript("arguments[0].click();", nextPageButton);

                        System.out.println("➡ Moving to page " + (pagina + 1) + "...");
                        Thread.sleep(5000);
                    }
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