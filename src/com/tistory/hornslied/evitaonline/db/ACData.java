package com.tistory.hornslied.evitaonline.db;

import java.util.Hashtable;

import com.tistory.hornslied.evitaonline.object.AncientCity;

public class ACData {
	public static Hashtable<String, AncientCity> acs = new Hashtable<String, AncientCity>();
	
	public static AncientCity getAC(String name) {
		if(acs.containsKey(name)) {
			return acs.get(name);
		} else {
			return null;
		}
	}
}
