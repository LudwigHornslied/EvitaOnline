package com.tistory.hornslied.evitaonline.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.tistory.hornslied.evitaonline.Main;

public class ACCommand implements CommandExecutor {
	@SuppressWarnings("unused")
	private final Main plugin;
	
	public ACCommand(Main instance) {
		plugin = instance;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		return false;
	}

}
