package me.toofifty.plugins.rapidirc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;
import org.jibble.pircbot.PircBot;
import org.jibble.pircbot.User;

public class Connector extends PircBot {

	public void createBot() {
		try {
			setVerbose(true);
			setName(RapidIRC.nick);
			connect(RapidIRC.server);
			if (RapidIRC.nickservPassword != null) {
				identify(RapidIRC.nickservPassword);
			}
			joinChannel(RapidIRC.channel);
		} catch (NickAlreadyInUseException e) {
			Bukkit.getLogger().warning("The set nickname is already in use, change the nickname and restart the bot.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IrcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendIRCMessage(String msg) {
		sendMessage(RapidIRC.channel, ColorMap.minecraftColorstoIRCColors(msg));
	}

	public void sendPrivateMessage(String user, String sender, String msg) {
		if (user.equals("Oracle")) {
			sendMessage(user, "<" + sender + "> " + ColorMap.minecraftColorstoIRCColors(msg));
		} else {
			sendMessage(user, sender + " whispers " + ColorMap.minecraftColorstoIRCColors(msg));
		}
		Bukkit.getPlayer(sender).sendMessage("[" + sender + "->" + user + "(IRC)] " + msg);
	}

	public void onMessage(String channel, String sender, String login, String hostname, String message) {
		if (!RapidIRC.ignoreIRC.contains(sender)) {
			String[] args = message.split(" ");
			if (args[0].equalsIgnoreCase("~players")) {
				if (Bukkit.getServer().getOnlinePlayers() != null) {
					List<String> playerList = new ArrayList<String>();
					for (Player p : Bukkit.getServer().getOnlinePlayers()) {
						playerList.add(p.getPlayerListName() + ChatColor.RESET);
					}
					sendIRCMessage("Online Players: " + ColorMap.minecraftColorstoIRCColors(StringUtils.join(playerList, ", ")));
				} else {
					sendIRCMessage("There are no players online.");
				}
			} else if (args[0].equalsIgnoreCase("~kick")) {
				User user = getUser(channel, sender);
				if (user.isOp()) {
					Player p = Bukkit.getServer().getPlayer(args[1]);
					if (p.isOnline()) {
						if (!args[2].isEmpty()) {
							p.kickPlayer(args[2]);
							sendIRCMessage(args[1] + " has been kicked. Reason: " + args[2]);
						} else {
							p.kickPlayer("Player kicked");
							sendIRCMessage(args[1] + " has been kicked.");
						}
					} else {
						sendIRCMessage("Cannot kick player: Are they online?");
					}
				} else {
					sendMessage(sender, "You don't have permission to do that.");
				}
			} else {
				Bukkit.broadcastMessage("[IRC] <" + sender + "> " + ColorMap.ircColorsToMinecraftColors(message));
			}
		}
	}

	public void onPrivateMessage(String sender, String login, String hostname, String message) {
		Boolean done = false;
		String[] words = message.split("[[ ]*|[,]*|[\\.]*|[:]*|[/]*|[!]*|[?]*|[+]*]+");
		Player p = Bukkit.getPlayer(words[0]);
		if (p != null) {
			message = message.replace(words[0] + " ", "");
			if (!sender.equals("Oracle")) {
				p.sendMessage("[IRC]-" + sender + "- " + message);
				p.getWorld().playSound(p.getLocation(), Sound.CAT_MEOW, 1, 0);
			} else {
				p.sendMessage("- " + ColorMap.ircColorsToMinecraftColors(message));
			}
			done = true;
		}
		if (!done) {
			sendMessage(sender, "Message not received by " + words[0] + ".");
		} else {
			sendMessage(sender, "[" + sender + "(IRC)->" + words[0] + "] " + ColorMap.minecraftColorstoIRCColors(message));
		}
	}

	public void onJoin(String channel, String sender, String login, String hostname) {
		Bukkit.broadcastMessage("[IRC] " + sender + " has joined");
	}

	public void onQuit(String sourceNick, String sourceLogin, String sourceHostname, String reason) {
		Bukkit.broadcastMessage("[IRC] " + sourceNick + " has quit (" + reason + ")");
	}

	public void onPart(String channel, String sender, String login, String hostname) {
		Bukkit.broadcastMessage("[IRC] " + sender + " has left");
	}

	public User getUser(String channel, String nickname) {
		User foundUser = null;
		for (User user : getUsers(channel)) {
			if (nickname.equals(user.getNick())) {
				foundUser = user;
				break;
			}
		}
		return foundUser;
	}
}