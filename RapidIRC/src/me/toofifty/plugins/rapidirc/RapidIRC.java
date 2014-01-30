package me.toofifty.plugins.rapidirc;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;
import org.jibble.pircbot.User;

public class RapidIRC extends JavaPlugin {

	public static Connector bot;
	public static String channel;
	public static String server;
	public static String nick;
	public static String quitReason;
	public static List<String> ignoreMinecraft;
	public static List<String> ignoreIRC;

	public void loadConfiguration() {
		getConfig().addDefault("Username", "RapidIRC");
		getConfig().addDefault("Server", "irc.esper.net");
		getConfig().addDefault("Channel", "#rapidcraft");
		getConfig().addDefault("Quit Reason", "Server shutting down.");
		getConfig().addDefault("UserIgnore.Minecraft", Arrays.asList("Example"));
		getConfig().addDefault("UserIgnore.IRC", Arrays.asList("Example"));
		getConfig().options().copyDefaults(true);
		nick = getConfig().getString("Username");
		server = getConfig().getString("Server");
		channel = getConfig().getString("Channel");
		quitReason = getConfig().getString("Quit Reason");
		saveConfig();
	}

	public void onEnable() {
		loadConfiguration();
		getServer().getPluginManager().registerEvents(new GameListener(), this);
		ignoreMinecraft = getConfig().getStringList("UserIgnore.Minecraft");
		ignoreIRC = getConfig().getStringList("UserIgnore.IRC");
		this.setBot(new Connector());
		createBot();
	}

	public void onDisable() {
		bot.quitServer(quitReason);
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
			if (!(args.length < 1)) {
				if (args[0].equalsIgnoreCase("list")) {
					User[] list = bot.getUsers(channel);
					String ulist = null;
					for (User user : list) {
						ulist += user.getNick() + " ";
					}
					sender.sendMessage("Users currently in IRC: " + ulist);
				} else {
					return false;
				}
			} else {
				return false;
			}
			return true;
		} else if (cmd.getName().equalsIgnoreCase("imsg")) {
			if (args.length > 1) {
				String msg = null;
				for (String word : args) {
					msg += word + " ";
				}
				String target = args[0];
				msg = msg.replace(args[0] + " ", "");
				bot.sendPrivateMessage(target, sender.getName(), msg);
			} else {
				return false;
			}
			return true;
		} else if (cmd.getName().equalsIgnoreCase("ircop")) {
			return false;
		}
		return false;
	}

	public void setBot(Connector bot) {
		RapidIRC.bot = bot;
	}
}
