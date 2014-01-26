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

	private Connector playerbot;

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        getLogger().info("New thingy made");
        this.playerbot.disconnect();
    }
    
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
    	this.playerbot.sendIRCMessage("#rapidcraft", event.getMessage());
    }
    
    public void onIRCChat(String user, String msg) {
    	for (Player player : Bukkit.getOnlinePlayers()) {
    		player.sendMessage("[IRC]<" + user + "> " + msg);
    	}
    }
    
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
    	if(cmd.getName().equalsIgnoreCase("irc")){
    		// irc commands
    		return true;
    	}
    	return false;
    }
}

class IRCListener implements Listener {
	
	private final RapidIRC plugin;
	
	public IRCListener(RapidIRC plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Create the task anonymously and schedule to run once in 20 ticks.
    	new BukkitRunnable {
    		
    		@Override
    		public void run() {
    	    	this.playerbot = new Connector(event.getPlayer().getDisplayName());
    			
    		}
    		
    	}.runTaskLater(this.plugin, 20);
/*
    	try {
    		playerbot.setVerbose(true);
    		playerbot.connect("irc.esper.net");
	    	//bot.sendMessage("nickserv","IDENTIFY <your password>" );
    		playerbot.joinChannel("#rapidcraft");
    		playerbot.sendRawMessage("#rapidcraft", "/me is connecting from the Rapid server.");
    	} catch (Exception e) {
    		e.printStackTrace();
		}*/
    }

	
}
