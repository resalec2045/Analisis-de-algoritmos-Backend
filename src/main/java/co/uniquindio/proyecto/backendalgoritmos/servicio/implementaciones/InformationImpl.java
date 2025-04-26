package co.uniquindio.proyecto.backendalgoritmos.servicio.implementaciones;

import co.uniquindio.proyecto.backendalgoritmos.models.DocumentsProperties;
import co.uniquindio.proyecto.backendalgoritmos.models.KeywordStat;
import co.uniquindio.proyecto.backendalgoritmos.models.ModelSortingResults;
import co.uniquindio.proyecto.backendalgoritmos.models.SortingResult;
import co.uniquindio.proyecto.backendalgoritmos.modules.OrderingMethods.SortingAlgorithms;
import co.uniquindio.proyecto.backendalgoritmos.modules.DocuemntsExtractor.DocumentsExtractor;
import co.uniquindio.proyecto.backendalgoritmos.modules.ProcesamientoTexto.Cluster;
import co.uniquindio.proyecto.backendalgoritmos.modules.ProcesamientoTexto.PreprocesamientoTexto;
import co.uniquindio.proyecto.backendalgoritmos.servicio.interfaces.InformationServicio;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class InformationImpl implements InformationServicio {

    private static final Set<String> KEYWORDS = Set.of(
            "Abstraction", "Motivation", "Algorithm", "Persistence",
            "Coding", "Block", "Creativity", "Mobile application",
            "Logic", "Programming", "Conditionals", "Robotic",
            "Loops", "Scratch"
    );

    private static final Map<String, Set<String>> RELACIONES_SEMANTICAS = Map.ofEntries(
            Map.entry("learn", Set.of("study", "education")),
            Map.entry("study", Set.of("learn", "education")),
            Map.entry("education", Set.of("learn", "study", "teaching")),
            Map.entry("computational", Set.of("computing", "computation")),
            Map.entry("computing", Set.of("computational", "computation")),
            Map.entry("computation", Set.of("computing", "computational")),
            Map.entry("teach", Set.of("education", "teaching")),
            Map.entry("teaching", Set.of("teach", "education"))
    );

    private static final Set<String> VERBOS_COMUNES = Set.of(
            "is", "are", "was", "were", "be", "been", "being",
            "have", "has", "had", "do", "does", "did",
            "can", "could", "will", "would", "shall", "should",
            "may", "might", "must", "ought", "make", "makes", "made",
            "use", "uses", "used",
            "create", "creates", "created", "provide", "provides", "provided",
            "apply", "applies", "applied", "consider", "considers", "considered"
    );

    @Override
    public List<Object> getInformation() {

        List<Object> modelFront = new ArrayList<>();

        // Obtener el directorio actual y leer el archivo .bib
        String directorioActual = System.getProperty("user.dir");
        String bibFilePath = directorioActual + "/src/main/resources/co.uniquindio.proyecto.backendalgoritmos/articulos.bib";
        List<DocumentsProperties> articles = DocumentsExtractor.readBibFile(bibFilePath);

        modelFront.add(getAuthorSortingResults(articles));
        modelFront.add(getTitleSortingResults(articles));
        modelFront.add(getNumberPagesSortingResults(articles));
        modelFront.add(getYearSortingResults(articles));

        // Devolver el objeto con la lista estructurada bajo el autor
        return modelFront;

    }

    @Override
    public List<Object> getInformationAbstract() {

        List<Object> modelFront = new ArrayList<>();
        String abstracts = "";

        // Obtener el directorio actual y leer el archivo .bib
        String directorioActual = System.getProperty("user.dir");
        String bibFilePath = directorioActual + "/src/main/resources/co.uniquindio.proyecto.backendalgoritmos/articulos.bib";
        List<DocumentsProperties> articles = DocumentsExtractor.readBibFile(bibFilePath);

        for (DocumentsProperties doc : articles) {
            String keywords = doc.getKeywords();
            if (keywords != null) {
                abstracts = abstracts + keywords + ", ";
            }
        }

        modelFront.add(countKeywords(abstracts));

        modelFront.add(getKeywordSortingResults(abstracts));

        // Devolver el objeto con la lista estructurada bajo el autor
        return modelFront;

    }

    @Override
    public Map<String, Object> preprocesamientoTexto() {
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

        if (abstracts.size() < 1) {
            throw new IllegalArgumentException("Se requiere al menos un abstract.");
        }

        Random random = new Random();
        String selectedAbstract = abstracts.get(random.nextInt(abstracts.size()));

        List<String> palabras = PreprocesamientoTexto.preprocesarTexto(selectedAbstract);

        Map<String, Object> dendrograma = clusteringJerarquicoPalabras(palabras);

        return dendrograma;
    }

    private Map<String, Object> clusteringJerarquicoPalabras(List<String> palabras) {
        // 1. Eliminar verbos comunes
        List<String> palabrasFiltradas = palabras.stream()
                .filter(p -> !VERBOS_COMUNES.contains(p))
                .toList();

        // 2. Inicializar cada palabra como su propio cluster
        List<Cluster> clusters = new ArrayList<>();
        for (String palabra : palabrasFiltradas) {
            clusters.add(new Cluster(palabra));
        }

        // 3. Clustering aglomerativo real
        while (clusters.size() > 1) {
            double mejorDistancia = Double.MAX_VALUE;
            int mejorI = 0;
            int mejorJ = 1;

            for (int i = 0; i < clusters.size(); i++) {
                for (int j = i + 1; j < clusters.size(); j++) {
                    double distancia = distanciaClusters(clusters.get(i), clusters.get(j));
                    if (distancia < mejorDistancia) {
                        mejorDistancia = distancia;
                        mejorI = i;
                        mejorJ = j;
                    }
                }
            }

            // 🔥 Aquí usamos calcularNombreCluster para nombrar bonito
            String nombreNuevo = calcularNombreCluster(clusters.get(mejorI), clusters.get(mejorJ));
            Cluster nuevo = new Cluster(nombreNuevo);
            nuevo.addChild(clusters.get(mejorI));
            nuevo.addChild(clusters.get(mejorJ));

            // Eliminar los viejos clusters
            clusters.remove(mejorJ); // remover el índice mayor primero
            clusters.remove(mejorI);

            // Agregar el nuevo cluster
            clusters.add(nuevo);
        }

        // 4. Convertir el cluster final a formato React-D3-Tree
        return convertirClusterAReactD3Tree(clusters.get(0));
    }

    private Map<String, Object> convertirClusterAReactD3Tree(Cluster cluster) {
        Map<String, Object> nodo = new HashMap<>();
        nodo.put("name", cluster.getName());

        if (cluster.getChildren() != null && !cluster.getChildren().isEmpty()) {
            List<Map<String, Object>> hijos = new ArrayList<>();
            for (Cluster hijo : cluster.getChildren()) {
                hijos.add(convertirClusterAReactD3Tree(hijo));
            }
            nodo.put("children", hijos);
        }

        return nodo;
    }

    private String calcularNombreCluster(Cluster c1, Cluster c2) {
        String nombre1 = encontrarPrimerNombre(c1);
        String nombre2 = encontrarPrimerNombre(c2);

        if (sonSemanticamenteRelacionadas(nombre1, nombre2)) {
            return nombre1 + "-" + nombre2;
        }

        String prefijo = prefijoComun(nombre1, nombre2);

        if (!prefijo.isEmpty()) {
            return prefijo;
        } else {
            return nombre1 + "-" + nombre2;
        }
    }

    private String encontrarPrimerNombre(Cluster cluster) {
        if (cluster.getChildren() == null || cluster.getChildren().isEmpty()) {
            return cluster.getName();
        }
        return encontrarPrimerNombre(cluster.getChildren().get(0));
    }

    private String prefijoComun(String a, String b) {
        int minLength = Math.min(a.length(), b.length());
        int i = 0;
        while (i < minLength && a.charAt(i) == b.charAt(i)) {
            i++;
        }
        return a.substring(0, i);
    }

    private boolean sonSemanticamenteRelacionadas(String palabra1, String palabra2) {
        Set<String> relaciones1 = RELACIONES_SEMANTICAS.getOrDefault(palabra1, Set.of());
        Set<String> relaciones2 = RELACIONES_SEMANTICAS.getOrDefault(palabra2, Set.of());

        return relaciones1.contains(palabra2) || relaciones2.contains(palabra1);
    }

    private double distanciaClusters(Cluster c1, Cluster c2) {
        String nombre1 = encontrarPrimerNombre(c1);
        String nombre2 = encontrarPrimerNombre(c2);

        if (nombre1.equals(nombre2)) {
            return 0.0; // iguales
        }

        if (sonSemanticamenteRelacionadas(nombre1, nombre2)) {
            return 0.2; // bastante cercanos si son sinónimos
        }

        String prefijo = prefijoComun(nombre1, nombre2);
        if (!prefijo.isEmpty() && prefijo.length() >= 3) {
            return 0.4; // algo cercanos si comparten prefijo
        }

        return 1.0; // máximo si son totalmente diferentes
    }

    private ModelSortingResults getAuthorSortingResults(List<DocumentsProperties> articles) {
        List<String> list = new ArrayList<>();

        for (DocumentsProperties doc : articles) {
            String keywords = doc.getAuthor();
            if (keywords != null) {
                list.add(keywords);
            }
        }

        // Obtener el nombre del autor (tomando el primero de la lista si existe)
        String author = "Autores";

        // Lista de resultados de los algoritmos
        List<SortingResult> results = new ArrayList<>();
        results.add(new SortingResult("TimSort", SortingAlgorithms.timSort(list)));
        results.add(new SortingResult("CombSort", SortingAlgorithms.combSort(list)));
        results.add(new SortingResult("SelectionSort", SortingAlgorithms.selectionSort(list)));
        results.add(new SortingResult("TreeSort", SortingAlgorithms.treeSort(list)));
        results.add(new SortingResult("QuickSort", SortingAlgorithms.quickSort(list)));
        results.add(new SortingResult("HeapSort", SortingAlgorithms.heapSort(list)));
        results.add(new SortingResult("GnomeSort", SortingAlgorithms.gnomeSort(list)));
        results.add(new SortingResult("BinaryInsertionSort", SortingAlgorithms.binaryInsertionSort(list)));
        results.add(new SortingResult("PigeonholeSort", SortingAlgorithms.pigeonholeSort(list)));
        results.add(new SortingResult("BucketSort", SortingAlgorithms.bucketSort(list)));
        results.add(new SortingResult("BitonicSort", SortingAlgorithms.bitonicSort(list)));
        results.add(new SortingResult("RadixSort", SortingAlgorithms.radixSort(list)));

        return new ModelSortingResults(author, results, list.size());

    }

    private ModelSortingResults getTitleSortingResults(List<DocumentsProperties> articles) {
        List<String> list = new ArrayList<>();

        for (DocumentsProperties doc : articles) {
            String keywords = doc.getTitle();
            if (keywords != null) {
                list.add(keywords);
            }
        }

        // Obtener el nombre del autor (tomando el primero de la lista si existe)
        String author = "Titulos";

        // Lista de resultados de los algoritmos
        List<SortingResult> results = new ArrayList<>();
        results.add(new SortingResult("TimSort", SortingAlgorithms.timSort(list)));
        results.add(new SortingResult("CombSort", SortingAlgorithms.combSort(list)));
        results.add(new SortingResult("SelectionSort", SortingAlgorithms.selectionSort(list)));
        results.add(new SortingResult("TreeSort", SortingAlgorithms.treeSort(list)));
        results.add(new SortingResult("QuickSort", SortingAlgorithms.quickSort(list)));
        results.add(new SortingResult("HeapSort", SortingAlgorithms.heapSort(list)));
        results.add(new SortingResult("GnomeSort", SortingAlgorithms.gnomeSort(list)));
        results.add(new SortingResult("BinaryInsertionSort", SortingAlgorithms.binaryInsertionSort(list)));
        results.add(new SortingResult("PigeonholeSort", SortingAlgorithms.pigeonholeSort(list)));
        results.add(new SortingResult("BucketSort", SortingAlgorithms.bucketSort(list)));
        results.add(new SortingResult("BitonicSort", SortingAlgorithms.bitonicSort(list)));
        results.add(new SortingResult("RadixSort", SortingAlgorithms.radixSort(list)));

        return new ModelSortingResults(author, results, list.size());

    }

    private ModelSortingResults getYearSortingResults(List<DocumentsProperties> articles) {
        List<String> list = new ArrayList<>();

        for (DocumentsProperties doc : articles) {
            String keywords = doc.getYear() + "";
            if (keywords != null) {
                list.add(keywords);
            }
        }

        // Obtener el nombre del autor (tomando el primero de la lista si existe)
        String author = "Año";

        // Lista de resultados de los algoritmos
        List<SortingResult> results = new ArrayList<>();
        results.add(new SortingResult("TimSort", SortingAlgorithms.timSort(list)));
        results.add(new SortingResult("CombSort", SortingAlgorithms.combSort(list)));
        results.add(new SortingResult("SelectionSort", SortingAlgorithms.selectionSort(list)));
        results.add(new SortingResult("TreeSort", SortingAlgorithms.treeSort(list)));
        results.add(new SortingResult("QuickSort", SortingAlgorithms.quickSort(list)));
        results.add(new SortingResult("HeapSort", SortingAlgorithms.heapSort(list)));
        results.add(new SortingResult("GnomeSort", SortingAlgorithms.gnomeSort(list)));
        results.add(new SortingResult("BinaryInsertionSort", SortingAlgorithms.binaryInsertionSort(list)));
        results.add(new SortingResult("PigeonholeSort", SortingAlgorithms.pigeonholeSort(list)));
        results.add(new SortingResult("BucketSort", SortingAlgorithms.bucketSort(list)));
        results.add(new SortingResult("BitonicSort", SortingAlgorithms.bitonicSort(list)));
        results.add(new SortingResult("RadixSort", SortingAlgorithms.radixSort(list)));

        return new ModelSortingResults(author, results, list.size());

    }

    private ModelSortingResults getNumberPagesSortingResults(List<DocumentsProperties> articles) {
        List<String> list = new ArrayList<>();

        for (DocumentsProperties doc : articles) {
            String keywords = doc.getNumpages() + "";
            if (keywords != null) {
                list.add(keywords);
            }
        }

        // Obtener el nombre del autor (tomando el primero de la lista si existe)
        String author = "Número de páginas";

        // Lista de resultados de los algoritmos
        List<SortingResult> results = new ArrayList<>();
        results.add(new SortingResult("TimSort", SortingAlgorithms.timSort(list)));
        results.add(new SortingResult("CombSort", SortingAlgorithms.combSort(list)));
        results.add(new SortingResult("SelectionSort", SortingAlgorithms.selectionSort(list)));
        results.add(new SortingResult("TreeSort", SortingAlgorithms.treeSort(list)));
        results.add(new SortingResult("QuickSort", SortingAlgorithms.quickSort(list)));
        results.add(new SortingResult("HeapSort", SortingAlgorithms.heapSort(list)));
        results.add(new SortingResult("GnomeSort", SortingAlgorithms.gnomeSort(list)));
        results.add(new SortingResult("BinaryInsertionSort", SortingAlgorithms.binaryInsertionSort(list)));
        results.add(new SortingResult("PigeonholeSort", SortingAlgorithms.pigeonholeSort(list)));
        results.add(new SortingResult("BucketSort", SortingAlgorithms.bucketSort(list)));
        results.add(new SortingResult("BitonicSort", SortingAlgorithms.bitonicSort(list)));
        results.add(new SortingResult("RadixSort", SortingAlgorithms.radixSort(list)));

        return new ModelSortingResults(author, results, list.size());

    }

    private ModelSortingResults getKeywordSortingResults(String keywordsString) {

        List<String> keywordWords = List.of(keywordsString.split(","));

        // Crear los resultados de ordenamiento
        List<SortingResult> results = new ArrayList<>();

        results.add(new SortingResult("TimSort", SortingAlgorithms.timSort(new ArrayList<>(keywordWords))));
        results.add(new SortingResult("CombSort", SortingAlgorithms.combSort(new ArrayList<>(keywordWords))));
        results.add(new SortingResult("SelectionSort", SortingAlgorithms.selectionSort(new ArrayList<>(keywordWords))));
        results.add(new SortingResult("TreeSort", SortingAlgorithms.treeSort(new ArrayList<>(keywordWords))));
        results.add(new SortingResult("QuickSort", SortingAlgorithms.quickSort(new ArrayList<>(keywordWords))));
        results.add(new SortingResult("HeapSort", SortingAlgorithms.heapSort(new ArrayList<>(keywordWords))));
        results.add(new SortingResult("GnomeSort", SortingAlgorithms.gnomeSort(new ArrayList<>(keywordWords))));
        results.add(new SortingResult("BinaryInsertionSort", SortingAlgorithms.binaryInsertionSort(new ArrayList<>(keywordWords))));
        results.add(new SortingResult("PigeonholeSort", SortingAlgorithms.pigeonholeSort(new ArrayList<>(keywordWords))));
        results.add(new SortingResult("BucketSort", SortingAlgorithms.bucketSort(new ArrayList<>(keywordWords))));
        results.add(new SortingResult("BitonicSort", SortingAlgorithms.bitonicSort(new ArrayList<>(keywordWords))));
        results.add(new SortingResult("RadixSort", SortingAlgorithms.radixSort(new ArrayList<>(keywordWords))));
        results.add(new SortingResult("Burbuja", SortingAlgorithms.bubbleSort(new ArrayList<>(keywordWords))));
        results.add(new SortingResult("Burbuja doble", SortingAlgorithms.cocktailSort(new ArrayList<>(keywordWords))));
        results.add(new SortingResult("Shell Sort", SortingAlgorithms.shellSort(new ArrayList<>(keywordWords))));

        return new ModelSortingResults("Keywords", results, keywordWords.size());
    }

    public static List<KeywordStat> countKeywords(String keywordsString) {
        String[] words = keywordsString.split(",");
        Map<String, Integer> keywordCount = new LinkedHashMap<>();

        for (String keyword : KEYWORDS) {
            keywordCount.put(keyword, 0);
        }

        for (String word : words) {
            String trimmedWord = word.trim();
            if (keywordCount.containsKey(trimmedWord)) {
                keywordCount.put(trimmedWord, keywordCount.get(trimmedWord) + 1);
            }
        }

        List<KeywordStat> result = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : keywordCount.entrySet()) {
            result.add(new KeywordStat(entry.getKey(), entry.getValue()));
        }

        return result;
    }

}
