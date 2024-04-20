package org.restaurant.pkgData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.*;
import com.mongodb.client.model.geojson.Point;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.restaurant.pkgMisc.GsonBigDecimalSerializer;
import org.restaurant.pkgMisc.GsonLocalDateSerializer;
import org.restaurant.pkgMisc.GsonSerializerObjectId;
import org.restaurant.pkgMisc.GsonPointSerializer;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Sorts.ascending;
import static com.mongodb.client.model.Sorts.orderBy;
import static com.mongodb.client.model.Updates.unset;

public class Database {
    private static String CONNECTSTRING,
            DATABASENAME = "myPubs",
            COLLECTION_RESTAURANTS = "Pubs",
            COLLECTION_LOCATIONS = "Locations";

    private static int PORT = 27017;
    private static String HOST = "127.0.0.1";
    private static MongoClient mongoClient = null;
    private static MongoDatabase mongoDatabase = null;
    private static Gson gson ;
    private static FindIterable<Document> filtered;



    /**
     * Singleton
     */
    private static Database database = null;

    public static Database getInstance() throws Exception {
        if (database == null) {
            database = new Database();
            CONNECTSTRING = HOST;
            mongoClient = new MongoClient(CONNECTSTRING, PORT);
            mongoDatabase = mongoClient.getDatabase(DATABASENAME);
            gson = new GsonBuilder()
                    .registerTypeAdapter(ObjectId.class, new GsonSerializerObjectId())
                    .registerTypeAdapter(LocalDate.class, new GsonLocalDateSerializer())
                    .registerTypeAdapter(BigDecimal.class, new GsonBigDecimalSerializer())
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

    public ObjectId insertPub(Restaurant restaurant) throws Exception {
        MongoCollection collection = mongoDatabase.getCollection(COLLECTION_RESTAURANTS);
        String jsonString = gson.toJson(restaurant);                // transfer java-object => json-format
        Document doc = Document.parse(jsonString);                  // transfer json-format => bson-format
        collection.insertOne(doc);                                  // insert doc into colelction; Object-Id is generated
        restaurant.setId(doc.get("_id", ObjectId.class));      // set Object-Id in car-instance
        return (ObjectId) doc.get("_id");                           // 2nd alternative of get
    }

    public ArrayList<Restaurant> getAllRestaurants() throws Exception {
        ArrayList<Restaurant> collRestaurants = new ArrayList<>();
        MongoCollection<Document> collection = mongoDatabase.getCollection(COLLECTION_RESTAURANTS);

        for (Document doc : collection.find().sort(orderBy(ascending("location"), ascending("name")))) {
            collRestaurants.add(generateRestaurantFromDoc(doc));
        }

        return collRestaurants;
    }

    public ArrayList<Restaurant> getFilteredRestaurants() throws Exception {
        if (filtered == null) return getAllRestaurants();

        ArrayList<Restaurant> filteredRestaurants = new ArrayList<>();
        for (Document doc : filtered.sort(orderBy(ascending("location"), ascending("name")))) {
            filteredRestaurants.add(generateRestaurantFromDoc(doc));
        }
        return filteredRestaurants;
    }

    public void filterLocationAndYear(String location, String founded) throws Exception {
        MongoCollection<Document> collection = mongoDatabase.getCollection(COLLECTION_RESTAURANTS);

        if (!location.isEmpty() && !founded.isEmpty()) {
            filtered = collection.find(and(eq("location", location), gte("founded", Integer.parseInt(founded))));
        } else if (!location.isEmpty()) {
            filtered = collection.find(eq("location", location));
        } else if (!founded.isEmpty()) {
            filtered = collection.find(gte("founded", Integer.parseInt(founded)));
        } else {
            filtered = collection.find();
        }
    }

    public void createIndex() {
        MongoCollection<Document> collection = mongoDatabase.getCollection(COLLECTION_RESTAURANTS);
        collection.createIndex(Indexes.text("description"));
    }

    public ArrayList<Restaurant> filterText(String text) throws Exception {
        TextSearchOptions options = new TextSearchOptions().caseSensitive(false);
        Bson filter = Filters.text(text, options);

        MongoCollection<Document> collection = mongoDatabase.getCollection(COLLECTION_RESTAURANTS);
        ArrayList<Restaurant> collRestaurants = new ArrayList<>();

        for (Document doc : collection.find(filter)) {
            collRestaurants.add(generateRestaurantFromDoc(doc));
        }

        return collRestaurants;
    }

//    public Database filterLocationEquals(String locationName) throws Exception {
//        MongoCollection<Document> collection = mongoDatabase.getCollection(COLLECTION_RESTAURANTS);
//
//        for (Document doc : collection.find(eq("location", locationName)).sort(orderBy(ascending("location"), ascending("name")))) {
//            filteredRestaurants.add(generateRestaurantFromDoc(doc));
//        }
//
//        return this;
//    }
//
//    public ArrayList<Guest> filterYearGreaterOrEquals(ObjectId pubId) throws Exception {
//        ArrayList<Restaurant> restaurants = new ArrayList<>();
//        MongoCollection<Document> collection = mongoDatabase.getCollection(COLLECTION_RESTAURANTS);
//
//        Document resDoc = collection.find(eq("_id", pubId)).first();
//        Restaurant r = generateRestaurantFromDoc(resDoc);
//
//        return r.getGuests();
//    }


    private Restaurant generateRestaurantFromDoc(Document doc) throws Exception {
        String jsonString = doc.toJson();               // bson => json
        Restaurant restaurant = gson.fromJson(jsonString, Restaurant.class);
//        System.out.println("==>" + restaurant.getId());        // as objId is special bson-type => conversion does not work (without adapter)
        restaurant.setId((ObjectId) doc.get("_id"));           // assign correct objId back
//        System.out.println("   " + restaurant.getId());
        return restaurant;
    }

    private Guest generateGuestFromDoc(Document doc) throws Exception {
        String jsonString = doc.toJson();               // bson => json
        Guest guest = gson.fromJson(jsonString, Guest.class);
        return guest;
    }

    public long deleteRestaurant(Restaurant res) {
        MongoCollection collection = mongoDatabase.getCollection(COLLECTION_RESTAURANTS);
        DeleteResult dr = collection.deleteOne(eq("_id", res.getId()));
        return dr.getDeletedCount();
    }

    public long updateRestaurant(Restaurant res) throws Exception {
        MongoCollection collection = mongoDatabase.getCollection(COLLECTION_RESTAURANTS);
        String jsonString = gson.toJson(res); // java-obj => json-format
        Document doc = Document.parse(jsonString);

        doc.remove("_id"); // oid must not be updated
        UpdateResult ur = collection.updateOne(
                eq("_id", res.getId()),
                new Document("$set", doc));

        return ur.getModifiedCount();
    }

    public long replaceRestaurant(Restaurant res) throws Exception {
        MongoCollection collection = mongoDatabase.getCollection(COLLECTION_RESTAURANTS);
        String jsonString = gson.toJson(res); // java-obj => json-format
        Document doc = Document.parse(jsonString);

        doc.remove("_id"); // oid must not be updated
        UpdateResult ur = collection.replaceOne(eq("_id", res.getId()), doc);

        return ur.getModifiedCount();
    }

    public long assignRestaurantOwner(Restaurant res, Owner owner) {
        MongoCollection collection = mongoDatabase.getCollection(COLLECTION_RESTAURANTS);
        String jsonString = gson.toJson(owner);
        Document doc = Document.parse(jsonString);  // Create owner document
        ObjectId id = getOwnerIdOrGenerate(owner);
        doc.append("_id", id);

        UpdateResult ur = collection.updateOne(
            eq("_id", res.getId()),
            new Document("$set", new Document("owner", doc)) // update _just_ the owner
        );

        return ur.getModifiedCount();
    }

    public long updateRestaurantOwner(Restaurant res, Owner owner) {
        MongoCollection collection = mongoDatabase.getCollection(COLLECTION_RESTAURANTS);
        String jsonString = gson.toJson(owner);
        Document doc = Document.parse(jsonString);  // Create owner document

        Document updateOwner = new Document()
                .append("owner.birthday", owner.getBirthday())
                .append("owner.name", owner.getName())
                .append("owner.cv", owner.getCv());

        ObjectId alreadyExistingId = getOwnerId(owner);
        if (alreadyExistingId != null) {
            updateOwner.append("owner._id", alreadyExistingId);
        }

        UpdateResult ur = collection.updateMany(
            eq("owner._id", getOwnerId(res.getOwner())),
            new Document("$set", updateOwner) // update _just_ the owner
        );

        return ur.getModifiedCount();
    }

    public long deleteRestaurantOwner(Restaurant res) throws Exception {
        res.setOwner(null);
        MongoCollection collection = mongoDatabase.getCollection(COLLECTION_RESTAURANTS);
        String jsonString = gson.toJson(res); // java-obj => json-format
        Document doc = Document.parse(jsonString);

        doc.remove("_id"); // oid must not be updated
        UpdateResult ur = collection.updateOne(
            eq("_id", res.getId()),
            unset("owner")
        );

        return ur.getModifiedCount();
    }

    public ArrayList<Restaurant> findRestaurantOwners(Owner owner) throws Exception {
        ArrayList<Restaurant> collRestaurants = new ArrayList<>();
        MongoCollection<Document> collection = mongoDatabase.getCollection(COLLECTION_RESTAURANTS);

        for (Document doc : collection.find(eq("owner.name", owner.getName())).sort(orderBy(ascending("location"), ascending("name")))) {
            collRestaurants.add(generateRestaurantFromDoc(doc));
        }

        return collRestaurants;
    }

    private ObjectId getOwnerId(Owner owner) {
        MongoCollection<Document> collection = mongoDatabase.getCollection(COLLECTION_RESTAURANTS);

        FindIterable<Document> found = collection.find(and(
            eq("owner.name", owner.getName()),
            eq("owner.cv", owner.getCv()),
            eq("owner.birthday", owner.getBirthday())
        ));

        Document first = found.first();

        if (first == null) {
            return null;
        } else {
            System.out.println(first.get("owner"));
            return (ObjectId)((Document) first.get("owner")).get("_id");
        }
    }

    private ObjectId getOwnerIdOrGenerate(Owner owner) {
        ObjectId possibleId = getOwnerId(owner);
        return possibleId != null ? possibleId : new ObjectId();
    }

    public long addGuestToRestaurant(Restaurant res, Guest newGuest) {
        MongoCollection collection = mongoDatabase.getCollection(COLLECTION_RESTAURANTS);
        String jsonString = gson.toJson(newGuest);

        Document doc = Document.parse(jsonString);  // Create owner document

        System.out.println(newGuest);


        Document newGuestDoc = new Document()
                .append("name", newGuest.getName())
                .append("birth", newGuest.getBirth())
                .append("totalAmount", newGuest.getTotalAmount())
                .append("location", newGuest.getLocation());

        UpdateResult ur = collection.updateOne(
                eq("_id", res.getId()),
                Updates.addToSet("CollGuests", newGuestDoc)
        );

        return ur.getModifiedCount();
    }

    public ArrayList<Location> getAllLocations() {
        ArrayList<Location> collRestaurants = new ArrayList<>();
        MongoCollection<Document> collection = mongoDatabase.getCollection(COLLECTION_LOCATIONS);

        for (Document doc : collection.find()) {
            collRestaurants.add(generateLocationFromDoc(doc));
        }

        return collRestaurants;
    }

    private Location generateLocationFromDoc(Document doc) {
        String jsonString = doc.toJson();               // bson => json
        Location loc = gson.fromJson(jsonString, Location.class);
        loc.setId((ObjectId) doc.get("_id"));           // assign correct objId back
        return loc;
    }


    public void createLocationIndex() {
        MongoCollection<Document> collection = mongoDatabase.getCollection(COLLECTION_LOCATIONS);
        collection.createIndex(Indexes.geo2dsphere("position"));
    }

    public ObjectId addLocation(Location loc) {
        MongoCollection collection = mongoDatabase.getCollection(COLLECTION_LOCATIONS);
        String jsonString = gson.toJson(loc);
        Document doc = Document.parse(jsonString);
        collection.insertOne(doc);
        loc.setId(doc.get("_id", ObjectId.class));
        return (ObjectId) doc.get("_id");
    }

    public long removeLocByName(Location loc) {
        MongoCollection collection = mongoDatabase.getCollection(COLLECTION_LOCATIONS);
        DeleteResult dr = collection.deleteOne(eq("name", loc.getName()));
        return dr.getDeletedCount();
    }


    public ArrayList<Restaurant> findRestaurantsWithin(Point p, double maxDistKm) throws Exception {
        ArrayList<Restaurant> collRes = new ArrayList<>();
        MongoCollection<Document> collection = mongoDatabase.getCollection(COLLECTION_LOCATIONS);

        Bson filter = Filters.near("position", p, maxDistKm, 0.d);

        // Doesn't work
//        ArrayList<Document> res = collection.aggregate(List.of(
//                Aggregates.match(filter),
//                Aggregates.lookup(COLLECTION_RESTAURANTS, "locationName", "location", "res")
//        )).into(new ArrayList<>());

        MongoCollection<Document> resCollection = mongoDatabase.getCollection(COLLECTION_RESTAURANTS);

        System.out.println("found spatial begin -----------------------");
        ArrayList<String> locationNames = new ArrayList<>();
        for (Document doc : collection.find(filter)) {
            System.out.println(doc.toJson());

            String jsonString = doc.toJson();
            Location loc = gson.fromJson(jsonString, Location.class);
            loc.setId((ObjectId) doc.get("_id"));

            locationNames.add(loc.getName());

            for (Document resDoc : resCollection.find(Filters.eq("location", loc.getName()))) {
                Restaurant res = generateRestaurantFromDoc(resDoc);
                System.out.println(res);
                collRes.add(res);
            }
        }
        System.out.println("found spatial end -----------------------");

        return collRes;
    }

//    public ArrayList<Guest> getGuestsForRestaurant(Restaurant res) throws Exception {
//        ArrayList<Guest> collGuests = new ArrayList<>();
//        MongoCollection<Document> collection = mongoDatabase.getCollection(COLLECTION_RESTAURANTS);
//
//        FindIterable<Document> filtered = collection.find(eq("_id", res.getId()));
//
//        for (Document doc : filtered) {
//            collGuests.add(generateGuestFromDoc(doc));
//        }
//
//        return collGuests;
//    }
}
