package Commands;

import java.util.List;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import BanManagerpkg.BanManager;
import BanManagerpkg.BanUnit;
import Main.Banmain;
import Ränge.MySQLRänge;

public class BanCommands implements CommandExecutor {
	
	private Banmain plugin;
	public BanCommands(Banmain plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("kick")) {
			String reason = "";
			for(int i = 1;i < args.length;i++) {
				reason += args[i] + " ";
			}
			Bukkit.getPlayer(args[0]).kickPlayer(plugin.Banprefix + ChatColor.RED + "Du wurdest gekickt! \n \n Grund: " + ChatColor.DARK_RED + ChatColor.BOLD + reason);
		}
		if(cmd.getName().equalsIgnoreCase("unban")) {
			if(sender instanceof Player) {
				if(MySQLRänge.getRank(((Player) sender).getUniqueId().toString()) == 3) {
				} else {
					sender.sendMessage(plugin.Banprefix + "Du hast nicht die benötigten Rechte!");
					return true;
				}
			} 
			if(args.length == 1) {
				String playername = args[0];
				if(BanManager.isBanned(getUUID(playername))) {
					if(BanManager.getEnd(getUUID(args[0])) == -1) {
					sender.sendMessage(plugin.Banprefix + ChatColor.GREEN + "Ban (" + ChatColor.GOLD + playername + ChatColor.GREEN + ") aufgehoben");
					BanManager.unban(getUUID(playername));
					return true;
					} else {
						sender.sendMessage(plugin.Banprefix + ChatColor.GREEN + "Temporärer Ban (" + ChatColor.GOLD + playername + ChatColor.GREEN + ") aufgehoben");
						BanManager.unban(getUUID(playername));
						return true;
					}
				} else {
					sender.sendMessage(plugin.Banprefix + ChatColor.RED + "Dieser Spieler ist nicht gebannt!");
				}
				return true;
			} else {
				sender.sendMessage(plugin.Banprefix + ChatColor.RED + "/unban <Spieler>");
				return true;
			}				
		}
		
		if(cmd.getName().equalsIgnoreCase("ban")) {
			if(sender instanceof Player) {
				if(MySQLRänge.getRank(((Player) sender).getUniqueId().toString()) == 3) {
				} else {
					sender.sendMessage(plugin.Banprefix + "Du hast nicht die benötigten Rechte!");
					return true;
				}
			} 
			if(args.length >= 2) {
				String playername = args[0];
				if(BanManager.isBanned(getUUID(playername))) {
					sender.sendMessage(plugin.Banprefix + ChatColor.RED + "Dieser Spieler ist bereits gebannt!");
				} else {
					String reason = "";
					for(int i = 1;i < args.length;i++) {
						reason += args[i] + " ";
					}
					BanManager.ban(getUUID(playername), playername, reason, -1);
					sender.sendMessage(plugin.Banprefix + ChatColor.GREEN + "Ban (" + ChatColor.GOLD + playername + ChatColor.GREEN + ") erstellt.");
				}
				return true;
			} else {
				sender.sendMessage(plugin.Banprefix + ChatColor.RED + "/ban <Spieler> <Grund>");
				return true;
			}				
		}
		
		if(cmd.getName().equalsIgnoreCase("tempban")) {
			if(sender instanceof Player) {
				if(MySQLRänge.getRank(((Player) sender).getUniqueId().toString()) == 3) {
				} else {
					sender.sendMessage(plugin.Banprefix + "Du hast nicht die benötigten Rechte!");
					return true;
				}
			} 
			if(args.length >= 4) {
				String playername = args[0];
				if(BanManager.isBanned(getUUID(playername))) {
					sender.sendMessage(plugin.Banprefix + ChatColor.RED + "Dieser Spieler ist bereits gebannt!");
				} else {
					long value = 0;
					String unitString = args[2];
					String reason = "";
					try {
						value = Integer.valueOf(args[1]);
					} catch(NumberFormatException e) {
						sender.sendMessage(plugin.Banprefix + ChatColor.RED + "<Dauer> muss eine Zahl sein!");
						return true;
					}
					if(value >= 500) {
						sender.sendMessage(plugin.Banprefix + ChatColor.RED + "Die Zahl muss unter 500 liegen!");
					}
					for(int i = 3;i < args.length;i++) {
						reason += args[i] + " ";
					}
					List<String> units = BanUnit.getUnitsAsString();
					if(units.contains(unitString.toLowerCase())) {
						BanUnit unit = BanUnit.getUnit(unitString);
						long seconds = value*unit.getToSecond();
						BanManager.ban(getUUID(playername), playername, reason, seconds);
						sender.sendMessage(plugin.Banprefix + ChatColor.GREEN + "Ban (" + ChatColor.GOLD + playername + " für " + value + " " + unit.getName() + ChatColor.GREEN + ") erstellt.");
						return true;
					} else {
						sender.sendMessage(plugin.Banprefix + ChatColor.RED + "Diese <Einheit> existiert nicht!");
					}
					return true;
					}
			} else {
				sender.sendMessage(plugin.Banprefix + ChatColor.RED + "/tempban <Spieler> <Dauer> <Einheit> <Grund>");
				return true;
			}		
		}
			if(cmd.getName().equalsIgnoreCase("bancheck")) {
				if(sender instanceof Player) {
					if(MySQLRänge.getRank(((Player) sender).getUniqueId().toString()) == 2 || MySQLRänge.getRank(((Player) sender).getUniqueId().toString()) == 3) {
					} else {
						sender.sendMessage(plugin.Banprefix + "Du hast nicht die benötigten Rechte!");
						return true;
					}
				} 
				if(args.length < 3 && args.length > 0) {
					if(args.length == 1) {
						if(args[0].equalsIgnoreCase("list")) {
							List<String> bans = BanManager.getBannedPlayers();
							if(bans.size() == 0) {
								sender.sendMessage(plugin.Banprefix + ChatColor.BLUE + "Momentan gibt es keine gebannten Spieler");
								return true;
							} else {
							sender.sendMessage(plugin.Banprefix + ChatColor.BLUE + "---------Ban-Liste---------");
							for(String allBanned: BanManager.getBannedPlayers()) {
								sender.sendMessage(plugin.Banprefix + ChatColor.BLUE + allBanned);
							}
						return true;
							}
						} else {
							sender.sendMessage(plugin.Banprefix + ChatColor.RED + "/bancheck <list>");
						}	
					} else {
						if(args[1].equalsIgnoreCase("Grund") || args[1].equalsIgnoreCase("Ende")) {
							if(args[1].equalsIgnoreCase("Grund")) {
								if(BanManager.isBanned(getUUID(args[0]))) {
								sender.sendMessage(plugin.Banprefix + ChatColor.GOLD + BanManager.getReason(getUUID(args[0])));
								} else {
									sender.sendMessage(plugin.Banprefix + ChatColor.RED + "Der Spieler ist nicht gebannt ==> /bancheck list");
								}
							}
							if(args[1].equalsIgnoreCase("Ende")) {
								if(BanManager.isBanned(getUUID(args[0]))) {
								sender.sendMessage(plugin.Banprefix + ChatColor.GOLD + BanManager.getRemainingTime(getUUID(args[0])));
								} else {
									sender.sendMessage(plugin.Banprefix + ChatColor.RED + "Der Spieler ist nicht gebannt ==> /bancheck list");
								}
							}
						
						} else {
							sender.sendMessage(plugin.Banprefix + ChatColor.RED + "/bancheck <Spieler> <Grund/Ende>");
						}
					return true;
					}
				} else { 
					sender.sendMessage(plugin.Banprefix + ChatColor.RED + "/bancheck <list> oder /bancheck <Spieler> <Grund/Ende>");
				
			}
			return true;
			}
		return false;
	}
	
	private String getUUID(String playername) {
		return Bukkit.getOfflinePlayer(playername).getUniqueId().toString();
	}

}
