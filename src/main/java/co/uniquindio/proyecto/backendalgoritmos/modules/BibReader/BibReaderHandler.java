package co.uniquindio.proyecto.backendalgoritmos.modules.BibReader;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.*;

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

        // Crea un conjunto para almacenar las claves únicas de los artículos.
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

                // Lee cada línea del archivo.
                while ((linea = br.readLine()) != null) {
                    // Agrega la línea a la entrada actual.
                    entradaActual.append(linea).append("\n");
                    // Verifica si la línea es el cierre de una entrada de artículo (}).
                    if (linea.equals("}")) { // Fin de una entrada
                        // Convierte la entrada actual a una cadena.
                        String entradaStr = entradaActual.toString();
                        // Genera una clave única para la entrada actual.
                        String claveUnica = generarClaveUnica(entradaStr); // Generar clave para la comparación

                        // Verifica si la clave única ya existe en el conjunto de entradas únicas.
                        if (!entradasUnicas.add(claveUnica)) {
                            // Si la clave ya existe, agrega la entrada a la lista de duplicados.
                            entradasDuplicadas.add(entradaStr);
                        } else {
                            // Si la clave es única, extrae la información del artículo y la agrega al StringBuilder de artículos generales.
                            articulosGenerales.append(extraerInfoArticulo(entradaStr));
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
        guardarArchivo(String.join("\n", entradasDuplicadas), archivoDuplicados);
    }

    // Genera una clave única para una entrada de artículo basada en el autor, título y año.
    private static String generarClaveUnica(String entrada) {
        // Extrae el valor del autor de la entrada.
        String autor = extraerValor(entrada, "author = \\{(.*?)\\}");
        // Extrae el valor del título de la entrada.
        String titulo = extraerValor(entrada, "title = \\{(.*?)\\}");
        // Extrae el valor del año de la entrada.
        String anio = extraerValor(entrada, "year = \\{(.*?)\\}");

        // Combina los valores de autor, título y año para crear la clave única.
        return autor + titulo + anio; // Combinación de campos clave para la comparación
    }

    // Extrae la información del artículo en un formato específico.
    private static String extraerInfoArticulo(String entrada) {
        // Extrae el valor del autor de la entrada.
        String autor = extraerValor(entrada, "author = \\{(.*?)\\}");
        // Extrae el valor del título de la entrada.
        String titulo = extraerValor(entrada, "title = \\{(.*?)\\}");
        // Extrae el valor del año de la entrada.
        String anio = extraerValor(entrada, "year = \\{(.*?)\\}");
        // Extrae el valor del resumen de la entrada.
        String resumen = extraerValor(entrada, "abstract = \\{(.*?)\\}");

        // Formatea la información del artículo en una cadena.
        return String.format("@article{\n  author = {%s},\n  title = {%s},\n  year = {%s},\n  abstract = {%s}\n}\n\n", autor, titulo, anio, resumen);
    }

    // Extrae un valor de una entrada de artículo utilizando una expresión regular.
    private static String extraerValor(String entrada, String regex) {
        // Compila la expresión regular en un patrón.
        Pattern patron = Pattern.compile(regex);
        // Crea un objeto Matcher para buscar coincidencias en la entrada.
        Matcher matcher = patron.matcher(entrada);
        // Verifica si se encontró una coincidencia.
        if (matcher.find()) {
            // Devuelve el valor capturado por el grupo 1 de la expresión regular.
            return matcher.group(1);
        }
        // Devuelve una cadena vacía si no se encontró el valor.
        return ""; // Devuelve vacío si no encuentra el valor
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
}