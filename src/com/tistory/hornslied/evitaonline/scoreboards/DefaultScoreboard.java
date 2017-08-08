package com.tistory.hornslied.evitaonline.scoreboards;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.TownyUniverse;
import com.tistory.hornslied.evitaonline.Main;
import com.tistory.hornslied.evitaonline.Resources;

public class DefaultScoreboard {
	private final ScoreboardManager sm;
	private final Scoreboard defaultScoreboard;
	private final Objective obj;
	private final Player player;
	private final Main plugin;
	private final Score spacer1;
	private final Team sc_playerName;
	private final Team sc_townName;
	private final Team sc_playerBalance;
	private final Score spacer2;
	private final Team sc_ePearlCooldown;
	private final Team sc_combatTag;
	private final Score spacer3;
	private final Score sc_serverName;
	
	public DefaultScoreboard(Player player, Main instance) {
		sm = Bukkit.getScoreboardManager();
		defaultScoreboard = sm.getNewScoreboard();
		obj = defaultScoreboard.registerNewObjective("Default", "dummy");
		obj.setDisplayName(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "Evita Online");
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		spacer1 = obj.getScore(Resources.blank);
		sc_playerName = defaultScoreboard.registerNewTeam("playerName");
		sc_townName = defaultScoreboard.registerNewTeam("townName");
		sc_playerBalance = defaultScoreboard.registerNewTeam("playerBalance");
		spacer2 = obj.getScore(Resources.blank + Resources.blank);
		sc_ePearlCooldown = defaultScoreboard.registerNewTeam("ePearlCD");
		sc_combatTag = defaultScoreboard.registerNewTeam("combatTag");
		spacer3 = obj.getScore(Resources.blank + Resources.blank + Resources.blank);
		sc_serverName = obj.getScore(ChatColor.YELLOW + "evitaonline.kr");
		
		sc_playerName.addEntry(ChatColor.AQUA.toString());
		sc_playerName.setPrefix("플레이어: ");
		
		sc_townName.addEntry(ChatColor.BLACK.toString());
		sc_townName.setPrefix("마을: ");
		
		sc_playerBalance.addEntry(ChatColor.BLUE.toString());
		sc_playerBalance.setPrefix("소지 금액: ");
		
		sc_ePearlCooldown.addEntry(ChatColor.BOLD.toString());
		sc_ePearlCooldown.setPrefix("엔더 진주: ");
		
		sc_combatTag.addEntry(ChatColor.DARK_AQUA.toString());
		sc_combatTag.setPrefix("컴뱃 태그: ");
		
		sc_serverName.setScore(0);
		spacer3.setScore(1);
		obj.getScore(ChatColor.DARK_AQUA.toString()).setScore(2);
		obj.getScore(ChatColor.BOLD.toString()).setScore(3);
		spacer2.setScore(4);
		obj.getScore(ChatColor.BLUE.toString()).setScore(5);
		obj.getScore(ChatColor.BLACK.toString()).setScore(6);
		obj.getScore(ChatColor.AQUA.toString()).setScore(7);
		spacer1.setScore(8);
		
		this.player = player;
		plugin = instance;
	}
	
	public void updateScoreboard() {
		String townName;
		
		try {
			townName = TownyUniverse.getDataSource().getResident(player.getName()).getTown().getName();
		} catch (NotRegisteredException e) {
			townName = "없음";
		}
		
		sc_playerName.setSuffix(ChatColor.GREEN + player.getName());
		sc_townName.setSuffix(ChatColor.GREEN + townName);
		sc_playerBalance.setSuffix(ChatColor.GREEN + Double.toString(plugin.economy.getBalance(player)) + " 페론");
		if(plugin.enderPearlListener.ePearlCooldown.containsKey(player)) sc_ePearlCooldown.setSuffix(ChatColor.GREEN + Integer.toString(plugin.enderPearlListener.ePearlCooldown.get(player)) + " 초");
		else sc_ePearlCooldown.setSuffix(ChatColor.GREEN + "사용 가능");
		if(plugin.combatListener.combatTag.containsKey(player)) sc_combatTag.setSuffix(ChatColor.GREEN + Integer.toString(plugin.combatListener.combatTag.get(player)) + " 초");
		else sc_combatTag.setSuffix(ChatColor.GREEN + "없음");

		player.setScoreboard(defaultScoreboard);
	}
}
