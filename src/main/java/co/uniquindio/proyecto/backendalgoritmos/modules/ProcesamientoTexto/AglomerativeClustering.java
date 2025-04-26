package co.uniquindio.proyecto.backendalgoritmos.modules.ProcesamientoTexto;

import java.util.*;

public class AglomerativeClustering {

    public static Cluster aglomerar(List<List<String>> documentos) {
        int n = documentos.size();
        List<Cluster> clusters = new ArrayList<>();

        // Cada documento comienza como su propio cluster
        for (int i = 0; i < n; i++) {
            clusters.add(new Cluster("Doc" + (i + 1)));
        }

        // Calcula la matriz de distancias
        double[][] distancias = calcularMatrizDistancias(documentos);

        while (clusters.size() > 1) {
            // Encontrar los dos clusters más cercanos
            int[] par = encontrarParMasCercano(clusters.size(), distancias);

            int i = par[0];
            int j = par[1];

            // Crear un nuevo cluster combinando los dos más cercanos
            Cluster nuevo = new Cluster("Grupo");
            nuevo.addChild(clusters.get(i));
            nuevo.addChild(clusters.get(j));

            // Eliminar los clusters viejos
            clusters.remove(j); // ⚠️ Primero el índice mayor
            clusters.remove(i);

            // Añadir el nuevo cluster
            clusters.add(nuevo);
        }

        return clusters.get(0); // Devuelve el cluster raíz
    }

    private static int[] encontrarParMasCercano(int n, double[][] distancias) {
        double mejorDistancia = Double.MAX_VALUE;
        int[] mejorPar = new int[2];

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (distancias[i][j] < mejorDistancia) {
                    mejorDistancia = distancias[i][j];
                    mejorPar[0] = i;
                    mejorPar[1] = j;
                }
            }
        }
        return mejorPar;
    }

    private static double[][] calcularMatrizDistancias(List<List<String>> documentos) {
        int n = documentos.size();
        double[][] distancias = new double[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                double sim = calcularSimilitud(documentos.get(i), documentos.get(j));
                distancias[i][j] = 1.0 - sim; // distancia = 1 - similitud
                distancias[j][i] = distancias[i][j];
            }
        }
        return distancias;
    }

    private static double calcularSimilitud(List<String> doc1, List<String> doc2) {
        Set<String> union = new HashSet<>(doc1);
        union.addAll(doc2);

        int interseccion = 0;
        for (String palabra : doc1) {
            if (doc2.contains(palabra)) {
                interseccion++;
            }
        }

        if (union.isEmpty()) {
            return 0.0;
        }

        return (double) interseccion / union.size();
    }
}
