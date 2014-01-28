package me.toofifty.plugins.rapidirc;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jibble.pircbot.*;

public class Connector extends PircBot {

	//Constructor
	public Connector(){

	}
	
	public void makeBot(String name) {
		setName(name); 
	}
	
	public void sendIRCMessage(String channel, String msg){
		sendMessage(channel, msg);
		
	}
	
	public void onMessage(String channel, String sender, String login, String hostname, String message) {
		Bukkit.broadcastMessage("[IRC]<" + sender + "> " + message);
	}
	
	public void onNotice(String sourceNick, String sourceLogin, String sourceHostname, String target, String notice) {
		if (!(sourceNick.contains(".net") || sourceNick.contains("irc."))) {
			Player rec = Bukkit.getPlayer(target);
			rec.sendMessage("[IRC]-" + sourceNick + "- " + notice);
		}
	}
	
}
