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
		RapidIRC.bot.sendIRCMessage(ChatColor.stripColor(event.getJoinMessage()));
	}

	@EventHandler
	public void onPlayerDisconnect(PlayerQuitEvent event) {
		RapidIRC.bot.sendIRCMessage(ChatColor.stripColor(event.getQuitMessage()));
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