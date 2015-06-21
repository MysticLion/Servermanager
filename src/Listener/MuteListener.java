package Listener;

import java.util.List;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import BanManagerpkg.MuteManager;
import Main.Banmain;
import Ränge.MySQLRänge;

public class MuteListener implements Listener {
	private Banmain plugin;
	public MuteListener(Banmain plugin) {
		this.plugin = plugin;
	}
	
	private String[] Playerprefix = new String[]{ChatColor.GREEN + "", ChatColor.RED + "Mod §7| " + ChatColor.RED, ChatColor.DARK_RED + "Admin §7| " + ChatColor.DARK_RED,
		  ChatColor.DARK_PURPLE + "YT §7| " + ChatColor.DARK_PURPLE, ChatColor.GOLD + "Premium §7| " + ChatColor.GOLD};
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		if(MuteManager.isBanned(p.getUniqueId().toString())) {
			if(MuteManager.getEnd(p.getUniqueId().toString()) < System.currentTimeMillis() && MuteManager.getEnd(p.getUniqueId().toString()) != -1) {
				MuteManager.unban(p.getUniqueId().toString());
			}
		}
		if(MuteManager.isBanned(p.getUniqueId().toString())) {
			e.setCancelled(true);
			p.sendMessage(plugin.Banprefix + ChatColor.RED + "Du wurdest aus dem Chat gebannt (gemutet)");
			p.sendMessage(plugin.Banprefix + ChatColor.RED + "Grund: " + ChatColor.BOLD + MuteManager.getReason(p.getUniqueId().toString()));
			p.sendMessage(plugin.Banprefix + ChatColor.RED + "Verbleibende Zeit: " + MuteManager.getRemainingTime(p.getUniqueId().toString()));
		} else {
			String msg = e.getMessage();
			e.setCancelled(true);
			if(!(plugin.nick.containsKey(p.getName()))) {
				Bukkit.broadcastMessage(Playerprefix[MySQLRänge.getRank(p.getUniqueId().toString()) - 1] + p.getName() + "§7: " + ChatColor.GRAY + msg);
			} else {
				Bukkit.broadcastMessage(p.getPlayerListName() + "§7: " + msg);
			}
			if(msg.contains(".")) {
				for(Player all : Bukkit.getOnlinePlayers()) {
					if(MySQLRänge.getRank(all.getUniqueId().toString()) == 3) {
						all.sendMessage(plugin.WerbungPrefix + ChatColor.AQUA + msg);
					}
				}
			}
			List<String> deniedWords = MuteManager.getKeywords();
			for(String words : deniedWords) {
				if(msg.toLowerCase().contains(words)) {
					MuteManager.mute(p.getUniqueId().toString(), p.getName(), MuteManager.getMuteReason(words), MuteManager.getStrength(words));
				}
			}		
		}
	}
}
