package co.uniquindio.proyecto.backendalgoritmos.helpers.Agrupamiento;

import java.util.ArrayList;
import java.util.List;

public class Cluster {
    private String name;
    private List<Cluster> children = new ArrayList<>();

    public Cluster(String name) {
        this.name = name;
    }

    public void addChild(Cluster child) {
        children.add(child);
    }

    public String getName() {
        return name;
    }

    public List<Cluster> getChildren() {
        return children;
    }
}
