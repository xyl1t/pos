package pkgData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Database {
    private final Map<Integer, Book> collBooks = new HashMap<Integer, Book>();
    private static Database instance = null;

    private Database() {
        fillMap();
    }

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    public ArrayList<Book> getBooks() {
        return new ArrayList<Book>(collBooks.values());
    }

    public Book getBook(int id) throws Exception {
        Book retBook = null;
        retBook = collBooks.get(id);
        if (retBook == null) {
            throw new Exception("no book found with id: " + id);
        }
        return retBook;
    }

    public void setBook(Book book) throws Exception {
        System.out.println(collBooks.get(book.getId()) == null);
        if (collBooks.get(book.getId()) == null) {
            collBooks.put(book.getId(), book);
        } else {
            throw new Exception("book-id " + book.getId() + " already used");
        }
    }

    public Book deleteBook(int id) throws Exception {
        Book retBook = null;
        if (collBooks.get(id) != null) {
            retBook = collBooks.remove(id);
        } else {
            throw new Exception("book-id " + id + " not in database");
        }
        return retBook;
    }

    private void fillMap() {
        Book b = new Book(11, "Raeuber Hotzenplotz", "Ottfried Preussler");
        collBooks.put(b.getId(), b);
        b = new Book(22, "Schatz am Slibersee", "Karl May");
        collBooks.put(b.getId(), b);
        b = new Book(33, "Hercule Poirot", "Agatha Christie");
        collBooks.put(b.getId(), b);
    }
}
