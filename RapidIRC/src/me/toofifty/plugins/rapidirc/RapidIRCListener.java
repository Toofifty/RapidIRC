package me.toofifty.plugins.rapidirc;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitScheduler;


class RapidIRCListener implements Listener {
	
	private final RapidIRC plugin;
	
	public RapidIRCListener(RapidIRC plugin) {
		this.plugin = plugin;
	}
	
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
    	
    	BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.runTaskLaterAsynchronously(this.plugin, new Runnable() {
    		
    		@Override
    		public void run() {
    			RapidIRC.getPlayerbot().makeBot(event.getPlayer().getDisplayName());
    		}
        }, 20L);

    	try {
    		playerbot.setVerbose(true);
    		playerbot.connect("irc.esper.net");
	    	//bot.sendMessage("nickserv","IDENTIFY <your password>" );
    		playerbot.joinChannel("#rapidcraft");
    		playerbot.sendRawMessage("#rapidcraft", "/me is connecting from the Rapid server.");
    	} catch (Exception e) {
    		e.printStackTrace();
		}
    	
    }
	
}