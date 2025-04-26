package co.uniquindio.proyecto.backendalgoritmos.servicio.implementaciones;

import co.uniquindio.proyecto.backendalgoritmos.helpers.Agrupamiento.DIANA.DivisiveClustering;
import co.uniquindio.proyecto.backendalgoritmos.models.DocumentsProperties;
import co.uniquindio.proyecto.backendalgoritmos.modules.DocuemntsExtractor.DocumentsExtractor;
import co.uniquindio.proyecto.backendalgoritmos.helpers.Agrupamiento.AGNES.ClusteringService;
import co.uniquindio.proyecto.backendalgoritmos.helpers.Agrupamiento.PreprocesamientoTexto;
import co.uniquindio.proyecto.backendalgoritmos.helpers.sorting.SortingHelper;
import co.uniquindio.proyecto.backendalgoritmos.servicio.interfaces.InformationServicio;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class InformationImpl implements InformationServicio {

    private final ClusteringService clusteringService = new ClusteringService();

    @Override
    public List<Object> getInformation() {
        List<Object> modelFront = new ArrayList<>();
        String directorioActual = System.getProperty("user.dir");
        String bibFilePath = directorioActual + "/src/main/resources/co.uniquindio.proyecto.backendalgoritmos/articulos.bib";
        List<DocumentsProperties> articles = DocumentsExtractor.readBibFile(bibFilePath);

        modelFront.add(SortingHelper.getAuthorSortingResults(articles));
        modelFront.add(SortingHelper.getTitleSortingResults(articles));
        modelFront.add(SortingHelper.getNumberPagesSortingResults(articles));
        modelFront.add(SortingHelper.getYearSortingResults(articles));

        return modelFront;
    }

    @Override
    public List<Object> getInformationAbstract() {
        List<Object> modelFront = new ArrayList<>();
        String abstracts = "";

        String directorioActual = System.getProperty("user.dir");
        String bibFilePath = directorioActual + "/src/main/resources/co.uniquindio.proyecto.backendalgoritmos/articulos.bib";
        List<DocumentsProperties> articles = DocumentsExtractor.readBibFile(bibFilePath);

        for (DocumentsProperties doc : articles) {
            String keywords = doc.getKeywords();
            if (keywords != null) {
                abstracts = abstracts + keywords + ", ";
            }
        }

        modelFront.add(SortingHelper.countKeywords(abstracts));
        modelFront.add(SortingHelper.getKeywordSortingResults(abstracts));

        return modelFront;
    }

//    ! Medoto para AGNES
    @Override
    public Map<String, Object> preprocesamientoTextoAgnes() {
        List<String> abstracts = new ArrayList<>();

        String directorioActual = System.getProperty("user.dir");
        String bibFilePath = directorioActual + "/src/main/resources/co.uniquindio.proyecto.backendalgoritmos/articulos.bib";
        List<DocumentsProperties> articles = DocumentsExtractor.readBibFile(bibFilePath);

        for (DocumentsProperties doc : articles) {
            String abstractDescription = doc.getAbstractDescription();
            if (abstractDescription != null && !abstractDescription.trim().isEmpty()) {
                abstracts.add(abstractDescription.trim());
            }
        }

        if (abstracts.isEmpty()) {
            throw new IllegalArgumentException("Se requiere al menos un abstract.");
        }

        Random random = new Random();
//        String selectedAbstract = abstracts.get(random.nextInt(abstracts.size()));
        String selectedAbstract = abstracts.get(0);
        List<String> palabras = PreprocesamientoTexto.preprocesarTexto(selectedAbstract);

        return clusteringService.clusteringJerarquicoPalabras(palabras);
    }

    //    ! Medoto para Diana
    @Override
    public Map<String, Object> preprocesamientoTextoDiana() {
        List<String> abstracts = new ArrayList<>();

        // 1. Leer archivo .bib
        String directorioActual = System.getProperty("user.dir");
        String bibFilePath = directorioActual + "/src/main/resources/co.uniquindio.proyecto.backendalgoritmos/articulos.bib";
        List<DocumentsProperties> articles = DocumentsExtractor.readBibFile(bibFilePath);

        // 2. Extraer abstracts
        for (DocumentsProperties doc : articles) {
            String abstractDescription = doc.getAbstractDescription();
            if (abstractDescription != null && !abstractDescription.trim().isEmpty()) {
                abstracts.add(abstractDescription.trim());
            }
        }

        // 3. Verificar que haya al menos un abstract
        if (abstracts.isEmpty()) {
            throw new IllegalArgumentException("Se requiere al menos un abstract para clustering divisivo.");
        }

        Random random = new Random();
//        String selectedAbstract = abstracts.get(random.nextInt(abstracts.size()));
        String selectedAbstract = abstracts.get(0);
        List<String> palabras = PreprocesamientoTexto.preprocesarTexto(selectedAbstract);

        // 6. Aplicar DIANA: clustering divisivo
        return DivisiveClustering.dividirPalabras(palabras);
    }

    @Override
    public String preprocesamientoDescriptionUtiliced() {
        List<String> abstracts = new ArrayList<>();

        String directorioActual = System.getProperty("user.dir");
        String bibFilePath = directorioActual + "/src/main/resources/co.uniquindio.proyecto.backendalgoritmos/articulos.bib";
        List<DocumentsProperties> articles = DocumentsExtractor.readBibFile(bibFilePath);

        for (DocumentsProperties doc : articles) {
            String abstractDescription = doc.getAbstractDescription();
            if (abstractDescription != null && !abstractDescription.trim().isEmpty()) {
                abstracts.add(abstractDescription.trim());
            }
        }

        return abstracts.get(0);
    }

}
