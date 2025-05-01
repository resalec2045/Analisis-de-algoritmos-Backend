package co.uniquindio.proyecto.backendalgoritmos.helpers.Agrupamiento.requisito5;

import java.util.*;

public class TextSimilarityGrouper {

    // Calcula la similitud de Jaccard entre dos textos
    public static double jaccardSimilarity(String text1, String text2) {
        // Tokenizar en palabras, usando solo caracteres alfanuméricos
        Set<String> set1 = new HashSet<>(Arrays.asList(text1.toLowerCase().split("\\W+")));
        Set<String> set2 = new HashSet<>(Arrays.asList(text2.toLowerCase().split("\\W+")));
        set1.remove(""); // eliminar posibles cadenas vacías
        set2.remove("");
        // Intersección
        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);
        // Unión
        Set<String> union = new HashSet<>(set1);
        union.addAll(set2);
        if (union.isEmpty()) {
            return 0.0; // evitar división por cero si ambos textos están vacíos
        }
        // Similitud de Jaccard = |intersección| / |unión|
        return (double) intersection.size() / union.size();
    }

    // Agrupa abstracts según similitud de Jaccard, con un umbral dado
    public static Map<String, List<List<String>>> agruparPorJaccard(List<String> abstracts, double umbral) {
        List<List<String>> grupos = new ArrayList<>();
        for (String abs : abstracts) {
            boolean asignado = false;
            for (List<String> grupo : grupos) {
                if (!grupo.isEmpty()) {
                    String representativo = grupo.get(0);
                    if (jaccardSimilarity(abs, representativo) >= umbral) {
                        grupo.add(abs);
                        asignado = true;
                        break;
                    }
                }
            }
            if (!asignado) {
                List<String> nuevoGrupo = new ArrayList<>();
                nuevoGrupo.add(abs);
                grupos.add(nuevoGrupo);
            }
        }

        Map<String, List<List<String>>> resultado = new HashMap<>();
        resultado.put("Texto", grupos);
        return resultado;
    }

}
