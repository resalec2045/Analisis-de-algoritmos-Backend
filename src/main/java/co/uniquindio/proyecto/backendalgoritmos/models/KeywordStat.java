package co.uniquindio.proyecto.backendalgoritmos.models;

public class KeywordStat {
    private String palabra;
    private int cantidad;

    public KeywordStat(String palabra, int cantidad) {
        this.palabra = palabra;
        this.cantidad = cantidad;
    }

    public String getPalabra() {
        return palabra;
    }

    public int getCantidad() {
        return cantidad;
    }

    @Override
    public String toString() {
        return palabra + " = " + cantidad;
    }
}
