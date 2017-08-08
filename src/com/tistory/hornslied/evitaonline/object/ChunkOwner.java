package com.tistory.hornslied.evitaonline.object;

import java.util.ArrayList;

import org.bukkit.Chunk;

public class ChunkOwner {
	ArrayList<Chunk> chunks = new ArrayList<Chunk>();
	
	public boolean isChunkOwner(Chunk chunk) {
		if(chunks.contains(chunk)) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean addChunk(Chunk chunk) {
		if(chunks.contains(chunk)) {
			return false;
		} else {
			chunks.add(chunk);
			return true;
		}
	}
	
	public boolean removeChunk(Chunk chunk) {
		if(chunks.contains(chunk)) {
			chunks.remove(chunk);
			return true;
		} else {
			return false;
		}
	}
}
