package com.tistory.hornslied.evitaonline.listeners;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.palmergames.bukkit.towny.event.PlayerChangePlotEvent;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.TownyUniverse;
import com.tistory.hornslied.evitaonline.Main;
import com.tistory.hornslied.evitaonline.Resources;

public class MoveChunkListener implements Listener {
	@SuppressWarnings("unused")
	private final Main plugin;
	public HashMap<Nation, Town> warState = new HashMap<Nation, Town>();
	public HashMap<Town, Integer> enemyNumber = new HashMap<Town, Integer>();
	public ArrayList<Town> additionalTime = new ArrayList<Town>();
	
	public MoveChunkListener(Main instance) {
		plugin = instance;
	}
	
	@EventHandler
	public void onMoveChunk(PlayerChangePlotEvent e) {
		Player player = e.getPlayer();
		
		Nation playerNation;
		try {
			playerNation = TownyUniverse.getDataSource().getResident(player.getName()).getTown().getNation();
		} catch (NotRegisteredException e1) {
			playerNation = null;
		}
		Town playerEnemy;
		
		if(warState.containsKey(playerNation)) {
			Town from;
			try {
				from = e.getFrom().getTownBlock().getTown();
			} catch (NotRegisteredException e1) {
				from = null;
			}
			Town to;
			try {
				to = e.getTo().getTownBlock().getTown();
			} catch (NotRegisteredException e1) {
				to = null;
			}
				
			playerEnemy = warState.get(playerNation);
			if(!(from == null) && from.equals(playerEnemy)) {
				if(to == null || !(to.equals(playerEnemy))) {
					enemyNumber.put(playerEnemy, enemyNumber.get(playerEnemy) -1);
					if(enemyNumber.get(playerEnemy) == 0) Bukkit.broadcastMessage(Resources.tagWar + ChatColor.GREEN + "모든 " 
					+ playerNation.getName() + " 의 군대가 " + playerEnemy + " 의 땅에서 쫓겨났습니다!");
				}
			} else {
				if(!(to == null) && (to.equals(playerEnemy))) {
					if(enemyNumber.get(playerEnemy) == 0) Bukkit.broadcastMessage(Resources.tagWar + ChatColor.GREEN 
							+ playerNation.getName() + " 국가의 군대가 " + playerEnemy + " 점령을 시도합니다!");
					enemyNumber.put(playerEnemy, enemyNumber.get(playerEnemy) +1);
				}
			}
		}
	}
}
