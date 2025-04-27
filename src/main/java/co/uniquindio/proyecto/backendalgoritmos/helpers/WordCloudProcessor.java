package co.uniquindio.proyecto.backendalgoritmos.helpers;

import co.uniquindio.proyecto.backendalgoritmos.models.WordCloudItem;
import java.util.*;

public class WordCloudProcessor {

    public static List<WordCloudItem> generarWordCloudContiene(List<String> palabras) {
        Map<String, Integer> contador = new HashMap<>();

        for (String palabraBase : palabras) {
            String palabraBaseLower = palabraBase.toLowerCase().trim();

            for (String palabraComparar : palabras) {
                String palabraCompararLower = palabraComparar.toLowerCase().trim();
                if (palabraCompararLower.contains(palabraBaseLower)) {
                    contador.put(palabraBase, contador.getOrDefault(palabraBase, 0) + 1);
                }
            }
        }

        List<WordCloudItem> resultado = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : contador.entrySet()) {
            if (entry.getValue() > 0) {
                resultado.add(new WordCloudItem(entry.getKey(), entry.getValue()));
            }
        }

        return resultado;
    }
}
