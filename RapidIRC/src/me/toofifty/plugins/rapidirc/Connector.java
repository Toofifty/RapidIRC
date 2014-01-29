package me.toofifty.plugins.rapidirc;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jibble.pircbot.*;

public class Connector extends PircBot {
	public void makeBot(String name) {
		setName(name);
	}

	public void sendIRCMessage(String msg) {
		sendMessage(RapidIRC.channel, msg);
	}

	public void sendPrivateMessage(String user, String sender, String msg) {
		sendMessage(user, sender + " whispers " + msg);
		Bukkit.getPlayer(sender).sendMessage("[" + sender + "->" + user + "(IRC)] " + msg);
	}

	public void onMessage(String channel, String sender, String login, String hostname, String message) {
		Bukkit.broadcastMessage("[IRC] <" + sender + "> " + message);
	}
	
	public void onPrivateMessage(String sender, String login, String hostname, String message) {		
		Boolean done = false;
		String[] words = message.split("[[ ]*|[,]*|[\\.]*|[:]*|[/]*|[!]*|[?]*|[+]*]+");
		Player p = Bukkit.getPlayer(words[0]);
		if (p != null) {
			message = message.replace(words[0] + " ", "");
			p.sendMessage("[IRC]-" + sender + "- " + message);
			p.getWorld().playSound(p.getLocation(), Sound.CAT_MEOW, 1, 0);
			done = true;
		}
		if (!done) {
			sendMessage(sender, "Message not received by " + words[0] + ".");
		} else {
			sendMessage(sender, "[" + sender + "(IRC)->" + words[0] + "] " + message);
		}
	}

}