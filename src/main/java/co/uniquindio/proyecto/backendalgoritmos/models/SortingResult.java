package co.uniquindio.proyecto.backendalgoritmos.models;

public class SortingResult {
    private String algorithm;
    private long time;

    public SortingResult(String algorithm, long time) {
        this.algorithm = algorithm;
        this.time = time;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public long getTime() {
        return time;
    }
}

