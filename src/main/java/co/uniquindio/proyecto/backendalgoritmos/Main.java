package co.uniquindio.proyecto.backendalgoritmos;

import co.uniquindio.proyecto.backendalgoritmos.models.DocumentsProperties;
import co.uniquindio.proyecto.backendalgoritmos.modules.PDFReader.KeywordExtractor;
import co.uniquindio.proyecto.backendalgoritmos.modules.PDFReader.PDFReader;
import co.uniquindio.proyecto.backendalgoritmos.modules.Selenium.SeleniumHandler;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        String bibFilePath = "/Users/mvalencia/Desktop/Backend Algoritmos/backendalgoritmos/src/main/resources/co/uniquindio/proyecto/backendalgoritmos/keywords.bib";
        PDFReader pdfReadernew = new PDFReader();
        pdfReadernew.ejecute();
        List<DocumentsProperties> allSortedKeywords = KeywordExtractor.readBibFile(bibFilePath);
        for (int i = 0; i < allSortedKeywords.size(); i++) {
            String keywords = allSortedKeywords.get(i).getKeywords();
            System.out.println("Palabras clave del artículo " + (i + 1) + ": keywords = " + keywords);
        }
        SeleniumHandler seleniumHandler = new SeleniumHandler();
        seleniumHandler.ejecute();
    }

    public static void main(String[] args) {
        launch();
    }
}