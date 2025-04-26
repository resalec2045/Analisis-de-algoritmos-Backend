package co.uniquindio.proyecto.backendalgoritmos.modules.ProcesamientoTexto;

import java.util.*;

public class TreeConverter {

    public static Map<String, Object> convertirClusterAReactD3Tree(Cluster cluster) {
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
