package BanManagerpkg;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.event.player.PlayerLoginEvent.Result;

import MySQL.MySQL;

public class BanManager {

	public static void ban(String uuid, String playername, String reason, long seconds) {
		long end = 0;
		if(seconds == -1) {
			end = -1;
		} else {
			long millis = seconds*1000;
			long current = System.currentTimeMillis();
			end = current + millis;
		}
		MySQL.update("INSERT INTO BannedPlayers (Spielername, UUID, Ende, Grund) VALUES ('" + playername + "','" + uuid + "', '" + end + "', '" + reason + "')");
		if(Bukkit.getPlayer(playername) != null) {
			if(getEnd(uuid) == - 1) {
				Bukkit.getPlayer(playername).kickPlayer(ChatColor.RED + "Du bist vom Server gebannt! \n Grund: " + ChatColor.BOLD + BanManager.getReason(uuid)+ ChatColor.RESET + "\n" +
						ChatColor.RED + "Verbleibende Zeit: " + ChatColor.BOLD + ChatColor.DARK_PURPLE + ChatColor.BOLD + "PERMANENT" + ChatColor.RESET + ChatColor.RED
						+ ChatColor.MAGIC + "\nAAA " + ChatColor.RESET + ChatColor.DARK_PURPLE + "Du wurdest von einem Administrator gebannt ===> Kein Entbannungsantrag!" 
						+ ChatColor.DARK_RED + ChatColor.BOLD + ChatColor.MAGIC + " AAA");
			} else {
				Bukkit.getPlayer(playername).kickPlayer(ChatColor.RED + "Du wurdest vom Server gebannt! \n Grund: " + ChatColor.BOLD + getReason(uuid) + ChatColor.RESET + "\n" +
						ChatColor.RED + "Verbleibende Zeit: " + ChatColor.BOLD + getRemainingTime(uuid)
						+ ChatColor.MAGIC + "\nAAA " + ChatColor.RESET + ChatColor.DARK_PURPLE + "Du wurdest von einem Administrator gebannt ===> Kein Entbannungsantrag!" 
						+ ChatColor.DARK_RED + ChatColor.BOLD + ChatColor.MAGIC + " AAA");
			}
		}
	}
	
	public static void unban(String uuid) {
		MySQL.update("DELETE FROM BannedPlayers WHERE UUID='" + uuid + "'");
	}
	
	public static Boolean isBanned(String uuid) {
		ResultSet rs = MySQL.getResult("SELECT ENDE FROM BannedPlayers WHERE UUID='" + uuid + "'");
		try {
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static String getReason(String uuid) {
		ResultSet rs = MySQL.getResult("SELECT * FROM BannedPlayers WHERE UUID='" + uuid + "'");
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
		ResultSet rs = MySQL.getResult("SELECT * FROM BannedPlayers WHERE UUID='" + uuid + "'");
		try {
			while(rs.next()) {
				return rs.getLong("ENDE");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static List<String> getBannedPlayers() {
		List<String> list = new ArrayList<String>();
		ResultSet rs = MySQL.getResult("SELECT * FROM BannedPlayers");
		try {
			while(rs.next()) {
				list.add(rs.getString("Spielername"));
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
