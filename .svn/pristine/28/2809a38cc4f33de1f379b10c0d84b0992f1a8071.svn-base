/*
 * DBConnection.java Jul 12, 2012 1.0
 */
package edu.cmu.gizmo.management.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 * The Class DBConnection.
 */
public final class DBConnection {
	
	/** The conn. */
	public static Connection conn = null;
	
	/**
	 * Gets the connection.
	 *
	 * @param dbName the db name
	 * @return the connection
	 */
	public static Connection getConnection(String dbName){
		String url = "jdbc:mysql://128.2.204.91:3306/";
		String driver = "com.mysql.jdbc.Driver";
		String userName = "root"; 
		String password = "gizmorulez";

		try {
			Class.forName(driver).newInstance();
			conn = DriverManager.getConnection(url + dbName, userName, password);
			System.out.println("[DBConnection] getConnection(): Connected to the database");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}

	/**
	 * Close connection.
	 */
	public static void closeConnection() {
		if (conn != null) {
			try {
				conn.close();
				System.out.println("[DBConnection] closeConnection(): Disconnected from database");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
