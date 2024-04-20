package pkgModel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DatabaseOracle {
	
	private static DatabaseOracle db = null;   //singleton
	private String oraUserName = null,
				   oraPassword = null;
	private Connection conn = null;
	private final String CONNSTRING = "jdbc:oracle:thin:@//tcif.htl-villach.at:1521/orcl";
	private final String SELECT_PARTICIPANTS =
		"select race.id as raceid, racer.id as racerid, racername, racermale, racetype, racertime from racer " +
		"inner join participation on participation.idRacer = racer.id " +
		"inner join race on race.id = participation.idRace " +
		"where upper(racername) LIKE upper(?) and race.id = ? " +
		"order by racetype, racername";
	private final String ADD_PARTICIPANT_TO_RACE =
		"INSERT INTO participation VALUES(?,?,?,?)";
	private final String REMOVE_PARTICIPANT_FROM_RACE =
		"DELETE FROM participation WHERE idRace = ? AND idRacer = ?";
	private final String INSERT_RACER =
		"INSERT INTO racer VALUES(sqRacer.NEXTVAL, ?, ?, ?, ?, ?)";
	private final String DELETE_RACER =
		"DELETE FROM racer WHERE id = ?";	
	private final String UPDATE_RACER =
		"UPDATE racer " +
		"SET racername = ?, racermale = ? " +
		"WHERE id = ?";
	private final String SELECT_ALL_RACES = 
		"SELECT id, racename, racedate FROM race";

	public static DatabaseOracle getInstance(String user, String password) throws Exception {
	
		if(db == null)
			db = new DatabaseOracle(user, password);
		
		return db;
	}
	
	private DatabaseOracle(String user, String password) {
		this.oraUserName = user;
		this.oraPassword = password;
	}
	
	public void createConnection() throws Exception {
		if(conn == null) {
			DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
		}
		conn = DriverManager.getConnection(CONNSTRING, oraUserName, oraPassword);
	}

	public List<Racer> getRacers(String filter, int raceId) throws Exception {
		createConnection();
		
		ArrayList<Racer> collRacers = new ArrayList<>();

		PreparedStatement selectRacersWithFilter = conn.prepareStatement(SELECT_PARTICIPANTS);
		selectRacersWithFilter.setString(1, "%" + filter + "%");
		selectRacersWithFilter.setInt(2, raceId);
		ResultSet rs = selectRacersWithFilter.executeQuery();

		while (rs.next()) {
			collRacers.add(new Racer(
				rs.getInt("racerid"),
				rs.getString("racername"),
				rs.getString("racermale").equals("1"),
				rs.getString("racetype"),
				rs.getTime("racertime").toLocalTime()
			));
		}

		conn.close();

		return collRacers;
	}
	
	public void removeParticipantFromRace(Racer participant, Race r) throws Exception {
		createConnection();
		
		PreparedStatement insertRacerQuery = conn.prepareStatement(REMOVE_PARTICIPANT_FROM_RACE);
		insertRacerQuery.setInt(1, r.getId());
		insertRacerQuery.setInt(2, participant.getRacerId());

		if (insertRacerQuery.executeUpdate() == 0) {
			throw new Exception("Could not delete participant");
		}
		
		conn.close();
	}

	public void addParticipantToRace(Racer participant, Race r) throws Exception {
		createConnection();
		
		PreparedStatement insertRacerQuery = conn.prepareStatement(ADD_PARTICIPANT_TO_RACE);
		insertRacerQuery.setInt(1, r.getId());
		insertRacerQuery.setInt(2, participant.getRacerId());
		insertRacerQuery.setString(3, participant.getType());
		insertRacerQuery.setTime(4, Time.valueOf(participant.getTime()));

		if (insertRacerQuery.executeUpdate() == 0) {
			throw new Exception("Could not add participant");
		}
		
		conn.close();
	}

//	public void addRacer(Participant newRacer) throws Exception {
//		createConnection();
//
//		PreparedStatement insertRacerQuery = conn.prepareStatement(INSERT_RACER);
//		insertRacerQuery.setString(1, newRacer.getName());
//		insertRacerQuery.setString(2, newRacer.isMale() ? "1" : "0");
//		insertRacerQuery.setString(3, newRacer.getType());
//		insertRacerQuery.setTime(4, Time.valueOf(newRacer.getTime()));
//		insertRacerQuery.setInt(5, newRacer.getRaceId());
//		
//		if (insertRacerQuery.executeUpdate() == 0) {
//			throw new Exception("Racer with id " + newRacer.getRacerId() + " not found");
//		}
//
//		conn.close();
//	}

	public void deleteRacer(Racer racer) throws Exception {
		createConnection();

		PreparedStatement deleteRacerQuery = conn.prepareStatement(DELETE_RACER);
		deleteRacerQuery.setInt(1, racer.getRacerId());

		if (deleteRacerQuery.executeUpdate() == 0) {
			throw new Exception("Racer with id " + racer.getRacerId() + " not found");
		}

		conn.close();
	}

	public void updateRacer(Racer currentRacer, Racer updatedRacer) throws Exception {
		createConnection();

		PreparedStatement updateRacerQuery = conn.prepareStatement(UPDATE_RACER);
		updateRacerQuery.setString(1, updatedRacer.getName());
		updateRacerQuery.setString(2, updatedRacer.isMale() ? "1" : "0");
		updateRacerQuery.setInt(3, currentRacer.getRacerId());
		
		if (updateRacerQuery.executeUpdate() == 0) {
			throw new Exception("Racer with id " + currentRacer.getRacerId() + " not found");
		}

		conn.close();
	}

	public Collection<Race> getAllRaces() throws Exception {
		createConnection();

		Statement stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY,
			ResultSet.CONCUR_READ_ONLY);
		ResultSet rs = stmt.executeQuery(SELECT_ALL_RACES);

		ArrayList<Race> races = new ArrayList<>();
		while (rs.next()) {
			races.add(new Race(rs.getInt("id"), rs.getString("racename"), rs.getDate("racedate").toLocalDate()));
		}

		conn.close();

		return races;
	}
}
