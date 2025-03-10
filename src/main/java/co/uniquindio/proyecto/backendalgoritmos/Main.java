package co.uniquindio.proyecto.backendalgoritmos;

import co.uniquindio.proyecto.backendalgoritmos.models.DocumentsProperties;
import co.uniquindio.proyecto.backendalgoritmos.modules.PDFReader.KeywordExtractor;
import co.uniquindio.proyecto.backendalgoritmos.modules.PDFReader.PDFReader;
import co.uniquindio.proyecto.backendalgoritmos.modules.Selenium.SeleniumHandler;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        String directorioActual = System.getProperty("user.dir");

        String bibFilePath = directorioActual + "/src/main/resources/co.uniquindio.proyecto.backendalgoritmos/keywords.bib";
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

}