package co.uniquindio.proyecto.backendalgoritmos.modules.OrderingMethods;
import java.util.*;

public class SortingAlgorithms {

    // 1. TimSort
    public static long timSort(List<List<String>> list) {
        // Guarda el tiempo de inicio en nanosegundos.
        long startTime = System.nanoTime();
        // Ordena la lista usando Collections.sort y un comparador personalizado.
        Collections.sort(list, (a, b) -> {
            // Itera a través de los elementos de las sublistas hasta la longitud mínima.
            for (int i = 0; i < Math.min(a.size(), b.size()); i++) {
                // Compara los elementos en la posición i de las sublistas.
                int cmp = a.get(i).compareTo(b.get(i));
                // Si los elementos son diferentes, devuelve el resultado de la comparación.
                if (cmp != 0) {
                    return cmp;
                }
            }
            // Si todas las comparaciones son iguales, compara la longitud de las sublistas.
            return Integer.compare(a.size(), b.size());
        });
        // Devuelve el tiempo transcurrido en nanosegundos.
        return System.nanoTime() - startTime;
    }

    // 2. Comb Sort
    public static long combSort(List<List<String>> list) {
        // Guarda el tiempo de inicio en nanosegundos.
        long startTime = System.nanoTime();
        // Inicializa el gap con el tamaño de la lista.
        int gap = list.size();
        // Inicializa el factor de reducción del gap.
        double shrink = 1.3;
        // Inicializa la bandera de intercambio.
        boolean swapped = true;
        // Continúa mientras el gap sea mayor que 1 o haya habido intercambios.
        while (gap > 1 || swapped) {
            // Reduce el gap según el factor de reducción.
            gap = (int) (gap / shrink);
            // Si el gap es menor que 1, lo establece a 1.
            if (gap < 1) {
                gap = 1;
            }
            // Restablece la bandera de intercambio.
            swapped = false;
            // Itera a través de la lista con el gap.
            for (int i = 0; i + gap < list.size(); i++) {
                // Compara las sublistas en la posición i e i + gap.
                if (compareLists(list.get(i), list.get(i + gap)) > 0) {
                    // Intercambia las sublistas si están fuera de orden.
                    Collections.swap(list, i, i + gap);
                    // Establece la bandera de intercambio a verdadero.
                    swapped = true;
                }
            }
        }
        // Devuelve el tiempo transcurrido en nanosegundos.
        return System.nanoTime() - startTime;
    }

    // 3. Selection Sort
    public static long selectionSort(List<List<String>> list) {
        // Guarda el tiempo de inicio en nanosegundos.
        long startTime = System.nanoTime();
        // Itera a través de la lista.
        for (int i = 0; i < list.size() - 1; i++) {
            // Inicializa el índice mínimo con la posición actual.
            int minIndex = i;
            // Itera a través de la sublista restante.
            for (int j = i + 1; j < list.size(); j++) {
                // Compara las sublistas en la posición minIndex y j.
                if (compareLists(list.get(minIndex), list.get(j)) > 0) {
                    // Actualiza el índice mínimo si se encuentra una sublista menor.
                    minIndex = j;
                }
            }
            // Intercambia las sublistas si el índice mínimo ha cambiado.
            if (minIndex != i) {
                Collections.swap(list, i, minIndex);
            }
        }
        // Devuelve el tiempo transcurrido en nanosegundos.
        return System.nanoTime() - startTime;
    }

    // 4. Tree Sort
    public static long treeSort(List<List<String>> list) {
        // Guarda el tiempo de inicio en nanosegundos.
        long startTime = System.nanoTime();
        // Crea un TreeSet con un comparador personalizado.
        TreeSet<List<String>> treeSet = new TreeSet<>(SortingAlgorithms::compareLists);
        // Agrega todos los elementos de la lista al TreeSet.
        treeSet.addAll(list);
        // Limpia la lista original.
        list.clear();
        // Agrega todos los elementos ordenados del TreeSet a la lista.
        list.addAll(treeSet);
        // Devuelve el tiempo transcurrido en nanosegundos.
        return System.nanoTime() - startTime;
    }

    // 5. Pigeonhole Sort (para números enteros)
    public static long pigeonholeSortNumber(int[] arr) {
        // Guarda el tiempo de inicio en nanosegundos.
        long startTime = System.nanoTime();
        // Verifica si el array es nulo o tiene menos de 2 elementos.
        if (arr == null || arr.length <= 1) return 0;

        // Encuentra el valor mínimo en el array.
        int min = Arrays.stream(arr).min().getAsInt();
        // Encuentra el valor máximo en el array.
        int max = Arrays.stream(arr).max().getAsInt();
        // Calcula el rango de valores.
        int range = max - min + 1;
        // Crea un array de pigeonholes.
        int[] pigeonholes = new int[range];

        // Cuenta la ocurrencia de cada valor en el array.
        for (int value : arr) pigeonholes[value - min]++;
        // Inicializa el índice para el array ordenado.
        int index = 0;
        // Itera a través de los pigeonholes y reconstruye el array ordenado.
        for (int i = 0; i < range; i++) {
            while (pigeonholes[i]-- > 0) {
                arr[index++] = i + min;
            }
        }
        // Devuelve el tiempo transcurrido en nanosegundos.
        return System.nanoTime() - startTime;
    }
    public static long pigeonholeSort(List<List<String>> list) {
        // Guarda el tiempo de inicio en nanosegundos.
        long startTime = System.nanoTime();
        // Verifica si la lista es nula o vacía.
        if (list == null || list.isEmpty()) return 0;

        // Itera a través de cada sublista.
        for (List<String> sublist : list) {
            // Verifica si la sublista es nula o vacía.
            if (sublist == null || sublist.isEmpty()) continue;

            // Obtiene el mínimo lexicográfico.
            String min = Collections.min(sublist);
            // Obtiene el máximo lexicográfico.
            String max = Collections.max(sublist);

            // Calcula el rango de valores.
            int range = max.compareTo(min) + 1;
            // Crea un TreeMap para almacenar los pigeonholes.
            TreeMap<String, Integer> pigeonholes = new TreeMap<>();

            // Cuenta la ocurrencia de cada valor en la sublista.
            for (String value : sublist) {
                pigeonholes.put(value, pigeonholes.getOrDefault(value, 0) + 1);
            }

            // Limpia la sublista.
            sublist.clear();
            // Reconstruye la sublista ordenada.
            for (Map.Entry<String, Integer> entry : pigeonholes.entrySet()) {
                for (int i = 0; i < entry.getValue(); i++) {
                    sublist.add(entry.getKey());
                }
            }
        }
        // Devuelve el tiempo transcurrido en nanosegundos.
        return System.nanoTime() - startTime;
    }

    // 6. Bucket Sort (para flotantes)
    public static long bucketSortNumber(float[] arr) {
        // Guarda el tiempo de inicio en nanosegundos.
        long startTime = System.nanoTime();
        // Verifica si el array es nulo o tiene menos de 2 elementos.
        if (arr == null || arr.length <= 1) return 0;

        // Obtiene el tamaño del array.
        int n = arr.length;
        // Crea un array de buckets.
        List<Float>[] buckets = new ArrayList[n];

        // Inicializa cada bucket.
        for (int i = 0; i < n; i++) buckets[i] = new ArrayList<>();

        // Distribuye los valores en los buckets.
        for (float value : arr) buckets[(int) (n * value)].add(value);

        // Ordena cada bucket.
        for (List<Float> bucket : buckets) Collections.sort(bucket);

        // Reconstruye el array ordenado.
        int index = 0;
        for (List<Float> bucket : buckets) {
            for (float value : bucket) arr[index++] = value;
        }
        // Devuelve el tiempo transcurrido en nanosegundos.
        return System.nanoTime() - startTime;
    }
    public static long bucketSort(List<List<String>> list) {
        // Guarda el tiempo de inicio en nanosegundos.
        long startTime = System.nanoTime();
        // Verifica si la lista es nula o vacía.
        if (list == null || list.isEmpty()) return 0;

        // Itera a través de cada sublista.
        for (List<String> sublist : list) {
            // Verifica si la sublista es nula o vacía.
            if (sublist == null || sublist.isEmpty()) continue;

            // Obtiene el tamaño de la sublista.
            int n = sublist.size();
            // Crea una lista de buckets.
            List<List<String>> buckets = new ArrayList<>(n);
            // Inicializa cada bucket.
            for (int i = 0; i < n; i++) buckets.add(new ArrayList<>());

            // Obtiene el mínimo lexicográfico.
            String min = Collections.min(sublist);
            // Obtiene el máximo lexicográfico.
            String max = Collections.max(sublist);

            // Distribuye los valores en los buckets.
            for (String value : sublist) {
                // Calcula el índice del bucket.
                int bucketIndex = (int) ((double) (value.compareTo(min)) / (max.compareTo(min) + 1) * n);
                // Agrega el valor al bucket.
                buckets.get(bucketIndex).add(value);
            }

            // Ordena cada bucket.
            for (List<String> bucket : buckets) Collections.sort(bucket);

            // Limpia la sublista.
            sublist.clear();
            // Reconstruye la sublista ordenada.
            for (List<String> bucket : buckets) sublist.addAll(bucket);
        }
        // Devuelve el tiempo transcurrido en nanosegundos.
        return System.nanoTime() - startTime;
    }

    // 7. QuickSort
    public static long quickSort(List<List<String>> list) {
        // Guarda el tiempo de inicio en nanosegundos.
        long startTime = System.nanoTime();
        // Llama a la función quickSort recursiva.
        quickSort(list, 0, list.size() - 1);
        // Devuelve el tiempo transcurrido en nanosegundos.
        return System.nanoTime() - startTime;
    }

    private static void quickSort(List<List<String>> list, int low, int high) {
        // Verifica si la sublista tiene más de un elemento.
        if (low < high) {
            // Divide la sublista en dos partes.
            int pi = partition(list, low, high);
            // Ordena la primera parte recursivamente.
            quickSort(list, low, pi - 1);
            // Ordena la segunda parte recursivamente.
            quickSort(list, pi + 1, high);
        }
    }

    private static int partition(List<List<String>> list, int low, int high) {
        // Selecciona el último elemento como pivote.
        List<String> pivot = list.get(high);
        // Inicializa el índice del elemento menor.
        int i = low - 1;
        // Itera a través de la sublista.
        for (int j = low; j < high; j++) {
            // Compara el elemento actual con el pivote.
            if (compareLists(list.get(j), pivot) <= 0) {
                // Incrementa el índice del elemento menor.
                i++;
                // Intercambia el elemento actual con el elemento menor.
                Collections.swap(list, i, j);
            }
        }
        // Intercambia el pivote con el elemento menor.
        Collections.swap(list, i + 1, high);
        // Devuelve el índice del pivote.
        return i + 1;
    }
    // 8. HeapSort
    public static long heapSort(List<List<String>> list) {
        // Guarda el tiempo de inicio en nanosegundos.
        long startTime = System.nanoTime();
        // Obtiene el tamaño de la lista.
        int n = list.size();

        // Construye un heap máximo (max heap).
        for (int i = n / 2 - 1; i >= 0; i--) heapify(list, n, i);

        // Extrae elementos del heap uno por uno.
        for (int i = n - 1; i > 0; i--) {
            // Mueve el heap raíz (elemento máximo) al final.
            Collections.swap(list, 0, i);
            // Llama a heapify en el heap reducido.
            heapify(list, i, 0);
        }
        // Devuelve el tiempo transcurrido en nanosegundos.
        return System.nanoTime() - startTime;
    }

    private static void heapify(List<List<String>> list, int n, int i) {
        // Inicializa el mayor como la raíz.
        int largest = i;
        // Calcula el hijo izquierdo.
        int l = 2 * i + 1;
        // Calcula el hijo derecho.
        int r = 2 * i + 2;

        // Si el hijo izquierdo es mayor que la raíz.
        if (l < n && compareLists(list.get(l), list.get(largest)) > 0) largest = l;
        // Si el hijo derecho es mayor que el mayor hasta ahora.
        if (r < n && compareLists(list.get(r), list.get(largest)) > 0) largest = r;

        // Si el mayor no es la raíz.
        if (largest != i) {
            // Intercambia la raíz con el elemento mayor.
            Collections.swap(list, i, largest);
            // Llama recursivamente a heapify en el subárbol afectado.
            heapify(list, n, largest);
        }
    }

    // 9. Gnome Sort
    public static long gnomeSort(List<List<String>> list) {
        // Guarda el tiempo de inicio en nanosegundos.
        long startTime = System.nanoTime();

        // Verifica si la lista es nula o tiene menos de 2 elementos.
        if (list == null || list.size() <= 1) {
            return System.nanoTime() - startTime; // No hay nada que ordenar
        }

        // Inicializa el índice en 1.
        int index = 1; // Empezamos en 1 porque el primer elemento ya está "ordenado"

        // Itera a través de la lista.
        while (index < list.size()) {
            // Si el elemento actual es menor que el anterior.
            if (index > 0 && compareLists(list.get(index), list.get(index - 1)) < 0) {
                // Intercambia los elementos.
                Collections.swap(list, index, index - 1);
                // Retrocede el índice.
                index--; // Retrocede si el elemento está mal ubicado
            } else {
                // Avanza el índice.
                index++; // Avanza cuando está en orden
            }
        }

        // Devuelve el tiempo transcurrido en nanosegundos.
        return System.nanoTime() - startTime;
    }

    // 10. Binary Insertion Sort
    public static long binaryInsertionSort(List<List<String>> list) {
        // Guarda el tiempo de inicio en nanosegundos.
        long startTime = System.nanoTime();
        // Itera a través de la lista desde el segundo elemento.
        for (int i = 1; i < list.size(); i++) {
            // Almacena el elemento actual.
            List<String> key = list.get(i);
            // Encuentra la posición de inserción usando búsqueda binaria.
            int insertedPosition = findInsertionPoint(list, 0, i - 1, key);

            // Mueve los elementos mayores una posición hacia adelante.
            for (int j = i - 1; j >= insertedPosition; j--) {
                list.set(j + 1, list.get(j));
            }
            // Inserta el elemento en la posición correcta.
            list.set(insertedPosition, key);
        }
        // Devuelve el tiempo transcurrido en nanosegundos.
        return System.nanoTime() - startTime;
    }

    private static int findInsertionPoint(List<List<String>> list, int low, int high, List<String> key) {
        // Realiza búsqueda binaria para encontrar la posición de inserción.
        while (low <= high) {
            int mid = low + (high - low) / 2;
            // Compara el elemento actual con el elemento medio.
            if (compareLists(key, list.get(mid)) < 0) {
                // Actualiza el límite superior.
                high = mid - 1;
            } else {
                // Actualiza el límite inferior.
                low = mid + 1;
            }
        }
        // Devuelve la posición de inserción.
        return low;
    }

    // 11. Bitonic Sort (Para listas de listas de Strings)
    public static long bitonicSort(List<List<String>> list) {
        // Guarda el tiempo de inicio en nanosegundos.
        long startTime = System.nanoTime();

        // Verifica si la lista es nula o tiene menos de 2 elementos.
        if (list == null || list.size() <= 1) {
            return System.nanoTime() - startTime; // No hay nada que ordenar
        }

        // Llama a la función auxiliar bitonicSortHelper.
        bitonicSortHelper(list, 0, list.size(), true);

        // Devuelve el tiempo transcurrido en nanosegundos.
        return System.nanoTime() - startTime;
    }
    private static void bitonicSortHelper(List<List<String>> list, int low, int count, boolean ascending) {
        // Verifica si el tamaño de la sublista es mayor que 1.
        if (count > 1) {
            // Divide la sublista en dos partes iguales.
            int k = count / 2;

            // Ordena la primera mitad en orden ascendente.
            bitonicSortHelper(list, low, k, true);
            // Ordena la segunda mitad en orden descendente.
            bitonicSortHelper(list, low + k, k, false);
            // Combina las dos mitades en orden ascendente o descendente.
            bitonicMerge(list, low, count, ascending);
        }
    }
    private static void bitonicMerge(List<List<String>> list, int low, int count, boolean ascending) {
        // Verifica si el tamaño de la sublista es mayor que 1.
        if (count > 1) {
            // Divide la sublista en dos partes iguales.
            int k = count / 2;
            // Itera a través de la primera mitad de la sublista.
            for (int i = low; i < low + k; i++) {
                // Compara e intercambia elementos si es necesario.
                compareAndSwap(list, i, i + k, ascending);
            }
            // Llama recursivamente a bitonicMerge en las dos mitades.
            bitonicMerge(list, low, k, ascending);
            bitonicMerge(list, low + k, k, ascending);
        }
    }
    private static void compareAndSwap(List<List<String>> list, int i, int j, boolean ascending) {
        // Compara e intercambia los elementos si es necesario.
        if ((compareLists(list.get(i), list.get(j)) > 0) == ascending) {
            Collections.swap(list, i, j);
        }
    }
    // 12. Radix Sort
    public static long radixSort(List<List<String>> list) {
        // Guarda el tiempo de inicio en nanosegundos.
        long startTime = System.nanoTime();
        // Encuentra la longitud máxima de las sublistas.
        int maxLen = getMaxLen(list);

        // Itera a través de las posiciones de los caracteres de derecha a izquierda.
        for (int pos = maxLen - 1; pos >= 0; pos--) {
            // Ordena la lista usando counting sort en la posición actual.
            countingSort(list, pos);
        }

        // Devuelve el tiempo transcurrido en nanosegundos.
        return System.nanoTime() - startTime;
    }

    private static int getMaxLen(List<List<String>> list) {
        // Inicializa la longitud máxima en 0.
        int maxLen = 0;
        // Itera a través de las sublistas.
        for (List<String> sublist : list) {
            // Calcula la longitud de la sublista concatenada.
            int length = String.join("", sublist).length();
            // Actualiza la longitud máxima si es necesario.
            maxLen = Math.max(maxLen, length);
        }
        // Devuelve la longitud máxima.
        return maxLen;
    }

    private static void countingSort(List<List<String>> list, int pos) {
        // Obtiene el tamaño de la lista.
        int n = list.size();
        // Crea una lista de salida.
        List<List<String>> output = new ArrayList<>(Collections.nCopies(n, null));
        // Crea un array de conteo para caracteres ASCII.
        int[] count = new int[256]; // Para caracteres ASCII

        // Inicializa el array de conteo en 0.
        Arrays.fill(count, 0);

        // Cuenta la ocurrencia de cada carácter en la posición dada.
        for (List<String> sublist : list) {
            // Concatena la sublista en una cadena.
            String key = String.join("", sublist);
            // Obtiene el carácter en la posición dada.
            char ch = (pos < key.length()) ? key.charAt(pos) : 0;
            // Incrementa el conteo del carácter.
            count[ch]++;
        }

        // Transforma el array de conteo para obtener las posiciones finales.
        for (int i = 1; i < 256; i++) {
            count[i] += count[i - 1];
        }

        // Construye la lista ordenada.
        for (int i = n - 1; i >= 0; i--) {
            // Concatena la sublista en una cadena.
            String key = String.join("", list.get(i));
            // Obtiene el carácter en la posición dada.
            char ch = (pos < key.length()) ? key.charAt(pos) : 0;
            // Coloca la sublista en la posición correcta en la lista de salida.
            output.set(--count[ch], list.get(i));
        }

        // Copia los resultados de vuelta a la lista original.
        for (int i = 0; i < n; i++) {
            list.set(i, output.get(i));
        }
    }

    // Método auxiliar para comparar dos sublistas de Strings.
    private static int compareLists(List<String> a, List<String> b) {
        // Itera a través de las sublistas hasta la longitud mínima.
        for (int i = 0; i < Math.min(a.size(), b.size()); i++) {
            // Compara los elementos en la posición actual.
            int cmp = a.get(i).compareTo(b.get(i));
            // Si los elementos son diferentes, devuelve el resultado de la comparación.
            if (cmp != 0) return cmp;
        }
        // Si todos los elementos son iguales, compara la longitud de las sublistas.
        return Integer.compare(a.size(), b.size());
    }
}