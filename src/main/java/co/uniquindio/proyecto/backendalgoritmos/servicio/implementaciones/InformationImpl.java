package co.uniquindio.proyecto.backendalgoritmos.servicio.implementaciones;

import co.uniquindio.proyecto.backendalgoritmos.models.DocumentsProperties;
import co.uniquindio.proyecto.backendalgoritmos.models.KeywordStat;
import co.uniquindio.proyecto.backendalgoritmos.models.ModelSortingResults;
import co.uniquindio.proyecto.backendalgoritmos.models.SortingResult;
import co.uniquindio.proyecto.backendalgoritmos.modules.OrderingMethods.SortingAlgorithms;
import co.uniquindio.proyecto.backendalgoritmos.modules.DocuemntsExtractor.DocumentsExtractor;
import co.uniquindio.proyecto.backendalgoritmos.servicio.interfaces.InformationServicio;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class InformationImpl implements InformationServicio {

    private static final Set<String> KEYWORDS = Set.of(
            "Abstraction", "Motivation", "Algorithm", "Persistence",
            "Coding", "Block", "Creativity", "Mobile application",
            "Logic", "Programming", "Conditionals", "Robotic",
            "Loops", "Scratch"
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
    public List<Object> generateKeywordReport() {
        // Lista donde se guardan los resultados que se van a devolver al frontend
        List<Object> modelFront = new ArrayList<>();

        // StringBuilder es más eficiente que usar concatenación de strings
        StringBuilder abstractsBuilder = new StringBuilder();

        // Ruta al archivo .bib (bibliografía/artículos) desde el directorio actual
        String bibFilePath = System.getProperty("user.dir") +
                "/src/main/resources/co.uniquindio.proyecto.backendalgoritmos/articulos.bib";

        // Se leen los artículos del archivo .bib y se guardan como objetos
        List<DocumentsProperties> articles = DocumentsExtractor.readBibFile(bibFilePath);

        // Por cada artículo, si tiene palabras clave, se agregan al StringBuilder
        for (DocumentsProperties doc : articles) {
            if (doc.getKeywords() != null) {
                //Aquí se está acumulando todas las keywords de los artículos en una sola cadena de forma eficiente
                abstractsBuilder.append(doc.getKeywords()).append(", ");
            }
        }

        // Se convierte el contenido del StringBuilder a una sola cadena
        String abstracts = abstractsBuilder.toString();

        // Se agrega a la lista el resultado del conteo de keywords
        modelFront.add(analyzeKeywordOccurrences(abstracts));

        // Se agrega a la lista el resultado de ordenar las keywords con diferentes algoritmos
        modelFront.add(applySortingAlgorithms(abstracts));

        // Retorna la lista con ambos resultados: conteo y ordenamientos
        return modelFront;
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

    private ModelSortingResults applySortingAlgorithms(String keywordsString) {
        List<String> keywordWords = Arrays.stream(keywordsString.split(","))
                .map(String::trim)
                .filter(word -> !word.isEmpty())
                .collect(Collectors.toList()); // recolecta los resultados de la stream

        List<SortingResult> results = new ArrayList<>();
        List<String> originalList = new ArrayList<>(keywordWords); // Para evitar split múltiple

        // Aplicar los algoritmos de sorting sobre copias de la lista original
        results.add(new SortingResult("TimSort", SortingAlgorithms.timSort(new ArrayList<>(originalList))));
        results.add(new SortingResult("CombSort", SortingAlgorithms.combSort(new ArrayList<>(originalList))));
        results.add(new SortingResult("SelectionSort", SortingAlgorithms.selectionSort(new ArrayList<>(originalList))));
        results.add(new SortingResult("TreeSort", SortingAlgorithms.treeSort(new ArrayList<>(originalList))));
        results.add(new SortingResult("QuickSort", SortingAlgorithms.quickSort(new ArrayList<>(originalList))));
        results.add(new SortingResult("HeapSort", SortingAlgorithms.heapSort(new ArrayList<>(originalList))));
        results.add(new SortingResult("GnomeSort", SortingAlgorithms.gnomeSort(new ArrayList<>(originalList))));
        results.add(new SortingResult("BinaryInsertionSort", SortingAlgorithms.binaryInsertionSort(new ArrayList<>(originalList))));
        results.add(new SortingResult("PigeonholeSort", SortingAlgorithms.pigeonholeSort(new ArrayList<>(originalList))));
        results.add(new SortingResult("BucketSort", SortingAlgorithms.bucketSort(new ArrayList<>(originalList))));
        results.add(new SortingResult("BitonicSort", SortingAlgorithms.bitonicSort(new ArrayList<>(originalList))));
        results.add(new SortingResult("RadixSort", SortingAlgorithms.radixSort(new ArrayList<>(originalList))));
        results.add(new SortingResult("bubbleSort", SortingAlgorithms.bubbleSort(new ArrayList<>(originalList))));

        return new ModelSortingResults("Keywords", results, keywordWords.size());
    }

    public static List<KeywordStat> analyzeKeywordOccurrences(String keywordsString) {
        // Mapa que guarda el conteo de cada palabra clave conocida
        Map<String, Integer> keywordCount = new LinkedHashMap<>();
        // mantiene el mismo orden en que aparecen las keywords originales

        // Inicializa todas las keywords conocidas con conteo 0
        for (String keyword : KEYWORDS) {
            keywordCount.put(keyword, 0);
        }

        // Divide el string, limpia espacios, y si la palabra está en el mapa, suma 1
        Arrays.stream(keywordsString.split(","))
                .map(String::trim)// quita espacios,convierte cada elemento en algo nuevo.
                .filter(word -> keywordCount.containsKey(word)) //filtra elementos que cumplan con la condicion
                .forEach(word -> keywordCount.put(word, keywordCount.get(word) + 1));

        // Convierte el mapa en una lista de objetos KeywordStat (nombre + cantidad)
        return keywordCount.entrySet().stream() // secuencia de datos que puedan procesar
                .map(entry -> new KeywordStat(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }
}
