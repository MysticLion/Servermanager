package Listener;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

import BanManagerpkg.BanManager;
import Main.Banmain;
import Ränge.MySQLRänge;

public class PlayerLogin implements Listener {	
	private Banmain plugin;
	public PlayerLogin(Banmain plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerLogin(PlayerLoginEvent e) {
		Player p = e.getPlayer();
		if(MySQLRänge.getRank(p.getUniqueId().toString()) == 0) {
			MySQLRänge.setRank(p.getUniqueId().toString(), p.getName(), 1);
		}
		if(BanManager.isBanned(p.getUniqueId().toString())) {
			long current = System.currentTimeMillis();
			long end = BanManager.getEnd(p.getUniqueId().toString());
				if(end == -1) {
				e.disallow(Result.KICK_BANNED, "" + ChatColor.RED + "Du bist vom Server gebannt! \n Grund: " + ChatColor.BOLD + BanManager.getReason(p.getUniqueId().toString()) + ChatColor.RESET + "\n" +
						ChatColor.RED + "Verbleibende Zeit: " + ChatColor.BOLD + ChatColor.DARK_PURPLE + "PERMANENT" + ChatColor.RED
						+ ChatColor.MAGIC + "\nAAA " + ChatColor.RESET + ChatColor.DARK_PURPLE + "Du wurdest von einem Administrator gebannt ===> Kein Entbannungsantrag!" 
						+ ChatColor.DARK_RED + ChatColor.BOLD + ChatColor.MAGIC + " AAA");
				} else {
					if(current < end) {
					e.disallow(Result.KICK_BANNED, "" + ChatColor.RED + "Du bist vom Server gebannt! \n Grund: " + ChatColor.BOLD + BanManager.getReason(p.getUniqueId().toString()) + ChatColor.RESET + "\n" +
							ChatColor.RED + "Verbleibende Zeit: " + ChatColor.BOLD + BanManager.getRemainingTime(p.getUniqueId().toString())
							+ ChatColor.MAGIC + "\nAAA " + ChatColor.RESET + ChatColor.DARK_PURPLE + "Du wurdest von einem Administrator gebannt ===> Kein Entbannungsantrag!" 
							+ ChatColor.DARK_RED + ChatColor.BOLD + ChatColor.MAGIC + " AAA");
				}
			}
		}
	}
}
