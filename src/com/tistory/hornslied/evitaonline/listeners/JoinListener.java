package com.tistory.hornslied.evitaonline.listeners;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.tistory.hornslied.evitaonline.Main;
import com.tistory.hornslied.evitaonline.Resources;
import com.tistory.hornslied.evitaonline.scoreboards.DefaultScoreboard;
import com.tistory.hornslied.evitaonline.scoreboards.WarScoreboard;

import net.md_5.bungee.api.ChatColor;

public class JoinListener implements Listener {
	private final Main plugin;
	private HashMap<Player, BukkitRunnable> updaters;
	
	public JoinListener(Main instance) {
		plugin = instance;
		
		updaters = new HashMap<Player, BukkitRunnable>();
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		
		DefaultScoreboard scoreboard = new DefaultScoreboard(player, plugin);
		WarScoreboard warScoreboard = new WarScoreboard(player, plugin);
		
		updaters.put(player, new BukkitRunnable() {
			public void run() {
				if(plugin.moveChunkListener.warState.isEmpty()) {
					scoreboard.updateScoreboard();
				} else {
					warScoreboard.updateScoreboard();
				}
			}
		});
		
		if(player.hasPlayedBefore()) {
			e.setJoinMessage(Resources.tagConnect  + ChatColor.GRAY + player.getName() + ChatColor.DARK_GRAY + " 님이 접속하셨습니다.");
		} else {
			e.setJoinMessage(Resources.tagConnect  + ChatColor.LIGHT_PURPLE + player.getName() + "님, 에비타 온라인에 오신 것을 환영합니다!");
		}
		
		if(plugin.moveChunkListener.warState.isEmpty()) {
			scoreboard.updateScoreboard();
		} else {
			warScoreboard.updateScoreboard();
		}
		
		updaters.get(player).runTaskTimer(plugin, 20, 20);
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		Player player = e.getPlayer();
		
		e.setQuitMessage(Resources.tagConnect  + ChatColor.GRAY + player.getName() + ChatColor.DARK_GRAY + " 님이 퇴장하셨습니다.");
		
		if(!(updaters.get(player) == null)) updaters.remove(player);
	}
}
