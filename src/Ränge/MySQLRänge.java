package Ränge;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;

import BanManagerpkg.BanManager;
import MySQL.MySQL;

public class MySQLRänge {
	
	public static void setRank(String uuid, String playername, int rank) {
		MySQL.update("DELETE FROM Ranks WHERE UUID='" + uuid + "'");
		MySQL.update("INSERT INTO Ranks (Spielername, UUID, Rang) VALUES ('" + playername + "','" + uuid + "', '" + rank + "')");
	}
	
	public static int getRank(String uuid) {
		ResultSet rs = MySQL.getResult("SELECT * FROM Ranks WHERE UUID='" + uuid + "'");
		try {
			while(rs.next()) {
				return rs.getInt("Rang");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
}
