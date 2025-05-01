package co.uniquindio.proyecto.backendalgoritmos.helpers.Agrupamiento;

import java.util.*;

public class TextSimilarity {

    public static double cosineSimilarity(String text1, String text2) {
        Map<String, Integer> freq1 = getTermFrequencies(text1);
        Map<String, Integer> freq2 = getTermFrequencies(text2);

        Set<String> allWords = new HashSet<>();
        allWords.addAll(freq1.keySet());
        allWords.addAll(freq2.keySet());

        int[] vec1 = new int[allWords.size()];
        int[] vec2 = new int[allWords.size()];

        int index = 0;
        for (String word : allWords) {
            vec1[index] = freq1.getOrDefault(word, 0);
            vec2[index] = freq2.getOrDefault(word, 0);
            index++;
        }

        return computeCosine(vec1, vec2);
    }

    private static Map<String, Integer> getTermFrequencies(String text) {
        text = text.toLowerCase().replaceAll("[^a-z\\s]", "");
        String[] tokens = text.split("\\s+");
        Set<String> stopwords = Set.of("the", "is", "and", "a", "an", "in", "of", "to", "with", "on");

        Map<String, Integer> freqMap = new HashMap<>();
        for (String token : tokens) {
            if (!stopwords.contains(token) && token.length() > 2) {
                freqMap.put(token, freqMap.getOrDefault(token, 0) + 1);
            }
        }
        return freqMap;
    }

    private static double computeCosine(int[] vec1, int[] vec2) {
        int dotProduct = 0;
        double normA = 0.0, normB = 0.0;

        for (int i = 0; i < vec1.length; i++) {
            dotProduct += vec1[i] * vec2[i];
            normA += Math.pow(vec1[i], 2);
            normB += Math.pow(vec2[i], 2);
        }

        if (normA == 0 || normB == 0) return 0.0;
        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }

}
