package com.tistory.hornslied.evitaonline.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.tistory.hornslied.evitaonline.Main;
import com.tistory.hornslied.evitaonline.Permissions;
import com.tistory.hornslied.evitaonline.Resources;

import net.md_5.bungee.api.ChatColor;

public class ModerationCommand implements CommandExecutor {
	@SuppressWarnings("unused")
	private final Main plugin;
	
	public ModerationCommand(Main instance) {
		plugin = instance;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		switch(label.toLowerCase()) {
			case "alert":
				if(sender.hasPermission(Permissions.alert)) {
					if(args.length < 1) {
						sender.sendMessage(Resources.tagServer + ChatColor.RED + "명령어 사용 방법: ");
						return false;
					} else {
						Bukkit.broadcastMessage(Resources.tagAlert + args[0]);
					}
				} else {
					sender.sendMessage(Resources.messagePermission);	
				}
			default:
				return false;
		}
	}
}
