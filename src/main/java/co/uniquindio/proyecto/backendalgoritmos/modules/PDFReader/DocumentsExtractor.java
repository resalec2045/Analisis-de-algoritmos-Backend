package co.uniquindio.proyecto.backendalgoritmos.modules.PDFReader; // Declaración del paquete donde se encuentra la clase DocumentsExtractor

import co.uniquindio.proyecto.backendalgoritmos.models.DocumentsProperties; // Importa la clase DocumentsProperties, que se usa para almacenar los datos extraídos

import java.io.BufferedReader; // Importa la clase BufferedReader para leer archivos de texto de manera eficiente
import java.io.FileReader; // Importa la clase FileReader para leer el archivo desde el sistema de archivos
import java.io.IOException; // Importa la clase IOException para manejar excepciones de entrada y salida
import java.util.ArrayList; // Importa la clase ArrayList para crear listas dinámicas
import java.util.List; // Importa la interfaz List que se utiliza para declarar listas
import java.util.regex.Matcher; // Importa la clase Matcher que permite realizar coincidencias con expresiones regulares
import java.util.regex.Pattern; // Importa la clase Pattern que permite compilar expresiones regulares

public class DocumentsExtractor { // Definición de la clase DocumentsExtractor

    public static List<DocumentsProperties> readBibFile(String bibFilePath) { // Método estático para leer el archivo BibTeX desde la ruta proporcionada
        List<DocumentsProperties> articles = new ArrayList<>(); // Crea una lista de objetos DocumentsProperties para almacenar los artículos extraídos

        try (BufferedReader reader = new BufferedReader(new FileReader(bibFilePath))) { // Usa BufferedReader para leer el archivo línea por línea
            String line; // Variable para almacenar cada línea leída
            StringBuilder articleContent = new StringBuilder(); // StringBuilder para almacenar el contenido de cada artículo

            while ((line = reader.readLine()) != null) { // Lee cada línea del archivo mientras haya contenido
                line = line.trim(); // Elimina espacios en blanco al principio y al final de la línea
                if (line.startsWith("@article")) { // Si la línea empieza con "@article", indica el inicio de un artículo
                    if (articleContent.length() > 0) { // Si ya había contenido en articleContent, procesa el artículo anterior
                        DocumentsProperties article = parseArticle(articleContent.toString()); // Llama a parseArticle para procesar el contenido del artículo
                        if (article != null) { // Si el artículo es válido, lo agrega a la lista
                            articles.add(article);
                        }
                    }
                    articleContent.setLength(0); // Resetea el StringBuilder para comenzar con el nuevo artículo
                    articleContent.append(line).append("\n"); // Agrega la línea actual al contenido del artículo
                } else {
                    articleContent.append(line).append("\n"); // Si la línea no es el inicio de un artículo, agrega la línea al contenido del artículo
                }
            }

            if (articleContent.length() > 0) { // Después de leer todas las líneas, si queda contenido en articleContent, lo procesa
                DocumentsProperties article = parseArticle(articleContent.toString()); // Procesa el último artículo
                if (article != null) { // Si es válido, lo agrega a la lista
                    articles.add(article);
                }
            }

        } catch (IOException e) { // Maneja las excepciones de entrada/salida
            e.printStackTrace(); // Imprime la traza de la excepción
        }

        return articles; // Devuelve la lista de artículos extraídos
    }

    private static DocumentsProperties parseArticle(String articleContent) { // Método privado para procesar el contenido de un artículo y extraer sus propiedades
        DocumentsProperties article = new DocumentsProperties(); // Crea un nuevo objeto DocumentsProperties para almacenar las propiedades del artículo

        article.setAuthor(extractAttribute(articleContent, "author")); // Extrae el autor del artículo
        article.setTitle(extractAttribute(articleContent, "title")); // Extrae el título del artículo
        article.setYear(extractYear(articleContent)); // Extrae el año del artículo
        article.setLocation(extractAttribute(articleContent, "location")); // Extrae la ubicación del artículo
        article.setAbstractDescription(extractAttribute(articleContent, "abstract")); // Extrae el resumen del artículo

//        if (article.getTitle() == null) { // Comentado: Si el artículo no tiene título, no se procesaría
//            return null;
//        }

        return article; // Devuelve el artículo con las propiedades extraídas
    }

    private static String extractAttribute(String articleContent, String attributeName) { // Método privado para extraer un atributo específico de un artículo dado su nombre
        String regex = attributeName + "\\s*=\\s*\\{(.*?)\\}"; // Crea una expresión regular para encontrar el atributo en el formato "attributeName = {value}"
        Pattern pattern = Pattern.compile(regex); // Compila la expresión regular
        Matcher matcher = pattern.matcher(articleContent); // Crea un matcher para buscar coincidencias en el contenido del artículo

        if (matcher.find()) { // Si se encuentra una coincidencia
            return matcher.group(1).trim(); // Devuelve el valor del atributo, eliminando los espacios en blanco
        }
        return null; // Si no se encuentra el atributo, devuelve null
    }

    private static int extractYear(String articleContent) { // Método privado para extraer el año de publicación del artículo
        String yearString = extractAttribute(articleContent, "year"); // Llama a extractAttribute para obtener el valor del año
        if (yearString != null) { // Si se obtuvo un valor para el año
            try {
                return Integer.parseInt(yearString); // Intenta convertir el valor a un número entero
            } catch (NumberFormatException e) { // Si ocurre un error al convertir, captura la excepción
                return 0; // Si no se puede convertir, devuelve 0 como valor predeterminado
            }
        }
        return 0; // Si no se obtuvo un año, devuelve 0
    }
}
