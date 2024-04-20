package pkgController;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

import com.mongodb.client.model.geojson.Point;
import jdk.jshell.spi.ExecutionControl;
import org.bson.types.ObjectId;
import org.glassfish.jersey.client.HttpUrlConnectorProvider;
import pkgData.Book;
import pkgData.Car;
import pkgData.PetrolStation;
import pkgMisc.GsonLocalDateTimeSerializer;
import pkgMisc.GsonPointSerializer;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static pkgMisc.Routes.*;

public class RESTController {
    public String uri = null;
    private Client client = null;
    private WebTarget webtarget = null;

    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime .class, new GsonLocalDateTimeSerializer())
            .registerTypeAdapter(Point .class, new GsonPointSerializer())
            .create();


    public RESTController(String _uri) throws Exception {
        setUri(_uri);
    }

    public void setUri(String uri) throws Exception {
        this.uri = uri;
        client = ClientBuilder.newClient();
        webtarget = client.target(uri);
    }

    public ArrayList<Car> getCars() throws Exception {
        Response response = null;

        ArrayList<Car> collCars = null;
        response = webtarget.path(CARS)
                .request(MediaType.APPLICATION_JSON)
                .get(Response.class);

        if (response.getStatus() != Response.Status.OK.getStatusCode()) {
            throw new Exception(response.getStatus() + " " + response.getStatusInfo());
        }

        String retCarsAsJson = response.readEntity(String.class);

        try {
            Type collectionType = new TypeToken<ArrayList<Car>>(){}.getType();
            collCars = gson.fromJson(retCarsAsJson, collectionType);
        } catch (Exception ex) {
            throw new Exception(response.getStatus() + " " + response.getStatusInfo() + ": " + retCarsAsJson);
        }

        return collCars;
    }

    public ArrayList<PetrolStation> getPetrolStations() throws Exception {
        Response response = null;

        ArrayList<PetrolStation> collPetrolStations = null;
        response = webtarget.path(PETROL_STATIONS)
                .request(MediaType.APPLICATION_JSON)
                .get(Response.class);

        if (response.getStatus() != Response.Status.OK.getStatusCode()) {
            throw new Exception(response.getStatus() + " " + response.getStatusInfo());
        }

        String retPetrolStationsAsJson = response.readEntity(String.class);

        try {
            Type collectionType = new TypeToken<ArrayList<PetrolStation>>(){}.getType();
            collPetrolStations = gson.fromJson(retPetrolStationsAsJson, collectionType);
        } catch (Exception ex) {
            throw new Exception(response.getStatus() + " " + response.getStatusInfo() + ": " + retPetrolStationsAsJson);
        }

        return collPetrolStations;
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

        String jsonBook = gson.toJson(book, Book.class);

        response = webtarget.path(BOOKS)
                .request()
                .post(Entity.entity(jsonBook, MediaType.APPLICATION_JSON));

        return response.getStatusInfo() + ": " + response.readEntity(String.class);
    }

    public String updateBook(Book book) throws Exception {
        Response response = null;

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

    public void initDb() throws Exception {
        this.initDb(10, 3);
    }

    public void initDb(int cars, int petrolStations) throws Exception {
        Response response = webtarget.path("generateInstances/")
                .queryParam("cars", cars)
                .queryParam("petrolstations", petrolStations)
                .request(MediaType.APPLICATION_JSON)
                .get(Response.class);

        if (response.getStatus() != Response.Status.OK.getStatusCode()) {
            throw new Exception(response.getStatus() + " " + response.getStatusInfo());
        }
    }

    public void updatePositions() throws Exception {
        Response response = webtarget.path("simulateMovements")
                .request(MediaType.APPLICATION_JSON)
                .get(Response.class);

        if (response.getStatus() != Response.Status.OK.getStatusCode()) {
            throw new Exception(response.getStatus() + " " + response.getStatusInfo());
        }
    }

    public void updateCarPositions(ObjectId carId) throws Exception {
        Response response = webtarget.path("simulateMovements")
                .queryParam("numMovements", 5)
                .queryParam("carId", carId)
                .request(MediaType.APPLICATION_JSON)
                .get(Response.class);

        if (response.getStatus() != Response.Status.OK.getStatusCode()) {
            throw new Exception(response.getStatus() + " " + response.getStatusInfo());
        }
    }

    public void createPetrolStationPositionIndex() {
        Response response = webtarget.path("petrolstations/createPositionIndex")
                .request(MediaType.APPLICATION_JSON)
                .get(Response.class);
    }

    public ArrayList<PetrolStation> getNearestPetrolStations(Car selectedCar, double distance) {
        Response response = webtarget.path("nearestPetrolStations")
                .queryParam("carId", selectedCar.getId())
                .queryParam("maxDistance", distance)
                .request(MediaType.APPLICATION_JSON)
                .get(Response.class);

        String retPetrolStationsAsJson = response.readEntity(String.class);

        System.out.println("################################ GET NEAREST");
        System.out.println("carid: " + selectedCar.getId());
        System.out.println("distance: " + distance);
        System.out.println(retPetrolStationsAsJson);

        Type collectionType = new TypeToken<ArrayList<PetrolStation>>(){}.getType();
        ArrayList<PetrolStation> petrolStations = gson.fromJson(retPetrolStationsAsJson, collectionType);
        System.out.println(petrolStations);
        return petrolStations;
    }
}
