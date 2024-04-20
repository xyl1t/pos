package pkgModel;

import org.bson.types.ObjectId;

public class Car {
	private ObjectId _id; // MongoDB uses ObjectID
	private String name;
	private int year;
	private int hp;
	private String description;

	public Car(String name, int hp, int year, String description) {
		this.name = name;
		this.hp = hp;
		this.year = year;
		this.description = description;
	}

	public ObjectId getId() {
		return _id;
	}
	public void setId(ObjectId _id) {
		this._id = _id;
	}

	@Override
	public String toString() {
		int length = Math.min(description.length(), 50);
		return name + ", year=" + year + ", hp=" + hp + ", description=" + description.substring(0, length);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
