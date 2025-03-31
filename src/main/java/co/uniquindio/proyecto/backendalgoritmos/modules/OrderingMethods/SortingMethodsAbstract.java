package co.uniquindio.proyecto.backendalgoritmos.modules.OrderingMethods;

import co.uniquindio.proyecto.backendalgoritmos.models.DocumentsProperties;
import co.uniquindio.proyecto.backendalgoritmos.modules.DocuemntsExtractor.DocumentsExtractor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;
import java.util.List;

import org.jfree.chart.*;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class SortingMethodsAbstract extends JFrame {

    private static final String[] keywords = {
            "Abstraction", "Motivation", "Algorithm", "Persistence", "Coding", "Block", "Creativity", "Mobile application",
            "Logic", "Programming", "Conditionals", "Robotic", "Loops", "Scratch"
    };

    public SortingMethodsAbstract() {
        setTitle("Frecuencia de Aparición y Tiempo de Ejecución");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        List<String> frequencyData = getDataAbstract();
        JTable frequencyTable = createFrequencyTable(frequencyData);
        JScrollPane scrollPane = new JScrollPane(frequencyTable);
        add(scrollPane, BorderLayout.NORTH);

        JFreeChart barChart = createExecutionTimeBarChart(frequencyData);
        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setPreferredSize(new Dimension(600, 300));
        add(chartPanel, BorderLayout.CENTER);
    }

    public List<String> getDataAbstract() {
        StringBuilder abstracts = new StringBuilder();
        String directorioActual = System.getProperty("user.dir");
        String bibFilePath = directorioActual + "/src/main/resources/co.uniquindio.proyecto.backendalgoritmos/articulos.bib";
        List<DocumentsProperties> articles = DocumentsExtractor.readBibFile(bibFilePath);

        for (DocumentsProperties doc : articles) {
            abstracts.append(doc.getAbstractDescription()).append(", ");
        }

        List<Object> keywordFrequencies = new ArrayList<>();
        for (String keyword : keywords) {
            int frequency = countKeywordsAbstract(keyword, String.valueOf(abstracts));
            keywordFrequencies.add(keyword + ": " + frequency);
        }

        List<String> resultStrings = new ArrayList<>();
        for (Object obj : keywordFrequencies) {
            resultStrings.add((String) obj);
        }

        SortingAlgorithms.timSort(resultStrings);  // Primero ordenamos por TimSort
        return resultStrings;
    }

    public static int countKeywordsAbstract(String keyword, String abstractDescription) {
        String text = abstractDescription.toLowerCase();
        String word = keyword.toLowerCase();
        int count = 0;
        int index = 0;

        while ((index = text.indexOf(word, index)) != -1) {
            count++;
            index += word.length();
        }

        return count;
    }

    private JTable createFrequencyTable(List<String> frequencyData) {
        String[] columnNames = {"Palabra Clave", "Frecuencia"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        for (String data : frequencyData) {
            String[] rowData = data.split(": ");
            model.addRow(rowData);
        }

        JTable table = new JTable(model);
        return table;
    }

    private JFreeChart createExecutionTimeBarChart(List<String> frequencyData) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String[] sortingAlgorithms = {
                "TimSort", "CombSort", "SelectionSort", "TreeSort", "PigeonholeSort", "BucketSort",
                "QuickSort", "HeapSort", "GnomeSort", "BinaryInsertionSort", "BitonicSort", "RadixSort"
        };

        Map<String, Long> executionTimes = new HashMap<>();

        List<String> testList = new ArrayList<>(frequencyData);
        executionTimes.put("TimSort", SortingAlgorithms.timSort(testList));
        testList = new ArrayList<>(frequencyData);
        executionTimes.put("CombSort", SortingAlgorithms.combSort(testList));
        testList = new ArrayList<>(frequencyData);
        executionTimes.put("SelectionSort", SortingAlgorithms.selectionSort(testList));
        testList = new ArrayList<>(frequencyData);
        executionTimes.put("TreeSort", SortingAlgorithms.treeSort(testList));
        testList = new ArrayList<>(frequencyData);
        executionTimes.put("PigeonholeSort", SortingAlgorithms.pigeonholeSort(testList));
        testList = new ArrayList<>(frequencyData);
        executionTimes.put("BucketSort", SortingAlgorithms.bucketSort(testList));
        testList = new ArrayList<>(frequencyData);
        executionTimes.put("QuickSort", SortingAlgorithms.quickSort(testList));
        testList = new ArrayList<>(frequencyData);
        executionTimes.put("HeapSort", SortingAlgorithms.heapSort(testList));
        testList = new ArrayList<>(frequencyData);
        executionTimes.put("GnomeSort", SortingAlgorithms.gnomeSort(testList));
        testList = new ArrayList<>(frequencyData);
        executionTimes.put("BinaryInsertionSort", SortingAlgorithms.binaryInsertionSort(testList));
        testList = new ArrayList<>(frequencyData);
        executionTimes.put("BitonicSort", SortingAlgorithms.bitonicSort(testList));
        testList = new ArrayList<>(frequencyData);
        executionTimes.put("RadixSort", SortingAlgorithms.radixSort(testList));

        for (String algorithm : sortingAlgorithms) {
            long executionTime = executionTimes.get(algorithm);
            dataset.addValue(executionTime, "Tiempo de Ejecución", algorithm);
        }

        return ChartFactory.createBarChart(
                "Tiempos de Ejecución de Algoritmos de Ordenamiento",
                "Algoritmo",
                "Tiempo (nanosegundos)",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false
        );
    }
}
