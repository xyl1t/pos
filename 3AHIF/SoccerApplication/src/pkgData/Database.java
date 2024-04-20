package pkgData;
import java.util.ArrayList;
import java.util.Collections;

public class Database {
	private ArrayList<Team> collTeams = new ArrayList<Team>();
	
	public ArrayList<Team> getCollTeams() {
		return collTeams;
	}

	public void setCollTeams(ArrayList<Team> collTeams) {
		this.collTeams = collTeams;
	}

	public Database() {

	}

	public void addResult(String textline) throws IllegalArgumentException {
		// splits the given record into logical chunks:
		// data[0]: name of first team
		// data[1]: name of second team
		// data[2]: goals of first team
		// data[3]: goals of second team
		if (textline.matches("[A-z]+-[A-z]+,\\d+:\\d+")) {
			String[] data = textline.split("[-,:]");
			
			int goalsLeft = Integer.parseInt(data[2]);
			int goalsRight = Integer.parseInt(data[3]);
			
			updateTeam(data[0], goalsLeft, goalsRight);
			updateTeam(data[1], goalsRight, goalsLeft);
		} else {
			throw new IllegalArgumentException("Incorrect data");
		}
	}

	private void updateTeam(String name, int goalsShot, int goalsGot) {
		Team t = new Team(name, goalsShot, goalsGot);
		
		int idx = collTeams.indexOf(t);
		if (idx < 0) { // add team to database if it doesn't exist 
			collTeams.add(t);
		} else {
			t = collTeams.get(idx);
			t.setGoalsShot(t.getGoalsShot() + goalsShot);
			t.setGoalsGot(t.getGoalsGot() + goalsGot);
		}
		
		// set points based on goals
		if (goalsShot > goalsGot) {
			t.setPoints(t.getPoints() + 3);
		} else if (goalsShot == goalsGot) {
			t.setPoints(t.getPoints() + 1);
		}
	}
	
	public void clear() {
		collTeams.clear();
	}

	public void alternateSort(java.util.Comparator<Team> comparator) { 
		collTeams.sort(comparator);
	}
	// sort by name
	public void defaultSort() { 
		Collections.sort(collTeams);
	}
	
	public Team[] getTeams() { 
		return collTeams.toArray(new Team[collTeams.size()]);
	};
}
