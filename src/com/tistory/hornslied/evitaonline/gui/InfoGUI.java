package com.tistory.hornslied.evitaonline.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import net.md_5.bungee.api.ChatColor;

public class InfoGUI {
	private Inventory limitGUI;
	
	public InfoGUI() {
		limitGUIInit();
	}
	
	private void limitGUIInit() {
		limitGUI = Bukkit.createInventory(null, 18, "제한 항목");
		
		ItemStack sharp = new ItemStack(Material.ENCHANTED_BOOK);
		setMeta(sharp, "날카로움: 2");
		
		ItemStack prot = new ItemStack(Material.ENCHANTED_BOOK);
		setMeta(prot, "보호: 2");
		
		ItemStack fireProt = new ItemStack(Material.ENCHANTED_BOOK);
		setMeta(fireProt, "화염으로부터 보호: 비활성화");
		
		ItemStack blastProt = new ItemStack(Material.ENCHANTED_BOOK);
		setMeta(blastProt, "폭발로부터 보호: 비활성화");
		
		ItemStack projProt = new ItemStack(Material.ENCHANTED_BOOK);
		setMeta(projProt, "발사체로부터 보호: 비활성화");
		
		ItemStack mending = new ItemStack(Material.ENCHANTED_BOOK);
		setMeta(mending, "수선: 비활성화");
		
		ItemStack sweepEdge = new ItemStack(Material.ENCHANTED_BOOK);
		setMeta(sweepEdge, "휘몰아치는 칼날: 비활성화");
		
		ItemStack frostWalker = new ItemStack(Material.ENCHANTED_BOOK);
		setMeta(frostWalker, "차가운 걸음: 비활성화");
		
		ItemStack knockback = new ItemStack(Material.ENCHANTED_BOOK);
		setMeta(knockback, "밀치기: 비활성화");
		
		ItemStack punch = new ItemStack(Material.ENCHANTED_BOOK);
		setMeta(punch, "밀어내기: 비활성화");
		
		ItemStack thorn = new ItemStack(Material.ENCHANTED_BOOK);
		setMeta(thorn, "가시: 비활성화");
		
		ItemStack depthStr = new ItemStack(Material.ENCHANTED_BOOK);
		setMeta(depthStr, "물갈퀴: 비활성화");
		
		ItemStack leapPot = new ItemStack(Material.POTION);
		setPotionMeta(leapPot, "도약의 포션: 비활성화", PotionType.JUMP);
		
		ItemStack harmPot = new ItemStack(Material.POTION);
		setPotionMeta(harmPot, "고통의 포션: 비활성화", PotionType.INSTANT_DAMAGE);
		
		ItemStack weakPot = new ItemStack(Material.POTION);
		setPotionMeta(weakPot, "나약함의 포션: 비활성화", PotionType.WEAKNESS);
		
		ItemStack regenPot = new ItemStack(Material.POTION);
		setPotionMeta(regenPot, "재생의 포션: 비활성화", PotionType.REGEN);
		
		ItemStack strPot = new ItemStack(Material.POTION);
		setPotionMeta(strPot, "힘의 포션: 비활성화", PotionType.STRENGTH);
		
		ItemStack elytra = new ItemStack(Material.ELYTRA);
		setMeta(elytra, "겉날개: 비활성화");
		
		limitGUI.setItem(0, sharp);
		limitGUI.setItem(1, prot);
		limitGUI.setItem(2, fireProt);
		limitGUI.setItem(3, blastProt);
		limitGUI.setItem(4, projProt);
		limitGUI.setItem(5, mending);
		limitGUI.setItem(6, sweepEdge);
		limitGUI.setItem(7, frostWalker);
		limitGUI.setItem(8, knockback);
		limitGUI.setItem(9, punch);
		limitGUI.setItem(10, thorn);
		limitGUI.setItem(11, depthStr);
		limitGUI.setItem(12, leapPot);
		limitGUI.setItem(13, harmPot);
		limitGUI.setItem(14, weakPot);
		limitGUI.setItem(15, regenPot);
		limitGUI.setItem(16, strPot);
		limitGUI.setItem(17, elytra);
	}
	
	public Inventory getLimitGUI() {
		return limitGUI;
	}
	
	private void setMeta(ItemStack item, String name) {
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(ChatColor.YELLOW + name);
		item.setItemMeta(im);
	}
	
	private void setPotionMeta(ItemStack item, String name, PotionType type) {
		PotionMeta im = (PotionMeta) item.getItemMeta();
		im.setDisplayName(ChatColor.YELLOW + name);
		im.setBasePotionData(new PotionData(type));
		item.setItemMeta(im);
	}
}
