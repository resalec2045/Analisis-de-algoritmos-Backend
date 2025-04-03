package co.uniquindio.proyecto.backendalgoritmos.modules.OrderingMethods;
import java.util.*;

public class SortingAlgorithms {

    // 1. TimSort
    public static long timSort(List<String> list) {
        long startTime = System.nanoTime();
        Collections.sort(list);
        return System.nanoTime() - startTime;
    }

    // 2. Comb Sort
    public static long combSort(List<String> list) {
        long startTime = System.nanoTime();
        int gap = list.size();
        double shrink = 1.3;
        boolean swapped = true;
        while (gap > 1 || swapped) {
            gap = (int) (gap / shrink);
            if (gap < 1) {
                gap = 1;
            }
            swapped = false;
            for (int i = 0; i + gap < list.size(); i++) {
                if (list.get(i).compareTo(list.get(i + gap)) > 0) {
                    Collections.swap(list, i, i + gap);
                    swapped = true;
                }
            }
        }
        return System.nanoTime() - startTime;
    }

    // 3. Selection Sort
    public static long selectionSort(List<String> list) {
        long startTime = System.nanoTime();
        for (int i = 0; i < list.size() - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < list.size(); j++) {
                if (list.get(minIndex).compareTo(list.get(j)) > 0) {
                    minIndex = j;
                }
            }
            if (minIndex != i) {
                Collections.swap(list, i, minIndex);
            }
        }
        return System.nanoTime() - startTime;
    }

    // 4. Tree Sort
    public static long treeSort(List<String> list) {
        long startTime = System.nanoTime();
        TreeSet<String> treeSet = new TreeSet<>(list);
        list.clear();
        list.addAll(treeSet);
        return System.nanoTime() - startTime;
    }

    // 5. Pigeonhole Sort (para números enteros)
    public static long pigeonholeSortNumber(int[] arr) {
        long startTime = System.nanoTime();
        if (arr == null || arr.length <= 1) return 0;

        int min = Arrays.stream(arr).min().getAsInt();
        int max = Arrays.stream(arr).max().getAsInt();
        int range = max - min + 1;
        int[] pigeonholes = new int[range];

        for (int value : arr) pigeonholes[value - min]++;
        int index = 0;
        for (int i = 0; i < range; i++) {
            while (pigeonholes[i]-- > 0) {
                arr[index++] = i + min;
            }
        }
        return System.nanoTime() - startTime;
    }
    public static long pigeonholeSort(List<String> list) {
        long startTime = System.nanoTime();
        if (list == null || list.size() <= 1) return 0;

        // Obtener el valor mínimo y máximo lexicográfico
        String min = Collections.min(list);
        String max = Collections.max(list);

        // Calcular el rango de índices basado en los valores lexicográficos de las cadenas
        int range = max.compareTo(min) + 1;
        if (range <= 0) {
            return 0;  // Si el rango no es positivo, no hay nada que ordenar
        }

        List<String>[] pigeonholes = new ArrayList[range];

        // Inicializar los "pigeonholes"
        for (int i = 0; i < range; i++) {
            pigeonholes[i] = new ArrayList<>();
        }

        // Colocar las cadenas en sus "pigeonholes" de acuerdo a su valor lexicográfico
        for (String value : list) {
            // Asegurarse de que el índice no esté fuera de los límites del rango
            int index = Math.min(value.compareTo(min), range - 1);
            pigeonholes[index].add(value);
        }

        int index = 0;
        // Colocar los valores ordenados de vuelta en la lista original
        for (List<String> hole : pigeonholes) {
            for (String value : hole) {
                list.set(index++, value);
            }
        }

        long endTime = System.nanoTime();
        return endTime - startTime;
    }

    // 6. Bucket Sort (para flotantes)
    public static long bucketSortNumber(float[] arr) {
        long startTime = System.nanoTime();
        if (arr == null || arr.length <= 1) return 0;

        int n = arr.length;
        List<Float>[] buckets = new ArrayList[n];

        for (int i = 0; i < n; i++) buckets[i] = new ArrayList<>();

        for (float value : arr) buckets[(int) (n * value)].add(value);

        for (List<Float> bucket : buckets) Collections.sort(bucket);

        int index = 0;
        for (List<Float> bucket : buckets) {
            for (float value : bucket) arr[index++] = value;
        }
        return System.nanoTime() - startTime;
    }
    public static long bucketSort(List<String> list) {
        long startTime = System.nanoTime();
        if (list == null || list.size() <= 1) return 0;

        // Inicializar los cubos
        List<String>[] buckets = new ArrayList[list.size()];

        // Crear los cubos
        for (int i = 0; i < list.size(); i++) {
            buckets[i] = new ArrayList<>();
        }

        // Dividir las cadenas en cubos basados en el primer carácter (o cualquier otro criterio)
        for (String value : list) {
            if (value.isEmpty()) {
                // Si la cadena está vacía, se puede manejar aquí (puedes asignarla a un cubo especial si lo deseas)
                continue; // O, por ejemplo: buckets[buckets.length - 1].add(value);
            }

            int bucketIndex = value.charAt(0) % list.size(); // Asignar según el primer carácter
            buckets[bucketIndex].add(value);
        }

        // Ordenar los cubos individualmente
        for (List<String> bucket : buckets) {
            Collections.sort(bucket);
        }

        int index = 0;
        // Reunir las cadenas ordenadas de vuelta en la lista original
        for (List<String> bucket : buckets) {
            for (String value : bucket) {
                list.set(index++, value);
            }
        }

        long endTime = System.nanoTime();
        return endTime - startTime;
    }

    // 7. QuickSort
    public static long quickSort(List<String> list) {
        long startTime = System.nanoTime();
        quickSort(list, 0, list.size() - 1);
        return System.nanoTime() - startTime;
    }

    private static void quickSort(List<String> list, int low, int high) {
        if (low < high) {
            int pi = partition(list, low, high);
            quickSort(list, low, pi - 1);
            quickSort(list, pi + 1, high);
        }
    }

    private static int partition(List<String> list, int low, int high) {
        String pivot = list.get(high);
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (list.get(j).compareTo(pivot) <= 0) {
                i++;
                Collections.swap(list, i, j);
            }
        }
        Collections.swap(list, i + 1, high);
        return i + 1;
    }

    // 8. HeapSort
    public static long heapSort(List<String> list) {
        long startTime = System.nanoTime();
        int n = list.size();

        for (int i = n / 2 - 1; i >= 0; i--) heapify(list, n, i);

        for (int i = n - 1; i > 0; i--) {
            Collections.swap(list, 0, i);
            heapify(list, i, 0);
        }
        return System.nanoTime() - startTime;
    }

    private static void heapify(List<String> list, int n, int i) {
        int largest = i;
        int l = 2 * i + 1;
        int r = 2 * i + 2;

        if (l < n && list.get(l).compareTo(list.get(largest)) > 0) largest = l;
        if (r < n && list.get(r).compareTo(list.get(largest)) > 0) largest = r;

        if (largest != i) {
            Collections.swap(list, i, largest);
            heapify(list, n, largest);
        }
    }

    // 9. Gnome Sort
    public static long gnomeSort(List<String> list) {
        long startTime = System.nanoTime();

        if (list == null || list.size() <= 1) {
            return System.nanoTime() - startTime; // No hay nada que ordenar
        }

        int index = 1; // Empezamos en 1 porque el primer elemento ya está "ordenado"

        while (index < list.size()) {
            if (index > 0 && list.get(index).compareTo(list.get(index - 1)) < 0) {
                Collections.swap(list, index, index - 1);
                index--; // Retrocede si el elemento está mal ubicado
            } else {
                index++; // Avanza cuando está en orden
            }
        }

        return System.nanoTime() - startTime;
    }

    // 10. Binary Insertion Sort
    public static long binaryInsertionSort(List<String> list) {
        long startTime = System.nanoTime();
        for (int i = 1; i < list.size(); i++) {
            String key = list.get(i);
            int insertedPosition = findInsertionPoint(list, 0, i - 1, key);

            for (int j = i - 1; j >= insertedPosition; j--) {
                list.set(j + 1, list.get(j));
            }
            list.set(insertedPosition, key);
        }
        return System.nanoTime() - startTime;
    }

    private static int findInsertionPoint(List<String> list, int low, int high, String key) {
        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (key.compareTo(list.get(mid)) < 0) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return low;
    }

    // 11. Bitonic Sort
    public static long bitonicSort(List<String> list) {
        long startTime = System.nanoTime();

        if (list == null || list.size() <= 1) {
            return System.nanoTime() - startTime; // No hay nada que ordenar
        }

        bitonicSortHelper(list, 0, list.size(), true);

        return System.nanoTime() - startTime;
    }

    private static void bitonicSortHelper(List<String> list, int low, int count, boolean ascending) {
        if (count > 1) {
            int k = count / 2;

            // Ordenar la primera mitad en orden ascendente
            bitonicSortHelper(list, low, k, true);
            // Ordenar la segunda mitad en orden descendente
            bitonicSortHelper(list, low + k, k, false);
            // Fusionar ambas mitades en orden ascendente o descendente
            bitonicMerge(list, low, count, ascending);
        }
    }

    private static void bitonicMerge(List<String> list, int low, int count, boolean ascending) {
        if (count > 1) {
            int k = count / 2;
            for (int i = low; i < low + k; i++) {
                compareAndSwap(list, i, i + k, ascending);
            }
            bitonicMerge(list, low, k, ascending);
            bitonicMerge(list, low + k, k, ascending);
        }
    }

    private static void compareAndSwap(List<String> list, int i, int j, boolean ascending) {
        if ((list.get(i).compareTo(list.get(j)) > 0) == ascending) {
            Collections.swap(list, i, j);
        }
    }

    // 12. Radix Sort
    public static long radixSort(List<String> list) {
        long startTime = System.nanoTime();
        int maxLen = getMaxLen(list);

        // Ordenar por cada posición de los caracteres
        for (int pos = maxLen - 1; pos >= 0; pos--) {
            countingSort(list, pos);
        }

        return System.nanoTime() - startTime;
    }

    // Obtener la longitud máxima de las cadenas
    private static int getMaxLen(List<String> list) {
        int maxLen = 0;
        for (String str : list) {
            maxLen = Math.max(maxLen, str.length());
        }
        return maxLen;
    }

    // Realizar el counting sort en función de la posición `pos`
    private static void countingSort(List<String> list, int pos) {
        int n = list.size();
        List<String> output = new ArrayList<>(Collections.nCopies(n, null));
        int[] count = new int[256]; // 256 para caracteres ASCII

        // Inicializar el arreglo de conteo
        Arrays.fill(count, 0);

        // Contar las ocurrencias de los caracteres en la posición dada
        for (String key : list) {
            // Usar un valor 0 si no existe un carácter en esa posición
            char ch = (pos < key.length()) ? key.charAt(pos) : 0;
            // Asegurar que el índice esté dentro del rango ASCII
            count[Math.min(ch, 255)]++;
        }

        // Convertir el array `count` para obtener las posiciones finales
        for (int i = 1; i < 256; i++) {
            count[i] += count[i - 1];
        }

        // Colocar los elementos en el array de salida en orden correcto
        for (int i = n - 1; i >= 0; i--) {
            String key = list.get(i);
            char ch = (pos < key.length()) ? key.charAt(pos) : 0;
            output.set(--count[Math.min(ch, 255)], key);
        }

        // Copiar los resultados de vuelta a la lista original
        for (int i = 0; i < n; i++) {
            list.set(i, output.get(i));
        }
    }

    //13. bubble sort
    public static long bubbleSort(List<String> list) {
        long startTime = System.nanoTime();
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = 0; j < list.size() - i - 1; j++) {
                if (list.get(j).compareTo(list.get(j + 1)) > 0) {
                    Collections.swap(list, j, j + 1);
                }
            }
        }
        return System.nanoTime() - startTime;
    }

    //14. cocktail shaker sort
    public static long cocktailShakerSort(List<String> list) {
        long startTime = System.nanoTime();
        boolean swapped = true;
        int start = 0;
        int end = list.size() - 1;

        while (swapped) {
            swapped = false;

            for (int i = start; i < end; i++) {
                if (list.get(i).compareTo(list.get(i + 1)) > 0) {
                    Collections.swap(list, i, i + 1);
                    swapped = true;
                }
            }

            if (!swapped) break;

            swapped = false;
            end--;

            for (int i = end - 1; i >= start; i--) {
                if (list.get(i).compareTo(list.get(i + 1)) > 0) {
                    Collections.swap(list, i, i + 1);
                    swapped = true;
                }
            }
            start++;
        }
        return System.nanoTime() - startTime;
    }

    //15. shell sort
    public static long shellSort(List<String> list) {
        long startTime = System.nanoTime();
        int n = list.size();
        for (int gap = n / 2; gap > 0; gap /= 2) {
            for (int i = gap; i < n; i++) {
                String temp = list.get(i);
                int j;
                for (j = i; j >= gap && list.get(j - gap).compareTo(temp) > 0; j -= gap) {
                    list.set(j, list.get(j - gap));
                }
                list.set(j, temp);
            }
        }
        return System.nanoTime() - startTime;
    }

}
