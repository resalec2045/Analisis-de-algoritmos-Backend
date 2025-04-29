package co.uniquindio.proyecto.backendalgoritmos.helpers.Agrupamiento.DIANA;

import smile.math.distance.EuclideanDistance;
import co.uniquindio.proyecto.backendalgoritmos.helpers.Agrupamiento.Cluster;

import java.util.*;

public class DivisiveClusteringSmile {

    /**
     * Punto de entrada para hacer clustering divisivo de palabras.
     */
    public static Map<String, Object> clusteringDivisivoJson(List<String> palabras) {
        Cluster raiz = dividirPalabrasEnClusters(palabras);
        return convertirClusterAJson(raiz);
    }

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

    private static void dividirRecursivamente(Cluster padre, List<Cluster> clusters) {
        if (clusters.size() <= 2) {
            padre.getChildren().addAll(clusters);
            return;
        }

        // Vectorizar las palabras
        double[][] vectores = clusters.stream()
                .map(c -> vectorizarPalabra(c.getName()))
                .toArray(double[][]::new);

        // Encontrar par más alejado
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

        Cluster centroide1 = clusters.get(idxA);
        Cluster centroide2 = clusters.get(idxB);

        List<Cluster> grupo1 = new ArrayList<>();
        List<Cluster> grupo2 = new ArrayList<>();

        for (int i = 0; i < clusters.size(); i++) {
            Cluster actual = clusters.get(i);
            if (distancia.d(vectorizarPalabra(actual.getName()), vectorizarPalabra(centroide1.getName()))
                    < distancia.d(vectorizarPalabra(actual.getName()), vectorizarPalabra(centroide2.getName()))) {
                grupo1.add(actual);
            } else {
                grupo2.add(actual);
            }
        }

        // Corte de seguridad: no pudo separar bien
        if (grupo1.isEmpty() || grupo2.isEmpty()) {
            padre.getChildren().addAll(clusters);
            return;
        }

        Cluster subgrupo1 = new Cluster(centroide1.getName());
        dividirRecursivamente(subgrupo1, grupo1);
        padre.addChild(subgrupo1);

        Cluster subgrupo2 = new Cluster(centroide2.getName());
        dividirRecursivamente(subgrupo2, grupo2);
        padre.addChild(subgrupo2);
    }

    /**
     * Convierte un nombre de palabra en un vector numérico simple basado en códigos de caracteres.
     */
    private static double[] vectorizarPalabra(String palabra) {
        int dimension = 10; // Puedes ajustar la dimensión
        double[] vector = new double[dimension];
        for (int i = 0; i < Math.min(palabra.length(), dimension); i++) {
            vector[i] = (double) palabra.charAt(i);
        }
        return vector;
    }

    /**
     * Convierte un Cluster recursivamente a un formato Map<String,Object> listo para JSON.
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
