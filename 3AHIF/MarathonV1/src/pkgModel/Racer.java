package pkgModel;

import java.time.LocalTime;

public class Racer {
	private int id;
	private String name;
	private boolean isMale;
	private String type;
	private LocalTime time;

	public Racer(int id, String name, boolean isMale, String type, LocalTime time) {
		this.id = id;
		this.name = name;
		this.isMale = isMale;
		this.type = type;
		this.time = time;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public boolean isMale() {
		return isMale;
	}

	public String getType() {
		return type;
	}

	public LocalTime getTime() {
		return time;
	}

}
