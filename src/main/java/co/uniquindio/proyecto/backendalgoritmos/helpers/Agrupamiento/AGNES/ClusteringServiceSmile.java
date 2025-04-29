package co.uniquindio.proyecto.backendalgoritmos.helpers.Agrupamiento.AGNES;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import smile.clustering.HierarchicalClustering;
import smile.clustering.linkage.CompleteLinkage;
import smile.clustering.linkage.Linkage;
import smile.clustering.linkage.UPGMALinkage;

public class ClusteringServiceSmile {

    // Mapa de relaciones semánticas: palabra -> conjunto de palabras relacionadas
    private static final Map<String, List<String>> RELACIONES_SEMANTICAS = new HashMap<>();
    static {
        // Ejemplo de carga de relaciones semánticas (sinónimos o términos relacionados)
        // RELACIONES_SEMANTICAS.put("rápido", Arrays.asList("veloz", "ligero"));
        // RELACIONES_SEMANTICAS.put("coche", Arrays.asList("auto", "automóvil"));
        // ... (otros pares de palabras relacionadas)
    }

    /**
     * Realiza el clustering jerárquico de una lista de palabras.
     * @param palabras Lista de palabras a clusterizar.
     * @return Nodo raíz del árbol de clusters jerárquico. Si no hay suficientes diferencias,
     *         se devuelve un árbol plano (cada palabra en su propio cluster).
     * @throws IllegalArgumentException si la lista de palabras es nula o vacía.
     */
    public ClusterNode clusterizarPalabras(List<String> palabras) {
        if (palabras == null || palabras.isEmpty()) {
            throw new IllegalArgumentException("La lista de palabras no puede ser nula ni vacía.");
        }
        if (palabras.size() == 1) {
            // Si solo hay una palabra, devolvemos un nodo único
            return new ClusterNode(palabras.get(0));
        }

        // 1. Calcular dinámicamente la dimensión del vector como el largo máximo de las palabras.
        int dimensionVector = calcularDimensionMaxima(palabras);

        // 2. Vectorizar palabras, rellenando vectores más cortos con ceros.
        double[][] vectores = vectorizarPalabras(palabras, dimensionVector);

        // 3. Validar diferencias: si los vectores son muy similares (por ejemplo, todos iguales),
        //    devolvemos un árbol plano (cada palabra separada).
        if (!tieneDiferenciasSuficientes(vectores)) {
            // Construir árbol plano: cada palabra es un nodo hijo del nodo raíz.
            ClusterNode raizPlano = new ClusterNode("ROOT");
            for (String palabra : palabras) {
                raizPlano.getHijos().add(new ClusterNode(palabra));
            }
            return raizPlano;
        }

        // 4. Calcular la matriz de distancias personalizada considerando relaciones semánticas.
        double[][] matrizDistancias = calcularMatrizDistancias(vectores, palabras);

        // 5. Realizar clustering jerárquico usando Smile (por ejemplo, enlace promedio UPGMA).
        ClusterNode nodoRaiz;
        try {
            // Elegimos el método de enlace (UPGMA - promedio) para la agrupación jerárquica.
            Linkage enlace = new UPGMALinkage(matrizDistancias);
            HierarchicalClustering resultado = HierarchicalClustering.fit(enlace);
            // Convertir el resultado de Smile (dendrograma) en nuestra estructura de árbol ClusterNode.
            nodoRaiz = construirArbolClusters(resultado, palabras);
        } catch (Exception e) {
            // Manejo básico de errores en clustering
            throw new RuntimeException("Error realizando el clustering jerárquico: " + e.getMessage(), e);
        }

        return nodoRaiz;
    }

    /**
     * Calcula la longitud máxima entre todas las palabras, para definir la dimensión del vector.
     */
    private int calcularDimensionMaxima(List<String> palabras) {
        int max = 0;
        for (String palabra : palabras) {
            if (palabra.length() > max) {
                max = palabra.length();
            }
        }
        return max;
    }

    /**
     * Convierte cada palabra en un vector numérico de longitud fija (dimensionVector).
     * Las palabras más cortas se rellenan con ceros al final.
     */
    private double[][] vectorizarPalabras(List<String> palabras, int dimensionVector) {
        double[][] vectores = new double[palabras.size()][dimensionVector];
        for (int i = 0; i < palabras.size(); i++) {
            String palabra = palabras.get(i);
            double[] vector = new double[dimensionVector];
            // Convertir cada caracter a valor numérico (por ejemplo, código Unicode)
            for (int j = 0; j < palabra.length(); j++) {
                vector[j] = palabra.charAt(j);
            }
            // Si la palabra es más corta que dimensionVector, 
            // las posiciones restantes del vector permanecen en 0.0 (ya inicializado).
            vectores[i] = vector;
        }
        return vectores;
    }

    /**
     * Verifica si existe suficiente diferencia entre los vectores de palabras 
     * para justificar el clustering. Si todos los vectores son iguales o muy similares, 
     * devuelve false.
     */
    private boolean tieneDiferenciasSuficientes(double[][] vectores) {
        // Verificar si al menos dos vectores difieren en algún valor.
        // Una forma simple: comparar todos los vectores con el primero.
        double[] primerVector = vectores[0];
        for (int i = 1; i < vectores.length; i++) {
            if (!sonVectoresIguales(primerVector, vectores[i])) {
                // Encontramos una diferencia
                return true;
            }
        }
        // Si llegamos aquí, todos los vectores son iguales (no hay diferencias suficientes).
        return false;
    }

    /**
     * Compara dos vectores componente a componente para verificar si son exactamente iguales.
     */
    private boolean sonVectoresIguales(double[] v1, double[] v2) {
        if (v1.length != v2.length) {
            return false;
        }
        for (int i = 0; i < v1.length; i++) {
            if (Double.compare(v1[i], v2[i]) != 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Calcula la matriz de distancias personalizada entre todos los pares de palabras,
     * incorporando la cercanía semántica definida en RELACIONES_SEMANTICAS.
     *
     * @param vectores matriz donde cada fila es el vector de una palabra.
     * @param palabras lista de palabras correspondiente a cada vector.
     * @return matriz simétrica de distancias.
     */
    private double[][] calcularMatrizDistancias(double[][] vectores, List<String> palabras) {
        int n = vectores.length;
        double[][] distancias = new double[n][n];
        for (int i = 0; i < n; i++) {
            distancias[i][i] = 0.0;
            for (int j = i + 1; j < n; j++) {
                // Calcular distancia euclidiana básica entre vectores i y j
                double distanciaBase = 0.0;
                double[] vi = vectores[i];
                double[] vj = vectores[j];
                for (int k = 0; k < vi.length; k++) {
                    double diff = vi[k] - vj[k];
                    distanciaBase += diff * diff;
                }
                distanciaBase = Math.sqrt(distanciaBase);

                // Mejorar la distancia si las palabras están relacionadas semánticamente
                String palabraI = palabras.get(i);
                String palabraJ = palabras.get(j);
                double factorSemantico = 1.0;
                if (estanRelacionadasSemanticamente(palabraI, palabraJ)) {
                    // Reducimos la distancia si son semánticamente cercanas
                    factorSemantico = 0.5;  // Ejemplo: la mitad de distancia si hay relación semántica
                }

                double distanciaFinal = distanciaBase * factorSemantico;
                distancias[i][j] = distanciaFinal;
                distancias[j][i] = distanciaFinal;
            }
        }
        return distancias;
    }

    /**
     * Verifica si dos palabras están marcadas como relacionadas semánticamente en el mapa.
     */
    private boolean estanRelacionadasSemanticamente(String palabra1, String palabra2) {
        if (RELACIONES_SEMANTICAS.containsKey(palabra1) &&
                RELACIONES_SEMANTICAS.get(palabra1).contains(palabra2)) {
            return true;
        }
        if (RELACIONES_SEMANTICAS.containsKey(palabra2) &&
                RELACIONES_SEMANTICAS.get(palabra2).contains(palabra1)) {
            return true;
        }
        return false;
    }

    /**
     * Convierte el resultado de Smile (HierarchicalClustering) en un árbol de ClusterNode.
     *
     * @param resultado Objeto HierarchicalClustering resultado de Smile.
     * @param palabras Lista original de palabras (para obtener nombres de nodos hoja).
     * @return Nodo raíz del árbol de clusters jerárquico.
     */
    private ClusterNode construirArbolClusters(HierarchicalClustering resultado, List<String> palabras) {
        int n = palabras.size();
        int[][] merges = resultado.getTree();    // matriz de merges (n-1 x 2)
        // Crear nodo para cada palabra inicial (cluster hoja)
        List<ClusterNode> nodos = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            nodos.add(new ClusterNode(palabras.get(i)));
        }
        // Procesar cada fusión para construir el árbol
        for (int k = 0; k < merges.length; k++) {
            int indiceA = merges[k][0];
            int indiceB = merges[k][1];
            ClusterNode nodoA = (indiceA < n ? nodos.get(indiceA) : nodos.get(indiceA));
            ClusterNode nodoB = (indiceB < n ? nodos.get(indiceB) : nodos.get(indiceB));
            // Crear nuevo cluster combinando A y B
            ClusterNode nuevoCluster = new ClusterNode("Cluster_" + (n + k));
            nuevoCluster.getHijos().add(nodoA);
            nuevoCluster.getHijos().add(nodoB);
            // Añadir el nuevo cluster a la lista
            nodos.add(nuevoCluster);
        }
        // El último nodo añadido es la raíz del dendrograma
        return nodos.get(nodos.size() - 1);
    }

    /**
     * Clase interna para representar un nodo en el árbol de clusters.
     * Cada nodo puede representar una palabra individual (hoja) o un cluster combinado (nodo interno).
     */
    public static class ClusterNode {
        private String nombre;
        private List<ClusterNode> hijos;

        public ClusterNode(String nombre) {
            this.nombre = nombre;
            this.hijos = new ArrayList<>();
        }

        public String getNombre() {
            return nombre;
        }

        public List<ClusterNode> getHijos() {
            return hijos;
        }
    }

    public static Map<String, Object> convertirClusterAJson(ClusteringServiceSmile.ClusterNode nodo) {
        Map<String, Object> resultado = new HashMap<>();
        resultado.put("name", nodo.getNombre());

        List<ClusteringServiceSmile.ClusterNode> hijos = nodo.getHijos();
        if (hijos != null && !hijos.isEmpty()) {
            List<Map<String, Object>> listaHijos = hijos.stream()
                    .map(ClusteringServiceSmile::convertirClusterAJson) // recursivo
                    .toList();
            resultado.put("children", listaHijos);
        }

        return resultado;
    }

    private Map<String, Object> convertirNodo(ClusterNode nodo) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", nodo.getNombre());

        if (!nodo.getHijos().isEmpty()) {
            List<Map<String, Object>> hijos = new ArrayList<>();
            for (ClusterNode hijo : nodo.getHijos()) {
                hijos.add(convertirNodo(hijo)); // llamada recursiva
            }
            map.put("children", hijos);
        }

        return map;
    }

}
