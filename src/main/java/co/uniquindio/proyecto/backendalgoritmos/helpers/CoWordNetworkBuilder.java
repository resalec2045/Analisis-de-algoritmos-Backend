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

        // ⚡ Usamos solo la mitad de las palabras
//        TODO: Cambiar el tamaño de la lista de palabras a la mitad :v
        int mitad = palabras.size() / 500;
        List<String> palabrasUsadas = palabras.subList(0, mitad);

        Map<String, Integer> palabraToId = new HashMap<>();
        int idCounter = 1;

        // Asignamos un ID a cada palabra usada
        for (String palabra : palabrasUsadas) {
            palabraToId.put(palabra, idCounter++);
        }

        // Creamos los nodos
        for (String palabra : palabraToId.keySet()) {
            Map<String, Object> node = new HashMap<>();
            node.put("id", palabraToId.get(palabra));
            node.put("label", palabra);
            nodes.add(node);
        }

        // ⚡ Mapa para contar conexiones únicas
        Map<String, Integer> edgeCounter = new HashMap<>();

        // Ahora, procesamos abstract por abstract
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

                    String key = idA < idB ? idA + "-" + idB : idB + "-" + idA; // evitar duplicados

                    edgeCounter.put(key, edgeCounter.getOrDefault(key, 0) + 1);
                }
            }
        }

        // Construimos las edges
        for (String key : edgeCounter.keySet()) {
            String[] ids = key.split("-");
            Map<String, Object> edge = new HashMap<>();
            edge.put("from", Integer.parseInt(ids[0]));
            edge.put("to", Integer.parseInt(ids[1]));
            edges.add(edge);
        }

        resultado.put("nodes", nodes);
        resultado.put("edges", edges);

        return resultado;
    }
}
