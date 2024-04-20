package pkgData;

import com.google.gson.Gson;

public class Book {
    private int id;
    private String  title = null;
    private String author = null;
    private String nameOfImage = null;

    public Book(int id, String title, String author){
        this.id = id;
        this.title = title;
        this.author = author;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getNameOfImage() {
        return nameOfImage;
    }

    public void setNameOfImage(String nameOfImage) {
        this.nameOfImage = nameOfImage;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}