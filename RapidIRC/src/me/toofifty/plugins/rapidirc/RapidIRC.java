package me.toofifty.plugins.rapidirc;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jibble.pircbot.User;

public class RapidIRC extends JavaPlugin {

	public static Connector bot;
	public static String channel;
	public static String server;
	public static String nick;
	public static String quitReason;
	public static String nickservPassword;
	public static String prefix;
	public static List<String> ignoreMinecraft;
	public static List<String> ignoreIRC;

	public void loadConfiguration() {
		getConfig().addDefault("Username", "RapidIRC");
		getConfig().addDefault("Server", "irc.esper.net");
		getConfig().addDefault("Channel", "#rapidcraft");
		getConfig().addDefault("Quit Reason", "Server shutting down.");
		getConfig().addDefault("NickServ Password", null);
		getConfig().addDefault("UserIgnore.Minecraft", Arrays.asList("Example"));
		getConfig().addDefault("UserIgnore.IRC", Arrays.asList("Example"));
		getConfig().addDefault("Command prefix", "~");
		getConfig().options().copyDefaults(true);
		nick = getConfig().getString("Username");
		server = getConfig().getString("Server");
		channel = getConfig().getString("Channel");
		quitReason = getConfig().getString("Quit Reason");
		prefix = getConfig().getString("Command prefix");
		nickservPassword = getConfig().getString("NickServ Password");
		ignoreMinecraft = getConfig().getStringList("UserIgnore.Minecraft");
		ignoreIRC = getConfig().getStringList("UserIgnore.IRC");
		saveConfig();
	}

	public void onEnable() {
		loadConfiguration();
		getServer().getPluginManager().registerEvents(new GameListener(), this);
		bot = new Connector();
		bot.createBot();
	}

	public void onDisable() {
		bot.quitServer(quitReason);
		bot.disconnect();
	}

	public void startBot() {
		bot = new Connector();
		bot.createBot();
	}

	public void stopBot(String reason) {
		bot.partChannel(channel, reason);
		bot.quitServer(reason);
		bot.disconnect();
		bot = null;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("irc")) {
			if (args.length > 0) {
				if (args[0].equalsIgnoreCase("list")) {
					User[] list = bot.getUsers(channel);
					String ulist = "";
					for (User user : list) {
						ulist += user.getNick() + " ";
					}
					sender.sendMessage("Users currently in IRC: " + ulist);
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else if (cmd.getName().equalsIgnoreCase("imsg")) {
			if (args.length > 1) {
				String msg = "";
				for (String word : args) {
					msg += word + " ";
				}
				String target = args[0];
				msg = msg.replace(args[0] + " ", "");
				bot.sendPrivateMessage(target, sender.getName(), msg);
				return true;
			} else {
				return false;
			}
		} else if (cmd.getName().equalsIgnoreCase("ircop")) {
			if (args.length < 1) {
				return false;
			} else if (args[0].equalsIgnoreCase("restart")) {
				stopBot("Restarting...");
				startBot();
				return true;
			} else if (args[0].equalsIgnoreCase("stop")) {
				if (bot != null) {
					if (args.length == 2) {
						stopBot(args[1]);
						return true;
					} else {
						stopBot("Stopping server");
						return true;
					}
				} else {
					sender.sendMessage("Bot is not running.");
					return true;
				}
			} else if (args[0].equalsIgnoreCase("start") && args.length == 1) {
				if (bot == null) {
					startBot();
					return true;
				} else {
					sender.sendMessage("Bot is already running.");
					return true;
				}
			} else {
				return false;
			}
		}
		return false;
	}
}
