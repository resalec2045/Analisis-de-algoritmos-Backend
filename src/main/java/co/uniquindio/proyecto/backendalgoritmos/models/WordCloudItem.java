package co.uniquindio.proyecto.backendalgoritmos.models;

public class WordCloudItem {
    private String text;
    private int value;

    public WordCloudItem(String text, int value) {
        this.text = text;
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public int getValue() {
        return value;
    }
}
