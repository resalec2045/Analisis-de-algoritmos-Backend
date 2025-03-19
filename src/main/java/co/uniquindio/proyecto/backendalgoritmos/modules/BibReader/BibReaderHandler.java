package co.uniquindio.proyecto.backendalgoritmos.modules.BibReader;

import java.io.*;
import java.util.*;
import java.util.regex.*;

public class BibReaderHandler {

    public static void execute() {
        // Obtiene el directorio de trabajo actual del usuario.
        String directorioActual = System.getProperty("user.dir");
        // Construye la ruta al directorio donde se encuentran los archivos .bib.
        String bibFilePath = directorioActual + "/src/main/resources/co.uniquindio.proyecto.backendalgoritmos/";
        // Construye la ruta al archivo de salida donde se guardarán los artículos únicos.
        String archivoSalida = directorioActual + "/src/main/resources/co.uniquindio.proyecto.backendalgoritmos/articulos.bib";
        // Construye la ruta al archivo de salida donde se guardarán los artículos duplicados.
        String archivoDuplicados = directorioActual + "/src/main/resources/co.uniquindio.proyecto.backendalgoritmos/duplicados.bib";

        // Llama al método para procesar los archivos .bib en el directorio especificado.
        procesarArchivosBibtex(bibFilePath, archivoSalida, archivoDuplicados);
    }

    public static void procesarArchivosBibtex(String directorio, String archivoSalida, String archivoDuplicados) {
        // Crea un objeto File para el directorio especificado.
        File carpeta = new File(directorio);
        // Obtiene una lista de archivos .bib en el directorio.
        File[] archivos = carpeta.listFiles((dir, name) -> name.toLowerCase().endsWith(".bib"));

        // Verifica si no se encontraron archivos .bib en el directorio.
        if (archivos == null || archivos.length == 0) {
            System.out.println("No se encontraron archivos .bib en el directorio especificado.");
            return;
        }

        // Crea un conjunto para almacenar las entradas completas únicas de los artículos.
        Set<String> entradasUnicas = new HashSet<>();
        // Crea una lista para almacenar los artículos duplicados.
        List<String> entradasDuplicadas = new ArrayList<>();
        // Crea un StringBuilder para construir el contenido del archivo de salida de artículos generales.
        StringBuilder articulosGenerales = new StringBuilder();

        // Itera sobre cada archivo .bib encontrado.
        for (File archivo : archivos) {
            // Intenta leer el archivo.
            try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
                String linea;
                // Crea un StringBuilder para construir la entrada actual del artículo.
                StringBuilder entradaActual = new StringBuilder();
                String tituloArticulo = null; // Variable para almacenar el título del artículo

                // Lee cada línea del archivo.
                while ((linea = br.readLine()) != null) {
                    // Agrega la línea a la entrada actual.
                    entradaActual.append(linea).append("\n");

                    // Verifica si la línea es el cierre de una entrada de artículo (}).
                    if (linea.equals("}")) { // Fin de una entrada
                        // Si se encontró una entrada completa, verifica si ya está registrada
                        String entradaCompleta = entradaActual.toString().trim();

                        // Si la entrada ya existe, es un duplicado.
                        if (!entradasUnicas.add(entradaCompleta)) {
                            // Si el artículo es un duplicado, lo agregamos a la lista de duplicados.
                            entradasDuplicadas.add(entradaCompleta);
                        } else {
                            // Si la entrada es única, agregamos el artículo a la lista de artículos generales.
                            articulosGenerales.append(entradaCompleta).append("\n");
                        }

                        // Reinicia el StringBuilder para la siguiente entrada.
                        entradaActual.setLength(0); // Reiniciar para la siguiente entrada
                    }
                }
            } catch (IOException e) {
                // Maneja la excepción si ocurre un error al leer el archivo.
                System.err.println("Error al procesar el archivo " + archivo.getName() + ": " + e.getMessage());
            }
        }

        // Guarda el contenido de artículos generales en el archivo de salida.
        guardarArchivo(articulosGenerales.toString(), archivoSalida);
        // Guarda el contenido de artículos duplicados en el archivo de duplicados.
        guardarArchivoDuplicados(entradasDuplicadas, archivoDuplicados);
    }

    // Guarda el contenido en un archivo con el nombre especificado.
    private static void guardarArchivo(String contenido, String nombreArchivo) {
        // Intenta escribir el contenido en el archivo.
        try (FileWriter fw = new FileWriter(nombreArchivo)) {
            fw.write(contenido);
        } catch (IOException e) {
            // Maneja la excepción si ocurre un error al escribir el archivo.
            System.err.println("Error al guardar el archivo " + nombreArchivo + ": " + e.getMessage());
        }
    }

    // Método para guardar los artículos duplicados en un archivo específico.
    private static void guardarArchivoDuplicados(List<String> entradasDuplicadas, String archivoDuplicados) {
        try (FileWriter fw = new FileWriter(archivoDuplicados)) {
            for (String entrada : entradasDuplicadas) {
                fw.write(entrada + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error al guardar el archivo de duplicados " + archivoDuplicados + ": " + e.getMessage());
        }
    }
}
