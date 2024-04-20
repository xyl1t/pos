package org.restaurant.pkgData;

import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

import javax.print.Doc;
import java.util.ArrayList;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Sorts.ascending;
import static com.mongodb.client.model.Sorts.orderBy;

public class Database {
    private static String CONNECTSTRING,
            DATABASENAME = "mydb",
            COLLECTION_PUBS = "Restaurants";

    private static int PORT = 27017;
    private static String HOST = "127.0.0.1";
    private static MongoClient mongoClient = null;
    private static MongoDatabase mongoDatabase = null;
    private static Gson gson ;

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
            gson = new Gson();
        }
        return database;
    }

    private Database() throws Exception {
    }

    public void close() throws Exception {
        mongoClient.close();
        database = null;
    }

    public ObjectId insertPub(Pub pub) throws Exception {
        MongoCollection collection = mongoDatabase.getCollection(COLLECTION_PUBS);
        String jsonString = gson.toJson(pub);                //transfer java-object => json-format
        Document doc = Document.parse(jsonString);           // transfer json-format => bson-format
        collection.insertOne(doc);                           // insert doc into colelction; Object-Id is generated
        pub.setId(doc.get("_id", ObjectId.class));      // set Object-Id in car-instance
        return (ObjectId) doc.get("_id");                    // 2nd alternative of get
    }

    public ArrayList<Pub> getAllRestaurants() throws Exception {
        ArrayList<Pub> collRestaurants = new ArrayList<>();
        MongoCollection<Document> collection = mongoDatabase.getCollection(COLLECTION_PUBS);

        for (Document doc : collection.find().sort(orderBy(ascending("location"), ascending("name")))) {
            collRestaurants.add(generateRestaurantFromDoc(doc));
        }

        return collRestaurants;
    }

    private Pub generateRestaurantFromDoc(Document doc) throws Exception {
        String jsonString = doc.toJson();               // bson => json
        Pub pub = gson.fromJson(jsonString, Pub.class);
        System.out.println("==>" + pub.getId());        // as objId is special bson-type => conversion does not work (without adapter)
        pub.setId((ObjectId) doc.get("_id"));           // assign correct objId back
        System.out.println("   " + pub.getId());
        return pub;
    }
}
