import com.apporiented.algorithm.clustering.Cluster;
import java.util.*;

public class DivisiveClustering {

    public static Cluster dividir(List<List<String>> documentos) {
        if (documentos.size() == 1) {
            return new Cluster("Doc1");
        }

        Cluster raiz = new Cluster("Grupo");

        dividirRecursivo(documentos, raiz);

        return raiz;
    }

    private static void dividirRecursivo(List<List<String>> documentos, Cluster padre) {
        if (documentos.size() <= 1) {
            padre.addChild(new Cluster("Doc"));
            return;
        }

        // Elegir un pivote aleatorio
        List<String> pivote = documentos.get(0);

        List<List<String>> grupo1 = new ArrayList<>();
        List<List<String>> grupo2 = new ArrayList<>();

        for (List<String> doc : documentos) {
            double sim = calcularSimilitud(pivote, doc);
            if (sim > 0.5) {
                grupo1.add(doc);
            } else {
                grupo2.add(doc);
            }
        }

        if (!grupo1.isEmpty()) {
            Cluster hijo1 = new Cluster("Subgrupo1");
            padre.addChild(hijo1);
            dividirRecursivo(grupo1, hijo1);
        }

        if (!grupo2.isEmpty()) {
            Cluster hijo2 = new Cluster("Subgrupo2");
            padre.addChild(hijo2);
            dividirRecursivo(grupo2, hijo2);
        }
    }

    private static double calcularSimilitud(List<String> doc1, List<String> doc2) {
        Set<String> union = new HashSet<>(doc1);
        union.addAll(doc2);

        int interseccion = 0;
        for (String palabra : doc1) {
            if (doc2.contains(palabra)) {
                interseccion++;
            }
        }

        return (double) interseccion / union.size();
    }
}
