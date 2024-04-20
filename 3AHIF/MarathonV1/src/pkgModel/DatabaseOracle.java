package pkgModel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class DatabaseOracle {
	
	private static DatabaseOracle db = null;   //singleton
	private String oraUserName = null, 
				   oraPassword = null;
	private Connection conn = null;
	private final String CONNSTRING = "jdbc:oracle:thin:@//tcif.htl-villach.at:1521/orcl";
	private final String SELECT_RACERS = 
		"SELECT ID, racername, racermale, racetype, racertime FROM racer WHERE upper(racername) LIKE upper(?) ORDER BY racetype, racername";
	private final String INSERT_RACER =
		"INSERT INTO racer VALUES(sqRacer.NEXTVAL, ?, ?, ?, ?)";
	private final String DELETE_RACER =
		"DELETE FROM racer WHERE id = ?";	
	private final String UPDATE_RACER =
		"UPDATE racer " +
		"SET racername = ?, racermale = ?, racetype = ?, racertime = ? " +
		"where id = ?";

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

	public List<Racer> getRacers(String filter) throws Exception {
		createConnection();
		
		ArrayList<Racer> collRacers = new ArrayList<>();

		PreparedStatement selectRacersWithFilter = conn.prepareStatement(SELECT_RACERS);
		selectRacersWithFilter.setString(1, "%" + filter + "%");
		ResultSet rs = selectRacersWithFilter.executeQuery();

		while (rs.next()) {
			collRacers.add(new Racer(
				rs.getInt("id"),
				rs.getString("racername"),
				rs.getString("racermale").equals("1"),
				rs.getString("racetype"),
				rs.getTime("racertime").toLocalTime()
			));
		}

		conn.close();

		return collRacers;
	}

	public void addRacer(Racer newRacer) throws Exception {
		createConnection();

		PreparedStatement insertRacerQuery = conn.prepareStatement(INSERT_RACER);
		insertRacerQuery.setString(1, newRacer.getName());
		insertRacerQuery.setString(2, newRacer.isMale() ? "1" : "0");
		insertRacerQuery.setString(3, newRacer.getType());
		insertRacerQuery.setTime(4, Time.valueOf(newRacer.getTime()));
		
		if (insertRacerQuery.executeUpdate() == 0) {
			throw new Exception("Racer with id " + newRacer.getId() + " not found");
		}

		conn.close();
	}

	public void deleteRacer(Racer racer) throws Exception {
		createConnection();

		PreparedStatement deleteRacerQuery = conn.prepareStatement(DELETE_RACER);
		deleteRacerQuery.setInt(1, racer.getId());

		if (deleteRacerQuery.executeUpdate() == 0) {
			throw new Exception("Racer with id " + racer.getId() + " not found");
		}

		conn.close();
	}

	public void updateRacer(Racer currentRacer, Racer updatedRacer) throws Exception {
		createConnection();

		PreparedStatement updateRacerQuery = conn.prepareStatement(UPDATE_RACER);
		updateRacerQuery.setString(1, updatedRacer.getName());
		updateRacerQuery.setString(2, updatedRacer.isMale() ? "1" : "0");
		updateRacerQuery.setString(3, updatedRacer.getType());
		updateRacerQuery.setTime(4, Time.valueOf(updatedRacer.getTime()));
		updateRacerQuery.setInt(5, currentRacer.getId());
		
		if (updateRacerQuery.executeUpdate() == 0) {
			throw new Exception("Racer with id " + currentRacer.getId() + " not found");
		}

		conn.close();
	}
}