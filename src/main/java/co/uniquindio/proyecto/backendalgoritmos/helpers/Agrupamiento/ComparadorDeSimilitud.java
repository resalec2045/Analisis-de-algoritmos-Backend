package co.uniquindio.proyecto.backendalgoritmos.helpers.Agrupamiento;

import co.uniquindio.proyecto.backendalgoritmos.helpers.Agrupamiento.requisito5.TextSimilarityGrouper;

import java.util.*;
import java.util.stream.Collectors;

public class ComparadorDeSimilitud {

    public static List<String> compararTodos(List<String> abstracts) {
        List<String> resultados = new ArrayList<>();

        // Tokenizar todos los abstracts
        List<List<String>> tokensPorDoc = new ArrayList<>();
        Set<String> vocabSet = new HashSet<>();

        for (String doc : abstracts) {
            List<String> tokens = Arrays.stream(doc.toLowerCase().split("\\W+"))
                    .filter(t -> t.length() > 2)
                    .collect(Collectors.toList());
            tokensPorDoc.add(tokens);
            vocabSet.addAll(tokens);
        }

        List<String> vocabulario = new ArrayList<>(vocabSet);
        int N = abstracts.size();

        // Calcular DF
        Map<String, Integer> df = new HashMap<>();
        for (String term : vocabulario) {
            int count = 0;
            for (List<String> doc : tokensPorDoc) {
                if (doc.contains(term)) count++;
            }
            df.put(term, count);
        }

        // Calcular TF-IDF matrix
        double[][] tfidfMatrix = new double[N][vocabulario.size()];
        for (int i = 0; i < N; i++) {
            List<String> doc = tokensPorDoc.get(i);
            Map<String, Long> tf = doc.stream().collect(Collectors.groupingBy(t -> t, Collectors.counting()));

            for (int j = 0; j < vocabulario.size(); j++) {
                String term = vocabulario.get(j);
                long tfVal = tf.getOrDefault(term, 0L);
                int dfVal = df.get(term);
                double idf = Math.log((double) N / (1 + dfVal));
                tfidfMatrix[i][j] = tfVal * idf;
            }
        }

        // Comparar todos los pares
        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                String abs1 = abstracts.get(i);
                String abs2 = abstracts.get(j);

                double jaccard = TextSimilarityGrouper.jaccardSimilarity(abs1, abs2) * 100.0;
                double cosine = cosineSimilarity(tfidfMatrix[i], tfidfMatrix[j]) * 100.0;

                resultados.add("Abstract" + (i + 1) + " - Abstract" + (j + 1)
                        + " | Jaccard: " + String.format("%.2f", jaccard) + "%"
                        + " | TF-IDF: " + String.format("%.2f", cosine) + "%");
            }
        }

        return resultados;
    }

    private static double cosineSimilarity(double[] vec1, double[] vec2) {
        double dotProduct = 0.0, normA = 0.0, normB = 0.0;
        for (int i = 0; i < vec1.length; i++) {
            dotProduct += vec1[i] * vec2[i];
            normA += Math.pow(vec1[i], 2);
            normB += Math.pow(vec2[i], 2);
        }
        if (normA == 0 || normB == 0) return 0.0;
        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }
}
