package com.tistory.hornslied.evitaonline.commands;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.palmergames.bukkit.towny.command.TownCommand;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.exceptions.TownyException;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.TownyUniverse;
import com.tistory.hornslied.evitaonline.Main;
import com.tistory.hornslied.evitaonline.Permissions;
import com.tistory.hornslied.evitaonline.Resources;

import net.md_5.bungee.api.ChatColor;

public class TownyCommand implements CommandExecutor {
	private final Main plugin;
	public final Logger logger = Logger.getLogger("TownyCommandLogger");
	
	public HashMap<Town, BukkitRunnable> warCounter = new HashMap<Town, BukkitRunnable>();
	public HashMap<Town, Integer> warPeriod = new HashMap<Town, Integer>();
	public HashMap<Town, Integer> occupyTime = new HashMap<Town, Integer>();
	
	public ArrayList<Nation> currentAttackNation = new ArrayList<Nation>();
	
	public TownyCommand(Main instance) {
		plugin = instance;
	}
	
	private void sendWarPeriod(String time) {
		Bukkit.broadcastMessage(Resources.tagWar + ChatColor.GRAY + "전쟁 시간이 " + ChatColor.GOLD + time + ChatColor.GRAY + " 남았습니다.");;
	}
	
	private void sendOccupyTime(String time) {
		Bukkit.broadcastMessage(Resources.tagWar + ChatColor.DARK_BLUE + "마을을 점령하기까지 " + ChatColor.GOLD + time + ChatColor.DARK_BLUE +" 남았습니다.");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(label.equalsIgnoreCase("evitatowny")) {
			if(args.length < 1) {
				return false;
			} else {
				switch(args[0].toLowerCase()) {
					case "sidge":
						if(sender.hasPermission(Permissions.sidge)) {
							if(!(args.length == 3)) {
								if(args[2].equals("stop")) {
									if(sender.hasPermission("evita.towny.stopsidge")) {
										if(currentAttackNation.isEmpty()) {
											sender.sendMessage(Resources.tagWar + ChatColor.RED + "전쟁중인 국가가 없습니다!");
											return false;
										} else {
											Bukkit.broadcastMessage(Resources.tagWar + ChatColor.RED + "공성전 중단 중...");
											endWar();
											Bukkit.broadcastMessage(Resources.tagWar + ChatColor.BLUE + "공성전 중단 완료");
											return true;
										}
									} else {
										sender.sendMessage(Resources.messagePermission);
										return false;
									}
								} else {
									sender.sendMessage(Resources.tagWar + "명령어 사용 방법: /evitatowny sidge <공격국가> <방어마을>");
									return false;
								}
							}
							
							Nation attacker;
							Town defender;
							List<Resident> attackerResidents;
							
							try {
								attacker = TownyUniverse.getDataSource().getNation(args[1]);
								attackerResidents = attacker.getResidents();
							} catch (NotRegisteredException e) {
								sender.sendMessage(Resources.tagWar + ChatColor.RED + "공격 국가가 존재하지 않습니다! /evitatowny sidge <공격국가> <방어마을>");
								return false;
							}
						
							try {
								defender = TownyUniverse.getDataSource().getTown(args[2]);
							} catch (NotRegisteredException e) {
								sender.sendMessage(Resources.tagWar + ChatColor.RED + "방어 마을이 존재하지 않습니다! /evitatowny sidge <공격국가> <방어마을>");
								return false;
							}
							
							try {
								if(defender.getNation().equals(attacker)) {
									sender.sendMessage(Resources.tagWar + ChatColor.RED + "공격 국가와 방어 마을은 같은 소속이어선 안됩니다!");
								} else {
									Bukkit.broadcastMessage(Resources.tagWar + ChatColor.GOLD + attacker.getName() + 
											" 국가가 " + defender.getNation().getName() + " 국가의 " + defender.getName() + " 마을을 침략합니다!");
									
									for(int i=0; i<attackerResidents.size(); i++) {
										Player residentPlayer = plugin.getServer().getPlayer(attackerResidents.get(i).getName());
										String split[] = {attackerResidents.get(i).getTown().getName()};
										
										if((residentPlayer.isOnline())) {
											try {
												TownCommand.townSpawn(residentPlayer, split, false);
											} catch (TownyException e) {
											}
										}
									}
									
									plugin.moveChunkListener.warState.put(attacker, defender);
									plugin.moveChunkListener.enemyNumber.put(defender, 0);
									warPeriod.put(defender, 3600);
									occupyTime.put(defender, 1200);
									currentAttackNation.add(attacker);
									
									setPvP(attacker, defender, true);
							
									warCounter.put(defender, new BukkitRunnable() {
										@Override
										public void run() {
											if((warPeriod.get(defender) > 0)) { //전쟁 시간이 끝나지 않았을때
												if(plugin.moveChunkListener.enemyNumber.get(defender) > 0) { //점령자가 있음
													warPeriod.put(defender, warPeriod.get(defender) -1);
													occupyTime.put(defender, occupyTime.get(defender) -1);
													
													switch(warPeriod.get(defender)) {
														case 1800:
															sendWarPeriod("30분");
															break;
														case 1200:
															sendWarPeriod("20분");
															break;
														case 600:
															sendWarPeriod("10분");
															break;
														case 300:
															sendWarPeriod("5분");
															break;
														case 60:
															sendWarPeriod("1분");
															break;
														case 30:
															sendWarPeriod("30초");
															break;
														case 5:
															sendWarPeriod("5초");
															break;
														case 4:
															sendWarPeriod("4초");
															break;
														case 3:
															sendWarPeriod("3초");
															break;
														case 2:
															sendWarPeriod("2초");
															break;
														case 1:
															sendWarPeriod("1초");
															break;
													}
													
													switch(occupyTime.get(defender)) {
														case 900:
															sendOccupyTime("15분");
															break;
														case 600:
															sendOccupyTime("10분");
															break;
														case 300:
															sendOccupyTime("5분");
															break;
														case 60:
															sendOccupyTime("1분");
															break;
														case 30:
															sendOccupyTime("30초");
															break;
														case 5:
															sendOccupyTime("5초");
															break;
													}
													
													if(occupyTime.get(defender) == 0) { //점령 성공
														endWar(attacker, defender, true);
													}
												} else { //점령자가 없음
													warPeriod.put(defender, warPeriod.get(defender) -1);
													occupyTime.put(defender, 1200);
												}
											} else { //전쟁 시간 끝
												if(plugin.moveChunkListener.enemyNumber.get(defender) > 0) { // 점령자가 있음
													if(!(plugin.moveChunkListener.additionalTime.contains(defender))) { //추가 시간 배열
														plugin.moveChunkListener.additionalTime.add(defender);
														warPeriod.put(defender, -1);
													
														Bukkit.broadcastMessage(Resources.tagWar + ChatColor.GREEN + attacker.getName() 
														+ "국가에게 추가 시간이 주어집니다, " + defender.getName() + " 마을을 점령할수 있는 마지막 기회입니다!");
													}
													
													occupyTime.put(defender, occupyTime.get(defender) -1);
													
													switch(occupyTime.get(defender)) {
														case 900:
															sendOccupyTime("15분");
															break;
														case 600:
															sendOccupyTime("10분");
															break;
														case 300:
															sendOccupyTime("5분");
															break;
														case 60:
															sendOccupyTime("1분");
															break;
														case 30:
															sendOccupyTime("30초");
															break;
														case 5:
															sendOccupyTime("5초");
															break;
													}
													
													if(occupyTime.get(defender) == 0) { //점령 성공
														endWar(attacker, defender, true);
													}	
												} else { //점령자가 없음
													endWar(attacker, defender, false);
												}
											}
										}
									});
									
									warCounter.get(defender).runTaskTimer(plugin, 20, 20);
								}
							} catch (NotRegisteredException e) {
								sender.sendMessage(Resources.tagWar + ChatColor.RED + "방어 마을의 소속 국가가 없습니다!");
								return false;
							}
							
							return true;
						} else {
							sender.sendMessage(Resources.messagePermission);
							return false;
						}
					case "warlog":
						if(sender.hasPermission(Permissions.warLog)) {
							sender.sendMessage(Resources.tagWar + ChatColor.RED + "전쟁 기록 불러오는 중...");
							String messages[];
							ResultSet rs = plugin.db.select("SELECT * FROM warlogs;");
							if(args.length < 2 || Integer.parseInt(args[1]) == 1) {
								if(getResultSetSize(rs) == 0) {
									sender.sendMessage(Resources.tagWar + ChatColor.RED + "전쟁 기록이 존재하지 않습니다.");
									return false;
								} else {
									
									try {
										if(getResultSetSize(rs) < 4) {
											messages = new String[getResultSetSize(rs)];
										} else {
											messages = new String[4];
										}
										
										int i = 0;
										while(rs.next() && i <= 3) {
											messages[i] = ChatColor.GRAY + rs.getString("date") + " " + ChatColor.RED
													+ rs.getString("attacknation") + " " + ChatColor.BLUE + rs.getString("defendtown") 
													+ " " + ChatColor.AQUA + rs.getBoolean("isWin");
											i++;
										}
										
										sender.sendMessage("========== " + ChatColor.GREEN + "전쟁 기록" + ChatColor.WHITE + "==========");
										sender.sendMessage(ChatColor.GRAY + "(날짜) " + ChatColor.RED + "(공격국가) " + ChatColor.BLUE + "(방어마을) " 
												+ ChatColor.AQUA + "(공격성공)");
										sender.sendMessage(messages);
									} catch (SQLException e) {
										e.printStackTrace();
										return false;
									}
									
									return true;
								}
							} else if(args[1].equals("delete")) {
								plugin.db.query("DELETE FROM warlogs");
							} else {
								int index;
								try {
									index = Integer.parseInt(args[1]);
								} catch(NumberFormatException e) {
									
									return false;
								}
								if(getResultSetSize(rs) < (index -1) * 4 + 1) {
									sender.sendMessage(Resources.tagWar + ChatColor.RED + "해당 페이지는 존재하지 않습니다.");
									return false;
								} else {
									try {
										if(getResultSetSize(rs) < index * 4) {
											messages = new String[getResultSetSize(rs) - (index -1) * 4];
										} else {
											messages = new String[4];
										}
										
										rs.absolute((index -1) * 4);
										
										int i = 0;
										while(rs.next() && i <= messages.length -1) {
											messages[i] = ChatColor.GRAY + rs.getString("date") + " " + ChatColor.RED
													+ rs.getString("attacknation") + " " + ChatColor.BLUE + rs.getString("defendtown") 
													+ " " + ChatColor.AQUA + rs.getBoolean("isWin");
											i++;
										}
										
										sender.sendMessage("========== " + ChatColor.GREEN + "전쟁 기록" + ChatColor.WHITE + "==========");
										sender.sendMessage(ChatColor.GRAY + "(날짜) " + ChatColor.RED + "(공격국가) " + ChatColor.BLUE + "(방어마을) " 
												+ ChatColor.AQUA + "(공격성공)");
										sender.sendMessage(messages);
									} catch (SQLException e) {
										e.printStackTrace();
										return false;
									}
									
									return true;
								}
							}
						} else {
							sender.sendMessage(Resources.messagePermission);
							return false;
						}
					default:
						return false;
				}
			}
		} else {
			return false;
		}
	}
	
	private int getResultSetSize(ResultSet resultSet) {
	    int size = -1;

	    try {
	        resultSet.last(); 
	        size = resultSet.getRow();
	        resultSet.beforeFirst();
	    } catch(SQLException e) {
	        return size;
	    }

	    return size;
	}
	
	public void endWar() {
		for(int i=0; i<currentAttackNation.size(); i++) {
			setPvP(currentAttackNation.get(i), plugin.moveChunkListener.warState.get(currentAttackNation.get(i)), false);
			warCounter.get(plugin.moveChunkListener.warState.get(currentAttackNation.get(i))).cancel();
			warCounter.remove(plugin.moveChunkListener.warState.get(currentAttackNation.get(i)));
		}
		currentAttackNation.clear();
		plugin.moveChunkListener.warState.clear();
		plugin.moveChunkListener.enemyNumber.clear();
		warPeriod.clear();
		occupyTime.clear();
		plugin.moveChunkListener.additionalTime.clear();
	}
	
	public void endWar(Nation attacker, Town defender, boolean isWin) {
		for(int i=0; i<currentAttackNation.size(); i++) {
			warCounter.get(plugin.moveChunkListener.warState.get(currentAttackNation.get(i))).cancel();
			warCounter.remove(plugin.moveChunkListener.warState.get(currentAttackNation.get(i)));
		}
		setPvP(attacker, defender, false);
		currentAttackNation.clear();
		plugin.moveChunkListener.warState.remove(attacker);
		plugin.moveChunkListener.enemyNumber.remove(defender);
		warPeriod.remove(defender);
		occupyTime.remove(defender);
		if(plugin.moveChunkListener.additionalTime.contains(defender)) plugin.moveChunkListener.additionalTime.remove(defender);
		
		Calendar cal = Calendar.getInstance();
		
		if(isWin) {
			Bukkit.broadcastMessage(Resources.tagWar + ChatColor.GREEN + "전쟁이 끝났습니다!" 
		+ attacker.getName() + " 국가가 " + defender.getName() + " 마을을 점령하는데 성공했습니다!");
			plugin.db.query("INSERT INTO warlogs (attacknation, defendtown, date, isWin) VALUES ('"
					+ attacker.getName() + "', '" + defender.getName() + "', '" + cal.get(Calendar.YEAR) 
					+ "-" + cal.get(Calendar.MONTH) + "-" + cal.get(Calendar.DATE) + "', 1);");
		} else {
			Bukkit.broadcastMessage(Resources.tagWar + ChatColor.GOLD + "전쟁이 끝났습니다! " 
			+ attacker.getName() + " 국가는 " + defender.getName() + " 마을을 점령하는데 실패했습니다!");
			plugin.db.query("INSERT INTO warlogs (attacknation, defendtown, date, isWin) VALUES ('"
					+ attacker.getName() + "', '" + defender.getName() + "', '" + cal.get(Calendar.YEAR) 
				+ "-" + cal.get(Calendar.MONTH) + "-" + cal.get(Calendar.DATE) + "', 0);");
		}
	}
	
	private void setPvP(Nation attacker, Town defender, boolean isPvP) {
		List<Town> attackerTowns = attacker.getTowns();
		for(int i=0; i<attackerTowns.size(); i++) {
			attackerTowns.get(i).setAdminEnabledPVP(isPvP);
			attackerTowns.get(i).setPVP(isPvP);
		}
		defender.setAdminEnabledPVP(isPvP);
		defender.setPVP(isPvP);
	}
}
