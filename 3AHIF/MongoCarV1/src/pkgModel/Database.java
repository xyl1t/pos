package pkgModel;

import java.util.ArrayList;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.gte;
import static com.mongodb.client.model.Indexes.ascending;
import static com.mongodb.client.model.Indexes.descending;
import static com.mongodb.client.model.Sorts.orderBy;

public class Database {

	private static String CONNECTSTRING,
	DATABASENAME = "mydb",
	COLLECTION_CARS = "Cars";
	
	private static int PORT = 27017;
	private static MongoClient mongoClient = null;
	private static MongoDatabase mongoDatabase = null;

	/**
	 * Singleton
	 */
	private static Database db = null;

	private Gson gson;
//	private TreeMap<String, Car> collCars;
//	public static final String FILENAME = "db";

	public static Database getInstance(String ip) throws Exception {
		if (db == null) {
			db = new Database();
			CONNECTSTRING = ip;
			mongoClient = new MongoClient(CONNECTSTRING, PORT);
			mongoDatabase = mongoClient.getDatabase(DATABASENAME);
			db = new Database();
		}
		return db;
	}

	private Database() throws Exception {
//		collCars = new TreeMap<>();
		gson = new GsonBuilder()
				.setPrettyPrinting()
				.enableComplexMapKeySerialization()
				.create();
	}
	
	public void close() throws Exception {
		mongoClient.close();
		db = null;
	}
	
	public ObjectId insertCar(Car car) throws Exception {
		MongoCollection<Document> collection = mongoDatabase.getCollection(COLLECTION_CARS);
		String jsonString = gson.toJson(car);      // transfer java-object => json-format
		Document doc = Document.parse(jsonString); // transfer json-format => bson-format
		collection.insertOne(doc);                 // insert doc into coll; ObjectId is generated
		car.setId(doc.get("_id", ObjectId.class)); // set Object-Id in car-instance
		return (ObjectId)doc.get("_id");           // 2nd alternative of get
	}

	public ArrayList<Car> getAllCars() throws Exception {
		ArrayList<Car> collCars = new ArrayList<>();
		MongoCollection<Document> collection = mongoDatabase.getCollection(COLLECTION_CARS);

		for (Document doc : collection.find()
				.sort(orderBy(descending("year"), ascending("name")))) {
			collCars.add(generateCarFromDoc(doc));
		}

		return collCars;
	}

	public Car generateCarFromDoc(Document doc) throws Exception {
		String jsonString = doc.toJson();
		Car car = gson.fromJson(jsonString, Car.class);
		car.setId((ObjectId)doc.get("_id"));
		return car;
	}

	public long deleteCar(Car car) throws Exception {
		if (car == null) {
			throw new Exception("no car selected");	
		}
		MongoCollection<Document> collection = mongoDatabase.getCollection(COLLECTION_CARS);
		DeleteResult dr = collection.deleteOne(eq("_id", car.getId()));
		
		return dr.getDeletedCount();
	}
	
	public long replaceCar(Car car) throws Exception {
		if (car == null) {
			throw new Exception("no car selected");
		}
		
		MongoCollection<Document> collection = mongoDatabase.getCollection(COLLECTION_CARS);
		String jsonString = gson.toJson(car);
		Document doc = Document.parse(jsonString);
		
		doc.remove("_id");
		UpdateResult ur = collection.replaceOne(eq("_id", car.getId()), doc);
		
		return ur.getModifiedCount();
	}

	public long updateCar(Car car) throws Exception {
		if (car == null) {
			throw new Exception("no car selected");
		}

		MongoCollection collection = mongoDatabase.getCollection(COLLECTION_CARS);
		String jsonString = gson.toJson(car);
		Document doc = Document.parse(jsonString);
		
		doc.remove("_id"); // old must not be updated
		UpdateResult ur = collection.updateOne(eq("_id", car.getId()), new Document ( "$set", doc));
		
		return ur.getModifiedCount();
	}

	public Car getCarById(ObjectId id) throws Exception {
		Car retCar = null;

		MongoCollection<Document> collection = mongoDatabase.getCollection(COLLECTION_CARS);
		Document doc = collection.find(eq("_id", id)).first();
		String jsonString = doc.toJson();
		retCar = gson.fromJson(jsonString, Car.class);
		retCar.setId(doc.get("_id", ObjectId.class));
		
		return retCar;
	}
	
	public ArrayList<Car> getCarsByYearHp(String year, String hp) throws Exception {
		ArrayList<Car> collCars = new ArrayList<>();
		
		MongoCollection<Document> collection = mongoDatabase.getCollection(COLLECTION_CARS);
		
		FindIterable<Document> coll = null;
		
		int intYear = year.isEmpty() ? 0 : Integer.parseInt(year);
		int intHp = hp.isEmpty() ? 0 : Integer.parseInt(hp);
		
		if (!year.isEmpty() && !hp.isEmpty()) {
			coll = collection.find(and(eq("hp", intHp), eq("year", intYear)))
				.sort(orderBy(descending("year"), ascending("name")));
		} else if (year.isEmpty() && !hp.isEmpty()) {
			coll = collection.find(eq("hp", intHp))
				.sort(orderBy(descending("year"), ascending("name")));
		} else if (!year.isEmpty() && hp.isEmpty()) {
			coll = collection.find(eq("year", intYear))
				.sort(orderBy(descending("year"), ascending("name")));
		}

		for (Document doc : coll) {
			collCars.add(generateCarFromDoc(doc));
		}

		return collCars;
	}
	
	public void createTextIndex() throws Exception {
		MongoCollection collection = mongoDatabase.getCollection(COLLECTION_CARS);
		collection.createIndex(Indexes.text("description"));
	}
	
	public ArrayList<Car> getAllCarsOrderByRelevance(String strFilter) throws Exception {
		ArrayList<Car> collCars = new ArrayList<>();
		
		MongoCollection<Document> collection = mongoDatabase.getCollection(COLLECTION_CARS);
		
		ArrayList<Document> collDocuments = 
			collection.find(new Document("$text",
				new Document("$search", strFilter)
					.append("$language", "english")
					.append("$caseSensitive", false)
					.append("$diacriticSensitive", false)))
			.projection(Projections.metaTextScore("score"))
			.sort(Sorts.metaTextScore("score"))
			.into(new ArrayList<>());

		for (Document doc : collDocuments) {
			collCars.add(generateCarFromDoc(doc));
		}
		
		return collCars;
	}
	
}

