package co.uniquindio.proyecto.backendalgoritmos.helpers.Agrupamiento;

import java.util.*;

public class PreprocesamientoTexto {

    // Lista de palabras vacías comunes (stopwords)
    private static final Set<String> STOPWORDS = Set.of(
            "i", "me", "my", "myself", "we", "our", "ours", "ourselves",
            "you", "your", "yours", "yourself", "yourselves",
            "he", "him", "his", "himself", "she", "her", "hers", "herself",
            "it", "its", "itself", "they", "them", "their", "theirs", "themselves",
            "what", "which", "who", "whom", "this", "that", "these", "those",
            "am", "is", "are", "was", "were", "be", "been", "being",
            "have", "has", "had", "having", "do", "does", "did", "doing",
            "a", "an", "the", "and", "but", "if", "or", "because", "as",
            "until", "while", "of", "at", "by", "for", "with", "about",
            "against", "between", "into", "through", "during", "before",
            "after", "above", "below", "to", "from", "up", "down", "in",
            "out", "on", "off", "over", "under", "again", "further", "then",
            "once", "here", "there", "when", "where", "why", "how", "all",
            "any", "both", "each", "few", "more", "most", "other", "some",
            "such", "no", "nor", "not", "only", "own", "same", "so", "than",
            "too", "very", "can", "will", "just", "don", "should", "now"
    );

    /**
     * Preprocesa un texto: minúsculas, eliminación de puntuación, tokenización, eliminación de stopwords.
     */
    public static List<String> preprocesarTexto(String texto) {
        // 1. Convertir a minúsculas
        texto = texto.toLowerCase();

        // 2. Eliminar caracteres que no sean letras ni espacios
        texto = texto.replaceAll("[^a-z\\s]", "");

        // 3. Dividir en palabras (tokenizar)
        String[] tokens = texto.split("\\s+");

        // 4. Eliminar stopwords, palabras menores a 4 letras y duplicados
        Set<String> palabrasUtiles = new LinkedHashSet<>(); // ⚡ Usamos Set para evitar duplicados manteniendo orden
        for (String token : tokens) {
            if (!STOPWORDS.contains(token) && !token.isEmpty() && token.length() >= 4) {
                palabrasUtiles.add(token);
            }
        }

        return new ArrayList<>(palabrasUtiles);
    }


}
