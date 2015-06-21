package Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import Main.Banmain;
import Ränge.MySQLRänge;

public class TabListCommands implements CommandExecutor {
	
	private Banmain plugin;
	public TabListCommands(Banmain plugin) {
		this.plugin = plugin;
	}
	
	private static String[] prefix = new String[]{ChatColor.GREEN + "", ChatColor.GOLD + "Mod §7| " + ChatColor.GOLD, ChatColor.DARK_RED + "Admin §7| " + ChatColor.DARK_RED,
												  ChatColor.DARK_PURPLE + "YT §7| " + ChatColor.DARK_PURPLE, ChatColor.GOLD + "Premium §7| " + ChatColor.GOLD};

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("gamemode")) {
				if(args.length == 2) {
					GameMode[] gamemode = new GameMode[]{GameMode.SURVIVAL, GameMode.CREATIVE, GameMode.ADVENTURE, GameMode.SPECTATOR};
					if(Bukkit.getOfflinePlayer(args[1]).isOnline()) {
						Bukkit.getPlayer(args[1]).setGameMode(gamemode[Integer.valueOf(args[0])]);
						sender.sendMessage(plugin.gameModePrefix + ChatColor.GREEN + "Spielmodus des Spielers §6" + args[1] + "§a auf den Spielmodus §6" + gamemode[Integer.valueOf(args[0])] + "§a gesetzt");
						return true;
					} else {
						sender.sendMessage(plugin.gameModePrefix + ChatColor.RED + "Der Spieler §4" + args[1] + "§c ist §4nicht online!");
						return true;
					}
				} else {
					sender.sendMessage(plugin.gameModePrefix + ChatColor.RED + "/gamemode <mode> <Spieler>");
					return true;
			}
		}
		if(cmd.getName().equalsIgnoreCase("broadcast")) {
			if(sender instanceof Player) {
				if(MySQLRänge.getRank(((Player) sender).getUniqueId().toString()) >= 2 && MySQLRänge.getRank(((Player) sender).getUniqueId().toString()) <= 4) {
					} else {
						sender.sendMessage(plugin.BroadcastPrefix + "Du hast nicht die benötigten Rechte!");
						return true;
					}
				}
				String msg = "";
				for(int i = 0;i < args.length;i++) {
					msg += args[i] + " ";
				}
				Bukkit.broadcastMessage(plugin.BroadcastPrefix + ChatColor.GOLD + ChatColor.BOLD + msg);
				sender.sendMessage(plugin.BroadcastPrefix + ChatColor.GREEN + "Broadcast erstellt");
				return true;
		}
		if(cmd.getName().equalsIgnoreCase("nick")) {
			if(sender instanceof Player) {
				if(MySQLRänge.getRank(((Player) sender).getUniqueId().toString()) >= 2 && MySQLRänge.getRank(((Player) sender).getUniqueId().toString()) < 5) {
				} else {
					sender.sendMessage(plugin.NickPrefix + "Du hast nicht die benötigten Rechte!");
					return true;
				}
			if(sender instanceof Player) {
				Player p = (Player) sender;
			}
				if(args.length == 1) {
					if(args[0].equalsIgnoreCase("reset")) {
						Player p = (Player) sender;
						if(plugin.nick.containsKey(p.getName())) {
							plugin.nick.remove(p.getName());
							sender.sendMessage(plugin.NickPrefix + ChatColor.GREEN + "Nickname zurückgesetzt!");
							p.setPlayerListName(p.getName());
							p.setDisplayName(p.getName());
							setTab(p, MySQLRänge.getRank(p.getUniqueId().toString()));
							return true;
						} else {
							p.sendMessage(plugin.NickPrefix + ChatColor.RED + "Du besitzt augenblicklich keinen Nickname");
						}
						return true;
					} else {
						if(args[0].length() < 16) {
							Player p = (Player) sender;
							plugin.nick.put(p.getName(), args[0]);
							sender.sendMessage(plugin.NickPrefix + ChatColor.GREEN + "Nickname " + ChatColor.GOLD + args[0] + ChatColor.GREEN + " gesetzt!");
							p.setDisplayName(args[0]);
							p.setPlayerListName(ChatColor.GREEN + p.getDisplayName());
							p.setDisplayName(args[0]);
							return true;
						} else {
							sender.sendMessage(plugin.NickPrefix + ChatColor.RED + "<name> darf höchstens 16 Zeichen lang sein!");
							return true;
						}
					}
				} else {
					sender.sendMessage(plugin.NickPrefix + ChatColor.RED + "/nick <name>");
					return true;
				}
			} else {
				sender.sendMessage(plugin.NickPrefix + ChatColor.RED + "Du musst ein Spieler sein!");
			}
		}
		if(cmd.getName().equalsIgnoreCase("setRank")) {
			if(sender instanceof Player) {
				if(MySQLRänge.getRank(((Player) sender).getUniqueId().toString()) == 3) {
				} else {
					sender.sendMessage(plugin.TabPrefix + "Du hast nicht die benötigten Rechte!");
					return true;
				}
			} 
			Player target =	Bukkit.getPlayer(args[0]);
			if(args.length == 2) {
				int rang;
				try {
					rang = Integer.valueOf(args[1]);
				} catch(NumberFormatException e) {
					sender.sendMessage(plugin.TabPrefix + ChatColor.RED + "<Rang> muss eine Zahl sein!");
					return true;
				}
				if(rang > 0 && rang <= 5) {
					MySQLRänge.setRank(target.getUniqueId().toString(), target.getName(), rang);
					setTab(target, rang);
				sender.sendMessage(plugin.TabPrefix + ChatColor.GREEN + "Der Spieler wurde dem Rang " + args[1] + " zugewiesen!");
				return true;
				} else {
					sender.sendMessage(plugin.TabPrefix + ChatColor.GREEN + "Der Rang muss zwischen 1 und 5 liegen!");
					return true;
				}
			} else {
				sender.sendMessage(plugin.TabPrefix + ChatColor.RED + "/setrank <Spielername> <Rang>");
				return true;
			}
		}
		return false;
	}
	
	public static void setTab(Player p, int rank) {
				Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
				Team eins = board.getTeam("eins");
				Team zwei = board.getTeam("zwei");
				Team drei = board.getTeam("drei");
				Team vier = board.getTeam("vier");
				Team fünf = board.getTeam("fünf");
				Team[] team = {eins, zwei, drei, vier, fünf};
				if(eins == null) {
					eins = board.registerNewTeam("eins");
				}
				if(zwei == null) {
					zwei = board.registerNewTeam("zwei");
				}
				if(drei == null) {
					drei = board.registerNewTeam("drei");
				}
				if(vier == null) {
					vier = board.registerNewTeam("vier");
				}
				if(fünf == null) {
					fünf = board.registerNewTeam("fünf");
				}
				eins.setPrefix(prefix[0]);
				zwei.setPrefix(prefix[1]);
				drei.setPrefix(prefix[2]);
				vier.setPrefix(prefix[3]);
				fünf.setPrefix(prefix[4]);
				team[(MySQLRänge.getRank(p.getUniqueId().toString()))-1].addPlayer(p);
				for(Player all : Bukkit.getOnlinePlayers()) {
					all.setScoreboard(board);
				}
	}	
}
