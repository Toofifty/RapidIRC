package me.toofifty.plugins.rapidirc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
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
			e.printStackTrace();
		} catch (IrcException e) {
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
			if (args[0].equalsIgnoreCase(RapidIRC.prefix + "help")) {
				User user = getUser(channel, sender);
				if (user.isOp()) {
					sendMessage(sender, "Commands: " + RapidIRC.prefix + "players, " + RapidIRC.prefix + "kick [username] (reason), " + RapidIRC.prefix + "ban [username] (reason), " + RapidIRC.prefix + "pardon [username]");
				} else {
					sendMessage(sender, "Commands: " + RapidIRC.prefix + "players, " + RapidIRC.prefix + "version");
				}

			} else if (args[0].equalsIgnoreCase(RapidIRC.prefix + "players")) {
				if (Bukkit.getServer().getOnlinePlayers() != null) {
					List<String> playerList = new ArrayList<String>();
					for (Player p : Bukkit.getServer().getOnlinePlayers()) {
						playerList.add(p.getPlayerListName() + ChatColor.RESET);
					}
					sendIRCMessage("Online Players: " + ColorMap.minecraftColorstoIRCColors(StringUtils.join(playerList, ", ")));
				} else {
					sendIRCMessage("There are no players online.");
				}
			} else if (args[0].equalsIgnoreCase(RapidIRC.prefix + "version")) {
				sendIRCMessage("Version: " + Bukkit.getServer().getPluginManager().getPlugin("RapidIRC").getDescription().getVersion());
			} else if (args[0].equalsIgnoreCase(RapidIRC.prefix + "kick")) {
				User user = getUser(channel, sender);
				if (user.isOp()) {
					if (args.length > 1) {
						try {
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
						} catch (NullPointerException e) {
							sendIRCMessage("Player does not exist");
						}
					} else {
						sendIRCMessage("Not enough arguments.");
					}
				} else {
					sendMessage(sender, "You must be op to do that.");
				}
			} else if (args[0].equalsIgnoreCase(RapidIRC.prefix + "ban")) {
				User user = getUser(channel, sender);
				if (user.isOp()) {
					if (args.length > 1) {
						try {
							Player p = Bukkit.getServer().getPlayer(args[1]);
							if (!p.isBanned()) {
								if (!args[2].isEmpty()) {
									p.setBanned(true);
									if (p.isOnline()) {
										p.kickPlayer(args[2]);
									}
									sendIRCMessage(args[1] + " has been banned. Reason: " + args[2]);
								} else {
									p.setBanned(true);
									if (p.isOnline()) {
										p.kickPlayer("Player banned");
									}
									sendIRCMessage(args[1] + " has been banned.");
								}
							} else {
								sendIRCMessage(args[1] + " is already banned.");
							}
						} catch (NullPointerException e) {
							sendIRCMessage("Player does not exist");
						}
					} else {
						sendIRCMessage("Not enough arguments.");
					}
				} else {
					sendMessage(sender, "You must be op to do that.");
				}
			} else if (args[0].equalsIgnoreCase(RapidIRC.prefix + "pardon")) {
				User user = getUser(channel, sender);
				if (user.isOp()) {
					if (args.length > 1) {
						try {
							OfflinePlayer p = Bukkit.getServer().getOfflinePlayer(args[1]);
							if (p.isBanned()) {
								p.setBanned(false);
								sendIRCMessage(args[1] + " has been pardoned.");
							} else {
								sendIRCMessage(args[1] + " is not banned.");
							}
						} catch (NullPointerException e) {
							sendIRCMessage("Player does not exist");
						}
					} else {
						sendIRCMessage("Not enough arguments.");
					}
				} else {
					sendMessage(sender, "You must be op to do that.");
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