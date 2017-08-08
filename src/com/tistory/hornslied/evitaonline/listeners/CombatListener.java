package com.tistory.hornslied.evitaonline.listeners;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.TownyUniverse;
import com.tistory.hornslied.evitaonline.Main;
import com.tistory.hornslied.evitaonline.Resources;

public class CombatListener implements Listener {
	private final Main plugin;
	public HashMap<Entity, Integer> combatTag;
	public HashMap<Entity, BukkitRunnable> combatCounter;
	
	public CombatListener(Main instance) {
		combatTag = new HashMap<Entity, Integer>();
		combatCounter = new HashMap<Entity, BukkitRunnable>();
		
		plugin = instance;
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onAllyAttack(EntityDamageByEntityEvent e) {
		Entity damager = e.getDamager();
		Entity damaged = e.getEntity();
		if(damager instanceof Player && damaged instanceof Player && !(damager.equals(damaged))) {
			Nation damagerNation;
			Nation damagedNation;
			try {
				damagerNation = TownyUniverse.getDataSource().getResident(damager.getName()).getTown().getNation();
			} catch (NotRegisteredException ex) {
				damagerNation = null;
			}
			
			try {
				damagedNation = TownyUniverse.getDataSource().getResident(damaged.getName()).getTown().getNation();
			} catch (NotRegisteredException ex) {
				damagedNation = null;
			}
			
			if(damagerNation != null && damagedNation != null && damagerNation.getAllies().contains(damagedNation)) e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onAttack(EntityDamageByEntityEvent e) {
		Entity damager = e.getDamager();
		Entity damaged = e.getEntity();
		if(damager instanceof Player && damaged instanceof Player && !(damager.equals(damaged))) {
			if(!(combatTag.containsKey(damager))) {
				damager.sendMessage(Resources.tagCombat + ChatColor.RED + "전투상태에 돌입합니다!");
				combatCounter.put(damager, new BukkitRunnable() {
					public void run() {
						combatTag.put(damager, combatTag.get(damager) -1);
						if(combatTag.get(damager) == 0) {
							combatTag.remove(damager);
							combatCounter.remove(damager);
							cancel();
						
							damager.sendMessage(Resources.tagCombat + ChatColor.GREEN + "당신은 전투상태에서 벗어났습니다!");
						}
					}
				});
			
				combatCounter.get(damager).runTaskTimer(plugin, 20, 20);
			}
		
			if(!(combatTag.containsKey(damaged))) {
				damaged.sendMessage(Resources.tagCombat + ChatColor.RED + "전투상태에 돌입합니다!");
				combatCounter.put(damaged, new BukkitRunnable() {
					public void run() {
						combatTag.put(damaged, combatTag.get(damaged) -1);
						if(combatTag.get(damaged) == 0) {
							combatTag.remove(damaged);
							combatCounter.remove(damaged);
							cancel();

							damaged.sendMessage(Resources.tagCombat + ChatColor.GREEN + "당신은 전투상태에서 벗어났습니다!");
						}
					}
				});
			
				combatCounter.get(damaged).runTaskTimer(plugin, 20, 20);
			}
			
			combatTag.put(damager, 15);
			combatTag.put(damaged, 15);
		}
	}
}
