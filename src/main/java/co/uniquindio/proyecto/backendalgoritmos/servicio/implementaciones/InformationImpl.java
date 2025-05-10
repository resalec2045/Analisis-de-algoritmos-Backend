package co.uniquindio.proyecto.backendalgoritmos.servicio.implementaciones;

import co.uniquindio.proyecto.backendalgoritmos.helpers.AbstractAnalyzer;
import co.uniquindio.proyecto.backendalgoritmos.helpers.Agrupamiento.AGNES.ClusteringServiceSmile;
import co.uniquindio.proyecto.backendalgoritmos.helpers.Agrupamiento.ComparadorDeSimilitud;
import co.uniquindio.proyecto.backendalgoritmos.helpers.Agrupamiento.DIANA.DivisiveClustering;
import co.uniquindio.proyecto.backendalgoritmos.helpers.Agrupamiento.DIANA.DivisiveClusteringSmile;
import co.uniquindio.proyecto.backendalgoritmos.helpers.Agrupamiento.TextSimilarity;
import co.uniquindio.proyecto.backendalgoritmos.helpers.Agrupamiento.requisito5.AgrupadorManual;
import co.uniquindio.proyecto.backendalgoritmos.helpers.Agrupamiento.requisito5.TextSimilarityGrouper;
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
    private final ClusteringServiceSmile clusteringServiceSmile = new ClusteringServiceSmile();

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

//    * Seguimiento 2
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

//        Random random = new Random();
//        String selectedAbstract = abstracts.get(random.nextInt(abstracts.size()));

//        String selectedAbstract = abstracts.get(0);

        String selectedAbstract = "We applied the AGNES algorithm, implemented in Java with SMILE, to hierarchically cluster semantically related words extracted from scientific abstracts. After preprocessing the text to retain only meaningful terms, each word was vectorized and grouped based on their Euclidean distances. The resulting dendrogram highlights lexical relationships, allowing intuitive exploration of abstract content.";
        List<String> palabras = PreprocesamientoTexto.preprocesarTexto(selectedAbstract);

//        return clusteringService.clusteringJerarquicoPalabras(palabras);
        return clusteringServiceSmile.convertirClusterAJson(clusteringServiceSmile.clusterizarPalabras(palabras));
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

//        Random random = new Random();
//        String selectedAbstract = abstracts.get(random.nextInt(abstracts.size()));

//        String selectedAbstract = abstracts.get(0);
        String selectedAbstract = "We applied the DIANA algorithm, implemented in Java with SMILE, to hierarchically cluster semantically related words extracted from scientific abstracts. After preprocessing the text to retain only meaningful terms, each word was vectorized and grouped based on their Euclidean distances. The resulting dendrogram highlights lexical relationships, allowing intuitive exploration of abstract content.";
        List<String> palabras = PreprocesamientoTexto.preprocesarTexto(selectedAbstract);

        // 6. Aplicar DIANA: clustering divisivo
//        return DivisiveClustering.dividirPalabras(palabras);
        return DivisiveClusteringSmile.clusteringDivisivoJson(palabras);
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

        return abstracts.get(0) + "+_+" + abstracts.get(1) + "+_+" + TextSimilarity.cosineSimilarity(abstracts.get(0), abstracts.get(1));
    }
//    * Seguimiento 2

//    ! Requerimiento 2

    public List<Object> requerimiento2() {
        List<String> autores = new ArrayList<>();
        List<String> publishers = new ArrayList<>();
        List<String> journals = new ArrayList<>();
        List<String> typeProduct = new ArrayList<>();
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
            String type = doc.getTypeDocument();
            if (type != null && !type.trim().isEmpty()) {
                typeProduct.add(type.trim());
            }
        }

        modelFront.add(ChartOrganizer.organizeChartData(countOccurrences(autores, 15), "Autores", "Cantidad", "Top 15 Autores"));
        modelFront.add(ChartOrganizer.organizeChartData(countOccurrences(journals, 15), "Journals", "Cantidad", "Top 15 Journals"));
        modelFront.add(ChartOrganizer.organizeChartData(countOccurrences(publishers, 15), "Publishers", "Cantidad", "Top 15 Publishers"));
        modelFront.add(ChartOrganizer.organizeChartData(countOccurrences(typeProduct, 15), "Tipos documentos", "Cantidad", "Documentos"));


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

    public Map<String, List<WordCloudItem>> requerimiento3PorCategoria() {
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

        return WordCloudProcessor.generarWordCloudPorCategoria(abstracts);
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

    public Map<String, Object> requerimiento5() {
        Map<String, Object> resultado = new LinkedHashMap<>();

        List<String> abstracts = new ArrayList<>();
        String directorioActual = System.getProperty("user.dir");
        String bibFilePath = directorioActual + "/src/main/resources/co.uniquindio.proyecto.backendalgoritmos/articulos.bib";

        List<DocumentsProperties> articles = DocumentsExtractor.readBibFile(bibFilePath);

        for (DocumentsProperties doc : articles) {
            String abs = doc.getAbstractDescription();
            if (abs != null && !abs.trim().isEmpty()) {
                abstracts.add(abs.trim());
            }
        }

        int limite = Math.max(1, abstracts.size() / 50); // Puedes ajustar esto según tus necesidades
        List<String> sublista = abstracts.subList(0, Math.min(limite, abstracts.size()));

        // Crear tabla de comparaciones
        List<String> comparaciones = new ArrayList<>();
        for (int i = 0; i < sublista.size(); i++) {
            for (int j = i + 1; j < sublista.size(); j++) {
                String a1 = sublista.get(i);
                String a2 = sublista.get(j);

                double jaccard = TextSimilarityGrouper.jaccardSimilarity(a1, a2) * 100.0;
                double tfidf = calcularSimilitudTFIDF(a1, a2, sublista);

                // Corta los abstracts largos para que no se vea tan extenso
                String resumen1 = a1.length() > 150 ? a1.substring(0, 150) + "..." : a1;
                String resumen2 = a2.length() > 150 ? a2.substring(0, 150) + "..." : a2;

                String linea = String.format(
                        "Comparación:\n- A: \"%s\"\n- B: \"%s\"\n→ Jaccard: %.2f%% | TF-IDF: %.2f%%\n",
                        resumen1, resumen2, jaccard, tfidf
                );

                comparaciones.add(linea);
            }
        }

        resultado.put("Comparaciones", comparaciones);
        return resultado;
    }

    private double calcularSimilitudTFIDF(String doc1, String doc2, List<String> corpus) {
        Set<String> vocab = new HashSet<>();
        List<String> tokens1 = Arrays.asList(doc1.toLowerCase().split("\\W+"));
        List<String> tokens2 = Arrays.asList(doc2.toLowerCase().split("\\W+"));
        List<List<String>> docsTokenizados = new ArrayList<>();

        for (String doc : corpus) {
            List<String> tokens = Arrays.asList(doc.toLowerCase().split("\\W+"));
            docsTokenizados.add(tokens);
            vocab.addAll(tokens);
        }

        List<String> vocabulario = new ArrayList<>(vocab);
        int N = corpus.size();
        Map<String, Integer> df = new HashMap<>();

        for (String term : vocabulario) {
            int count = 0;
            for (List<String> tokens : docsTokenizados) {
                if (tokens.contains(term)) count++;
            }
            df.put(term, count);
        }

        double[] vec1 = new double[vocabulario.size()];
        double[] vec2 = new double[vocabulario.size()];

        for (int i = 0; i < vocabulario.size(); i++) {
            String term = vocabulario.get(i);
            long tf1 = tokens1.stream().filter(t -> t.equals(term)).count();
            long tf2 = tokens2.stream().filter(t -> t.equals(term)).count();
            int dfVal = df.get(term);
            double idf = Math.log((double) N / (1 + dfVal));
            vec1[i] = tf1 * idf;
            vec2[i] = tf2 * idf;
        }

        return AgrupadorManual.cosineSimilarity(vec1, vec2) * 100.0;
    }


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
