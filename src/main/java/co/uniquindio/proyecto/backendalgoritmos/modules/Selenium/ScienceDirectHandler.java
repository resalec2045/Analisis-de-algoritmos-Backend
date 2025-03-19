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
    private static final String CHROME_DRIVER_PATH = directorioActual + "/src/main/resources/drivers/chromedriver-win64/chromedriver.exe";
    private static final String CHROME_DRIVER_PATH_MAC = directorioActual + "/src/main/resources/drivers/chromedriver-mac-x64/chromedriver";
    private static final String BASE_URL = "https://www-sciencedirect-com.crai.referencistas.com/";
    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(10);
    private static final String SEARCH_KEY = "computational thinking";
    private static final String PDF_FOLDER_PATH = directorioActual+ "/src/main/resources/co.uniquindio.proyecto.backendalgoritmos/";

    public static void ejectue() { // Define el método estático "ejectue", que puede ser llamado sin necesidad de instanciar la clase.
        String downloadPath = PDF_FOLDER_PATH; // Asigna la ruta del directorio de descarga desde la constante PDF_FOLDER_PATH. Asegúrate de que esta constante esté definida previamente.

        // Crear el directorio de descargas si no existe
        File downloadDir = new File(downloadPath); // Crea un objeto File con la ruta del directorio de descarga.
        if (!downloadDir.exists()) { // Verifica si el directorio no existe.
            downloadDir.mkdirs(); // Si no existe, lo crea.
        }

        // Configura las opciones de Chrome para las descargas automáticas y evitar la detección
        ChromeOptions options = new ChromeOptions(); // Crea una instancia de ChromeOptions para personalizar la configuración del navegador Chrome.
        options.addArguments("--disable-blink-features=AutomationControlled"); // Desactiva la característica "AutomationControlled" que puede indicar que el navegador es automatizado.
        options.addArguments("--start-maximized"); // Inicia el navegador con la ventana maximizada.
        options.addArguments("--disable-popup-blocking"); // Desactiva el bloqueo de ventanas emergentes.
        options.addArguments("--disable-infobars"); // Desactiva la barra de información que muestra que Chrome está controlado por un software de automatización.
        options.addArguments("--no-sandbox"); // Desactiva el modo sandbox de Chrome, relacionado con la seguridad.
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"}); // Excluye el interruptor "enable-automation", que es una señal de que el navegador está siendo automatizado.
        options.setExperimentalOption("useAutomationExtension", false); // Desactiva la extensión de automatización de Chrome, para evitar la detección.
        options.setExperimentalOption("prefs", new java.util.HashMap<String, Object>() {{ // Establece preferencias personalizadas en Chrome mediante un mapa de opciones.
            put("download.default_directory", downloadPath); // Define el directorio de descarga predeterminado donde se guardarán los archivos descargados.
            put("download.prompt_for_download", false); // Desactiva la solicitud de confirmación antes de iniciar la descarga de archivos.
            put("download.directory_upgrade", true); // Permite actualizar el directorio de descarga si ya existe.
            put("safebrowsing.enabled", true); // Habilita la protección contra la navegación insegura.
        }});

        // Inicializa WebDriver
        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_PATH_MAC); // Establece la propiedad del sistema para el controlador de Chrome, especificando la ruta al archivo del controlador en CHROME_DRIVER_PATH_MAC.

        ChromeDriverService service = ChromeDriverService.createDefaultService(); // Crea un servicio predeterminado para el controlador ChromeDriver.
        WebDriver driver = new ChromeDriver(service, options); // Inicializa el WebDriver con el servicio y las opciones configuradas previamente. Se utiliza para controlar el navegador Chrome.

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