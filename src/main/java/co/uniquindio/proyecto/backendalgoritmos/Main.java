package co.uniquindio.proyecto.backendalgoritmos;

import co.uniquindio.proyecto.backendalgoritmos.models.DocumentsProperties;
import co.uniquindio.proyecto.backendalgoritmos.modules.BibReader.BibReaderHandler;
import co.uniquindio.proyecto.backendalgoritmos.modules.PDFReader.DocumentsExtractor;
import co.uniquindio.proyecto.backendalgoritmos.modules.Selenium.ACMHandler;
import co.uniquindio.proyecto.backendalgoritmos.modules.Selenium.SageHandler;
import co.uniquindio.proyecto.backendalgoritmos.modules.Selenium.ScienceDirectHandler;

import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        String directorioActual = System.getProperty("user.dir");
        String bibFilePath = directorioActual + "/src/main/resources/co.uniquindio.proyecto.backendalgoritmos/articulos.bib";
       // ScienceDirectHandler.ejectue();
      //  ACMHandler seleniumHandler = new ACMHandler();
    //    seleniumHandler.ejecute();
      //  SageHandler.ejectue();
        BibReaderHandler.execute();
        List<DocumentsProperties> articles = DocumentsExtractor.readBibFile(bibFilePath);
        for (DocumentsProperties article : articles) {
            System.out.println(article.getTitle());
        }
    }
}