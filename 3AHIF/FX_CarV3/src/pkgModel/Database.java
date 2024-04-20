package pkgModel;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.TreeMap;

import javax.swing.filechooser.FileNameExtensionFilter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class Database {
	private TreeMap<String, Car> collCars;
	private Gson gson;
	public static final String FILENAME = "db";

	public Database() throws Exception {
		collCars = new TreeMap<>();
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
	public void deleteRepair(String carName, LocalDate repairDate) throws Exception {
		Car c = collCars.get(carName);
		if (c == null) {
			throw new Exception("Car with name " + carName + " not found.");
		}

		c.deleteRepair(repairDate);
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
	
	public void save() throws Exception {
		FileOutputStream fos = new FileOutputStream(FILENAME + ".bin");
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		
		oos.writeObject(collCars);
		oos.writeObject(Repair.getRepairId());
		
		fos.close();
	}
	
	@SuppressWarnings("unchecked")
	public void load() throws Exception {
		FileInputStream fis = new FileInputStream(FILENAME + ".bin");
		ObjectInputStream ois = new ObjectInputStream(fis);

		TreeMap<String, Car> temp = (TreeMap<String, Car>)ois.readObject();
		Repair.setRepairId((int) ois.readObject());
		
		collCars.clear();
		collCars.putAll(temp);

		ois.close();
		fis.close();
	}

	public void saveToJson() throws Exception {
		FileWriter fw = new FileWriter(FILENAME + ".json");
		gson.toJson(collCars, fw);

		FileWriter fwRepairId = new FileWriter(FILENAME + "_repairId.txt");
		fwRepairId.write(String.valueOf(Repair.getRepairId()));

		fw.flush();
		fw.close();
		fwRepairId.flush();
		fwRepairId.close();
	}
	
	public void loadFromJson() throws Exception {
		FileReader fr = new FileReader(FILENAME + ".json");

		Type collType = new TypeToken<TreeMap<String, Car>>(){}.getType();
		TreeMap<String, Car> newCars = gson.fromJson(fr, collType);
		fr.close();

		collCars.clear();
		collCars.putAll(newCars);

		// setNextRepairId();
        BufferedReader repairIdReader = new BufferedReader(new FileReader(FILENAME + "_repairId.txt"));

		int repairId = Integer.parseInt(repairIdReader.readLine());
		Repair.setRepairId(repairId);

		fr.close();
		repairIdReader.close();
	}
	
	public void update(String name, String id, String strDate) throws Exception {
		if (!collCars.containsKey(name)) {
			throw new Exception("Car with name " + name + " not stored.");
		}
		this.deleteCar(name);
		this.addCar(new Car(id, name, strDate));
	}
	
	public void addRepairToCar(String carName, Repair r) throws Exception {
		if (!collCars.containsKey(carName)) {
			throw new Exception("Car with name " + carName + " not stored.");
		}
		collCars.get(carName).addRepair(r);
	}
	public void updateRepair(String repairName, Repair r) throws Exception {
//		if (!collCars.containsKey(repairName)) {
//			throw new Exception("repair with name " + repairName + " not stored.");
//		}
		collCars.get(repairName).updateRepair(r);
	}
	
	private void setNextRepairId() {
		for (Car car : collCars.values()) {
			for (Repair rep : car.getRepairs()) {
				if (rep.getId() > Repair.getRepairId()) {
					Repair.setRepairId(rep.getId());
				}
			}
		}
		Repair.setRepairId(Repair.getRepairId() + 1);
	}
}
