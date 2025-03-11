package co.uniquindio.proyecto.backendalgoritmos.modules.BibReader;

import co.uniquindio.proyecto.backendalgoritmos.models.DocumentsProperties;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DocumentsHandler {

    public DocumentsProperties processPublication(String publicationText) {
        DocumentsProperties publication = new DocumentsProperties();

        publication.setAuthor(extractValue(publicationText, "ACM Reference Format:\\s*(.*?)\\.\\s*\\d{4}"));
        publication.setTitle(extractValue(publicationText, "^(.+)\\n"));
        publication.setYear(extractIntValue(publicationText, "ACM Reference Format:\\s*.*?\\.\\s*(\\d{4})"));
        publication.setLocation(extractValue(publicationText, "[A-Z]+ \\d{4},.*,\\s(.*)"));
        if (publication.getLocation() == null) {
            publication.setLocation(extractValue(publicationText, "[A-Z]+\\s’\\d{2},\\s[A-Za-z]+\\s\\d{1,2}–\\d{1,2},\\s\\d{4},\\s(.*)"));
        }
        publication.setAbstractDescription(extractValue(publicationText, "(?i)abstract\\s*([\\s\\S]*?)(?=(?:CCS CONCEPTS|KEYWORDS|ACM Reference Format|1 INTRODUCTION|\\Z))"));
        return publication;
    }

    private String extractValue(String text, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return null;
    }

    private int extractIntValue(String text, String regex) {
        String value = extractValue(text, regex);
        if (value != null) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }
}