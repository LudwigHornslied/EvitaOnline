package com.tistory.hornslied.evitaonline.listeners;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.tistory.hornslied.evitaonline.Main;
import com.tistory.hornslied.evitaonline.Resources;

public class EnderpearlListener implements Listener {
	private final Main plugin;
	public HashMap<Player, Integer> ePearlCooldown;
	public HashMap<Player, BukkitRunnable> ePearlCounter;
	public HashMap<Player, Integer> messageCooldown;
	public HashMap<Player, BukkitRunnable> messageCounter;
	
	public EnderpearlListener(Main instance) {
		ePearlCooldown = new HashMap<Player, Integer>();
		ePearlCounter = new HashMap<Player, BukkitRunnable>();
		messageCooldown = new HashMap<Player, Integer>();
		messageCounter = new HashMap<Player, BukkitRunnable>();
		plugin = instance;
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		Player player = e.getPlayer();

		if(e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) {
			if(player.getInventory().getItemInMainHand().getType().equals(Material.ENDER_PEARL)) {
				
				if(ePearlCooldown.containsKey(player)) {
					if(!(messageCooldown.containsKey(player))) {
						player.sendMessage(Resources.tagCombat + "엔더 진주 재사용 대기시간: "+ ChatColor.GREEN + ePearlCooldown.get(player) + "초");
						messageCooldown.put(player, 1);
						messageCounter.put(player, new BukkitRunnable() {
							public void run() {
								messageCooldown.put(player, messageCooldown.get(player) -1);
								if(messageCooldown.get(player) == 0) {
									messageCooldown.remove(player);
									messageCounter.remove(player);
									cancel();
								}
							}
						});
						
						messageCounter.get(player).runTaskTimer(plugin, 20, 20);
					}
					e.setCancelled(true);
				} else {
					ePearlCooldown.put(player, 15);
					ePearlCounter.put(player, new BukkitRunnable() {
						public void run() {
							ePearlCooldown.put(player, ePearlCooldown.get(player) -1);
							if(ePearlCooldown.get(player) == 0) {
								ePearlCooldown.remove(player);
								ePearlCounter.remove(player);
								cancel();
								
								player.sendMessage(Resources.tagCombat + ChatColor.GREEN + "이제 엔더 진주 사용이 가능합니다.");
							}
						}
					});
					
					ePearlCounter.get(player).runTaskTimer(plugin, 20, 20);
				}
			}
		}
	}
}
