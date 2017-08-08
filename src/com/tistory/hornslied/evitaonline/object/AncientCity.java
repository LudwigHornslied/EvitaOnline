package com.tistory.hornslied.evitaonline.object;

import com.palmergames.bukkit.towny.object.Nation;

public class AncientCity extends ChunkOwner {
	private String name;
	private Nation owner;
	
	public AncientCity(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Nation getOwner() {
		return owner;
	}
	
	public void setOwner(Nation nation) {
		owner = nation;
	}
	
	public void removeOwner() {
		owner = null;
	}
}
