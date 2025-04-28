package co.uniquindio.proyecto.backendalgoritmos.servicio.implementaciones;

import co.uniquindio.proyecto.backendalgoritmos.helpers.AbstractAnalyzer;
import co.uniquindio.proyecto.backendalgoritmos.helpers.Agrupamiento.DIANA.DivisiveClustering;
import co.uniquindio.proyecto.backendalgoritmos.helpers.CoWordNetworkBuilder;
import co.uniquindio.proyecto.backendalgoritmos.helpers.WordCloudProcessor;
import co.uniquindio.proyecto.backendalgoritmos.models.DocumentsProperties;
import co.uniquindio.proyecto.backendalgoritmos.models.WordCloudItem;
import co.uniquindio.proyecto.backendalgoritmos.modules.DocuemntsExtractor.DocumentsExtractor;
import co.uniquindio.proyecto.backendalgoritmos.helpers.Agrupamiento.AGNES.ClusteringService;
import co.uniquindio.proyecto.backendalgoritmos.helpers.Agrupamiento.PreprocesamientoTexto;
import co.uniquindio.proyecto.backendalgoritmos.helpers.sorting.SortingHelper;
import co.uniquindio.proyecto.backendalgoritmos.helpers.ChartOrganizer;
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

//    ! Descripcion utiliizada
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

//    ! Requerimiento 2

    public List<Object> requerimiento2() {
        List<String> autores = new ArrayList<>();
        List<String> publishers = new ArrayList<>();
        List<String> journals = new ArrayList<>();
        List<Object> modelFront = new ArrayList<>();

        String directorioActual = System.getProperty("user.dir");
        String bibFilePath = directorioActual + "/src/main/resources/co.uniquindio.proyecto.backendalgoritmos/articulos.bib";
        List<DocumentsProperties> articles = DocumentsExtractor.readBibFile(bibFilePath);

        for (DocumentsProperties doc : articles) {
            String abstractAutor = doc.getAuthor();
            if (abstractAutor != null && !abstractAutor.trim().isEmpty()) {
                autores.add(abstractAutor.trim());
            }
            String abstractPublishers = doc.getPublishers();
            if (abstractPublishers != null && !abstractPublishers.trim().isEmpty()) {
                publishers.add(abstractPublishers.trim());
            }
            String abstractJournal = doc.getJournal();
            if (abstractJournal != null && !abstractJournal.trim().isEmpty()) {
                journals.add(abstractJournal.trim());
            }
        }

        modelFront.add(ChartOrganizer.organizeChartData(countOccurrences(autores, 15), "Autores", "Cantidad", "Top 15 Autores"));
        modelFront.add(ChartOrganizer.organizeChartData(countOccurrences(journals, 15), "Journals", "Cantidad", "Top 15 Journals"));
        modelFront.add(ChartOrganizer.organizeChartData(countOccurrences(publishers, 15), "Publishers", "Cantidad", "Top 15 Publishers"));


        return modelFront;
    }

//    ! Requerimiento 3

    public Map<String, Object> requerimiento3_1() {
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

        abstracts = PreprocesamientoTexto.preprocesarTexto(abstracts);

        return AbstractAnalyzer.analizarAbstracts(abstracts);
    }

    public List<WordCloudItem> requerimiento3() {
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

        abstracts = PreprocesamientoTexto.preprocesarTexto(abstracts);

        return WordCloudProcessor.generarWordCloudContiene(abstracts);
    }

    public Map<String, Object> requerimiento3_2() {
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

        Map <String, Object> coWordNetwork = CoWordNetworkBuilder.construirCoWordNetworkMitad(abstracts, PreprocesamientoTexto.preprocesarTexto(abstracts));

        return coWordNetwork;
    }

//    ! Requerimiento 5



//    !METODOS

    public static Map<String, Object> countOccurrences(List<String> items, int topN) {
        Map<String, Integer> counter = new HashMap<>();

        // Contar cada ítem
        for (String item : items) {
            counter.put(item, counter.getOrDefault(item, 0) + 1);
        }

        // Ordenar por cantidad descendente
        List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(counter.entrySet());
        sortedEntries.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        // Tomar solo los topN elementos
        List<String> categories = new ArrayList<>();
        List<Integer> series = new ArrayList<>();
        int count = 0;
        for (Map.Entry<String, Integer> entry : sortedEntries) {
            if (count >= topN) break;
            categories.add(entry.getKey());
            series.add(entry.getValue());
            count++;
        }

        // Empaquetar en un Map
        Map<String, Object> result = new HashMap<>();
        result.put("categories", categories);
        result.put("series", series);
        return result;
    }

}
