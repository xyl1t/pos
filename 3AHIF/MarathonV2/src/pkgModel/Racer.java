package pkgModel;

import java.time.LocalTime;

public class Racer {
	private int id;
	private int raceId;
	private String name;
	private boolean isMale;
	private String type;
	private LocalTime time;

	public Racer(int id, int raceId, String name, boolean isMale, String type, LocalTime time) {
		this.id = id;
		this.raceId = raceId;
		this.name = name;
		this.isMale = isMale;
		this.type = type;
		this.time = time;
	}

	public int getId() {
		return id;
	}

	public int getRaceId() {
		return raceId;
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
