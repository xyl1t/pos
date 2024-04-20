package pkgModel;

import java.time.LocalDate;

public class Race {
	private int id;
	private String name;
	private LocalDate date;

	public Race(int id, String name, LocalDate date) {
		super();
		this.id = id;
		this.name = name;
		this.date = date;
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

	@Override
	public String toString() {
		return id + ", " + name + ", " + date;
	}
	
}
