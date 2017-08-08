package com.tistory.hornslied.evitaonline;

import java.io.File;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.tistory.hornslied.evitaonline.commands.InfoCommand;
import com.tistory.hornslied.evitaonline.commands.TownyCommand;
import com.tistory.hornslied.evitaonline.db.DB;
import com.tistory.hornslied.evitaonline.listeners.CombatListener;
import com.tistory.hornslied.evitaonline.listeners.DeathListener;
import com.tistory.hornslied.evitaonline.listeners.EnderpearlListener;
import com.tistory.hornslied.evitaonline.listeners.JoinListener;
import com.tistory.hornslied.evitaonline.listeners.MoveChunkListener;

import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin {
	private PluginManager pm;
	
	public JoinListener joinListener;
	public EnderpearlListener enderPearlListener;
	public CombatListener combatListener;
	public MoveChunkListener moveChunkListener;
	public DeathListener deathListener;
	public InfoCommand infoCommand;
	public TownyCommand townyCommand;
	
	public File configFile;
	public DB db;
	public Economy economy;
	
	@Override
	public void onEnable() {
		pm = Bukkit.getPluginManager();
		configFile = new File(getDataFolder(), "config.yml");
		if(!(configFile.exists())) saveDefaultConfig();
		
		String dburl;
		
		if(getConfig().getBoolean("db.useSSL")) {
			dburl = "jdbc:mysql://" + getConfig().getString("db.host") + ":" + getConfig().getString("db.port") + "/" 
					+ getConfig().getString("db.dbname");
		} else {
			dburl = "jdbc:mysql://" + getConfig().getString("db.host") + ":" + getConfig().getString("db.port") + "/" 
					+ getConfig().getString("db.dbname") + "?useSSL=false";
		}
		
		db = new DB(dburl, getConfig().getString("db.user"), getConfig().getString("db.password"));
		
		registerEvents();
		setupEconomy();
		try {
			createDBTables();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		initCommands();
	}
	
	@Override
	public void onDisable() {
		townyCommand.endWar();
	}
	
	private void registerEvents() {
		joinListener = new JoinListener(this);
		enderPearlListener = new EnderpearlListener(this);
		combatListener = new CombatListener(this);
		moveChunkListener = new MoveChunkListener(this);
		deathListener = new DeathListener();
		infoCommand = new InfoCommand(this);
		townyCommand = new TownyCommand(this);
		
		pm.registerEvents(joinListener, this);
		pm.registerEvents(enderPearlListener, this);
		pm.registerEvents(combatListener, this);
		pm.registerEvents(moveChunkListener, this);
		pm.registerEvents(deathListener, this);
	}
	
	private boolean setupEconomy() {
        if (pm.getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }
	
	private void initCommands() {
		getCommand("cafe").setExecutor(infoCommand);
		getCommand("evitatowny").setExecutor(townyCommand);
	}
	
	private void createDBTables() throws SQLException {
		DatabaseMetaData md = db.getConnection().getMetaData();
		if(!(md.getTables(null, null, "warlogs", null).next())) {
			db.query("CREATE TABLE warlogs (id int NOT NULL PRIMARY KEY AUTO_INCREMENT, attacknation varchar(255) NOT NULL, "
					+ "defendtown varchar(255) NOT NULL, date varchar(255) NOT NULL, isWin tinyint(1) DEFAULT 0);");
		}
		
		if(!(md.getTables(null, null, "reported", null).next())) {
			db.query("CREATE TABLE reported (id int NOT NULL PRIMARY KEY AUTO_INCREMENT, date varchar(255) NOT NULL, "
					+ "reporter varchar(255) NOT NULL, reported varchar(255) NOT NULL, reason varchar(255) NOT NULL);");
		}
	}
}
