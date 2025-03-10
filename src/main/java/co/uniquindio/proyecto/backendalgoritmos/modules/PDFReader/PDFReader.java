package co.uniquindio.proyecto.backendalgoritmos.modules.PDFReader;

import co.uniquindio.proyecto.backendalgoritmos.models.DocumentsProperties;
import co.uniquindio.proyecto.backendalgoritmos.modules.BibReader.DocumentsHandler;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Set;

public class PDFReader {

    private static final String directorioActual = System.getProperty("user.dir");
    private static final String PDF_FOLDER_PATH = directorioActual+ "/src/main/resources/co.uniquindio.proyecto.backendalgoritmos/";
    private static final String BIB_FILE_PATH = directorioActual+ "/src/main/resources/co.uniquindio.proyecto.backendalgoritmos/keywords.bib";
    private static final String DUPLICATES_BIB_FILE_PATH = directorioActual+ "/src/main/resources/co.uniquindio.proyecto.backendalgoritmos/duplicates.bib";

    private final DocumentsHandler publicationProcessor;
    private final Set<String> processedHashes = new HashSet<>();

    public PDFReader() {
        this.publicationProcessor = new DocumentsHandler();
    }

    public void ejecute() {
        File folder = new File(PDF_FOLDER_PATH);
        File[] listOfFiles = folder.listFiles();

        if (listOfFiles != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(BIB_FILE_PATH));
                 BufferedWriter duplicatesWriter = new BufferedWriter(new FileWriter(DUPLICATES_BIB_FILE_PATH))) {
                for (File file : listOfFiles) {
                    if (file.isFile() && file.getName().toLowerCase().endsWith(".pdf")) {
                        processPdfFile(file, writer, duplicatesWriter);
                    }
                }
                System.out.println("Proceso completado. Los resultados se guardaron en " + BIB_FILE_PATH);
                System.out.println("Los duplicados se guardaron en " + DUPLICATES_BIB_FILE_PATH);
            } catch (IOException | NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("La carpeta especificada no existe o está vacía.");
        }
    }

    private void processPdfFile(File file, BufferedWriter writer, BufferedWriter duplicatesWriter) throws IOException, NoSuchAlgorithmException {
        String pdfFilePath = file.getAbsolutePath();
        String pdfText = extractPdfText(pdfFilePath);
        if (pdfText != null && !pdfText.isEmpty()) {
            String hash = calculateHash(pdfText);
            if (!processedHashes.contains(hash)) {
                processedHashes.add(hash);
                DocumentsProperties publication = publicationProcessor.processPublication(pdfText);
                writeBibEntry(writer, publication, file.getName().replace(".pdf", ""));
                System.out.println("Información extraída de " + file.getName());
            } else {
                System.out.println("PDF duplicado encontrado: " + file.getName());
                DocumentsProperties publication = publicationProcessor.processPublication(pdfText);
                writeBibEntry(duplicatesWriter, publication, file.getName().replace(".pdf", ""));
                if (file.delete()) {
                    System.out.println("PDF duplicado eliminado: " + file.getName());
                } else {
                    System.out.println("No se pudo eliminar el PDF duplicado: " + file.getName());
                }
            }
        } else {
            System.out.println("No se encontró información en " + file.getName());
        }
    }

    private String extractPdfText(String pdfFilePath) throws IOException {
        File file = new File(pdfFilePath);
        try (PDDocument document = PDDocument.load(file)) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            return pdfStripper.getText(document);
        }
    }

    private void writeBibEntry(BufferedWriter writer, DocumentsProperties publication, String articleId) throws IOException {
        writer.write("@article{" + articleId + ",\n");
        writeAttribute(writer, "author", publication.getAuthor());
        writeAttribute(writer, "title", publication.getTitle());
        writeAttribute(writer, "year", String.valueOf(publication.getYear()));
        writeAttribute(writer, "location", publication.getLocation());
        writeAttribute(writer, "keywords", publication.getKeywords());
        writeAttribute(writer, "abstract", publication.getAbstractDescription());
        writer.write("}\n\n");
    }

    private void writeAttribute(BufferedWriter writer, String attributeName, String attributeValue) throws IOException {
        if (attributeValue != null && !attributeValue.isEmpty() && !attributeValue.equals("0")) {
            writer.write("  " + attributeName + " = {" + attributeValue + "},\n");
        }
    }

    private String calculateHash(String text) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] hashBytes = md.digest(text.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : hashBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}