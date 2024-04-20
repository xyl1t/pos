package org.restaurant.pkgData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.restaurant.pkgMisc.GsonLocalDateSerializer;

import java.time.LocalDate;
import java.util.ArrayList;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Sorts.ascending;
import static com.mongodb.client.model.Sorts.orderBy;

public class Database {
    private static String CONNECTSTRING,
            DATABASENAME = "mydb",
            COLLECTION_RESTAURANTS = "Restaurants";

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
                    .registerTypeAdapter(LocalDate.class, new GsonLocalDateSerializer())
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
        String jsonString = gson.toJson(restaurant);         //transfer java-object => json-format
        Document doc = Document.parse(jsonString);           // transfer json-format => bson-format
        collection.insertOne(doc);                           // insert doc into colelction; Object-Id is generated
        restaurant.setId(doc.get("_id", ObjectId.class));    // set Object-Id in car-instance
        return (ObjectId) doc.get("_id");                    // 2nd alternative of get
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
//    public Database filterYearGreaterOrEquals(String year) throws Exception {
//        MongoCollection<Document> collection = mongoDatabase.getCollection(COLLECTION_RESTAURANTS);
//
//        for (Document doc : collection.find(gte("founded", Integer.parseInt(year))).sort(orderBy(ascending("location"), ascending("name")))) {
//            filteredRestaurants.add(generateRestaurantFromDoc(doc));
//        }
//
//        return this;
//    }


    private Restaurant generateRestaurantFromDoc(Document doc) throws Exception {
        String jsonString = doc.toJson();               // bson => json
        Restaurant restaurant = gson.fromJson(jsonString, Restaurant.class);
        System.out.println("==>" + restaurant.getId());        // as objId is special bson-type => conversion does not work (without adapter)
        restaurant.setId((ObjectId) doc.get("_id"));           // assign correct objId back
        System.out.println("   " + restaurant.getId());
        return restaurant;
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
}
