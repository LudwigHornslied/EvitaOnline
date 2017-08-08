package com.tistory.hornslied.evitaonline.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.tistory.hornslied.evitaonline.Main;

public class GUIListener implements Listener {
	@SuppressWarnings("unused")
	private final Main plugin;
	
	public GUIListener(Main instance) {
		plugin = instance;
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if(e.getInventory().getName().equals("제한 항목")) {
			e.setCancelled(true);
		}
	}
}
