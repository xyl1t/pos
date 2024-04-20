package pkgModel;

import java.io.FileInputStream;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeMap;

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
		"INSERT INTO cars VALUES(sqCar.NEXTVAL, ?, ?, NULL)";
	private static final String SELECT_LATEST_CAR = "select idCar, nameCar, dateCar from cars where idcar = (select max(idcar) from cars)";
	private static final String DELETE_CAR = "DELETE FROM cars WHERE idCar = ?";
	private static final String UPDATE_CAR =
		"UPDATE cars " +
		"SET nameCar = ?, dateCar = ? " +
		"WHERE idCar = ?";
	private static final String SELECT_CARS_ID =
		"SELECT idCar, nameCar, dateCar FROM cars WHERE idCar = ?";
	private static final String SELECT_RENTS_CAR_ID =
		"select customer, startdate, enddate, amount, idcar from rents where idCar = ?";
	private static final String INSERT_RENT =
		"insert into rents values(?, ?, ?, ?, ?)";
	private static final String DELETE_RENT =
		"delete from rents where startDate = ?";
	private static final String SELECT_REPAIRS_CAR_ID =
		"select idrepair, daterepair, textrepair, amount, idcar from repairs where idCar = ?";
	private static final String SELECT_MAX_REPAIR_ID =
		"select max(idRepair) from repairs";
	private static final String INSERT_REPAIR =
		"insert into repairs values(sqRepair.nextval, ?, ?, ?, ?)";
	private static final String DELETE_REPAIR =
		"delete from repairs where idRepair = ?";
	private static final String UPDATE_REPAIR =
		"update repairs set dateRepair = ?, textRepair = ?, amount = ? where idRepair = ?";
	private static final String SELECT_RENT_DATE_BETWEEN =
		"select startDate, endDate from rents" +
		"where ? between startDate and endDate" +
		"OR ? between startDate and endDate" +
		"OR startDate between ? and ?";
	private static final String SELECT_SUM_REPAIRS_BY_CAR =
		"select sum(amount) from repairs where idCar = ?";
	private static final String SELECT_SUM_RENTS_BY_CAR =
		"select sum(amount) from rents where idCar = ?";
	private static final String INSERT_IMAGE = 
		"UPDATE cars SET imageCar = ? WHERE idCar = ?";
	private static final String SELECT_IMAGE = 
		"SELECT imageCar FROM cars WHERE idCar = ?";
	
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

		PreparedStatement stmt = conn.prepareStatement(DELETE_CAR);
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
	
	public void storeCarImage(Car car, String imgPath) throws Exception {
		createConnection();
		
		PreparedStatement stmt = conn.prepareStatement(INSERT_IMAGE);
		FileInputStream fis = new FileInputStream(imgPath);
		stmt.setBinaryStream(1,  fis, fis.available());
		stmt.setInt(2,  car.getId());
		stmt.executeUpdate();

		conn.close();
	}

	public byte[] loadCarImage(Car car) throws Exception {
		byte[] arrByte = null;
		createConnection();
		
		PreparedStatement stmt = conn.prepareStatement(SELECT_IMAGE);
		stmt.setInt(1,  car.getId());

		ResultSet rs = stmt.executeQuery();
		if (rs.next()) {
			Blob blob = rs.getBlob("imageCar");
			arrByte = blob.getBytes(1, (int)blob.length()); // from 1st pos length x bytes
		}

		conn.close();
		return arrByte;
	}

	public Collection<Repair> getRepairs(int idCar) throws Exception {
		createConnection();
		PreparedStatement selectCarStmt = conn.prepareStatement(SELECT_REPAIRS_CAR_ID);
		selectCarStmt.setInt(1, idCar);
		ResultSet rs = selectCarStmt.executeQuery();

		ArrayList<Repair> rents = new ArrayList<>();
		while (rs.next()) {
			rents.add(new Repair(
				rs.getInt("idRepair"),
				rs.getDate("dateRepair").toLocalDate(),
				rs.getString("textRepair"),
				rs.getBigDecimal("amount")
			));
		}

		conn.close();

		return rents;
	}

	public void addRepair(int carId, Repair repair) throws Exception {
		createConnection();

		PreparedStatement stmt = conn.prepareStatement(INSERT_REPAIR);
		stmt.setInt(1, carId);
		stmt.setDate(2, Date.valueOf(repair.getDate()));
		stmt.setString(3, repair.getText());
		stmt.setBigDecimal(4, repair.getAmount());
		stmt.executeUpdate();

		conn.close();
	}

	public void deleteRepair(int repairId) throws Exception {
		createConnection();

		PreparedStatement stmt = conn.prepareStatement(DELETE_REPAIR);
		stmt.setInt(1, repairId);
		if (stmt.executeUpdate() == 0) {
			throw new Exception("Repair with id " + repairId + " not found!");
		}

		conn.close();
	}

	public void updateRepair(Repair rep) throws Exception {
		createConnection();

		PreparedStatement stmt = conn.prepareStatement(UPDATE_REPAIR);
		stmt.setDate(1, Date.valueOf(rep.getDate()));
		stmt.setString(2, rep.getText());
		stmt.setBigDecimal(3, rep.getAmount());
		stmt.setInt(4, rep.getId());
		if (stmt.executeUpdate() == 0) {
			throw new Exception("Repair with id " + rep.getId() + " not found!");
		}

		conn.close();
	}

	public int getNextRepairId() throws Exception {
		createConnection();

		Statement stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY,
				ResultSet.CONCUR_READ_ONLY);
		ResultSet rs = stmt.executeQuery(SELECT_MAX_REPAIR_ID);
		
		int nextRepairId = 0;

		if (rs.next()) {
			nextRepairId = rs.getInt(1)+1;
		}

		conn.close();

		return nextRepairId;
	}

	public Collection<Rent> getRents(int idCar) throws Exception {
		createConnection();
		PreparedStatement selectCarStmt = conn.prepareStatement(SELECT_RENTS_CAR_ID);
		selectCarStmt.setInt(1, idCar);
		ResultSet rs = selectCarStmt.executeQuery();

		TreeMap<LocalDate, Rent> rents = new TreeMap<>();
		while (rs.next()) {
			rents.put(
				rs.getDate("startDate").toLocalDate(),
				new Rent(rs.getString("customer"), rs.getDate("startDate").toLocalDate(),
					rs.getDate("endDate").toLocalDate(), rs.getDouble("amount")));
		}
		
		conn.close();

		return rents.values();
	}

	public void addRent(int carId, Rent newRent) throws Exception {
		createConnection();

		// Rent retRent = null;

		if (isRentOverlapping(newRent)) {
			throw new Exception("Car is already used");
		}

		PreparedStatement stmt = conn.prepareStatement(INSERT_RENT);
		stmt.setString(1, newRent.getCustomer());
		stmt.setDate(2, Date.valueOf(newRent.getStartDate()));
		stmt.setDate(3, Date.valueOf(newRent.getEndDate()));
		stmt.setDouble(4, newRent.getAmount());
		stmt.setInt(5, carId);
		stmt.executeUpdate();

		conn.close();

		// return retRent;
	}

	public void deleteRent(LocalDate startDate) throws Exception {
		createConnection();

		PreparedStatement stmt = conn.prepareStatement(DELETE_RENT);
		stmt.setDate(1, Date.valueOf(startDate));
		if (stmt.executeUpdate() == 0) {
			throw new Exception("Rent with start date " + startDate + " not found!");
		}

		conn.close();
	}

	private boolean isRentOverlapping(Rent r) throws Exception {
		PreparedStatement stmt = conn.prepareStatement(SELECT_RENT_DATE_BETWEEN);
		stmt.setDate(1, Date.valueOf(r.getStartDate()));
		stmt.setDate(2, Date.valueOf(r.getEndDate()));
		stmt.setDate(3, Date.valueOf(r.getStartDate()));
		stmt.setDate(4, Date.valueOf(r.getEndDate()));

		ResultSet rs = stmt.executeQuery();
		
		return rs.next();
	}

	public BigDecimal getSumRepairs(int carId) throws Exception {
		createConnection();

		PreparedStatement stmt = conn.prepareStatement(SELECT_SUM_REPAIRS_BY_CAR);
		stmt.setInt(1, carId);
		ResultSet rs = stmt.executeQuery();

		BigDecimal sum = new BigDecimal(0);
		if (rs.next()) {
			sum = rs.getBigDecimal(1);
		}
		conn.close();

		return sum;
	}

	public double getSumRents(int carId) throws Exception {
		createConnection();

		PreparedStatement stmt = conn.prepareStatement(SELECT_SUM_RENTS_BY_CAR);
		stmt.setInt(1, carId);
		ResultSet rs = stmt.executeQuery();

		double sum = 0;
		if (rs.next()) {
			sum = rs.getDouble(1);
		}
		conn.close();

		return sum;
	}



	// public Car addRentToCar(Rent r) throws Exception {
	// 	return retCar;
	// }

}
