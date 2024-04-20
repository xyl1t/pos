package pkgMisc;

import java.util.Comparator;

import pkgData.Team;

public class AlternateComparator implements Comparator<Team> {
	@Override
	public int compare(Team arg0, Team arg1) {
		int returnValue = arg1.getPoints() - arg0.getPoints();
		return returnValue;
	}
}
