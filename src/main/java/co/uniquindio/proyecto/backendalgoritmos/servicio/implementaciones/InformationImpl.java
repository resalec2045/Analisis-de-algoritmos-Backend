package co.uniquindio.proyecto.backendalgoritmos.servicio.implementaciones;

import co.uniquindio.proyecto.backendalgoritmos.models.DocumentsProperties;
import co.uniquindio.proyecto.backendalgoritmos.modules.OrderingMethods.SortingAlgorithms;
import co.uniquindio.proyecto.backendalgoritmos.modules.PDFReader.DocumentsExtractor;
import co.uniquindio.proyecto.backendalgoritmos.modules.PDFReader.PDFReader;
import co.uniquindio.proyecto.backendalgoritmos.servicio.interfaces.InformationServicio;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class InformationImpl implements InformationServicio {

    @Override
    public List<Map<String, Object>> getInformation() {
        List<String> list = new ArrayList<>();

        // ! Logica de Mariana
        String directorioActual = System.getProperty("user.dir");
        String bibFilePath = directorioActual + "/src/main/resources/co.uniquindio.proyecto.backendalgoritmos/keywords.bib";
        PDFReader pdfReadernew = new PDFReader();
        pdfReadernew.ejecute();
        List<DocumentsProperties> allSortedKeywords = DocumentsExtractor.readBibFile(bibFilePath);

        for (int i = 0; i < allSortedKeywords.size(); i++) {
            String keywords = allSortedKeywords.get(i).getAuthor();
            System.out.println("Palabras clave del artículo " + (i + 1) + ": keywords = " + keywords);
            if (keywords != null) {
                list.add(keywords);
            }
        }

        // Copia de la lista original para evitar modificarla en cada ordenamiento
        List<List<String>> sortableList = Collections.singletonList(new ArrayList<>(list));

        // Lista para almacenar los resultados
        List<Map<String, Object>> results = new ArrayList<>();

        // 1. TimSort
        long timeTimSort = SortingAlgorithms.timSort(new ArrayList<>(sortableList));
        results.add(createResult("TimSort", timeTimSort));

        // 2. Comb Sort
        long timeCombSort = SortingAlgorithms.combSort(new ArrayList<>(sortableList));
        results.add(createResult("CombSort", timeCombSort));

        // 3. Selection Sort
        long timeSelectionSort = SortingAlgorithms.selectionSort(new ArrayList<>(sortableList));
        results.add(createResult("SelectionSort", timeSelectionSort));

        // 4. Tree Sort
        long timeTreeSort = SortingAlgorithms.treeSort(new ArrayList<>(sortableList));
        results.add(createResult("TreeSort", timeTreeSort));

        // 5. QuickSort
        long timeQuickSort = SortingAlgorithms.quickSort(new ArrayList<>(sortableList));
        results.add(createResult("QuickSort", timeQuickSort));

        // 6. HeapSort
        long timeHeapSort = SortingAlgorithms.heapSort(new ArrayList<>(sortableList));
        results.add(createResult("HeapSort", timeHeapSort));

        // 7. Gnome Sort
//        long timeGnomeSort = SortingAlgorithms.gnomeSort(new ArrayList<>(sortableList));
//        results.add(createResult("GnomeSort", timeGnomeSort));

        // 8. Binary Insertion Sort
        long timeBinaryInsertionSort = SortingAlgorithms.binaryInsertionSort(new ArrayList<>(sortableList));
        results.add(createResult("BinaryInsertionSort", timeBinaryInsertionSort));

        return results;
    }

    private Map<String, Object> createResult(String metodo, long time) {
        Map<String, Object> result = new HashMap<>();
        result.put("metodo", metodo);
        result.put("time", time);
        return result;
    }
}
