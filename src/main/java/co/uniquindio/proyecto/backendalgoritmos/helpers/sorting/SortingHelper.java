package co.uniquindio.proyecto.backendalgoritmos.helpers.sorting;

import co.uniquindio.proyecto.backendalgoritmos.models.DocumentsProperties;
import co.uniquindio.proyecto.backendalgoritmos.models.KeywordStat;
import co.uniquindio.proyecto.backendalgoritmos.models.ModelSortingResults;
import co.uniquindio.proyecto.backendalgoritmos.models.SortingResult;
import co.uniquindio.proyecto.backendalgoritmos.modules.OrderingMethods.SortingAlgorithms;

import java.util.*;

public class SortingHelper {

    private static final Set<String> KEYWORDS = Set.of(
            "Abstraction", "Motivation", "Algorithm", "Persistence",
            "Coding", "Block", "Creativity", "Mobile application",
            "Logic", "Programming", "Conditionals", "Robotic",
            "Loops", "Scratch"
    );

    public static ModelSortingResults getAuthorSortingResults(List<DocumentsProperties> articles) {
        List<String> list = new ArrayList<>();
        for (DocumentsProperties doc : articles) {
            String author = doc.getAuthor();
            if (author != null) {
                list.add(author);
            }
        }
        return buildSortingResults("Autores", list);
    }

    public static ModelSortingResults getTitleSortingResults(List<DocumentsProperties> articles) {
        List<String> list = new ArrayList<>();
        for (DocumentsProperties doc : articles) {
            String title = doc.getTitle();
            if (title != null) {
                list.add(title);
            }
        }
        return buildSortingResults("Títulos", list);
    }

    public static ModelSortingResults getYearSortingResults(List<DocumentsProperties> articles) {
        List<String> list = new ArrayList<>();
        for (DocumentsProperties doc : articles) {
            list.add(String.valueOf(doc.getYear()));
        }
        return buildSortingResults("Año", list);
    }

    public static ModelSortingResults getNumberPagesSortingResults(List<DocumentsProperties> articles) {
        List<String> list = new ArrayList<>();
        for (DocumentsProperties doc : articles) {
            list.add(String.valueOf(doc.getNumpages()));
        }
        return buildSortingResults("Número de páginas", list);
    }

    public static ModelSortingResults getKeywordSortingResults(String keywordsString) {
        List<String> keywordWords = List.of(keywordsString.split(","));
        List<SortingResult> results = buildAllSortingAlgorithms(new ArrayList<>(keywordWords));

        // Agregamos también algoritmos extra que estaban solo en keywords
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

    private static ModelSortingResults buildSortingResults(String title, List<String> list) {
        List<SortingResult> results = buildAllSortingAlgorithms(list);
        return new ModelSortingResults(title, results, list.size());
    }

    private static List<SortingResult> buildAllSortingAlgorithms(List<String> list) {
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
        return results;
    }
}
