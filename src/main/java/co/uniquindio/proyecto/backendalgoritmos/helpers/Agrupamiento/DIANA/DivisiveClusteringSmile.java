package co.uniquindio.proyecto.backendalgoritmos.helpers.Agrupamiento.DIANA;

import smile.math.distance.EuclideanDistance;
import co.uniquindio.proyecto.backendalgoritmos.helpers.Agrupamiento.Cluster;

import java.util.*;

/**
 * Implementación de clustering jerárquico divisivo (DIANA) usando distancia euclidiana
 * y una vectorización básica de palabras. Utiliza la librería SMILE para el cálculo de distancias.
 */
public class DivisiveClusteringSmile {

    /**
     * Punto de entrada para realizar clustering divisivo sobre una lista de palabras.
     * Devuelve un árbol jerárquico en formato Map (útil para serializar a JSON).
     *
     * @param palabras Lista de palabras a agrupar
     * @return Árbol jerárquico de clusters en formato Map<String, Object>
     */
    public static Map<String, Object> clusteringDivisivoJson(List<String> palabras) {
        Cluster raiz = dividirPalabrasEnClusters(palabras);
        return convertirClusterAJson(raiz);
    }

    /**
     * Inicializa el proceso de división de palabras agrupándolas bajo un nodo raíz.
     *
     * @param palabras Lista de palabras a dividir
     * @return Cluster raíz con estructura jerárquica generada
     */
    private static Cluster dividirPalabrasEnClusters(List<String> palabras) {
        Cluster raiz = new Cluster("Root");

        if (palabras == null || palabras.isEmpty()) {
            return raiz;
        }

        List<Cluster> nodosIniciales = new ArrayList<>();
        for (String palabra : palabras) {
            nodosIniciales.add(new Cluster(palabra));
        }

        dividirRecursivamente(raiz, nodosIniciales);
        return raiz;
    }

    /**
     * Realiza la división recursiva de un grupo de clusters en dos subconjuntos basados en distancia máxima.
     * Si no se puede dividir apropiadamente, se agregan todos los clusters como hijos del nodo actual.
     *
     * @param padre    Nodo actual del árbol
     * @param clusters Lista de clusters a dividir
     */
    private static void dividirRecursivamente(Cluster padre, List<Cluster> clusters) {
        if (clusters.size() <= 2) {
            padre.getChildren().addAll(clusters); // Base case: no se puede dividir más
            return;
        }

        // Convertir nombres de palabras a vectores numéricos
        double[][] vectores = clusters.stream()
                .map(c -> vectorizarPalabra(c.getName()))
                .toArray(double[][]::new);

        // Encontrar las dos palabras más alejadas
        EuclideanDistance distancia = new EuclideanDistance();
        double maxDistancia = -1.0;
        int idxA = 0, idxB = 0;
        int n = vectores.length;

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                double dist = distancia.d(vectores[i], vectores[j]);
                if (dist > maxDistancia) {
                    maxDistancia = dist;
                    idxA = i;
                    idxB = j;
                }
            }
        }

        // Usar los puntos más lejanos como centroides para formar 2 grupos
        Cluster centroide1 = clusters.get(idxA);
        Cluster centroide2 = clusters.get(idxB);
        List<Cluster> grupo1 = new ArrayList<>();
        List<Cluster> grupo2 = new ArrayList<>();

        for (int i = 0; i < clusters.size(); i++) {
            Cluster actual = clusters.get(i);
            double[] vectorActual = vectorizarPalabra(actual.getName());

            if (distancia.d(vectorActual, vectorizarPalabra(centroide1.getName()))
                    < distancia.d(vectorActual, vectorizarPalabra(centroide2.getName()))) {
                grupo1.add(actual);
            } else {
                grupo2.add(actual);
            }
        }

        // Corte de seguridad: si un grupo queda vacío, se detiene la división
        if (grupo1.isEmpty() || grupo2.isEmpty()) {
            padre.getChildren().addAll(clusters);
            return;
        }

        // Crear subgrupos recursivamente
        Cluster subgrupo1 = new Cluster(centroide1.getName());
        dividirRecursivamente(subgrupo1, grupo1);
        padre.addChild(subgrupo1);

        Cluster subgrupo2 = new Cluster(centroide2.getName());
        dividirRecursivamente(subgrupo2, grupo2);
        padre.addChild(subgrupo2);
    }

    /**
     * Convierte una palabra en un vector numérico simple usando los códigos ASCII de los primeros caracteres.
     * Este vector es usado para calcular distancias entre palabras.
     *
     * @param palabra Palabra a vectorizar
     * @return Vector numérico de dimensión fija
     */
    private static double[] vectorizarPalabra(String palabra) {
        int dimension = 10; // Dimensión fija del vector
        double[] vector = new double[dimension];

        for (int i = 0; i < Math.min(palabra.length(), dimension); i++) {
            vector[i] = (double) palabra.charAt(i);
        }

        return vector;
    }

    /**
     * Convierte un objeto Cluster en un mapa anidado que representa la estructura del árbol.
     * Este mapa puede convertirse fácilmente a JSON.
     *
     * @param cluster Nodo raíz del árbol
     * @return Mapa con estructura jerárquica
     */
    private static Map<String, Object> convertirClusterAJson(Cluster cluster) {
        Map<String, Object> nodo = new HashMap<>();
        nodo.put("name", cluster.getName());

        if (cluster.getChildren() != null && !cluster.getChildren().isEmpty()) {
            List<Map<String, Object>> hijos = new ArrayList<>();
            for (Cluster hijo : cluster.getChildren()) {
                hijos.add(convertirClusterAJson(hijo));
            }
            nodo.put("children", hijos);
        }

        return nodo;
    }
}
