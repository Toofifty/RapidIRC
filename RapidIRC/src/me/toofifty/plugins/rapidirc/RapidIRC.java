package me.toofifty.plugins.rapidirc;

import java.io.IOException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;

public class RapidIRC extends JavaPlugin {

	public static Connector bot;
	public static String channel;
	public static String server;
	public static String nick;

	public void loadConfiguration() {
		getConfig().addDefault("Username", "RapidIRC");
		getConfig().addDefault("Server", "irc.esper.net");
		getConfig().addDefault("Channel", "#rapidcraft");
		getConfig().options().copyDefaults(true);
		nick = getConfig().getString("Username");
		server = getConfig().getString("Server");
		channel = getConfig().getString("Channel");
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
			bot.makeBot(nick);
			bot.connect(server);
			bot.joinChannel(channel);
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
		} else if (cmd.getName().equalsIgnoreCase("imsg")) {
			String msg = "";
			for (String word : args) {
				msg += word + " ";
			}
			String target = args[0];
			msg = msg.replace(args[0] + " ", "");
			bot.sendPrivateMessage(target, sender.getName(), msg);
			return true;
		} else if (cmd.getName().equalsIgnoreCase("ircop")) {
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
