package pkgData;

import java.lang.Comparable;

public class Team implements Comparable<Team> {
	private String name;
	private int points;
	private int goalsShot;
	private int goalsGot;

	public Team() {
		this(null, 0, 0);
	}	
	public Team(String name) {
		this(name, 0, 0);
	}
	public Team(String name, int goalsShot, int goalsGot) {
		this.name = name;
		this.goalsShot = goalsShot;
		this.goalsGot = goalsGot;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPoints() {
		return points;
	}
	public void setPoints(int points) {
		this.points = points;
	}
	public int getGoalsShot() {
		return goalsShot;
	}
	public void setGoalsShot(int goalsShot) {
		this.goalsShot = goalsShot;
	}
	public int getGoalsGot() {
		return goalsGot;
	}
	public void setGoalsGot(int goalsGot) {
		this.goalsGot = goalsGot;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Team other = (Team) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Team [name=" + name + ", points=" + points + ", goalsShot=" + goalsShot + ", goalsGot=" + goalsGot
				+ "]";
	}
	
	@Override
	public int compareTo(Team other) {
		return this.getName().compareTo(other.getName());
	}
}
