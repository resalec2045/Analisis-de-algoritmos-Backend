package co.uniquindio.proyecto.backendalgoritmos.helpers;

import co.uniquindio.proyecto.backendalgoritmos.models.WordCloudItem;

import java.util.*;
import java.util.stream.Collectors;

public class WordCloudProcessor {

    public static final Map<String, List<String>> CATEGORIAS = Map.ofEntries(
            Map.entry("Habilidades", List.of(
                    "Abstraction", "Algorithm", "Algorithmic thinking", "Coding", "Collaboration", "Cooperation",
                    "Creativity", "Critical thinking", "Debug", "Decomposition", "Evaluation", "Generalization",
                    "Logic", "Logical thinking", "Modularity", "Patterns recognition", "Problem solving", "Programming"
            )),
            Map.entry("Conceptos Computacionales", List.of(
                    "Conditionals", "Control structures", "Directions", "Events", "Funtions", "Loops", "Modular structure",
                    "Parallelism", "Sequences", "Software/hardware", "Variables"
            )),
            Map.entry("Actitudes", List.of(
                    "Emotional", "Engagement", "Motivation", "Perceptions", "Persistence", "Self-efficacy", "Self-perceived"
            )),
            Map.entry("Propiedades psicométricas", List.of(
                    "CTT", "CFA", "EFA", "IRT", "Reliability", "SEM", "Validity"
            )),
            Map.entry("Herramienta de evaluación", List.of(
                    "BCTt", "ESCAS", "Collaborative Computing Observation Instrument", "cCTt", "CTST",
                    "Computational concepts", "CTA-CES", "CTC", "CTLS", "CTS", "CTt",
                    "Computational Thinking Test", "Computational Thinking Test for Elementary School Students",
                    "CTtLP", "Computational thinking-skill tasks on numbers and arithmetic", "CAPCT", "CTS",
                    "ESCAS", "General self-efficacy scale", "ICT competency test", "Instrument of computational identity",
                    "KBIT", "Mastery of computational concepts Test and an Algorithmic Test",
                    "Multidimensional 21st Century Skills Scale", "Self-efficacy scale", "STEM learning attitude scale",
                    "The computational thinking scale"
            )),
            Map.entry("Diseño de investigación", List.of(
                    "No experimental", "Experimental", "Longitudinal research", "Mixed methods",
                    "Post-test", "Pre-test", "Quasi-experiments"
            )),
            Map.entry("Nivel de escolaridad", List.of(
                    "Upper elementary school", "Primary school", "Kindergarten", "Secondary school",
                    "High school", "University"
            )),
            Map.entry("Medio", List.of(
                    "Block programming", "Mobile application", "Pair programming", "Plugged activities",
                    "Programming", "Robotics", "Spreadsheet", "STEM", "Unplugged activities"
            )),
            Map.entry("Estrategia", List.of(
                    "Construct-by-self mind mapping", "Construct-on-scaffold mind mapping", "Design-based learning",
                    "Evidence-centred design approach", "Gamification", "Reverse engineering pedagogy",
                    "Technology-enhanced learning", "Collaborative learning", "Cooperative learning", "Flipped classroom",
                    "Game-based learning", "Inquiry-based learning", "Personalized learning", "Problem-based learning",
                    "Project-based learning", "Universal design for learning"
            )),
            Map.entry("Herramienta", List.of(
                    "Alice", "Arduino", "Scratch", "ScratchJr", "Blockly Games", "Code.org", "Codecombat",
                    "CSUnplugged", "Robot Turtles", "Hello Ruby", "Kodable", "LightbotJr", "KIBO", "BEE BOT",
                    "CUBETTO", "Minecraft", "Agent Sheets", "Mimo", "Py– Learn", "SpaceChem"
            ))
    );

    public static List<WordCloudItem> generarWordCloudContiene(List<String> palabras) {
        int limite = Math.max(1, palabras.size() / 4);
        List<String> sublista = palabras.subList(0, limite);

        Map<String, Integer> contador = new HashMap<>();
        for (String palabraBase : sublista) {
            String base = palabraBase.toLowerCase().trim();
            for (String comparar : palabras) {
                if (comparar.toLowerCase().trim().contains(base)) {
                    contador.put(base, contador.getOrDefault(base, 0) + 1);
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

    public static Map<String, List<WordCloudItem>> generarWordCloudPorCategoria(List<String> palabras) {
        Map<String, List<WordCloudItem>> resultadoPorCategoria = new HashMap<>();

        for (Map.Entry<String, List<String>> categoria : CATEGORIAS.entrySet()) {
            String nombreCategoria = categoria.getKey();
            List<String> palabrasCategoria = categoria.getValue();

            Map<String, Integer> contador = new HashMap<>();

            for (String palabraClave : palabrasCategoria) {
                String claveLower = palabraClave.toLowerCase();
                for (String palabra : palabras) {
                    if (palabra.toLowerCase().contains(claveLower)) {
                        contador.put(palabraClave, contador.getOrDefault(palabraClave, 0) + 1);
                    }
                }
            }

            List<WordCloudItem> items = contador.entrySet().stream()
                    .map(e -> {
                        int count = e.getValue();
                        if (count >= 1 && count <= 5) {
                            count += 10;
                        }
                        return new WordCloudItem(e.getKey(), count);
                    })
                    .collect(Collectors.toList());

            resultadoPorCategoria.put(nombreCategoria, items);
        }

        return resultadoPorCategoria;
    }

}
