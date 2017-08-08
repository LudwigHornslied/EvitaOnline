package com.tistory.hornslied.evitaonline.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.tistory.hornslied.evitaonline.Main;
import com.tistory.hornslied.evitaonline.Resources;

public class ModerationCommand implements CommandExecutor {
	@SuppressWarnings("unused")
	private final Main plugin;
	
	public ModerationCommand(Main instance) {
		plugin = instance;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(label.equalsIgnoreCase("alert")) {
			if(args.length < 1) {
				return false;
			}
			
			Bukkit.broadcastMessage(Resources.tagAlert + args[0]);
			return true;
		} else {
			return false;
		}
	}
}
