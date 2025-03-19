package co.uniquindio.proyecto.backendalgoritmos.servicio.implementaciones;

import co.uniquindio.proyecto.backendalgoritmos.models.DocumentsProperties;
import co.uniquindio.proyecto.backendalgoritmos.models.ModelSortingResults;
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
        String bibFilePath = directorioActual + "/src/main/resources/co.uniquindio.proyecto.backendalgoritmos/articulos.bib";
        List<DocumentsProperties> articles = DocumentsExtractor.readBibFile(bibFilePath);

        modelFront.add(getAuthorSortingResults(articles));
        modelFront.add(getTitleSortingResults(articles));
        modelFront.add(getNumberPagesSortingResults(articles));
        modelFront.add(getYearSortingResults(articles));

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

        return new ModelSortingResults(author, results);

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

        return new ModelSortingResults(author, results);

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

        return new ModelSortingResults(author, results);

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

        return new ModelSortingResults(author, results);

    }

}
