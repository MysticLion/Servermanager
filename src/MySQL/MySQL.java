package MySQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQL {

		public static String host;
		public static String port;
		public static String database;
		public static String username;
		public static String password;
		public static Connection con;
		
		public static void connect(){
			if(!(isConnected())){
				try {
				con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
				System.out.println("[MySQL] Verbindung zu " + host + " " + port + " " + database + " " + username + "  aufgebaut");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		  }
		}
		
		public static void disconnect(){
		if(isConnected()){
			try {
				con.close();
				System.out.println("[MySQL] Verbindung zu " + host + " " + port + " " + database + " " + username + " getrennt!");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		}
		
		public static boolean isConnected(){
			return (con == null ? false : true);
		}
		public static Connection getConnection(){
			return con;
		}
		public static void createTable() {
			if(isConnected()) {
				try {
					con.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS BannedPlayers (Spielername VARCHAR(100), UUID VARCHAR(100), Ende VARCHAR(100), Grund VARCHAR(100))");
					con.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS MutedPlayers (Spielername VARCHAR(100), UUID VARCHAR(100), Ende VARCHAR(100), Grund VARCHAR(100))");
					con.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS Ranks (Spielername VARCHAR(100), UUID VARCHAR(100), Rang INT(100))");
					con.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS Keywords (Wort VARCHAR(100), Stärke INT(100), Grund VARCHAR(100))");
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		public static void update(String qry) {
			if(isConnected()) {
				try {
					con.createStatement().executeUpdate(qry);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		public static ResultSet getResult(String qry) {
			try {
				return con.createStatement().executeQuery(qry);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return null;
		}
}
