package Main;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import Commands.BanCommands;
import Commands.MuteCommands;
import Commands.TabListCommands;
import Listener.MuteListener;
import Listener.PlayerJoinListener;
import Listener.PlayerLogin;
import MySQL.MySQL;
import MySQL.MySQLconfig;

public class Banmain extends JavaPlugin {

	
	public String Banprefix = "[" + ChatColor.RED + "Ban" + ChatColor.RESET + "] "; 
	public String TabPrefix = "[" + ChatColor.GREEN + "Tab" + ChatColor.RESET + "] "; 
	public String WerbungPrefix = "[" + ChatColor.LIGHT_PURPLE + "Werbung" + ChatColor.RESET + "] "; 
	public String NickPrefix = "[" + ChatColor.AQUA + "Nick" + ChatColor.RESET + "] ";
	public String gameModePrefix = "[" + ChatColor.BLUE + "GameMode" + ChatColor.RESET + "] ";
	public String BroadcastPrefix = "[" + ChatColor.RED + "Broadcast" + ChatColor.RESET + "] ";
	
	public HashMap<String, String> nick = new HashMap<String, String>();
	
	public void onEnable() {
		System.out.println("Plugin aktiviert");
		MySQLconfig FileManager = new MySQLconfig();
		FileManager.setStandard();
		FileManager.readData();
		MySQL.connect();
		MySQL.createTable();
		registerCommands();
		Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(this), this);
		Bukkit.getPluginManager().registerEvents(new PlayerLogin(this), this);
		Bukkit.getPluginManager().registerEvents(new MuteListener(this), this);
	}
	
	public void registerCommands() {
		BanCommands banCMD = new BanCommands(this);
		MuteCommands muteCMD = new MuteCommands(this);
		TabListCommands tabCMD = new TabListCommands(this);
		getCommand("kick").setExecutor(banCMD);
		getCommand("ban").setExecutor(banCMD);
		getCommand("tempban").setExecutor(banCMD);
		getCommand("unban").setExecutor(banCMD);
		getCommand("bancheck").setExecutor(banCMD);
		getCommand("setrank").setExecutor(tabCMD);
		getCommand("nick").setExecutor(tabCMD);
		getCommand("broadcast").setExecutor(tabCMD);
		getCommand("gamemode").setExecutor(tabCMD);
		getCommand("mute").setExecutor(muteCMD);
		getCommand("tempmute").setExecutor(muteCMD);
		getCommand("unmute").setExecutor(muteCMD);
		getCommand("mutecheck").setExecutor(muteCMD);
		getCommand("keywords").setExecutor(muteCMD);
	}
	
	

	
	public void onDisable() {
		MySQL.disconnect();
		System.out.println("Plugin deaktiviert");
	}
}
