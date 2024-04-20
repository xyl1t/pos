package pkgModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.TreeMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class Database {
	private TreeMap<String, Car> collCars;
	private TreeMap<LocalDate, Repair> collRepairs;
	private Gson gson;

	public Database() throws Exception {
		collCars = new TreeMap<>();
		collRepairs = new TreeMap<>();
		// gson = new Gson();
		gson = new GsonBuilder()
				.setPrettyPrinting()
				.enableComplexMapKeySerialization()
				.create();
	}
	
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
	public void deleteRepair(String id) throws Exception{
		if(collRepairs.remove(id) == null) {
			throw new Exception("Repair with id " + id + " not found");
		}
	}
	
	public Collection<Car> getCars() {
		return collCars.values();
	}
	public Collection<Repair> getRepairs(String carName) throws Exception {
		if (!collCars.containsKey(carName)) {
			throw new Exception("Car with name " + carName + " not found");
		}
		return collCars.get(carName).getRepairs();
	}
	
	public void save(String filename) throws Exception {
		FileOutputStream fos = new FileOutputStream(filename);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		File repairIdFile = new File("repairIds.txt");
		FileWriter fw = new FileWriter(repairIdFile);
		
		oos.writeObject(collCars);
		oos.writeObject(Repair.getNextRepairId());
		fw.write(String.valueOf(Repair.getNextRepairId()));
		
		fos.close();
		fw.close();
	}
	
	@SuppressWarnings("unchecked")
	public void load(String filename) throws Exception {
		FileInputStream fis = new FileInputStream(filename);
		ObjectInputStream ois = new ObjectInputStream(fis);
		

		TreeMap<String, Car> temp = (TreeMap<String, Car>)ois.readObject();
		Repair.setNextRepairId((int) ois.readObject());
		
		collCars.clear();
		collCars.putAll(temp);

		ois.close();
		fis.close();
	}

	public void saveToJson(String filename) throws Exception {
		FileWriter fw = new FileWriter(filename);

		gson.toJson(collCars, fw);

		fw.flush();
		fw.close();
	}
	
	public void loadFromJson(String filename) throws Exception {
		FileReader fr = new FileReader(filename);

		Type collType = new TypeToken<TreeMap<String, Car>>(){}.getType();
		TreeMap<String, Car> newCars = gson.fromJson(fr, collType);
		fr.close();
		
		collCars.clear();
		collCars.putAll(newCars);
		
		setNextRepairId();
	}
	
	
	public void updateRepair(String carName, String id, LocalDate date, String text, String amount) throws Exception {
		
		if(!collRepairs.containsKey(date)) {
			throw new Exception("Repair with date " + date + " not stored.");
		}else {
			collRepairs.get(date).setText(text);
			collRepairs.get(date).setAmount(new BigDecimal (amount));
			
		}
	}
	public void updateCar(String name, String id, String strDate) throws Exception {
		Car car = collCars.get(name);
		if (car == null) {
			throw new Exception("Car with name " + name + " not stored.");
		}
		car.setDate(strDate);
		this.deleteCar(name);
		this.addCar(new Car(id, name, strDate));
	}
	public void addRepairToCar(String carName, Repair r) throws Exception {
		if (!collCars.containsKey(carName)) {
			throw new Exception("Car with name " + carName + " not stored.");
		}
		collRepairs.put(r.getDate(), r);
	}
	
	private void setNextRepairId() {
		for (Car car : collCars.values()) {
			for (Repair rep : car.getRepairs()) {
				if (rep.getId() > Repair.getNextRepairId()) {
					Repair.setNextRepairId(rep.getId());
				}
			}
		}
		Repair.setNextRepairId(Repair.getNextRepairId()+1);

	}
	
	
}
