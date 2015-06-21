package Commands;

import java.util.List;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import BanManagerpkg.MuteManager;
import BanManagerpkg.BanUnit;
import Main.Banmain;
import MySQL.MySQL;
import Ränge.MySQLRänge;

public class MuteCommands implements CommandExecutor {
	
	private Banmain plugin;
	public MuteCommands(Banmain plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("keywords")) {
			if(sender instanceof Player) {
				if(MySQLRänge.getRank(((Player) sender).getUniqueId().toString()) >= 2) {
				} else {
					sender.sendMessage(plugin.Banprefix + "Du hast nicht die benötigten Rechte!");
					return true;
				}
			if(args.length != 4) {
				sender.sendMessage(plugin.Banprefix + ChatColor.RED + "/keywords <add/remove> <Wort> <Stärke> <Grund>");
				return true;
			} else {
				if(args[0].equalsIgnoreCase("add")) {
					MySQL.update("DELETE FROM Keywords WHERE Wort='" + args[1] + "'");
					MySQL.update("INSERT INTO Keywords (Wort, Stärke, Grund) VALUES ('" + args[1] + "','" + Integer.valueOf(args[2]) + "','" + args[3] + "')");
					sender.sendMessage(plugin.Banprefix + ChatColor.GREEN + "Keyword " + ChatColor.GOLD + args [1] + ChatColor.GREEN + " hinzugefügt");
					return true;
				} else if(args[0].equalsIgnoreCase("remove")) {
					MySQL.update("DELETE FROM Keywords WHERE UUID='" + args[1] + "'");
					return true;
				} else {
					sender.sendMessage(plugin.Banprefix + ChatColor.RED + "/keywords <add/remove> <Wort> <Stärke> <Grund>");
					return true;
				}
				}
			}
		}
		if(cmd.getName().equalsIgnoreCase("unmute")) {
			if(sender instanceof Player) {
				if(MySQLRänge.getRank(((Player) sender).getUniqueId().toString()) == 3) {
				} else {
					sender.sendMessage(plugin.Banprefix + "Du hast nicht die benötigten Rechte!");
					return true;
				}
			} 
			if(args.length == 1) {
				String playername = args[0];
				if(MuteManager.isBanned(getUUID(playername))) {
					if(MuteManager.getEnd(getUUID(args[0])) == -1) {
					sender.sendMessage(plugin.Banprefix + ChatColor.GREEN + "Mute (" + ChatColor.GOLD + playername + ChatColor.GREEN + ") aufgehoben");
					MuteManager.unban(getUUID(playername));
					return true;
					} else {
						sender.sendMessage(plugin.Banprefix + ChatColor.GREEN + "Temporärer Mute (" + ChatColor.GOLD + playername + ChatColor.GREEN + ") aufgehoben");
						MuteManager.unban(getUUID(playername));
						return true;
					}
				} else {
					sender.sendMessage(plugin.Banprefix + ChatColor.RED + "Dieser Spieler ist nicht gemutet!");
				}
				return true;
			} else {
				sender.sendMessage(plugin.Banprefix + ChatColor.RED + "/unmute <Spieler>");
				return true;
			}				
		}
		
		if(cmd.getName().equalsIgnoreCase("mute")) {
			if(sender instanceof Player) {
				if(MySQLRänge.getRank(((Player) sender).getUniqueId().toString()) == 3) {
				} else {
					sender.sendMessage(plugin.Banprefix + "Du hast nicht die benötigten Rechte!");
					return true;
				}
			} 
			if(args.length >= 2) {
				String playername = args[0];
				if(MuteManager.isBanned(getUUID(playername))) {
					sender.sendMessage(plugin.Banprefix + ChatColor.RED + "Dieser Spieler ist bereits gemutet!");
				} else {
					String reason = "";
					for(int i = 1;i < args.length;i++) {
						reason += args[i] + " ";
					}
					MuteManager.mute(getUUID(playername), playername, reason, -1);
					sender.sendMessage(plugin.Banprefix + ChatColor.GREEN + "Mute (" + ChatColor.GOLD + playername + ChatColor.GREEN + ") erstellt.");
				}
				return true;
			} else {
				sender.sendMessage(plugin.Banprefix + ChatColor.RED + "/mute <Spieler> <Grund>");
				return true;
			}				
		}
		
		if(cmd.getName().equalsIgnoreCase("tempmute")) {
			if(sender instanceof Player) {
				if(MySQLRänge.getRank(((Player) sender).getUniqueId().toString()) == 3) {
				} else {
					sender.sendMessage(plugin.Banprefix + "Du hast nicht die benötigten Rechte!");
					return true;
				}
			} 
			if(args.length >= 4) {
				String playername = args[0];
				if(MuteManager.isBanned(getUUID(playername))) {
					sender.sendMessage(plugin.Banprefix + ChatColor.RED + "Dieser Spieler ist bereits gemuted!");
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
						MuteManager.mute(getUUID(playername), playername, reason, seconds);
						sender.sendMessage(plugin.Banprefix + ChatColor.GREEN + "Mute (" + ChatColor.GOLD + playername + " für " + value + " " + unit.getName() + ChatColor.GREEN + ") erstellt.");
						return true;
					} else {
						sender.sendMessage(plugin.Banprefix + ChatColor.RED + "Diese <Einheit> existiert nicht!");
					}
					return true;
					}
			} else {
				sender.sendMessage(plugin.Banprefix + ChatColor.RED + "/tempmute <Spieler> <Dauer> <Einheit> <Grund>");
				return true;
			}		
		}
			if(cmd.getName().equalsIgnoreCase("mutecheck")) {
				if(sender instanceof Player) {
					if(MySQLRänge.getRank(((Player) sender).getUniqueId().toString()) == 2 ||MySQLRänge.getRank(((Player) sender).getUniqueId().toString()) == 3) {
					} else {
						sender.sendMessage(plugin.Banprefix + "Du hast nicht die benötigten Rechte!");
						return true;
					}
				} 
				if(args.length < 3 && args.length > 0) {
					if(args.length == 1) {
						if(args[0].equalsIgnoreCase("list")) {
							List<String> bans = MuteManager.getMutedPlayers();
							if(bans.size() == 0) {
								sender.sendMessage(plugin.Banprefix + ChatColor.BLUE + "Momentan gibt es keine gemuteten Spieler");
								return true;
							} else {
							sender.sendMessage(plugin.Banprefix + ChatColor.BLUE + "---------Mute-Liste---------");
							for(String allBanned: MuteManager.getMutedPlayers()) {
								sender.sendMessage(plugin.Banprefix + ChatColor.BLUE + allBanned);
							}
						return true;
							}
						} else {
							sender.sendMessage(plugin.Banprefix + ChatColor.RED + "/mutecheck <list>");
						}	
					} else {
						if(args[1].equalsIgnoreCase("Grund") || args[1].equalsIgnoreCase("Ende")) {
							if(args[1].equalsIgnoreCase("Grund")) {
								if(MuteManager.isBanned(getUUID(args[0]))) {
								sender.sendMessage(plugin.Banprefix + ChatColor.GOLD + MuteManager.getReason(getUUID(args[0])));
								} else {
									sender.sendMessage(plugin.Banprefix + ChatColor.RED + "Der Spieler ist nicht gemutet ==> /mutecheck list");
								}
							}
							if(args[1].equalsIgnoreCase("Ende")) {
								if(MuteManager.isBanned(getUUID(args[0]))) {
								sender.sendMessage(plugin.Banprefix + ChatColor.GOLD + MuteManager.getRemainingTime(getUUID(args[0])));
								} else {
									sender.sendMessage(plugin.Banprefix + ChatColor.RED + "Der Spieler ist nicht gebmutet ==> /mutecheck list");
								}
							}
						
						} else {
							sender.sendMessage(plugin.Banprefix + ChatColor.RED + "/mutecheck <Spieler> <Grund/Ende>");
						}
					return true;
					}
				} else { 
					sender.sendMessage(plugin.Banprefix + ChatColor.RED + "/mutecheck <list> oder /mutecheck <Spieler> <Grund/Ende>");
				
			}
			return true;
			}
		return false;
	}
	
	private String getUUID(String playername) {
		return Bukkit.getOfflinePlayer(playername).getUniqueId().toString();
	}

}
