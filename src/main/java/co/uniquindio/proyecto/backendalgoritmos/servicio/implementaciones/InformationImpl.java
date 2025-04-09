package co.uniquindio.proyecto.backendalgoritmos.servicio.implementaciones;

import co.uniquindio.proyecto.backendalgoritmos.models.DocumentsProperties;
import co.uniquindio.proyecto.backendalgoritmos.models.KeywordStat;
import co.uniquindio.proyecto.backendalgoritmos.models.ModelSortingResults;
import co.uniquindio.proyecto.backendalgoritmos.models.SortingResult;
import co.uniquindio.proyecto.backendalgoritmos.modules.OrderingMethods.RadixAlgorithms.ColaEnlazada;
import co.uniquindio.proyecto.backendalgoritmos.modules.OrderingMethods.SortingAlgorithms;
import co.uniquindio.proyecto.backendalgoritmos.modules.DocuemntsExtractor.DocumentsExtractor;
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
        results.add(new SortingResult("Burbuja", SortingAlgorithms.burbujaString(list)));
        results.add(new SortingResult("BurbujaDoble", SortingAlgorithms.burbujaDobleString(list)));
        results.add(new SortingResult("Seleccion", SortingAlgorithms.seleccionString(list)));
        results.add(new SortingResult("ShellSort", SortingAlgorithms.shellSortString(list)));

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
        results.add(new SortingResult("Burbuja", SortingAlgorithms.burbujaString(list)));
        results.add(new SortingResult("BurbujaDoble", SortingAlgorithms.burbujaDobleString(list)));
        results.add(new SortingResult("Seleccion", SortingAlgorithms.seleccionString(list)));
        results.add(new SortingResult("ShellSort", SortingAlgorithms.shellSortString(list)));

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
        int[] arreglo = convertirListaAEnteros(list);
        ColaEnlazada[] Q =  {
                new ColaEnlazada(),
                new ColaEnlazada(),
                new ColaEnlazada(),
                new ColaEnlazada(),
                new ColaEnlazada(),
                new ColaEnlazada(),
                new ColaEnlazada(),
                new ColaEnlazada(),
                new ColaEnlazada(),
                new ColaEnlazada()
        };

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
        results.add(new SortingResult("Burbuja", SortingAlgorithms.burbuja(list)));
        results.add(new SortingResult("BurbujaDoble", SortingAlgorithms.burbujaDoble(list)));
        results.add(new SortingResult("Seleccion", SortingAlgorithms.seleccion(list)));
        results.add(new SortingResult("ShellSort", SortingAlgorithms.shellSort(list)));
        results.add(new SortingResult("Radix", SortingAlgorithms.radix(arreglo, Q)));
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
        int[] arreglo = convertirListaAEnteros(list);
        ColaEnlazada[] Q =  {
                new ColaEnlazada(),
                new ColaEnlazada(),
                new ColaEnlazada(),
                new ColaEnlazada(),
                new ColaEnlazada(),
                new ColaEnlazada(),
                new ColaEnlazada(),
                new ColaEnlazada(),
                new ColaEnlazada(),
                new ColaEnlazada()
        };

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
        results.add(new SortingResult("Burbuja", SortingAlgorithms.burbuja(list)));
        results.add(new SortingResult("BurbujaDoble", SortingAlgorithms.burbujaDoble(list)));
        results.add(new SortingResult("Seleccion", SortingAlgorithms.seleccion(list)));
        results.add(new SortingResult("ShellSort", SortingAlgorithms.shellSort(list)));
        results.add(new SortingResult("Radix", SortingAlgorithms.radix(arreglo, Q)));

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
        results.add(new SortingResult("Burbuja", SortingAlgorithms.burbujaString(new ArrayList<>(keywordWords))));
        results.add(new SortingResult("BurbujaDoble", SortingAlgorithms.burbujaDobleString(new ArrayList<>(keywordWords))));
        results.add(new SortingResult("Seleccion", SortingAlgorithms.seleccionString(new ArrayList<>(keywordWords))));
        results.add(new SortingResult("ShellSort", SortingAlgorithms.shellSortString(new ArrayList<>(keywordWords))));

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

    public static int[] convertirListaAEnteros(List<String> lista) {
        int[] resultado = new int[lista.size()];
        for (int i = 0; i < lista.size(); i++) {
            resultado[i] = Integer.parseInt(lista.get(i));
        }
        return resultado;
    }

}
