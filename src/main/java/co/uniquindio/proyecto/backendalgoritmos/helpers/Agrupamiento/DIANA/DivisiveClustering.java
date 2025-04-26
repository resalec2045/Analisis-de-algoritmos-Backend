package co.uniquindio.proyecto.backendalgoritmos.helpers.Agrupamiento.DIANA;

import co.uniquindio.proyecto.backendalgoritmos.helpers.Agrupamiento.Cluster;
import java.util.*;

public class DivisiveClustering {

    public static Map<String, Object> dividirPalabras(List<String> palabras) {
        Cluster raiz = new Cluster("Grupo");

        // 1. Agrupar palabras por prefijo común
        Map<String, List<String>> agrupaciones = agruparPorPrefijo(palabras);

        // 2. Crear clusters de cada prefijo
        for (Map.Entry<String, List<String>> entry : agrupaciones.entrySet()) {
            String prefijo = entry.getKey();
            List<String> grupoPalabras = entry.getValue();

            Cluster subgrupo;
            if (grupoPalabras.size() > 1) {
                subgrupo = new Cluster(prefijo);
                for (String palabra : grupoPalabras) {
                    subgrupo.addChild(new Cluster(palabra));
                }
                dividirRecursivamente(subgrupo); // ⚡ Aplicar división solo si tiene más de 1
            } else {
                subgrupo = new Cluster(grupoPalabras.get(0));
            }
            raiz.addChild(subgrupo);
        }

        return convertirClusterAReactD3Tree(raiz);
    }

    // Agrupar palabras que compartan al menos 3 letras iniciales
    private static Map<String, List<String>> agruparPorPrefijo(List<String> palabras) {
        Map<String, List<String>> grupos = new HashMap<>();

        for (String palabra : palabras) {
            boolean agrupado = false;
            for (String prefijoExistente : grupos.keySet()) {
                if (palabra.startsWith(prefijoExistente)) {
                    grupos.get(prefijoExistente).add(palabra);
                    agrupado = true;
                    break;
                }
            }
            if (!agrupado) {
                String nuevoPrefijo = palabra.length() >= 3 ? palabra.substring(0, 3) : palabra;
                grupos.put(nuevoPrefijo, new ArrayList<>(List.of(palabra)));
            }
        }
        return grupos;
    }

    private static void dividirRecursivamente(Cluster cluster) {
        if (cluster.getChildren() == null || cluster.getChildren().size() <= 2) {
            return;
        }

        List<Cluster> palabras = new ArrayList<>(cluster.getChildren());
        cluster.getChildren().clear();

        Cluster referencia = encontrarMasDiferente(palabras);

        List<Cluster> subgrupo = new ArrayList<>();
        List<Cluster> grupo = new ArrayList<>();

        subgrupo.add(referencia);

        for (Cluster palabra : palabras) {
            if (palabra == referencia) continue;

            double distanciaAReferencia = distanciaEntreClusters(palabra, referencia);

            if (distanciaAReferencia < 0.5) {
                subgrupo.add(palabra);
            } else {
                grupo.add(palabra);
            }
        }

        // 💥 AGREGAR ESTE CORTE
        if (subgrupo.size() == palabras.size() || subgrupo.size() == 0) {
            // Si no hubo separación real, no seguir dividiendo
            cluster.getChildren().addAll(palabras);
            return;
        }

        if (subgrupo.size() > 1) {
            Cluster nuevoSubgrupo = new Cluster("Subgrupo");
            nuevoSubgrupo.getChildren().addAll(subgrupo);
            cluster.addChild(nuevoSubgrupo);
        } else {
            cluster.addChild(subgrupo.get(0));
        }

        for (Cluster palabra : grupo) {
            cluster.addChild(palabra);
        }

        for (Cluster hijo : cluster.getChildren()) {
            if (hijo.getChildren() != null && hijo.getChildren().size() > 1) {
                dividirRecursivamente(hijo);
            }
        }
    }


    private static Cluster encontrarMasDiferente(List<Cluster> clusters) {
        Cluster elegido = null;
        double peorSimilitud = Double.MAX_VALUE;

        for (Cluster candidato : clusters) {
            double sumaDistancias = 0.0;
            for (Cluster otro : clusters) {
                if (candidato != otro) {
                    sumaDistancias += distanciaEntreClusters(candidato, otro);
                }
            }
            double promedio = sumaDistancias / (clusters.size() - 1);
            if (promedio < peorSimilitud) {
                peorSimilitud = promedio;
                elegido = candidato;
            }
        }
        return elegido;
    }

    private static double distanciaEntreClusters(Cluster a, Cluster b) {
        return distanciaBasica(a.getName(), b.getName());
    }

    private static double distanciaBasica(String a, String b) {
        if (a == null || b == null) return 1.0;
        if (a.equalsIgnoreCase(b)) return 0.0;

        int minLength = Math.min(a.length(), b.length());
        int matches = 0;
        for (int i = 0; i < minLength; i++) {
            if (a.charAt(i) == b.charAt(i)) {
                matches++;
            }
        }
        return 1.0 - ((double) matches / minLength);
    }

    private static Map<String, Object> convertirClusterAReactD3Tree(Cluster cluster) {
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
