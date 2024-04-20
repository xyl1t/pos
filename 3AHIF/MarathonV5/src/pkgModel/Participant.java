package pkgModel;

import java.time.LocalDate;
import java.time.LocalTime;

import javafx.scene.image.Image;

public class Participant {
	private String name;
	private LocalDate date;
	private String type;
	private LocalTime time;
	private int rank;

	public Participant(String name, LocalDate date, String type, LocalTime time, int rank) {
		super();
		this.name = name;
		this.date = date;
		this.type = type;
		this.time = time;
		this.rank = rank;
	}

	public String getName() {
		return name;
	}

	public LocalDate getDate() {
		return date;
	}

	public String getType() {
		return type;
	}

	public LocalTime getTime() {
		return time;
	}

	public int getRank() {
		return rank;
	}

	@Override
	public String toString() {
		return "Participant [name=" + name + ", date=" + date + ", type=" + type + ", time=" + time + ", rank=" + rank
				+ "]";
	}
}
