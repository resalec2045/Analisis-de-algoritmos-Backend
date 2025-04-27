package co.uniquindio.proyecto.backendalgoritmos.helpers;

import java.util.*;

public class AbstractAnalyzer {

    public static Map<String, Object> analizarAbstracts(List<String> palabrasProcesadas) {
        Map<String, Object> respuesta = new LinkedHashMap<>();
        List<Map<String, Object>> categoriasRespuesta = new ArrayList<>();

        // Definimos directamente las categorias, variables y sinonimos
        Map<String, Map<String, List<String>>> categoriasVariables = new LinkedHashMap<>();

        // Habilidades
        categoriasVariables.put("Habilidades", Map.ofEntries(
                Map.entry("Abstraction", List.of("Abstraction")),
                Map.entry("Algorithm", List.of("Algorithm")),
                Map.entry("Algorithmic thinking", List.of("Algorithmic thinking")),
                Map.entry("Coding", List.of("Coding")),
                Map.entry("Collaboration", List.of("Collaboration")),
                Map.entry("Cooperation", List.of("Cooperation")),
                Map.entry("Creativity", List.of("Creativity")),
                Map.entry("Critical thinking", List.of("Critical thinking")),
                Map.entry("Debug", List.of("Debug")),
                Map.entry("Decomposition", List.of("Decomposition")),
                Map.entry("Evaluation", List.of("Evaluation")),
                Map.entry("Generalization", List.of("Generalization")),
                Map.entry("Logic", List.of("Logic")),
                Map.entry("Logical thinking", List.of("Logical thinking")),
                Map.entry("Modularity", List.of("Modularity")),
                Map.entry("Patterns recognition", List.of("Patterns recognition")),
                Map.entry("Problem solving", List.of("Problem solving")),
                Map.entry("Programming", List.of("Programming"))
        ));

        // Conceptos Computacionales
        categoriasVariables.put("Conceptos Computacionales", Map.ofEntries(
                Map.entry("Conditionals", List.of("Conditionals")),
                Map.entry("Control structures", List.of("Control structures")),
                Map.entry("Directions", List.of("Directions")),
                Map.entry("Events", List.of("Events")),
                Map.entry("Functions", List.of("Functions", "Funtions")),
                Map.entry("Loops", List.of("Loops")),
                Map.entry("Modular structure", List.of("Modular structure")),
                Map.entry("Parallelism", List.of("Parallelism")),
                Map.entry("Sequences", List.of("Sequences")),
                Map.entry("Software/hardware", List.of("Software/hardware")),
                Map.entry("Variables", List.of("Variables"))
        ));

        // Actitudes
        categoriasVariables.put("Actitudes", Map.ofEntries(
                Map.entry("Emotional", List.of("Emotional")),
                Map.entry("Engagement", List.of("Engagement")),
                Map.entry("Motivation", List.of("Motivation")),
                Map.entry("Perceptions", List.of("Perceptions")),
                Map.entry("Persistence", List.of("Persistence")),
                Map.entry("Self-efficacy", List.of("Self-efficacy")),
                Map.entry("Self-perceived", List.of("Self-perceived"))
        ));

        // Propiedades Psicométricas
        categoriasVariables.put("Propiedades Psicométricas", Map.ofEntries(
                Map.entry("Classical Test Theory", List.of("Classical Test Theory", "CTT")),
                Map.entry("Confirmatory Factor Analysis", List.of("Confirmatory Factor Analysis", "CFA")),
                Map.entry("Exploratory Factor Analysis", List.of("Exploratory Factor Analysis", "EFA")),
                Map.entry("Item Response Theory", List.of("Item Response Theory", "IRT")),
                Map.entry("Reliability", List.of("Reliability")),
                Map.entry("Structural Equation Model", List.of("Structural Equation Model", "SEM")),
                Map.entry("Validity", List.of("Validity"))
        ));

        // Herramienta de Evaluación
        categoriasVariables.put("Herramienta de Evaluación", Map.ofEntries(
                Map.entry("Beginners Computational Thinking test", List.of("Beginners Computational Thinking test", "BCTt")),
                Map.entry("Coding Attitudes Survey", List.of("Coding Attitudes Survey", "ESCAS")),
                Map.entry("Computational Thinking Assessment for Chinese Elementary Students", List.of("Computational Thinking Assessment for Chinese Elementary Students", "CTA-CES")),
                Map.entry("Computational Thinking Scale", List.of("Computational Thinking Scale", "CTS"))
        ));

        // Nivel Escolaridad
        categoriasVariables.put("Nivel de Escolaridad", Map.ofEntries(
                Map.entry("Upper elementary school", List.of("Upper elementary education", "Upper elementary school")),
                Map.entry("Primary school", List.of("Primary school", "Primary education", "Elementary school")),
                Map.entry("Early childhood education", List.of("Early childhood education", "Kindergarten", "Preschool")),
                Map.entry("Secondary school", List.of("Secondary school", "Secondary education")),
                Map.entry("High school", List.of("high school", "higher education")),
                Map.entry("University", List.of("University", "College"))
        ));

        // Procesamiento de palabras
        for (Map.Entry<String, Map<String, List<String>>> categoriaEntry : categoriasVariables.entrySet()) {
            String categoria = categoriaEntry.getKey();
            Map<String, List<String>> variables = categoriaEntry.getValue();

            Map<String, Integer> contador = new LinkedHashMap<>();
            Map<String, String> sinonimoUsado = new LinkedHashMap<>();

            for (String texto : palabrasProcesadas) {
                String textoLower = texto.toLowerCase();

                for (Map.Entry<String, List<String>> variableEntry : variables.entrySet()) {
                    String palabraPrincipal = variableEntry.getKey();
                    List<String> sinonimos = variableEntry.getValue();

                    for (String sinonimo : sinonimos) {
                        if (textoLower.contains(sinonimo.toLowerCase())) {
                            contador.put(palabraPrincipal, contador.getOrDefault(palabraPrincipal, 0) + 1);
                            sinonimoUsado.putIfAbsent(palabraPrincipal, sinonimo);
                            break;
                        }
                    }
                }
            }

            List<Map<String, Object>> resultados = new ArrayList<>();
            int totalParametros = 0;
            for (Map.Entry<String, Integer> entry : contador.entrySet()) {
                Map<String, Object> registro = new LinkedHashMap<>();
                registro.put("word", entry.getKey());
                registro.put("synonymUsed", sinonimoUsado.get(entry.getKey()));
                registro.put("count", entry.getValue());
                resultados.add(registro);
                totalParametros += entry.getValue();
            }

            if (!resultados.isEmpty()) {
                Map<String, Object> categoriaData = new LinkedHashMap<>();
                categoriaData.put("author", categoria);
                categoriaData.put("results", resultados);
                categoriaData.put("cantParametros", totalParametros);
                categoriasRespuesta.add(categoriaData);
            }
        }

        respuesta.put("categorias", categoriasRespuesta);
        return respuesta;
    }
}