package co.uniquindio.proyecto.backendalgoritmos.models;

import java.util.List;

public class ModelSortingResults {
    private String author;
    private List<SortingResult> results;
    private int cantParametros;

    public ModelSortingResults(String author, List<SortingResult> results, int cantParametros) {
        this.author = author;
        this.results = results;
        this.cantParametros = cantParametros;
    }

    public String getAuthor() {
        return author;
    }

    public List<SortingResult> getResults() {
        return results;
    }

    public int getCantParametros() {
        return cantParametros;
    }
}
