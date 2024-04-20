package pkgData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.geojson.Point;
import com.mongodb.client.model.geojson.Position;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.types.ObjectId;
import pkgMisc.GsonLocalDateTimeSerializer;
import pkgMisc.GsonPointSerializer;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.exclude;
import static com.mongodb.client.model.Projections.include;
import static com.mongodb.client.model.Sorts.ascending;
import static com.mongodb.client.model.Sorts.orderBy;

public class Database {
    private static String CONNECTSTRING;
    private static String DATABASENAME = "dbMaratIsaw";
    private static String COLLECTION_CARS = "cars";
    private static String COLLECTION_PETROL_STATIONS = "petrolStations";

    private static int PORT = 27017;
    private static String HOST = "127.0.0.1";

    private static MongoClient mongoClient = null;
    private static MongoDatabase mongoDatabase = null;

    private static Gson gson;
    public static Gson getGson() {
        return gson;
    }

    private static Database database = null;

    public static Database getInstance() throws Exception {
        if (database == null) {
            database = new Database();
            CONNECTSTRING = HOST;
            mongoClient = new MongoClient(CONNECTSTRING, PORT);
            mongoDatabase = mongoClient.getDatabase(DATABASENAME);
            gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTimeSerializer())
                    .registerTypeAdapter(Point.class, new GsonPointSerializer())
                    .create();
        }
        return database;
    }

    private Database() throws Exception {
    }

    public void close() throws Exception {
        mongoClient.close();
        database = null;
    }

    public ObjectId insertCar(Car car) throws Exception {
        MongoCollection collection = mongoDatabase.getCollection(COLLECTION_CARS);
        String jsonString = gson.toJson(car);                // transfer java-object => json-format
        Document doc = Document.parse(jsonString);           // transfer json-format => bson-format
        collection.insertOne(doc);                           // insert doc into colelction; Object-Id is generated
        car.setId(doc.get("_id", ObjectId.class));      // set Object-Id in car-instance
        return (ObjectId) doc.get("_id");                    // 2nd alternative of get
    }

    public long updateCar(Car car) throws Exception {
        MongoCollection collection = mongoDatabase.getCollection(COLLECTION_CARS);
        String jsonString = gson.toJson(car); // java-obj => json-format
        Document doc = Document.parse(jsonString);

        doc.remove("_id"); // oid must not be updated
        UpdateResult ur = collection.updateOne(
                eq("_id", car.getId()),
                new Document("$set", doc));

        return ur.getModifiedCount();
    }

    public Car deleteCar(ObjectId id) throws Exception {
        MongoCollection collection = mongoDatabase.getCollection(COLLECTION_CARS);
        Document doc = (Document) collection.find(eq("_id", id)).first();
        Car c = generateCarFromDoc(doc);
        DeleteResult dr = collection.deleteOne(eq("_id", id));
        return c;
    }

    public ArrayList<Car> getAllCars() throws Exception {
        ArrayList<Car> collCars = new ArrayList<>();
        MongoCollection<Document> collection = mongoDatabase.getCollection(COLLECTION_CARS);

        for (Document doc : collection.find().sort(orderBy(ascending("location"), ascending("name")))) {
            collCars.add(generateCarFromDoc(doc));
        }

        return collCars;
    }

    public ArrayList<Car> getAllCarsName() throws Exception {
        ArrayList<Car> collCars = new ArrayList<>();
        MongoCollection<Document> collection = mongoDatabase.getCollection(COLLECTION_CARS);

        for (Document doc : collection.find().sort(orderBy(ascending("carName"))).projection(exclude("positions"))) {
            collCars.add(generateCarFromDoc(doc));
        }

        return collCars;
    }

    public Car getCarById(ObjectId id) throws Exception {
        Car ret = null;
        MongoCollection<Document> collection = mongoDatabase.getCollection(COLLECTION_CARS);

        Document doc = collection.find(eq("_id", id)).first();
        if (doc != null) {
            ret = generateCarFromDoc(doc);
        }

        return ret;
    }

    private Car generateCarFromDoc(Document doc) throws Exception {
        String jsonString = doc.toJson(); // bson => json
        Car car = gson.fromJson(jsonString, Car.class);
        car.setId((ObjectId) doc.get("_id")); // assign correct objId back
        return car;
    }

    public Car getCarWithPositions(ObjectId id) throws Exception {
        Car ret = null;
        MongoCollection<Document> collection = mongoDatabase.getCollection(COLLECTION_CARS);

        Document doc = collection.find(eq("_id", id)).projection(include("positions")).first();
        if (doc != null) {
            ret = generateCarFromDoc(doc);
            // order by datetime desc
            ret.getPositions().sort((p1, p2) -> p2.getDatetime().compareTo(p1.getDatetime()));
        }

        return ret;
    }

    public void insertPetrolStation(PetrolStation petrolStation) throws Exception {
        MongoCollection collection = mongoDatabase.getCollection(COLLECTION_PETROL_STATIONS);
        String jsonString = gson.toJson(petrolStation);                // transfer java-object => json-format
        System.out.println("################# jsonString" + jsonString);
        Document doc = Document.parse(jsonString);           // transfer json-format => bson-format
        collection.insertOne(doc);                           // insert doc into colelction; Object-Id is generated
        petrolStation.setId(doc.get("_id", ObjectId.class));      // set Object-Id in car-instance
    }

    public long updatePetrolStation(PetrolStation petrolStation) throws Exception {
        MongoCollection collection = mongoDatabase.getCollection(COLLECTION_PETROL_STATIONS);
        String jsonString = gson.toJson(petrolStation);
        Document doc = Document.parse(jsonString);

        doc.remove("_id");
        UpdateResult ur = collection.updateOne(
                eq("_id", petrolStation.getId()),
                new Document("$set", doc));

        return ur.getModifiedCount();
    }

    public PetrolStation deletePetrolStation(ObjectId id) throws Exception {
        MongoCollection collection = mongoDatabase.getCollection(COLLECTION_PETROL_STATIONS);
        Document doc = (Document) collection.find(eq("_id", id)).first();
        PetrolStation p = generatePetrolStationFromDoc(doc);
        DeleteResult dr = collection.deleteOne(eq("_id", id));
        return p;
    }

    public ArrayList<PetrolStation> getAllPetrolStations(String sortBy) throws Exception {
        ArrayList<PetrolStation> collPetrolStations = new ArrayList<>();
        MongoCollection<Document> collection = mongoDatabase.getCollection(COLLECTION_PETROL_STATIONS);

        org.bson.conversions.Bson sort = null;
        switch (sortBy.toLowerCase()) {
            case "name":
                sort = orderBy(ascending("stationName"));
                break;
            case "location":
                sort = orderBy(ascending("location"), ascending("stationName"));
                break;
            default:
                sort = orderBy(ascending("stationName"));
                break;
        }

        for (Document doc : collection.find().sort(sort)) {
            collPetrolStations.add(generatePetrolStationFromDoc(doc));
        }

        return collPetrolStations;
    }

    public PetrolStation getPetrolStationById(ObjectId id) throws Exception {
        PetrolStation ret;
        MongoCollection<Document> collection = mongoDatabase.getCollection(COLLECTION_PETROL_STATIONS);

        Document doc = collection.find(eq("_id", id)).first();
        ret = generatePetrolStationFromDoc(doc);

        return ret;
    }

    private PetrolStation generatePetrolStationFromDoc(Document doc) throws Exception {
        String jsonString = doc.toJson(); // bson => json
        PetrolStation petrolStation = gson.fromJson(jsonString, PetrolStation.class);
        petrolStation.setId((ObjectId) doc.get("_id")); // assign correct objId back
        return petrolStation;
    }

    public ArrayList<PetrolStation> getPetrolStationsSrotedByName() throws Exception {
        ArrayList<PetrolStation> collPetrolStations = new ArrayList<>();
        MongoCollection<Document> collection = mongoDatabase.getCollection(COLLECTION_PETROL_STATIONS);

        for (Document doc : collection.find().sort(orderBy(ascending("name")))) {
            collPetrolStations.add(generatePetrolStationFromDoc(doc));
        }

        return collPetrolStations;
    }

    public ArrayList<Car> getCarsProjectedName() throws Exception {
        ArrayList<Car> collCars = new ArrayList<>();
        MongoCollection<Document> collection = mongoDatabase.getCollection(COLLECTION_CARS);

        for (Document doc : collection.find().projection(new Document("name", 1).append("location", 1))) {
            collCars.add(generateCarFromDoc(doc));
        }

        return collCars;
    }

    public void createPetrolStationPositionIndex() {
        MongoCollection<Document> collection = mongoDatabase.getCollection(COLLECTION_PETROL_STATIONS);
        collection.createIndex(new Document("position", "2dsphere"));
    }

    public ArrayList<PetrolStation> getNearestPetrolStations(ObjectId carId, double numMaxDistance) throws Exception {
        ArrayList<PetrolStation> ret = new ArrayList<>();
        Car car = null;
        car = getCarById(carId);
        if (car == null) {
            throw new Exception("Car not found (carId=" + carId + ")");
        }

        Point point = car.getLastPosition().getLocation();
        System.out.println("last long: " + point.getPosition().getValues().get(0));
        System.out.println("last lat: " + point.getPosition().getValues().get(1));
        Document pointDoc = new Document("type", "Point").append("coordinates", point.getPosition().getValues());
        System.out.println("pointDoc: " + pointDoc.toJson());

        MongoCollection<Document> collection = mongoDatabase.getCollection(COLLECTION_PETROL_STATIONS);
        Document filter = new Document("position",
                new Document("$near",
                        new Document("$geometry", pointDoc)
                    .append("$maxDistance", numMaxDistance)
                    .append("$minDistance", 0)));

        System.out.println("filter: " + filter.toJson());

        for (Document doc : collection.find(filter)) {
            ret.add(generatePetrolStationFromDoc(doc));
        }

        return ret;
    }
}
