package co.uniquindio.proyecto.backendalgoritmos.servicio.implementaciones;

import co.uniquindio.proyecto.backendalgoritmos.models.ModelSortingResults;
import co.uniquindio.proyecto.backendalgoritmos.models.DocumentsProperties;
import co.uniquindio.proyecto.backendalgoritmos.models.SortingResult;
import co.uniquindio.proyecto.backendalgoritmos.modules.OrderingMethods.SortingAlgorithms;
import co.uniquindio.proyecto.backendalgoritmos.modules.DocuemntsExtractor.DocumentsExtractor;
import co.uniquindio.proyecto.backendalgoritmos.servicio.interfaces.InformationServicio;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class InformationImpl implements InformationServicio {

    @Override
    public List<Object> getInformation() {

        List<Object> modelFront = new ArrayList<>();

        // Obtener el directorio actual y leer el archivo .bib
        String directorioActual = System.getProperty("user.dir");
        String bibFilePath = directorioActual + "/src/main/resources/co.uniquindio.proyecto.backendalgoritmos/keywords.bib";

        List<DocumentsProperties> allSortedKeywords = DocumentsExtractor.readBibFile(bibFilePath);

        modelFront.add(getAuthorSortingResults(allSortedKeywords));
        modelFront.add(getTitleSortingResults(allSortedKeywords));
        modelFront.add(getLocationSortingResults(allSortedKeywords));
        modelFront.add(getYearSortingResults(allSortedKeywords));

        // Devolver el objeto con la lista estructurada bajo el autor
        return modelFront;

    }

    private ModelSortingResults getAuthorSortingResults(List<DocumentsProperties> allSortedKeywords) {
        List<String> list = new ArrayList<>();

        for (DocumentsProperties doc : allSortedKeywords) {
            String keywords = doc.getAuthor();
            if (keywords != null) {
                list.add(keywords);
            }
        }

        // Obtener el nombre del autor (tomando el primero de la lista si existe)
        String author = "Autores";

        // Copia de la lista original para evitar modificarla en cada ordenamiento
        List<List<String>> sortableList = Collections.singletonList(new ArrayList<>(list));

        // Lista de resultados de los algoritmos
        List<SortingResult> results = new ArrayList<>();
        results.add(new SortingResult("TimSort", SortingAlgorithms.timSort(new ArrayList<>(sortableList))));
        results.add(new SortingResult("CombSort", SortingAlgorithms.combSort(new ArrayList<>(sortableList))));
        results.add(new SortingResult("SelectionSort", SortingAlgorithms.selectionSort(new ArrayList<>(sortableList))));
        results.add(new SortingResult("TreeSort", SortingAlgorithms.treeSort(new ArrayList<>(sortableList))));
        results.add(new SortingResult("QuickSort", SortingAlgorithms.quickSort(new ArrayList<>(sortableList))));
        results.add(new SortingResult("HeapSort", SortingAlgorithms.heapSort(new ArrayList<>(sortableList))));
        results.add(new SortingResult("GnomeSort", SortingAlgorithms.gnomeSort(new ArrayList<>(sortableList))));
        results.add(new SortingResult("BinaryInsertionSort", SortingAlgorithms.binaryInsertionSort(new ArrayList<>(sortableList))));
        results.add(new SortingResult("PigeonholeSort", SortingAlgorithms.pigeonholeSort(new ArrayList<>(sortableList))));
        results.add(new SortingResult("BucketSort", SortingAlgorithms.bucketSort(new ArrayList<>(sortableList))));
        results.add(new SortingResult("BitonicSort", SortingAlgorithms.bitonicSort(new ArrayList<>(sortableList))));
        results.add(new SortingResult("RadixSort", SortingAlgorithms.radixSort(new ArrayList<>(sortableList))));

        return new ModelSortingResults(author, results);

    }

    private ModelSortingResults getTitleSortingResults(List<DocumentsProperties> allSortedKeywords) {
        List<String> list = new ArrayList<>();

        for (DocumentsProperties doc : allSortedKeywords) {
            String keywords = doc.getTitle();
            if (keywords != null) {
                list.add(keywords);
            }
        }

        // Obtener el nombre del autor (tomando el primero de la lista si existe)
        String author = "Titulos";

        // Copia de la lista original para evitar modificarla en cada ordenamiento
        List<List<String>> sortableList = Collections.singletonList(new ArrayList<>(list));

        // Lista de resultados de los algoritmos
        List<SortingResult> results = new ArrayList<>();
        results.add(new SortingResult("TimSort", SortingAlgorithms.timSort(new ArrayList<>(sortableList))));
        results.add(new SortingResult("CombSort", SortingAlgorithms.combSort(new ArrayList<>(sortableList))));
        results.add(new SortingResult("SelectionSort", SortingAlgorithms.selectionSort(new ArrayList<>(sortableList))));
        results.add(new SortingResult("TreeSort", SortingAlgorithms.treeSort(new ArrayList<>(sortableList))));
        results.add(new SortingResult("QuickSort", SortingAlgorithms.quickSort(new ArrayList<>(sortableList))));
        results.add(new SortingResult("HeapSort", SortingAlgorithms.heapSort(new ArrayList<>(sortableList))));
        results.add(new SortingResult("GnomeSort", SortingAlgorithms.gnomeSort(new ArrayList<>(sortableList))));
        results.add(new SortingResult("BinaryInsertionSort", SortingAlgorithms.binaryInsertionSort(new ArrayList<>(sortableList))));
        results.add(new SortingResult("PigeonholeSort", SortingAlgorithms.pigeonholeSort(new ArrayList<>(sortableList))));
        results.add(new SortingResult("BucketSort", SortingAlgorithms.bucketSort(new ArrayList<>(sortableList))));
        results.add(new SortingResult("BitonicSort", SortingAlgorithms.bitonicSort(new ArrayList<>(sortableList))));
        results.add(new SortingResult("RadixSort", SortingAlgorithms.radixSort(new ArrayList<>(sortableList))));

        return new ModelSortingResults(author, results);

    }

    private ModelSortingResults getLocationSortingResults(List<DocumentsProperties> allSortedKeywords) {
        List<String> list = new ArrayList<>();

        for (DocumentsProperties doc : allSortedKeywords) {
            String keywords = doc.getLocation();
            if (keywords != null) {
                list.add(keywords);
            }
        }

        // Obtener el nombre del autor (tomando el primero de la lista si existe)
        String author = "Localizacion";

        // Copia de la lista original para evitar modificarla en cada ordenamiento
        List<List<String>> sortableList = Collections.singletonList(new ArrayList<>(list));

        // Lista de resultados de los algoritmos
        List<SortingResult> results = new ArrayList<>();
        results.add(new SortingResult("TimSort", SortingAlgorithms.timSort(new ArrayList<>(sortableList))));
        results.add(new SortingResult("CombSort", SortingAlgorithms.combSort(new ArrayList<>(sortableList))));
        results.add(new SortingResult("SelectionSort", SortingAlgorithms.selectionSort(new ArrayList<>(sortableList))));
        results.add(new SortingResult("TreeSort", SortingAlgorithms.treeSort(new ArrayList<>(sortableList))));
        results.add(new SortingResult("QuickSort", SortingAlgorithms.quickSort(new ArrayList<>(sortableList))));
        results.add(new SortingResult("HeapSort", SortingAlgorithms.heapSort(new ArrayList<>(sortableList))));
        results.add(new SortingResult("GnomeSort", SortingAlgorithms.gnomeSort(new ArrayList<>(sortableList))));
        results.add(new SortingResult("BinaryInsertionSort", SortingAlgorithms.binaryInsertionSort(new ArrayList<>(sortableList))));
        results.add(new SortingResult("PigeonholeSort", SortingAlgorithms.pigeonholeSort(new ArrayList<>(sortableList))));
        results.add(new SortingResult("BucketSort", SortingAlgorithms.bucketSort(new ArrayList<>(sortableList))));
        results.add(new SortingResult("BitonicSort", SortingAlgorithms.bitonicSort(new ArrayList<>(sortableList))));
        results.add(new SortingResult("RadixSort", SortingAlgorithms.radixSort(new ArrayList<>(sortableList))));

        return new ModelSortingResults(author, results);

    }

    private ModelSortingResults getYearSortingResults(List<DocumentsProperties> allSortedKeywords) {
        List<String> list = new ArrayList<>();

        for (DocumentsProperties doc : allSortedKeywords) {
            String keywords = doc.getYear() + "";
            if (keywords != null) {
                list.add(keywords);
            }
        }

        // Obtener el nombre del autor (tomando el primero de la lista si existe)
        String author = "Año";

        // Copia de la lista original para evitar modificarla en cada ordenamiento
        List<List<String>> sortableList = Collections.singletonList(new ArrayList<>(list));

        // Lista de resultados de los algoritmos
        List<SortingResult> results = new ArrayList<>();
        results.add(new SortingResult("TimSort", SortingAlgorithms.timSort(new ArrayList<>(sortableList))));
        results.add(new SortingResult("CombSort", SortingAlgorithms.combSort(new ArrayList<>(sortableList))));
        results.add(new SortingResult("SelectionSort", SortingAlgorithms.selectionSort(new ArrayList<>(sortableList))));
        results.add(new SortingResult("TreeSort", SortingAlgorithms.treeSort(new ArrayList<>(sortableList))));
        results.add(new SortingResult("QuickSort", SortingAlgorithms.quickSort(new ArrayList<>(sortableList))));
        results.add(new SortingResult("HeapSort", SortingAlgorithms.heapSort(new ArrayList<>(sortableList))));
        results.add(new SortingResult("GnomeSort", SortingAlgorithms.gnomeSort(new ArrayList<>(sortableList))));
        results.add(new SortingResult("BinaryInsertionSort", SortingAlgorithms.binaryInsertionSort(new ArrayList<>(sortableList))));
        results.add(new SortingResult("PigeonholeSort", SortingAlgorithms.pigeonholeSort(new ArrayList<>(sortableList))));
        results.add(new SortingResult("BucketSort", SortingAlgorithms.bucketSort(new ArrayList<>(sortableList))));
        results.add(new SortingResult("BitonicSort", SortingAlgorithms.bitonicSort(new ArrayList<>(sortableList))));
        results.add(new SortingResult("RadixSort", SortingAlgorithms.radixSort(new ArrayList<>(sortableList))));

        return new ModelSortingResults(author, results);

    }

}
