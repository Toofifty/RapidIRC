package me.toofifty.plugins.rapidirc;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class GameListener implements Listener {
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		RapidIRC.bot.sendIRCMessage("#rapidcraft", ChatColor.stripColor(event.getJoinMessage()));
	}

	@EventHandler
	public void onPlayerDisconnect(PlayerQuitEvent event) {
		RapidIRC.bot.sendIRCMessage("#rapidcraft", ChatColor.stripColor(event.getQuitMessage()));
	}

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		RapidIRC.bot.sendIRCMessage("#rapidcraft", "<" + event.getPlayer().getDisplayName() + "> " + ChatColor.stripColor(event.getMessage()));
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		RapidIRC.bot.sendIRCMessage("#rapidcraft", ChatColor.stripColor(event.getDeathMessage()));
	}

}