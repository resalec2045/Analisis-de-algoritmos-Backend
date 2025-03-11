package co.uniquindio.proyecto.backendalgoritmos.modules.BibReader;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.io.*;
import java.util.*;

public class BibReaderHandler {

    public static void execute() {
        String directorioActual = System.getProperty("user.dir");
        String bibFilePath = directorioActual + "/src/main/resources/co.uniquindio.proyecto.backendalgoritmos/";
        String archivoSalida = directorioActual + "/src/main/resources/co.uniquindio.proyecto.backendalgoritmos/articulos.bib";
        String archivoDuplicados = directorioActual + "/src/main/resources/co.uniquindio.proyecto.backendalgoritmos/duplicados.bib";

        procesarArchivosBibtex(bibFilePath, archivoSalida, archivoDuplicados);
    }

    public static void procesarArchivosBibtex(String directorio, String archivoSalida, String archivoDuplicados) {
        File carpeta = new File(directorio);
        File[] archivos = carpeta.listFiles((dir, name) -> name.toLowerCase().endsWith(".bib"));

        if (archivos == null || archivos.length == 0) {
            System.out.println("No se encontraron archivos .bib en el directorio especificado.");
            return;
        }

        Set<String> entradasUnicas = new HashSet<>();
        List<String> entradasDuplicadas = new ArrayList<>();
        StringBuilder articulosGenerales = new StringBuilder();

        for (File archivo : archivos) {
            try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
                String linea;
                StringBuilder entradaActual = new StringBuilder();

                while ((linea = br.readLine()) != null) {
                    entradaActual.append(linea).append("\n");
                    if (linea.equals("}")) { // Fin de una entrada
                        String entradaStr = entradaActual.toString();
                        String claveUnica = generarClaveUnica(entradaStr); // Generar clave para la comparación

                        if (!entradasUnicas.add(claveUnica)) {
                            entradasDuplicadas.add(entradaStr);
                        } else {
                            articulosGenerales.append(extraerInfoArticulo(entradaStr));
                        }

                        entradaActual.setLength(0); // Reiniciar para la siguiente entrada
                    }
                }
            } catch (IOException e) {
                System.err.println("Error al procesar el archivo " + archivo.getName() + ": " + e.getMessage());
            }
        }

        guardarArchivo(articulosGenerales.toString(), archivoSalida);
        guardarArchivo(String.join("\n", entradasDuplicadas), archivoDuplicados);
    }

    private static String generarClaveUnica(String entrada) {
        String autor = extraerValor(entrada, "author = \\{(.*?)\\}");
        String titulo = extraerValor(entrada, "title = \\{(.*?)\\}");
        String anio = extraerValor(entrada, "year = \\{(.*?)\\}");

        return autor + titulo + anio; // Combinación de campos clave para la comparación
    }

    private static String extraerInfoArticulo(String entrada) {
        String autor = extraerValor(entrada, "author = \\{(.*?)\\}");
        String titulo = extraerValor(entrada, "title = \\{(.*?)\\}");
        String anio = extraerValor(entrada, "year = \\{(.*?)\\}");
        String resumen = extraerValor(entrada, "abstract = \\{(.*?)\\}");

        return String.format("@article{\n  author = {%s},\n  title = {%s},\n  year = {%s},\n  abstract = {%s}\n}\n\n", autor, titulo, anio, resumen);
    }

    private static String extraerValor(String entrada, String regex) {
        Pattern patron = Pattern.compile(regex);
        Matcher matcher = patron.matcher(entrada);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return ""; // Devuelve vacío si no encuentra el valor
    }

    private static void guardarArchivo(String contenido, String nombreArchivo) {
        try (FileWriter fw = new FileWriter(nombreArchivo)) {
            fw.write(contenido);
        } catch (IOException e) {
            System.err.println("Error al guardar el archivo " + nombreArchivo + ": " + e.getMessage());
        }
    }
}