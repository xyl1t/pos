package pkgModel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.TreeMap;

public class Database {
	private TreeMap<String, Car> collCars = new TreeMap<>();
	
	public void addCar(Car car) throws Exception {
		if (collCars.put(car.getName(), car) != null) {
			throw new Exception("Car with name " + car.getName() + " already stored");
		}	
	}
	public void deleteCar(String name) throws Exception {
		if (collCars.remove(name) == null) {
			throw new Exception("Car with name " + name + " not found");
		}
	}
	
	public Collection<Car> getCars() {
		return collCars.values();
	}
	
	public void save(String filename) throws Exception {
		FileOutputStream fos = new FileOutputStream(filename);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		
		oos.writeObject(collCars);
		
		oos.close();
		fos.close();		
	}
	
	public void load(String filename) throws Exception {
		FileInputStream fis = new FileInputStream(filename);
		ObjectInputStream ois = new ObjectInputStream(fis);

		collCars = (TreeMap<String, Car>)ois.readObject();

		ois.close();
		fis.close();
	}
	
	public void update(String name, String id, String strDate) throws Exception {
		if (!collCars.containsKey(name)) {
			throw new Exception("Car with name " + name + " not stored.");
		}
		this.deleteCar(name);
		this.addCar(new Car(id, name, strDate));
		//collCars.get(name).setId(Integer.parseInt(id));
		//collCars.get(name).setDate(LocalDate.parse(strDate, DateTimeFormatter.ofPattern("dd.MM.yyyy")));
	}
}
