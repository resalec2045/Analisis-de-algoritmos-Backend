package co.uniquindio.proyecto.backendalgoritmos.modules.PDFReader;
import co.uniquindio.proyecto.backendalgoritmos.models.DocumentsProperties;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KeywordExtractor {

    public static List<DocumentsProperties> readBibFile(String bibFilePath) {
        List<DocumentsProperties> articles = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(bibFilePath))) {
            String line;
            StringBuilder articleContent = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("@article")) {
                    if (articleContent.length() > 0) {
                        DocumentsProperties article = parseArticle(articleContent.toString());
                        if (article != null) {
                            articles.add(article);
                        }
                    }
                    articleContent.setLength(0);
                    articleContent.append(line).append("\n");
                } else {
                    articleContent.append(line).append("\n");
                }
            }

            if (articleContent.length() > 0) {
                DocumentsProperties article = parseArticle(articleContent.toString());
                if (article != null) {
                    articles.add(article);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return articles;
    }

    private static DocumentsProperties parseArticle(String articleContent) {
        DocumentsProperties article = new DocumentsProperties();

        article.setAuthor(extractAttribute(articleContent, "author"));
        article.setTitle(extractAttribute(articleContent, "title"));
        article.setYear(extractYear(articleContent));
        article.setKeywords(extractAttribute(articleContent, "keywords"));
        article.setLocation(extractAttribute(articleContent, "location"));

        if (article.title == null) {
            return null;
        }

        return article;
    }

    private static String extractAttribute(String articleContent, String attributeName) {
        String regex = attributeName + "\\s*=\\s*\\{(.*?)\\}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(articleContent);

        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return null;
    }

    private static int extractYear(String articleContent) {
        String yearString = extractAttribute(articleContent, "year");
        if (yearString != null) {
            try {
                return Integer.parseInt(yearString);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }
}