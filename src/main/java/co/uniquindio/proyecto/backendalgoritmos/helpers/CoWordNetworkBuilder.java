package co.uniquindio.proyecto.backendalgoritmos.helpers;
import java.util.*;

public class CoWordNetworkBuilder {

    public static Map<String, Object> construirCoWordNetworkMitad(List<String> abstracts, List<String> palabras) {
        Map<String, Object> resultado = new HashMap<>();
        List<Map<String, Object>> nodes = new ArrayList<>();
        List<Map<String, Object>> edges = new ArrayList<>();

        if (palabras.isEmpty() || abstracts.isEmpty()) {
            resultado.put("nodes", nodes);
            resultado.put("edges", edges);
            return resultado;
        }

        // Tomamos solo una fracción de las palabras
        int mitad = palabras.size() / 500;
        List<String> palabrasUsadas = palabras.subList(0, mitad);

        Map<String, Integer> palabraToId = new HashMap<>();
        int idCounter = 1;
        for (String palabra : palabrasUsadas) {
            palabraToId.put(palabra, idCounter++);
        }

        for (String palabra : palabraToId.keySet()) {
            Map<String, Object> node = new HashMap<>();
            node.put("id", palabraToId.get(palabra));
            node.put("label", palabra);
            nodes.add(node);
        }

        Map<String, Integer> edgeCounter = new HashMap<>();

        for (String abstracto : abstracts) {
            Set<String> palabrasEnAbstract = new HashSet<>();
            String abstractLower = abstracto.toLowerCase();

            for (String palabra : palabrasUsadas) {
                if (abstractLower.contains(palabra.toLowerCase())) {
                    palabrasEnAbstract.add(palabra);
                }
            }

            List<String> listaPalabras = new ArrayList<>(palabrasEnAbstract);

            for (int i = 0; i < listaPalabras.size(); i++) {
                for (int j = i + 1; j < listaPalabras.size(); j++) {
                    String palabraA = listaPalabras.get(i);
                    String palabraB = listaPalabras.get(j);

                    int idA = palabraToId.get(palabraA);
                    int idB = palabraToId.get(palabraB);

                    String key = idA < idB ? idA + "-" + idB : idB + "-" + idA;
                    edgeCounter.put(key, edgeCounter.getOrDefault(key, 0) + 1);
                }
            }
        }
        // 🧠 Nuevo: Limitar a 2 relaciones por palabra
        Map<Integer, Integer> conexionesPorNodo = new HashMap<>();

        for (String key : edgeCounter.keySet()) {
            String[] ids = key.split("-");
            int from = Integer.parseInt(ids[0]);
            int to = Integer.parseInt(ids[1]);

            int countFrom = conexionesPorNodo.getOrDefault(from, 0);
            int countTo = conexionesPorNodo.getOrDefault(to, 0);

            if (countFrom < 4 && countTo < 4) {
                Map<String, Object> edge = new HashMap<>();
                edge.put("from", from);
                edge.put("to", to);
                edges.add(edge);

                conexionesPorNodo.put(from, countFrom + 1);
                conexionesPorNodo.put(to, countTo + 1);
            }
        }

        resultado.put("nodes", nodes);
        resultado.put("edges", edges);

        return resultado;
    }


}
