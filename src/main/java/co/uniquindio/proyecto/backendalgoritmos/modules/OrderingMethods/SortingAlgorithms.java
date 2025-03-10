package co.uniquindio.proyecto.backendalgoritmos.modules.OrderingMethods;
import java.util.*;

public class SortingAlgorithms {

    // 1. TimSort (modified merge sort, used by Java Collections.sort())
    public static void timSort(List<List<String>> list) {
        Collections.sort(list, (a, b) -> {
            for (int i = 0; i < Math.min(a.size(), b.size()); i++) {
                int cmp = a.get(i).compareTo(b.get(i));
                if (cmp != 0) {
                    return cmp;
                }
            }
            return Integer.compare(a.size(), b.size());
        });
    }

    // 2. Comb Sort
    public static void combSort(List<List<String>> list) {
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
    }

    // 3. Selection Sort
    public static void selectionSort(List<List<String>> list) {
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
    }

    // 4. Tree Sort (requires a custom Tree structure)
    public static void treeSort(List<List<String>> list) {
        TreeSet<List<String>> treeSet = new TreeSet<>(SortingAlgorithms::compareLists);
        treeSet.addAll(list);
        list.clear();
        list.addAll(treeSet);
    }

    // 5. Pigeonhole Sort ES PARA NUMEROS
    // Use Bucket Sort instead

    public static void pigeonholeSort(int[] arr) {
        if (arr == null || arr.length <= 1) {
            return; // No es necesario ordenar si el arreglo está vacío o tiene un solo elemento
        }

        // 1. Encontrar el valor mínimo y máximo en el arreglo
        int min = arr[0];
        int max = arr[0];
        for (int value : arr) {
            if (value < min) {
                min = value;
            }
            if (value > max) {
                max = value;
            }
        }

        // 2. Crear el "pigeonhole" array
        int range = max - min + 1;
        int[] pigeonholes = new int[range];

        // 3. Colocar cada elemento en su "pigeonhole"
        for (int value : arr) {
            pigeonholes[value - min]++;
        }

        // 4. Recolectar los elementos de los "pigeonholes" en orden
        int index = 0;
        for (int i = 0; i < range; i++) {
            while (pigeonholes[i]-- > 0) {
                arr[index++] = i + min;
            }
        }
    }

    // para floats
    public static void bucketSort(float[] arr) {
        if (arr == null || arr.length <= 1) {
            return; // No es necesario ordenar si el arreglo está vacío o tiene un solo elemento
        }

        int n = arr.length;
        List<Float>[] buckets = new ArrayList[n];

        // 1. Crear buckets vacíos
        for (int i = 0; i < n; i++) {
            buckets[i] = new ArrayList<>();
        }

        // 2. Colocar elementos en los buckets
        for (float value : arr) {
            int bucketIndex = (int) (n * value); // Asumiendo que los valores están en el rango [0, 1)
            buckets[bucketIndex].add(value);
        }

        // 3. Ordenar elementos dentro de cada bucket
        for (List<Float> bucket : buckets) {
            Collections.sort(bucket);
        }

        // 4. Concatenar los buckets ordenados
        int index = 0;
        for (List<Float> bucket : buckets) {
            for (float value : bucket) {
                arr[index++] = value;
            }
        }
    }

    // 7. QuickSort
    public static void quickSort(List<List<String>> list) {
        quickSort(list, 0, list.size() - 1);
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
        int i = (low - 1);
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
    public static void heapSort(List<List<String>> list) {
        int n = list.size();

        // Build heap (rearrange array)
        for (int i = n / 2 - 1; i >= 0; i--)
            heapify(list, n, i);

        // One by one extract an element from heap
        for (int i = n - 1; i > 0; i--) {
            // Move current root to end
            Collections.swap(list, 0, i);

            // call max heapify on the reduced heap
            heapify(list, i, 0);
        }
    }

    // To heapify a subtree rooted with node i which is
    // an index in arr. n is size of heap
    private static void heapify(List<List<String>> list, int n, int i) {
        int largest = i; // Initialize largest as root
        int l = 2 * i + 1; // left = 2*i + 1
        int r = 2 * i + 2; // right = 2*i + 2

        // If left child is larger than root
        if (l < n && compareLists(list.get(l), list.get(largest)) > 0)
            largest = l;

        // If right child is larger than largest so far
        if (r < n && compareLists(list.get(r), list.get(largest)) > 0)
            largest = r;

        // If largest is not root
        if (largest != i) {
            Collections.swap(list, i, largest);

            // Recursively heapify the affected sub-tree
            heapify(list, n, largest);
        }
    }

    // 9. Bitonic Sort (efficient for hardware implementation, not ideal for Java lists)

    public static void bitonicSort(int[] arr) {
        bitonicSort(arr, 0, arr.length, true);
    }

    private static void bitonicSort(int[] arr, int low, int cnt, boolean dir) {
        if (cnt > 1) {
            int k = cnt / 2;
            bitonicSort(arr, low, k, true);
            bitonicSort(arr, low + k, k, false);
            bitonicMerge(arr, low, cnt, dir);
        }
    }

    private static void bitonicMerge(int[] arr, int low, int cnt, boolean dir) {
        if (cnt > 1) {
            int k = cnt / 2;
            for (int i = low; i < low + k; i++) {
                compareAndSwap(arr, i, i + k, dir);
            }
            bitonicMerge(arr, low, k, dir);
            bitonicMerge(arr, low + k, k, dir);
        }
    }

    private static void compareAndSwap(int[] arr, int i, int j, boolean dir) {
        if ((arr[i] > arr[j] && dir) || (arr[i] < arr[j] && !dir)) {
            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
    }

    public static void main(String[] args) {
        int[] arr = {3, 7, 4, 8, 6, 2, 1, 5};
        bitonicSort(arr);
        System.out.println(Arrays.toString(arr));
    }

    // Use Merge Sort or QuickSort instead

    // 10. Gnome Sort
    public static void gnomeSort(List<List<String>> list) {
        int index = 0;
        while (index < list.size()) {
            if (index == 0) {
                index++;
            }
            if (compareLists(list.get(index), list.get(index - 1)) >= 0) {
                index++;
            } else {
                Collections.swap(list, index, index - 1);
                index--;
            }
        }
    }

    // 11. Binary Insertion Sort
    public static void binaryInsertionSort(List<List<String>> list) {
        for (int i = 1; i < list.size(); i++) {
            List<String> key = list.get(i);
            int insertedPosition = findInsertionPoint(list, 0, i - 1, key);
            // Shifting elements to the right to make space for the key
            for (int j = i - 1; j >= insertedPosition; j--) {
                list.set(j + 1, list.get(j));
            }
            list.set(insertedPosition, key);
        }
    }

    // Helper function for Binary Insertion Sort to find the insertion point efficiently
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


    // 12. Radix Sort (not suitable for lists of strings)
    // Use Bucket Sort instead

    // Helper function to compare two lists of strings lexicographically
    private static int compareLists(List<String> a, List<String> b) {
        for (int i = 0; i < Math.min(a.size(), b.size()); i++) {
            int cmp = a.get(i).compareTo(b.get(i));
            if (cmp != 0) {
                return cmp;
            }
        }
        return Integer.compare(a.size(), b.size());
    }

    public static void radixSort(int[] arr) {
        int max = Arrays.stream(arr).max().orElse(0);
        for (int exp = 1; max / exp > 0; exp *= 10) {
            countingSort(arr, exp);
        }
    }

    private static void countingSort(int[] arr, int exp) {
        int n = arr.length;
        int[] output = new int[n];
        int[] count = new int[10];
        Arrays.fill(count, 0);

        for (int value : arr) {
            count[(value / exp) % 10]++;
        }

        for (int i = 1; i < 10; i++) {
            count[i] += count[i - 1];
        }

        for (int i = n - 1; i >= 0; i--) {
            output[count[(arr[i] / exp) % 10] - 1] = arr[i];
            count[(arr[i] / exp) % 10]--;
        }

        System.arraycopy(output, 0, arr, 0, n);
    }
}