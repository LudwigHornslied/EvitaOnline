package com.tistory.hornslied.evitaonline.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.tistory.hornslied.evitaonline.Main;
import com.tistory.hornslied.evitaonline.Resources;

import net.md_5.bungee.api.ChatColor;

public class InfoCommand implements CommandExecutor {
	@SuppressWarnings("unused")
	private final Main plugin;
	
	public InfoCommand(Main instance) {
		plugin = instance;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage("플레이어만 명령어 사용이 가능합니다.");
			return false;
		}
		
		switch(label.toLowerCase()) {
			case "cafe":
				sender.sendMessage(Resources.tagInfo + ChatColor.YELLOW + "카페 주소: http://cafe.naver.com/evitaonline");
				return true;
			case "limit":
				return true;
			default:
				return false;
		}
	}
}
