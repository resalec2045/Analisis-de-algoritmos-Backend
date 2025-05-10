package co.uniquindio.proyecto.backendalgoritmos.helpers.Agrupamiento.requisito5;

import java.util.*;
import java.util.stream.Collectors;

public class AgrupadorManual {

    public static Map<String, List<List<String>>> agruparPorTFIDFManual(List<String> abstracts, int numClusters, int maxIter) {
        List<List<String>> tokensPorDoc = new ArrayList<>();
        Set<String> vocabSet = new HashSet<>();

        // 1. Tokenizar y construir vocabulario
        for (String doc : abstracts) {
            List<String> tokens = Arrays.stream(doc.toLowerCase().split("\\W+"))
                    .filter(t -> t.length() > 2) // filtrar tokens muy cortos
                    .collect(Collectors.toList());
            tokensPorDoc.add(tokens);
            vocabSet.addAll(tokens);
        }

        List<String> vocabulario = new ArrayList<>(vocabSet);
        int N = abstracts.size();

        // 2. Calcular DF (Document Frequency)
        Map<String, Integer> df = new HashMap<>();
        for (String term : vocabulario) {
            int count = 0;
            for (List<String> doc : tokensPorDoc) {
                if (doc.contains(term)) count++;
            }
            df.put(term, count);
        }

        // 3. Calcular matriz TF-IDF
        double[][] tfidfMatrix = new double[N][vocabulario.size()];
        for (int i = 0; i < N; i++) {
            List<String> doc = tokensPorDoc.get(i);
            Map<String, Long> tf = doc.stream()
                    .collect(Collectors.groupingBy(t -> t, Collectors.counting()));

            for (int j = 0; j < vocabulario.size(); j++) {
                String term = vocabulario.get(j);
                long tfVal = tf.getOrDefault(term, 0L);
                int dfVal = df.get(term);
                double idf = Math.log((double) N / (1 + dfVal));
                tfidfMatrix[i][j] = tfVal * idf;
            }
        }

        // 4. KMeans desde cero
        int[] labels = new int[N];
        Random rand = new Random();
        double[][] centroids = new double[numClusters][vocabulario.size()];

        // Inicializar centroides aleatoriamente
        for (int k = 0; k < numClusters; k++) {
            centroids[k] = Arrays.copyOf(tfidfMatrix[rand.nextInt(N)], vocabulario.size());
        }

        for (int iter = 0; iter < maxIter; iter++) {
            // Asignar a clusters
            for (int i = 0; i < N; i++) {
                labels[i] = nearestCentroid(tfidfMatrix[i], centroids);
            }

            // Recalcular centroides
            double[][] newCentroids = new double[numClusters][vocabulario.size()];
            int[] counts = new int[numClusters];

            for (int i = 0; i < N; i++) {
                int label = labels[i];
                for (int j = 0; j < vocabulario.size(); j++) {
                    newCentroids[label][j] += tfidfMatrix[i][j];
                }
                counts[label]++;
            }

            for (int k = 0; k < numClusters; k++) {
                if (counts[k] == 0) continue; // evitar división por cero
                for (int j = 0; j < vocabulario.size(); j++) {
                    newCentroids[k][j] /= counts[k];
                }
            }

            centroids = newCentroids;
        }

        // 5. Agrupar resultados
        List<List<String>> grupos = new ArrayList<>();
        for (int i = 0; i < numClusters; i++) {
            grupos.add(new ArrayList<>());
        }

        for (int i = 0; i < N; i++) {
            grupos.get(labels[i]).add(abstracts.get(i));
        }

        Map<String, List<List<String>>> resultado = new HashMap<>();
        resultado.put("Texto", grupos);
        return resultado;
    }

    private static int nearestCentroid(double[] vector, double[][] centroids) {
        double minDist = Double.MAX_VALUE;
        int label = 0;
        for (int k = 0; k < centroids.length; k++) {
            double dist = euclideanDistance(vector, centroids[k]);
            if (dist < minDist) {
                minDist = dist;
                label = k;
            }
        }
        return label;
    }

    private static double euclideanDistance(double[] a, double[] b) {
        double sum = 0.0;
        for (int i = 0; i < a.length; i++) {
            double diff = a[i] - b[i];
            sum += diff * diff;
        }
        return Math.sqrt(sum);
    }

    public static double cosineSimilarity(double[] vec1, double[] vec2) {
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;

        for (int i = 0; i < vec1.length; i++) {
            dotProduct += vec1[i] * vec2[i];
            normA += Math.pow(vec1[i], 2);
            normB += Math.pow(vec2[i], 2);
        }

        if (normA == 0 || normB == 0) return 0.0;

        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }

}
