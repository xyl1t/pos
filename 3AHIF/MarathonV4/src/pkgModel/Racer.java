package pkgModel;

import java.time.LocalTime;

public class Racer {
	private int racerId;
	private String name;
	private boolean isMale;
	private String type;
	private LocalTime time;

	public Racer(int racerId, String name, boolean isMale, String type, LocalTime time) {
		this.racerId = racerId;
		this.name = name;
		this.isMale = isMale;
		this.type = type;
		this.time = time;
	}

	public int getRacerId() {
		return racerId;
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
	
	@Override
	public String toString() {
		return "Racer [(" + racerId + ") " + name + ", male=" + isMale + ", " + type + ", " + time + "]";
	}
}
