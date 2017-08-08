package com.tistory.hornslied.evitaonline.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.tistory.hornslied.evitaonline.Resources;

import net.md_5.bungee.api.ChatColor;

public class DeathListener implements Listener {
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		Player player = e.getEntity();
		
		player.getWorld().strikeLightningEffect(player.getLocation());
		
		String deathMessage = e.getDeathMessage();
		
		if(deathMessage.contains("was slain by") || deathMessage.contains("got finished off")) {
			if(e.getEntity().getKiller() instanceof Player) {
				e.setDeathMessage(Resources.tagDeath + ChatColor.DARK_RED + player.getName() + ChatColor.RED 
						+ " 님이 " + ChatColor.DARK_RED + e.getEntity().getKiller().getName() + ChatColor.RED 
						+ " 님에게 살해당하셨습니다.");
			} else {
				e.setDeathMessage(Resources.tagDeath + ChatColor.DARK_RED + player.getName() + ChatColor.RED 
						+ " 님이 " + ChatColor.DARK_RED + e.getEntity().getKiller().getName() + ChatColor.RED 
						+ " 에게 살해당하셨습니다.");
			}
		} else if(deathMessage.contains("was shot by")) {
			if(!deathMessage.contains("using") && deathMessage.contains("arrow")) {
				e.setDeathMessage(Resources.tagDeath + ChatColor.DARK_RED + player.getName() + ChatColor.RED
						+ "님이 저격당하셨습니다.");
			} else if(e.getEntity().getKiller() instanceof Player) {
				e.setDeathMessage(Resources.tagDeath + ChatColor.DARK_RED + player.getName() + ChatColor.RED 
						+ " 님이 " + ChatColor.DARK_RED + e.getEntity().getKiller().getName() + ChatColor.RED 
						+ " 님에게 저격당하셨습니다.");
			} else {
				e.setDeathMessage(Resources.tagDeath + ChatColor.DARK_RED + player.getName() + ChatColor.RED 
						+ " 님이 " + ChatColor.DARK_RED + e.getEntity().getKiller().getName() + ChatColor.RED 
						+ " 에게 저격당하셨습니다.");
			}
		} else if(deathMessage.contains("was pricked to") || deathMessage.contains("hugged a cactus") 
				|| deathMessage.contains("was stabbed to")) {
			e.setDeathMessage(Resources.tagDeath + ChatColor.DARK_RED + player.getName() + ChatColor.RED 
					+ " 님이 선인장에 찔려 사망하셨습니다.");
		} else if(deathMessage.contains("walked into a cactus")) {
			e.setDeathMessage(Resources.tagDeath + ChatColor.DARK_RED + player.getName() + ChatColor.RED 
					+ " 님이" + ChatColor.DARK_RED + e.getEntity().getKiller() + ChatColor.RED 
					+ "님과의 전투 중 선인장에 찔려 사망하셨습니다.");
		} else if(deathMessage.contains("drowned")) {
			if(deathMessage.contains("trying to escape")) {
				e.setDeathMessage(Resources.tagDeath + ChatColor.DARK_RED + player.getName() + ChatColor.RED 
						+ " 님이" + ChatColor.DARK_RED + e.getEntity().getKiller() + ChatColor.RED 
						+ "님과의 전투 중 익사하셨습니다.");
			} else {
				e.setDeathMessage(Resources.tagDeath + ChatColor.DARK_RED + player.getName() + ChatColor.RED 
						+ " 님이 익사하셨습니다.");
			}
		}
	}
}
