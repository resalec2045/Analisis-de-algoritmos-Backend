package co.uniquindio.proyecto.backendalgoritmos.modules.ProcesamientoTexto;

import java.util.*;

public class ClusteringService {

    private static final Map<String, Set<String>> RELACIONES_SEMANTICAS = Map.ofEntries(
            Map.entry("learn", Set.of("study", "education")),
            Map.entry("study", Set.of("learn", "education")),
            Map.entry("education", Set.of("learn", "study", "teaching")),
            Map.entry("computational", Set.of("computing", "computation")),
            Map.entry("computing", Set.of("computational", "computation")),
            Map.entry("computation", Set.of("computing", "computational")),
            Map.entry("teach", Set.of("education", "teaching")),
            Map.entry("teaching", Set.of("teach", "education"))
    );

    private static final Set<String> VERBOS_COMUNES = Set.of(
            "is", "are", "was", "were", "be", "been", "being",
            "have", "has", "had", "do", "does", "did",
            "can", "could", "will", "would", "shall", "should",
            "may", "might", "must", "ought",
            "make", "makes", "made",
            "use", "uses", "used",
            "create", "creates", "created",
            "provide", "provides", "provided",
            "apply", "applies", "applied",
            "consider", "considers", "considered"
    );

    public Map<String, Object> clusteringJerarquicoPalabras(List<String> palabras) {
        List<String> palabrasFiltradas = palabras.stream()
                .filter(p -> !VERBOS_COMUNES.contains(p))
                .toList();

        List<Cluster> clusters = new ArrayList<>();
        for (String palabra : palabrasFiltradas) {
            clusters.add(new Cluster(palabra));
        }

        while (clusters.size() > 1) {
            double mejorDistancia = Double.MAX_VALUE;
            int mejorI = 0;
            int mejorJ = 1;

            for (int i = 0; i < clusters.size(); i++) {
                for (int j = i + 1; j < clusters.size(); j++) {
                    double distancia = distanciaClusters(clusters.get(i), clusters.get(j));
                    if (distancia < mejorDistancia) {
                        mejorDistancia = distancia;
                        mejorI = i;
                        mejorJ = j;
                    }
                }
            }

            String nombreNuevo = calcularNombreCluster(clusters.get(mejorI), clusters.get(mejorJ));
            Cluster nuevo = new Cluster(nombreNuevo);
            nuevo.addChild(clusters.get(mejorI));
            nuevo.addChild(clusters.get(mejorJ));

            clusters.remove(mejorJ);
            clusters.remove(mejorI);

            clusters.add(nuevo);
        }

        return convertirClusterAReactD3Tree(clusters.get(0));
    }

    private String calcularNombreCluster(Cluster c1, Cluster c2) {
        String nombre1 = encontrarPrimerNombre(c1);
        String nombre2 = encontrarPrimerNombre(c2);

        if (sonSemanticamenteRelacionadas(nombre1, nombre2)) {
            return nombre1 + "-" + nombre2;
        }

        String prefijo = prefijoComun(nombre1, nombre2);

        if (!prefijo.isEmpty()) {
            return prefijo;
        } else {
            return nombre1 + "-" + nombre2;
        }
    }

    private double distanciaClusters(Cluster c1, Cluster c2) {
        String nombre1 = encontrarPrimerNombre(c1);
        String nombre2 = encontrarPrimerNombre(c2);

        if (nombre1.equals(nombre2)) {
            return 0.0;
        }

        if (sonSemanticamenteRelacionadas(nombre1, nombre2)) {
            return 0.2;
        }

        String prefijo = prefijoComun(nombre1, nombre2);
        if (!prefijo.isEmpty() && prefijo.length() >= 3) {
            return 0.4;
        }

        return 1.0;
    }

    private boolean sonSemanticamenteRelacionadas(String palabra1, String palabra2) {
        Set<String> relaciones1 = RELACIONES_SEMANTICAS.getOrDefault(palabra1, Set.of());
        Set<String> relaciones2 = RELACIONES_SEMANTICAS.getOrDefault(palabra2, Set.of());

        return relaciones1.contains(palabra2) || relaciones2.contains(palabra1);
    }

    private String prefijoComun(String a, String b) {
        int minLength = Math.min(a.length(), b.length());
        int i = 0;
        while (i < minLength && a.charAt(i) == b.charAt(i)) {
            i++;
        }
        return a.substring(0, i);
    }

    private String encontrarPrimerNombre(Cluster cluster) {
        if (cluster.getChildren() == null || cluster.getChildren().isEmpty()) {
            return cluster.getName();
        }
        return encontrarPrimerNombre(cluster.getChildren().get(0));
    }

    private Map<String, Object> convertirClusterAReactD3Tree(Cluster cluster) {
        Map<String, Object> nodo = new HashMap<>();
        nodo.put("name", cluster.getName());

        if (cluster.getChildren() != null && !cluster.getChildren().isEmpty()) {
            List<Map<String, Object>> hijos = new ArrayList<>();
            for (Cluster hijo : cluster.getChildren()) {
                hijos.add(convertirClusterAReactD3Tree(hijo));
            }
            nodo.put("children", hijos);
        }

        return nodo;
    }
}
