package co.uniquindio.proyecto.backendalgoritmos.models;

import java.util.Objects;

public class DocumentsProperties {

    private String author;
    private String title;
    private int year;
    private String location;
    private String abstractDescription;

    public DocumentsProperties() {
    }

    public DocumentsProperties(String author, String title, int year, String location, String abstractDescription) {
        this.author = author;
        this.title = title;
        this.year = year;
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
                ", location='" + location + '\'' +
                ", abstractDescription='" + abstractDescription + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DocumentsProperties that = (DocumentsProperties) o;
        return year == that.year && Objects.equals(author, that.author) && Objects.equals(title, that.title) && Objects.equals(location, that.location) && Objects.equals(abstractDescription, that.abstractDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(author, title, year, location, abstractDescription);
    }
}