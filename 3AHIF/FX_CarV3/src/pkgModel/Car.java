package pkgModel;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.TreeMap;

public class Car implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private String name;
	private LocalDate date;
	private final TreeMap<LocalDate, Repair> collRepairs = new TreeMap<>();
	
	public Car(int id, String name, LocalDate date) {
		this.id = id;
		this.name = name;
		this.date = date;
	}

	public Car(String id, String name, String strDate) {
		this.id = Integer.parseInt(id);
		this.name = name;
		this.date = LocalDate.parse(strDate, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
	}
	
	public void addRepair(Repair r) throws Exception {
		if (collRepairs.containsKey(r.getDate())) {
			throw new Exception("Repair with date " + r.getDate() + " already stored");
		}
		collRepairs.put(r.getDate(), r);
	}

	public void updateRepair(Repair r) throws Exception {
		Repair repair = collRepairs.get(r.getDate());
		if (repair == null) {
			throw new Exception("Repair with date " + r.getDate() + " not found");
		}

		repair.setText(r.getText());
		repair.setAmount(r.getAmount());
	}
	public void deleteRepair(LocalDate repairDate) throws Exception {
		if (collRepairs.remove(repairDate) == null) {
			throw new Exception("Repair with date " + repairDate + " not found");
		}
	}
	
	public Collection<Repair> getRepairs() {
		return collRepairs.values();
	} 

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public LocalDate getDate() {
		return date;
	}
	public void setId(int id) {
		this.id = id;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	
	public String getDateAsString() {
		return date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
	}
	
	@Override
	public String toString() {
		return id + ", " + name + ", " + getDateAsString();
	}
}
