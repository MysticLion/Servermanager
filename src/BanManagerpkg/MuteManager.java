package BanManagerpkg;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.event.player.PlayerLoginEvent.Result;

import MySQL.MySQL;

public class MuteManager {

	public static void mute(String uuid, String playername, String reason, long seconds) {
		long end = 0;
		if(seconds == -1) {
			end = -1;
		} else {
			long millis = seconds*1000;
			long current = System.currentTimeMillis();
			end = current + millis;
		}
		MySQL.update("INSERT INTO MutedPlayers (Spielername, UUID, Ende, Grund) VALUES ('" + playername + "','" + uuid + "', '" + end + "', '" + reason + "')");
	}
	
	public static void unban(String uuid) {
		MySQL.update("DELETE FROM MutedPlayers WHERE UUID='" + uuid + "'");
	}
	
	public static Boolean isBanned(String uuid) {
		ResultSet rs = MySQL.getResult("SELECT ENDE FROM MutedPlayers WHERE UUID='" + uuid + "'");
		try {
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static String getReason(String uuid) {
		ResultSet rs = MySQL.getResult("SELECT * FROM MutedPlayers WHERE UUID='" + uuid + "'");
		try {
			while(rs.next()) {
				return rs.getString("GRUND");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static Long getEnd(String uuid) {
		ResultSet rs = MySQL.getResult("SELECT * FROM MutedPlayers WHERE UUID='" + uuid + "'");
		try {
			while(rs.next()) {
				return rs.getLong("ENDE");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static long getStrength(String Keyword) {
		ResultSet rs = MySQL.getResult("SELECT * FROM Keywords WHERE Wort='" + Keyword + "'");
		try {
			while(rs.next()) {
				return rs.getLong("Stärke");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public static String getMuteReason(String Keyword) {
		ResultSet rs = MySQL.getResult("SELECT * FROM Keywords WHERE Wort='" + Keyword + "'");
		try {
			while(rs.next()) {
				return rs.getString("Grund");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static List<String> getMutedPlayers() {
		List<String> list = new ArrayList<String>();
		ResultSet rs = MySQL.getResult("SELECT * FROM MutedPlayers");
		try {
			while(rs.next()) {
				list.add(rs.getString("Spielername"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public static List<String> getKeywords() {
		List<String> list = new ArrayList<String>();
		ResultSet rs = MySQL.getResult("SELECT * FROM Keywords");
		try {
			while(rs.next()) {
				list.add(rs.getString("Wort"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public static String getRemainingTime(String uuid) {
		long current = System.currentTimeMillis();
		long end = getEnd(uuid);
		long different = end - current;
		
		long seconds = 0;
		long minutes = 0;
		long hours = 0;
		long days = 0;
		long weeks = 0;
		
		while(different > 1000) {
			different -= 1000;
			seconds++;
		}
		while(seconds > 60) {
			seconds -= 60;
			minutes++;
		}
		while(minutes > 60) {
			minutes -= 60;
			hours++;
		}
		while(hours > 24) {
			hours -= 24;
			days++;
		}
		while(days > 7) {
			days -= 7;
			weeks++;
		}
		if(end == -1) {
			return ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "PERMANENT";
		} else {
		return ChatColor.DARK_RED + "" + weeks + " Woche(n) " + days + " Tag(e) " + hours + " Stunde(n) " + minutes +  " Minuten " + seconds + " Sekunde(n)";
		}
	}
	
}
