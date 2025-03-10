package co.uniquindio.proyecto.backendalgoritmos.models;

public class DocumentsProperties {

    public String author;
    public String title;
    public int year;
    public String keywords;
    public String location;

    public DocumentsProperties() {
    }

    public DocumentsProperties(String author, String title, int year, String keywords, String location) {
        this.author = author;
        this.title = title;
        this.year = year;
        this.keywords = keywords;
        this.location = location;
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

    @Override
    public String toString() {
        return "DocumentsProperties{" +
                "author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", year=" + year +
                ", keywords='" + keywords + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}