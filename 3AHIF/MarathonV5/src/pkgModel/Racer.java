package pkgModel;

import java.time.LocalTime;

import javafx.scene.image.Image;

public class Racer {
	private int id;
	private String name;
	private boolean isMale;
	private String type;
	private LocalTime time;
	private Image image;

	public Racer(int id, String name, boolean isMale, String type, LocalTime time, Image image) {
		this.id = id;
		this.name = name;
		this.isMale = isMale;
		this.type = type;
		this.time = time;
		this.image = image;
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
	
	public Image getImage() {
		return image;
	}
	
	@Override
	public String toString() {
		return "Racer [(" + id + ") " + name + ", male=" + isMale + ", " + type + ", " + time + "]";
	}
}
