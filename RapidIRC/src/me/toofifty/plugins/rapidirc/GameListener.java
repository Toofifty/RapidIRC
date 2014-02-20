package me.toofifty.plugins.rapidirc;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jibble.pircbot.User;

public class GameListener implements Listener {
	private java.util.List<String> ignoreIRC;

	public GameListener(java.util.List<String> ignoreIRC) {
		this.ignoreIRC = ignoreIRC;
	}

	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		RapidIRC.bot.sendIRCMessage(event.getJoinMessage());
		Player p = event.getPlayer();
		int numberOfIRCUsers = RapidIRC.bot.getUsers(RapidIRC.channel).length;
		for (User user : RapidIRC.bot.getUsers(RapidIRC.channel)) {
			if(ignoreIRC.contains(user.getNick())) {
				numberOfIRCUsers =- 1;
			}
		}
		p.sendMessage(ChatColor.GREEN + "There are " + numberOfIRCUsers + " people online in IRC.");
	}

	@EventHandler
	public void onPlayerDisconnect(PlayerQuitEvent event) {
		RapidIRC.bot.sendIRCMessage(event.getQuitMessage());
	}

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		if (event.getPlayer().hasPermission("rapidtools.chatcolors"))
			event.setMessage(ChatColor.translateAlternateColorCodes('&', event.getMessage()));
		if (!RapidIRC.ignoreMinecraft.contains(event.getPlayer().getDisplayName())) {
			RapidIRC.bot.sendIRCMessage("<" + event.getPlayer().getDisplayName() + "> " + event.getMessage());
		}
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		RapidIRC.bot.sendIRCMessage(event.getDeathMessage());
	}

}