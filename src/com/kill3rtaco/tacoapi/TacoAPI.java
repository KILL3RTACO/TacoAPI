package com.kill3rtaco.tacoapi;

import java.io.File;

import com.kill3rtaco.api.bukkit.TEcon;
import com.kill3rtaco.api.bukkit.TWorldEdit;
import com.kill3rtaco.api.database.Database;
import com.kill3rtaco.api.rabbitmq.TRabbit;
import com.kill3rtaco.tacoapi.plugin.TacoAPIConfig;
import com.kill3rtaco.tacoapi.plugin.TacoAPIPlugin;

/**
 * Class holding static references to constants in TacoAPI
 * 
 * @author KILL3RTACO
 *
 * @since TacoAPI/Bukkit 3.0
 */
public final class TacoAPI {
	
	private static Database		DB;
	private static boolean		_econOnline, _weOnline, _rabbitOnline;
	
	/**
	 * Tab Completion List ID for a list of all possible item names. (
	 * {@link #TCL_DISPLAY_NAMES} and {@link #TCL_MATERIALS} combined)
	 * 
	 * @since TacoAPI/Bukkit 3.0
	 * @see #TCL_DISPLAY_NAMES
	 * @see #TCL_MATERIALS
	 */
	public static final String	TCL_ALL_ITEM_NAMES		= "TacoAPI.tcl.allItemNames";
	
	/**
	 * Tab Completion List ID for a list of display names for blocks and items.
	 * This uses the same list as DisplayName.
	 * {@link com.kill3rtaco.api.bukkit.util.ItemUtils.DisplayName#values()
	 * values()}
	 * 
	 * @since TacoAPI/Bukkit 3.0
	 */
	public static final String	TCL_DISPLAY_NAMES		= "TacoAPI.tcl.displayNames";
	
	/**
	 * Tab Completion List ID for a list of enchantment names
	 */
	public static final String	TCL_ENCHANTMENT_NAMES	= "TacoAPI.tcl.enchantmentNames";
	
	/**
	 * Tab Completion List ID for a list of entity types
	 * 
	 * @since TacoAPI/Bukkit 3.0
	 */
	public static final String	TCL_ENTITY_TYPES		= "TacoAPI.tcl.entityTypes";
	
	/**
	 * Tab Completion List ID for a list of material names
	 * 
	 * @since TacoAPI/Bukkit 3.0
	 */
	public static final String	TCL_MATERIALS			= "TacoAPI.tcl.materials";
	
	/**
	 * Tab Completion List ID for a list of player names
	 * 
	 * @since TacoAPI/Bukkit 3.0
	 */
	public static final String	TCL_PLAYER_NAMES		= "TacoAPI.tcl.playerNames";
	
	/**
	 * Tab Completion List ID for a list of world names
	 * 
	 * @since TacoAPI/Bukkit 3.0
	 */
	public static final String	TCL_WORLD_NAMES			= "TacoAPI.tcl.worldNames";
	
	static {
		
		try {
			TacoAPIConfig config = TacoAPIPlugin.config;
			DB = new Database(config.getMySqlServerAddress(), config.getMySqlServerPort(), config.getDatabaseName(), config.getDatabaseUsername(), config.getDatabasePassword());
		} catch (Exception e) {
			e.printStackTrace();
			TacoAPIPlugin.chat.out("Could not connect to MySQL server");
			DB = null;
		}
	}
	
	/**
	 * Get the Database object
	 * 
	 * @return the Database object
	 * @since TacoAPI/Bukkit 3.0
	 */
	public static Database getDB() {
		return DB;
	}
	
	public static void initLibraries() {
		//Economy (Vault)
		try {
			TEcon.init();
			_econOnline = true;
		} catch (NoClassDefFoundError e) {
			_econOnline = false;
		}
		
		//WorldEdit
		try {
			TWorldEdit.init();
			_weOnline = true;
		} catch (NoClassDefFoundError e) {
			_weOnline = false;
		}
		
		//RabbitMQ
		try {
			TRabbit.init(new File(TacoAPIPlugin.plugin.getDataFolder() + "/lib/rabbitmq-client.jar"), TacoAPIPlugin.config.getRabbitMqHost());
			_rabbitOnline = true;
		} catch (NoClassDefFoundError e) {
			_rabbitOnline = false;
		}
	}
	
	/**
	 * Is the economy api online?
	 * 
	 * @return true if the TEcon class can be used
	 * @since TacoAPI/Bukkit 3.0
	 */
	public static boolean econOnline() {
		return _econOnline;
	}
	
	/**
	 * Is the WorldEdit api online?
	 * 
	 * @return true if the TWorldEdit class can be used
	 * @since TacoAPI/Bukkit 3.0
	 */
	public static boolean worldEditOnline() {
		return _weOnline;
	}
	
	/**
	 * Is the RabbitMQ api online?
	 * 
	 * @return true if the TRabbit class can be used
	 * @since TacoAPI/RabbbitMQ 1.0
	 */
	public static boolean rabbitOnline() {
		return _rabbitOnline;
	}
	
	private TacoAPI() {}
}
