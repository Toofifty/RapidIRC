package me.toofifty.plugins.rapidirc;

import java.io.IOException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;

public class RapidIRC extends JavaPlugin {

	public static Connector bot;

	public void loadConfiguration() {
		getConfig().addDefault("Username", "RapidIRC");
		getConfig().addDefault("Server", "irc.esper.net");
		getConfig().addDefault("Channel", "#rapidcraft");
		getConfig().options().copyDefaults(true);
		saveConfig();
	}

	public void onEnable() {
		loadConfiguration();
		getServer().getPluginManager().registerEvents(new GameListener(), this);
		this.setBot(new Connector());
		createBot();
	}

	public void onDisable() {
		bot.disconnect();
	}

	public void createBot() {
		try {
			bot.setVerbose(true);
			bot.makeBot(getConfig().getString("Username"));
			bot.connect("irc.esper.net");
			bot.joinChannel("#rapidcraft");
			// bot.sendMessage("nickserv", "IDENTIFY <your password>");
		} catch (NickAlreadyInUseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IrcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("irc")) {
			// irc commands
			return true;
		}
		return false;
	}

	public void setBot(Connector bot) {
		RapidIRC.bot = bot;
	}
	
	public void ircToMcChat(String chat) {
		
	}
}
