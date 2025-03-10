package co.uniquindio.proyecto.backendalgoritmos.models;

public class DocumentsProperties {

    private String author;
    private String title;
    private int year;
    private String keywords;
    private String location;
    private String abstractDescription;

    public DocumentsProperties() {
    }

    public DocumentsProperties(String author, String title, int year, String keywords, String location, String abstractDescription) {
        this.author = author;
        this.title = title;
        this.year = year;
        this.keywords = keywords;
        this.location = location;
        this.abstractDescription = abstractDescription;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAbstractDescription() {
        return abstractDescription;
    }

    public void setAbstractDescription(String abstractDescription) {
        this.abstractDescription = abstractDescription;
    }

    @Override
    public String toString() {
        return "DocumentsProperties{" +
                "author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", year=" + year +
                ", keywords='" + keywords + '\'' +
                ", location='" + location + '\'' +
                ", abstractDescription='" + abstractDescription + '\'' +
                '}';
    }
}