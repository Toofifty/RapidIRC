package me.toofifty.plugins.rapidirc;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class RapidIRC extends JavaPlugin {

	private static Connector playerbot;
	
	public void loadConfiguration() {
		getConfig().addDefault("Username", "RapidIRC");
		getConfig().options().copyDefaults(true);
		saveConfig();
	}
	
	public void onEnable() {
		new RapidIRCListener(this);
		loadConfiguration();
		getServer().getPluginManager().registerEvents(new RapidIRCListener(this), this);
    	this.setPlayerbot(new Connector());
	}

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        this.getPlayerbot().disconnect();
    }
    
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
    	this.getPlayerbot().sendIRCMessage("#rapidcraft", event.getMessage());
    }
    
    public void onIRCChat(String user, String msg) {
		Bukkit.broadcastMessage("[IRC]<" + user + "> " + msg);
    }
    
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
    	if(cmd.getName().equalsIgnoreCase("irc")){
    		// irc commands
    		return true;
    	}
    	return false;
    }

	public static Connector getPlayerbot() {
		return playerbot;
	}

	public void setPlayerbot(Connector playerbot) {
		this.playerbot = playerbot;
	}
}
