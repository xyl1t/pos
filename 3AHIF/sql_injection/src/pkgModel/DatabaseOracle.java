package pkgModel;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.ListView;

public class DatabaseOracle {
	private static DatabaseOracle db = null;

	private String oraUserName = null;
	private String oraPassword = null;
	private static final String CONNSTRING = 
		"jdbc:oracle:thin:@//tcif.htl-villach.at:1521/orcl";
	private Connection conn = null;
	
	private static final String SELECT_CARS =
		"SELECT idCar, nameCar, dateCar FROM cars ORDER BY nameCar";
	private static final String INSERT_CAR =
		"INSERT INTO cars VALUES(sqCar.NEXTVAL, ?, ?)";
	private static final String SELECT_LATEST_CAR =
		"select idCar, nameCar, dateCar from cars " +
		"where idcar = (select max(idcar) from cars)";
	private static final String DELET_CAR = "DELETE FROM cars WHERE idCar = ?";
	private static final String UPDATE_CAR =
		"UPDATE cars " +
		"SET nameCar = ?, dateCar = ? " +
		"WHERE idCar = ?";
	private static final String SELECT_CARS_ID =
		"SELECT idCar, nameCar, dateCar FROM cars WHERE idCar = ?";

	private static final String SELECT_MEMBER =
		"SELECT * FROM member WHERE username = ? and password = ?";

	private static final String SELECT_MEMBER_2 =
		"SELECT COUNT(*) as cnt FROM member WHERE username = ? and password = ?";

	public static DatabaseOracle getInstance(String user, String password) throws Exception {
		if (db == null) {
			db = new DatabaseOracle(user, password);
		}
		return db;
	}

	private DatabaseOracle(String user, String password) throws Exception {
		this.oraUserName = user;
		this.oraPassword = password;
	}
	
	public void createConnection() throws Exception {
		if (conn == null) {
			DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
		}
		conn = DriverManager.getConnection(CONNSTRING, oraUserName, oraPassword);
	}
	
	public void login(String username, String password) throws Exception {

		createConnection();

		PreparedStatement selectCarStmt = conn.prepareStatement(SELECT_MEMBER_2);
		selectCarStmt.setString(1, username);
		selectCarStmt.setString(2, password);
		ResultSet rs = selectCarStmt.executeQuery();
		
		if (!rs.next()) {
			throw new Exception("login failed");
		}
		
		conn.close();
	}

	public boolean isLoginOK(String username, String password) throws Exception {
		boolean isOK = false;

		createConnection();
		PreparedStatement stmt = conn.prepareStatement(SELECT_MEMBER_2);
//		selectCarStmt.setString(1, username);
//		selectCarStmt.setString(2, password);
//		ResultSet rs = selectCarStmt.executeQuery();
//		if (rs.next() && rs.getInt(1) > 0) {
//			isOK = true;
//		}

		conn.close();

		return isOK;
	}
	
	public List<Car> getAllCars() throws Exception {
		ArrayList<Car> collCars = new ArrayList<>(); 

		createConnection();
		Statement stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, 
			ResultSet.CONCUR_READ_ONLY);
		ResultSet rs = stmt.executeQuery(SELECT_CARS);

		while (rs.next()) {
			Car car = new Car(rs.getInt("idCar"), rs.getString("nameCar"), 
				rs.getDate("dateCar").toLocalDate());
			collCars.add(car);
		}
		conn.close();

		return collCars;
	}
	
	public Car addCar(Car car) throws Exception {
		Car retCar = null;

		createConnection();

		// used when there are variable parameters `?`
		PreparedStatement stmt = conn.prepareStatement(INSERT_CAR);
		stmt.setString(1, car.getName());
		stmt.setDate(2, Date.valueOf(car.getDate()));
		stmt.executeUpdate();
		// uberflussig
//		int countUpdated = stmt.executeUpdate();
//		if (countUpdated != 1) throw ...
		
		retCar = getLastCar();

		conn.close();
		
		return retCar;
	}

	public void deleteCar(int idCar) throws Exception {
		createConnection();

		PreparedStatement stmt = conn.prepareStatement(DELET_CAR);
		stmt.setInt(1, idCar);
		if (stmt.executeUpdate() == 0) {
			throw new Exception("Car with id " + idCar + " not found!");
		}

		conn.close();
	}

	// public void updateCar(int idCar, String newName, LocalDate newDate) throws Exception {
	public Car updateCar(Car updatedCar) throws Exception {
		createConnection();

		Car oldCar = getCarWithName(updatedCar.getName());

		PreparedStatement stmt = conn.prepareStatement(UPDATE_CAR);
		stmt.setString(1, updatedCar.getName());
		stmt.setDate(2, Date.valueOf(updatedCar.getDate()));
		stmt.setInt(3, updatedCar.getId());
		if (stmt.executeUpdate() == 0) {
			throw new Exception("Car with id " + updatedCar.getId() + " not found!");
		}

		conn.close();

		return oldCar;
	}

	private Car getLastCar() throws Exception {
		Car latestCar = null;

		Statement stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY,
			ResultSet.CONCUR_READ_ONLY);
		ResultSet rs = stmt.executeQuery(SELECT_LATEST_CAR);

		if (rs.next()) {
			latestCar = new Car(rs.getInt("idCar"), rs.getString("nameCar"),
				rs.getDate("dateCar").toLocalDate());
		}

		return latestCar;
	}

	private Car getCarWithName(String name) throws SQLException {

		PreparedStatement selectCarStmt = conn.prepareStatement(SELECT_CARS_ID);
		selectCarStmt.setString(1, name);
		ResultSet rs = selectCarStmt.executeQuery(SELECT_CARS);

		Car car = null;
		if (rs.next()) {
			car = new Car(rs.getInt("idCar"), rs.getString("nameCar"),
					rs.getDate("dateCar").toLocalDate());
		}

		return car;
	}
}
