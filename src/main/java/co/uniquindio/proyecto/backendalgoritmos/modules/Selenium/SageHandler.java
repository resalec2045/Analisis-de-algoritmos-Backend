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

    private static final String directorioActual = System.getProperty("user.dir");
    private static final String CHROME_DRIVER_PATH_MAC = directorioActual + "/src/main/resources/drivers/chromedriver-mac-x64/chromedriver";
    private static final String BASE_URL = "https://login.crai.referencistas.com/login?url=https://journals.sagepub.com";
    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(20);
    private static final String SEARCH_KEY = "computational thinking";
    private static final String BIB_FOLDER_PATH = directorioActual + "/src/main/resources/co.uniquindio.proyecto.backendalgoritmos/";

    public static void ejectue() { // Define el método estático "ejectue", que se puede llamar sin instanciar la clase
        String downloadPath = BIB_FOLDER_PATH; // Asigna la ruta del directorio de descargas desde la constante BIB_FOLDER_PATH

        File downloadDir = new File(downloadPath); // Crea un objeto File con la ruta del directorio de descargas
        if (!downloadDir.exists()) { // Verifica si el directorio de descargas no existe
            downloadDir.mkdirs(); // Si el directorio no existe, crea el directorio
        }

        ChromeOptions options = new ChromeOptions(); // Crea un objeto ChromeOptions para configurar opciones personalizadas para el navegador Chrome
        options.addArguments("--disable-blink-features=AutomationControlled"); // Desactiva la característica "AutomationControlled" que indica que el navegador es automatizado
        options.addArguments("--start-maximized"); // Inicia el navegador maximizado
        options.addArguments("--disable-popup-blocking"); // Desactiva el bloqueo de ventanas emergentes
        options.addArguments("--disable-infobars"); // Desactiva las barras informativas del navegador (como la barra de "Chrome está controlado por software de automatización")
        options.addArguments("--no-sandbox"); // Desactiva el modo "sandbox", que está relacionado con las restricciones de seguridad del navegador
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"}); // Establece una opción experimental para excluir el interruptor "enable-automation" que indica que el navegador está automatizado
        options.setExperimentalOption("useAutomationExtension", false); // Desactiva la extensión de automatización en Chrome
        options.setExperimentalOption("prefs", new java.util.HashMap<String, Object>() {{ // Establece preferencias personalizadas para el navegador en un mapa de opciones
            put("download.default_directory", downloadPath); // Especifica la ruta donde se guardarán los archivos descargados
            put("download.prompt_for_download", false); // Desactiva el cuadro de diálogo que pide confirmación antes de descargar archivos
            put("download.directory_upgrade", true); // Permite la actualización del directorio de descarga si ya existe
            put("safebrowsing.enabled", true); // Habilita la protección contra la navegación no segura
        }});

        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_PATH_MAC); // Establece la propiedad del sistema para el controlador de Chrome, usando la constante CHROME_DRIVER_PATH_MAC para especificar la ruta del controlador

        ChromeDriverService service = ChromeDriverService.createDefaultService(); // Crea un servicio predeterminado para el controlador ChromeDriver
        WebDriver driver = new ChromeDriver(service, options); // Crea una nueva instancia del controlador ChromeDriver con el servicio y las opciones configuradas previamente
        WebDriverWait wait = new WebDriverWait(driver, DEFAULT_TIMEOUT); // Crea una instancia de WebDriverWait para esperar elementos con un tiempo de espera predeterminado especificado por DEFAULT_TIMEOUT

        try {
            driver.get(BASE_URL);

            WebElement googleButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("btn-google")));
            googleButton.click();
            System.out.println("Clicked Google login button.");

            WebElement username = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("identifierId")));
            escribirComoHumano(driver, username, "mariana.valenciae1@uqvirtual.edu.co");
            username.sendKeys(Keys.RETURN);
            Thread.sleep(3000);

            WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("Passwd")));
            escribirComoHumano(driver, passwordField, "Lindaraja11,.");
            passwordField.sendKeys(Keys.RETURN);
            Thread.sleep(10000);

            WebElement searchInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@type='search']")));
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("onload-background")));
            searchInput.sendKeys(SEARCH_KEY);
            WebElement searchButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[data-id='global-homepage-quick-search']")));
            searchButton.click();
            System.out.println("Clicked search button");

            Thread.sleep(2000);

            for (int pagina = 1; pagina <= 60; pagina++) {
                try {
                    WebElement allSelector = wait.until(ExpectedConditions.elementToBeClickable(By.id("action-bar-select-all")));
                    allSelector.click();
                    Thread.sleep(2000);

                    WebElement exportCitationsButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[data-id='srp-export-citations']")));
                    exportCitationsButton.click();
                    Thread.sleep(2000);

                    WebElement selectElement = wait.until(ExpectedConditions.elementToBeClickable(By.id("citation-format")));
                    selectElement.click();
                    Thread.sleep(2000);

                    Select select = new Select(selectElement);
                    select.selectByValue("bibtex");
                    Thread.sleep(2000);

                    // Usar JavascriptExecutor para forzar el clic
                    WebElement downloadButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a.btn.btn-secondary.download__btn")));
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", downloadButton);
                    System.out.println("Download button clicked successfully.");
                    Thread.sleep(2000);

                    wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.modal__content")));
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
            System.out.println("Download completed up to page 60.");
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