package co.uniquindio.proyecto.backendalgoritmos.modules.DocuemntsExtractor; // Declaración del paquete donde se encuentra la clase DocumentsExtractor

import co.uniquindio.proyecto.backendalgoritmos.models.DocumentsProperties; // Importa la clase DocumentsProperties, que se usa para almacenar los datos extraídos

import java.io.BufferedReader; // Importa la clase BufferedReader para leer archivos de texto de manera eficiente
import java.io.FileReader; // Importa la clase FileReader para leer el archivo desde el sistema de archivos
import java.io.IOException; // Importa la clase IOException para manejar excepciones de entrada y salida
import java.util.ArrayList; // Importa la clase ArrayList para crear listas dinámicas
import java.util.List; // Importa la interfaz List que se utiliza para declarar listas
import java.util.regex.Matcher; // Importa la clase Matcher que permite realizar coincidencias con expresiones regulares
import java.util.regex.Pattern; // Importa la clase Pattern que permite compilar expresiones regulares

public class DocumentsExtractor { // Definición de la clase DocumentsExtractor

    public static List<DocumentsProperties> readBibFile(String bibFilePath) {
        List<DocumentsProperties> articles = new ArrayList<>();
        String currentType = null; // Nuevo: para almacenar el tipo de documento actual

        try (BufferedReader reader = new BufferedReader(new FileReader(bibFilePath))) {
            String line;
            StringBuilder articleContent = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("@")) { // Generaliza para cualquier tipo de entrada: @article, @inproceedings, @book, etc.
                    if (articleContent.length() > 0) {
                        DocumentsProperties article = parseArticle(articleContent.toString(), currentType); // Pasa el tipo al parser
                        if (article != null) {
                            articles.add(article);
                        }
                    }
                    articleContent.setLength(0);
                    articleContent.append(line).append("\n");

                    // Extrae el tipo del documento (ej. "article" de "@article{...") y lo guarda
                    int braceIndex = line.indexOf("{");
                    if (braceIndex > 1) {
                        currentType = line.substring(1, braceIndex).toLowerCase(); // Guarda "article", "inproceedings", etc.
                    }
                } else {
                    articleContent.append(line).append("\n");
                }
            }

            if (articleContent.length() > 0) {
                DocumentsProperties article = parseArticle(articleContent.toString(), currentType); // Pasa el tipo del último artículo
                if (article != null) {
                    articles.add(article);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return articles;
    }

    private static DocumentsProperties parseArticle(String articleContent, String typeDocument) {
        DocumentsProperties article = new DocumentsProperties();
        article.setTypeDocument(typeDocument);
        article.setAuthor(extractAttribute(articleContent, "author"));
        article.setTitle(extractAttribute(articleContent, "title"));
        article.setYear(extractYear(articleContent));
        article.setLocation(extractAttribute(articleContent, "location"));
        article.setPublishers(extractAttribute(articleContent, "publisher"));
        article.setKeywords(extractAttribute(articleContent, "keywords"));
        article.setNumpages(extractPages(articleContent));
        article.setAbstractDescription(extractAttribute(articleContent, "abstract"));
        article.setJournal(extractAttribute(articleContent, "journal"));

        return article;
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

    private static int extractPages(String articleContent) { // Método privado para extraer el año de publicación del artículo
        String pages = extractAttribute(articleContent, "numpages"); // Llama a extractAttribute para obtener el valor del año
        if (pages != null) { // Si se obtuvo un valor para el año
            try {
                return Integer.parseInt(pages); // Intenta convertir el valor a un número entero
            } catch (NumberFormatException e) { // Si ocurre un error al convertir, captura la excepción
                return 0; // Si no se puede convertir, devuelve 0 como valor predeterminado
            }
        }
        return 0; // Si no se obtuvo un año, devuelve 0
    }
}
