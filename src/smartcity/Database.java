package smartcity;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

	private static final String DB_URL = "jdbc:mariadb://localhost:3306/smartcity";
	private static final String USERNAME = "smartcity";
	private static final String PASSWORD = "smartcity";
	
	private static Database instance;
	
	private Connection connection;

	public Database() throws SQLException {
		this.connection = DriverManager.getConnection(Database.DB_URL, Database.USERNAME, Database.PASSWORD);
	}
	
	public Connection getConnection() {
		return this.connection;
	}
	
	public static Database getInstance() throws SQLException {
		return Database.instance != null ? Database.instance : (Database.instance = new Database());
	}
}
