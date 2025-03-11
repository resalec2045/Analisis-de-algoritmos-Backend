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
    public static long pigeonholeSort(int[] arr) {
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

    // 6. Bucket Sort (para flotantes)
    public static long bucketSort(float[] arr) {
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
}
