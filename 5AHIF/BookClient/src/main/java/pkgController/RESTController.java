package pkgController;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

import org.glassfish.jersey.client.HttpUrlConnectorProvider;
import pkgData.Book;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

import static pkgMisc.Routes.BOOKS;

public class RESTController {
    public String uri = null;
    private Client client = null;
    private WebTarget webtarget = null;


    public RESTController(String _uri) throws Exception {
        setUri(_uri);
    }

    public Book getBook(String id) throws Exception {
        Book retBook = null;
        String retBookAsJson = null;
        Response response = null;
        response = webtarget.path(BOOKS + id)
                .request(MediaType.APPLICATION_JSON)
                .get(Response.class);

        if (response.getStatus() != Response.Status.OK.getStatusCode()) {
            throw new Exception(response.getStatus() + " " + response.getStatusInfo());
        }

        Gson gson = new Gson();
        retBookAsJson = response.readEntity(String.class);
        try {
            retBook = gson.fromJson(retBookAsJson, Book.class);
        } catch (Exception ex) {
            throw new Exception(response.getStatusInfo() + ": " + retBookAsJson);
        }

        return retBook;
    }

    public ArrayList<Book> getBooks() throws Exception {
        Response response = null;

        ArrayList<Book> collBooks = null;
        response = webtarget.path(BOOKS)
                .request(MediaType.APPLICATION_JSON)
                .get(Response.class);

        if (response.getStatus() != Response.Status.OK.getStatusCode()) {
            throw new Exception(response.getStatus() + " " + response.getStatusInfo());
        }

        Gson gson = new Gson();
        String retBooksAsJson = response.readEntity(String.class);

        try {
            Type collectionType = new TypeToken<ArrayList<Book>>(){}.getType();
            collBooks = gson.fromJson(retBooksAsJson, collectionType);
        } catch (Exception ex) {
            throw new Exception(response.getStatus() + " " + response.getStatusInfo() + ": " + retBooksAsJson);
        }

        return collBooks;
    }

    public String addBook(Book book) throws Exception {
        Response response;

        Gson gson = new Gson();
        String jsonBook = gson.toJson(book, Book.class);

        response = webtarget.path(BOOKS)
                .request()
                .post(Entity.entity(jsonBook, MediaType.APPLICATION_JSON));

        return response.getStatusInfo() + ": " + response.readEntity(String.class);
    }

    public String updateBook(Book book) throws Exception {
        Response response = null;
        Gson gson = new Gson();

        try {
            String jsonBook = gson.toJson(book, Book.class);

            response = webtarget
                    .path(BOOKS + book.getId())
                    .request()
                    .build("PATCH", Entity.entity(jsonBook, MediaType.APPLICATION_JSON))
                    .property(HttpUrlConnectorProvider.SET_METHOD_WORKAROUND, true)
                    .invoke();

        } catch (Exception ex) {
            ex.printStackTrace();
            if (response != null) {
                throw new Exception(response.getStatusInfo() + ": " + response.readEntity(String.class));
            } else {
                throw ex;
            }
        }
        return response.getStatusInfo() + ": " + response.readEntity(String.class);
    }

    public void setUri(String uri) throws Exception {
        this.uri = uri;
        client = ClientBuilder.newClient();
        webtarget = client.target(uri);
    }
}
