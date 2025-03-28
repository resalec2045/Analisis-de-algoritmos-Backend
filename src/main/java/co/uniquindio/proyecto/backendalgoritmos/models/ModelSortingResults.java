package co.uniquindio.proyecto.backendalgoritmos.models;

import java.util.List;

public class ModelSortingResults {
    private String author;
    private List<SortingResult> results;

    public ModelSortingResults(String author, List<SortingResult> results) {
        this.author = author;
        this.results = results;
    }

    public String getAuthor() {
        return author;
    }

    public List<SortingResult> getResults() {
        return results;
    }
}
