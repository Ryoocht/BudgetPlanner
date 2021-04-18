package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySqlConnect {
	public static Connection Connect() {
		try {
			String DATABASE_NAME = "budget_db";
			String PROPERTIES = "?charactorEncoding=UTF-8";
			String URL = "jdbc:mySQL://localhost/" + DATABASE_NAME + PROPERTIES;
			String USER = "root";
			String PASS = "ryoocht0826";

			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection conn = DriverManager.getConnection(URL, USER, PASS);
			return conn;
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
}
