package pkgModel;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Car implements Serializable {
	private int id;
	private String name;
	private LocalDate date;
	
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
