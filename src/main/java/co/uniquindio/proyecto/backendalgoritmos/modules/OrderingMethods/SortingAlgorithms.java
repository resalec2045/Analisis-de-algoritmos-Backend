package co.uniquindio.proyecto.backendalgoritmos.modules.OrderingMethods;
import java.util.*;

public class SortingAlgorithms {

    // 1. TimSort
    public static long timSort(List<List<String>> list) {
        long startTime = System.nanoTime();
        Collections.sort(list, (a, b) -> {
            for (int i = 0; i < Math.min(a.size(), b.size()); i++) {
                int cmp = a.get(i).compareTo(b.get(i));
                if (cmp != 0) {
                    return cmp;
                }
            }
            return Integer.compare(a.size(), b.size());
        });
        return System.nanoTime() - startTime;
    }

    // 2. Comb Sort
    public static long combSort(List<List<String>> list) {
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
                if (compareLists(list.get(i), list.get(i + gap)) > 0) {
                    Collections.swap(list, i, i + gap);
                    swapped = true;
                }
            }
        }
        return System.nanoTime() - startTime;
    }

    // 3. Selection Sort
    public static long selectionSort(List<List<String>> list) {
        long startTime = System.nanoTime();
        for (int i = 0; i < list.size() - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < list.size(); j++) {
                if (compareLists(list.get(minIndex), list.get(j)) > 0) {
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
    public static long treeSort(List<List<String>> list) {
        long startTime = System.nanoTime();
        TreeSet<List<String>> treeSet = new TreeSet<>(SortingAlgorithms::compareLists);
        treeSet.addAll(list);
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
    public static long pigeonholeSort(List<List<String>> list) {
        long startTime = System.nanoTime();
        if (list == null || list.isEmpty()) return 0;

        for (List<String> sublist : list) {
            if (sublist == null || sublist.isEmpty()) continue;

            // Obtener el mínimo y máximo lexicográfico
            String min = Collections.min(sublist);
            String max = Collections.max(sublist);

            int range = max.compareTo(min) + 1;
            TreeMap<String, Integer> pigeonholes = new TreeMap<>();

            for (String value : sublist) {
                pigeonholes.put(value, pigeonholes.getOrDefault(value, 0) + 1);
            }

            sublist.clear();
            for (Map.Entry<String, Integer> entry : pigeonholes.entrySet()) {
                for (int i = 0; i < entry.getValue(); i++) {
                    sublist.add(entry.getKey());
                }
            }
        }
        return System.nanoTime() - startTime;
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
    public static long bucketSort(List<List<String>> list) {
        long startTime = System.nanoTime();
        if (list == null || list.isEmpty()) return 0;

        for (List<String> sublist : list) {
            if (sublist == null || sublist.isEmpty()) continue;

            int n = sublist.size();
            List<List<String>> buckets = new ArrayList<>(n);
            for (int i = 0; i < n; i++) buckets.add(new ArrayList<>());

            String min = Collections.min(sublist);
            String max = Collections.max(sublist);

            for (String value : sublist) {
                int bucketIndex = (int) ((double) (value.compareTo(min)) / (max.compareTo(min) + 1) * n);
                buckets.get(bucketIndex).add(value);
            }

            for (List<String> bucket : buckets) Collections.sort(bucket);

            sublist.clear();
            for (List<String> bucket : buckets) sublist.addAll(bucket);
        }
        return System.nanoTime() - startTime;
    }

    // 7. QuickSort
    public static long quickSort(List<List<String>> list) {
        long startTime = System.nanoTime();
        quickSort(list, 0, list.size() - 1);
        return System.nanoTime() - startTime;
    }

    private static void quickSort(List<List<String>> list, int low, int high) {
        if (low < high) {
            int pi = partition(list, low, high);
            quickSort(list, low, pi - 1);
            quickSort(list, pi + 1, high);
        }
    }

    private static int partition(List<List<String>> list, int low, int high) {
        List<String> pivot = list.get(high);
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (compareLists(list.get(j), pivot) <= 0) {
                i++;
                Collections.swap(list, i, j);
            }
        }
        Collections.swap(list, i + 1, high);
        return i + 1;
    }

    // 8. HeapSort
    public static long heapSort(List<List<String>> list) {
        long startTime = System.nanoTime();
        int n = list.size();

        for (int i = n / 2 - 1; i >= 0; i--) heapify(list, n, i);

        for (int i = n - 1; i > 0; i--) {
            Collections.swap(list, 0, i);
            heapify(list, i, 0);
        }
        return System.nanoTime() - startTime;
    }

    private static void heapify(List<List<String>> list, int n, int i) {
        int largest = i;
        int l = 2 * i + 1;
        int r = 2 * i + 2;

        if (l < n && compareLists(list.get(l), list.get(largest)) > 0) largest = l;
        if (r < n && compareLists(list.get(r), list.get(largest)) > 0) largest = r;

        if (largest != i) {
            Collections.swap(list, i, largest);
            heapify(list, n, largest);
        }
    }

    // 9. Gnome Sort
    public static long gnomeSort(List<List<String>> list) {
        long startTime = System.nanoTime();

        if (list == null || list.size() <= 1) {
            return System.nanoTime() - startTime; // No hay nada que ordenar
        }

        int index = 1; // Empezamos en 1 porque el primer elemento ya está "ordenado"

        while (index < list.size()) {
            if (index > 0 && compareLists(list.get(index), list.get(index - 1)) < 0) {
                Collections.swap(list, index, index - 1);
                index--; // Retrocede si el elemento está mal ubicado
            } else {
                index++; // Avanza cuando está en orden
            }
        }

        return System.nanoTime() - startTime;
    }

    // 10. Binary Insertion Sort
    public static long binaryInsertionSort(List<List<String>> list) {
        long startTime = System.nanoTime();
        for (int i = 1; i < list.size(); i++) {
            List<String> key = list.get(i);
            int insertedPosition = findInsertionPoint(list, 0, i - 1, key);

            for (int j = i - 1; j >= insertedPosition; j--) {
                list.set(j + 1, list.get(j));
            }
            list.set(insertedPosition, key);
        }
        return System.nanoTime() - startTime;
    }

    private static int findInsertionPoint(List<List<String>> list, int low, int high, List<String> key) {
        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (compareLists(key, list.get(mid)) < 0) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return low;
    }

    private static int compareLists(List<String> a, List<String> b) {
        for (int i = 0; i < Math.min(a.size(), b.size()); i++) {
            int cmp = a.get(i).compareTo(b.get(i));
            if (cmp != 0) return cmp;
        }
        return Integer.compare(a.size(), b.size());
    }

    // 11. Bitonic Sort (Para listas de listas de Strings)
    public static long bitonicSort(List<List<String>> list) {
        long startTime = System.nanoTime();

        if (list == null || list.size() <= 1) {
            return System.nanoTime() - startTime; // No hay nada que ordenar
        }

        bitonicSortHelper(list, 0, list.size(), true);

        return System.nanoTime() - startTime;
    }
    private static void bitonicSortHelper(List<List<String>> list, int low, int count, boolean ascending) {
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
    private static void bitonicMerge(List<List<String>> list, int low, int count, boolean ascending) {
        if (count > 1) {
            int k = count / 2;
            for (int i = low; i < low + k; i++) {
                compareAndSwap(list, i, i + k, ascending);
            }
            bitonicMerge(list, low, k, ascending);
            bitonicMerge(list, low + k, k, ascending);
        }
    }
    private static void compareAndSwap(List<List<String>> list, int i, int j, boolean ascending) {
        if ((compareLists(list.get(i), list.get(j)) > 0) == ascending) {
            Collections.swap(list, i, j);
        }
    }

    // 12. Radix Sort
    public static long radixSort(List<List<String>> list) {
        long startTime = System.nanoTime();
        int maxLen = getMaxLen(list);

        for (int pos = maxLen - 1; pos >= 0; pos--) {
            countingSort(list, pos);
        }

        return System.nanoTime() - startTime;
    }

    private static int getMaxLen(List<List<String>> list) {
        int maxLen = 0;
        for (List<String> sublist : list) {
            int length = String.join("", sublist).length();
            maxLen = Math.max(maxLen, length);
        }
        return maxLen;
    }

    private static void countingSort(List<List<String>> list, int pos) {
        int n = list.size();
        List<List<String>> output = new ArrayList<>(Collections.nCopies(n, null));
        int[] count = new int[256]; // Para caracteres ASCII

        // Inicializar el arreglo de conteo
        Arrays.fill(count, 0);

        // Contar la ocurrencia de cada carácter en la posición dada
        for (List<String> sublist : list) {
            String key = String.join("", sublist);
            char ch = (pos < key.length()) ? key.charAt(pos) : 0;
            count[ch]++;
        }

        // Transformar count para obtener las posiciones finales
        for (int i = 1; i < 256; i++) {
            count[i] += count[i - 1];
        }

        // Construir la lista ordenada
        for (int i = n - 1; i >= 0; i--) {
            String key = String.join("", list.get(i));
            char ch = (pos < key.length()) ? key.charAt(pos) : 0;
            output.set(--count[ch], list.get(i));
        }

        // Copiar los resultados de vuelta a la lista original
        for (int i = 0; i < n; i++) {
            list.set(i, output.get(i));
        }
    }

}
