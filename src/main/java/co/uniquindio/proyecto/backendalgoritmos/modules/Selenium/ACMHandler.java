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
    private static final String CHROME_DRIVER_PATH_MAC = directorioActual+ "/src/main/resources/drivers/chromedriver-mac-x64/chromedriver";
    private static final String BASE_URL = "https://library.uniquindio.edu.co/databases";
    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(10);
    private static final String SEARCH_KEY = "computational thinking";
    private static final String BIB_FOLDER_PATH = directorioActual+ "/src/main/resources/co.uniquindio.proyecto.backendalgoritmos/";;

    private WebDriver driver;
    private WebDriverWait wait;

    public ACMHandler() { // Constructor de la clase ACMHandler
        File downloadDir = new File(BIB_FOLDER_PATH); // Crea un objeto File con la ruta del directorio de descargas especificada por la constante BIB_FOLDER_PATH
        if (!downloadDir.exists()) { // Verifica si el directorio no existe
            downloadDir.mkdirs(); // Si no existe, crea el directorio
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
            put("download.default_directory", BIB_FOLDER_PATH); // Especifica la ruta donde se guardarán los archivos descargados
            put("download.prompt_for_download", false); // Desactiva el cuadro de diálogo que pide confirmación antes de descargar archivos
            put("download.directory_upgrade", true); // Permite la actualización del directorio de descarga si ya existe
            put("safebrowsing.enabled", true); // Habilita la protección contra la navegación no segura
        }});

        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_PATH_MAC); // Establece la propiedad del sistema para el controlador de Chrome, usando la constante CHROME_DRIVER_PATH_MAC para especificar la ruta del controlador
        driver = new ChromeDriver(options); // Crea una nueva instancia del controlador ChromeDriver con las opciones configuradas previamente
        driver.get(BASE_URL); // Abre la URL base especificada por la constante BASE_URL en el navegador
        wait = new WebDriverWait(driver, DEFAULT_TIMEOUT); // Crea una instancia de WebDriverWait para esperar elementos con un tiempo de espera predeterminado especificado por DEFAULT_TIMEOUT
    } // Fin del constructor


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
        for (int pagina = 1; pagina <= 60; pagina++) {
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