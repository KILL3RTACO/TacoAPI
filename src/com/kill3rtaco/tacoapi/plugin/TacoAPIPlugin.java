package com.kill3rtaco.tacoapi.plugin;

import java.io.File;

import com.kill3rtaco.api.bukkit.TChat;
import com.kill3rtaco.api.bukkit.menu.InventoryMenuListener;
import com.kill3rtaco.api.bukkit.plugin.TacoPlugin;
import com.kill3rtaco.tacoapi.TacoAPI;

public class TacoAPIPlugin extends TacoPlugin {
	
	public static TacoAPIPlugin	plugin;
	public static final TChat	chat	= new TChat("TacoAPI");
	public static TacoAPIConfig	config;
	public static File			playerData;
	
	@Override
	public void onStart() {
		plugin = this;
		config = new TacoAPIConfig();
		playerData = new File(getDataFolder() + "/playerData");
		
		TacoAPI.initLibraries();
		
		chat.out("EconAPI: " + onOff(TacoAPI.econOnline()));
		chat.out("WorldEditAPI: " + onOff(TacoAPI.worldEditOnline()));
		chat.out("RabbitAPI: " + onOff(TacoAPI.rabbitOnline()));
		
		if (TacoAPI.rabbitOnline()) {
			
		}
		
		if (config.useMetrics()) {
			chat.out("Starting Metrics...");
			startMetrics();
		}
		
		registerEvents(new InventoryMenuListener());
	}
	
	@Override
	public void onStop() {
		
	}
	
	private String onOff(boolean enabled) {
		return enabled ? "Online" : "Offline";
	}
	
}
