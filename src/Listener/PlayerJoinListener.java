package Listener;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import BanManagerpkg.BanManager;
import Commands.TabListCommands;
import Main.Banmain;
import Ränge.MySQLRänge;

public class PlayerJoinListener implements Listener {	
	private Banmain plugin;
	public PlayerJoinListener(Banmain plugin) {
		this.plugin = plugin;
	}
	
	private String[] Playerprefix = new String[]{ChatColor.GREEN + "", ChatColor.RED + "Mod §7| " + ChatColor.RED, ChatColor.DARK_RED + "Admin §7| " + ChatColor.DARK_RED,
		  ChatColor.DARK_PURPLE + "YT §7| " + ChatColor.DARK_PURPLE, ChatColor.GOLD + "Premium §7| " + ChatColor.GOLD};
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		if(MySQLRänge.getRank(p.getUniqueId().toString()) >= 2 && MySQLRänge.getRank(p.getUniqueId().toString()) <= 5) {
			p.setOp(true);
		} else {
			p.setOp(false);
		}
		e.setJoinMessage("§7> " + Playerprefix[MySQLRänge.getRank(p.getUniqueId().toString()) - 1] + p.getName() + "§9 hat den Server betreten!");
		if(BanManager.isBanned(p.getUniqueId().toString())) {
			BanManager.unban(p.getUniqueId().toString());
		}
		TabListCommands.setTab(p, MySQLRänge.getRank(p.getUniqueId().toString()));
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		e.setQuitMessage("§7> " + Playerprefix[MySQLRänge.getRank(p.getUniqueId().toString()) - 1] + p.getName() + "§9 hat den Server verlassen!");
	}
}