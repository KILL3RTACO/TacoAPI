package com.kill3rtaco.api.bukkit.plugin;

import java.io.IOException;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.kill3rtaco.api.bukkit.TChat;

/**
 * Represents a bukkit plugin
 * 
 * @author KILL3RTACO
 * @since TacoAPI/Bukkit 2.0
 *
 */
public abstract class TacoPlugin extends JavaPlugin {
	
	private long	timeStart, timeEnd;
	private Metrics	metrics;
	
	public void onEnable() {
		timeStart = System.currentTimeMillis();
		try {
			metrics = new Metrics(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
		onStart();
		timeEnd = System.currentTimeMillis();
		new TChat("&r" + this.getDescription().getName()).out("Enabled - " + timeTakenToEnable() + "s");
	}
	
	public void onDisable() {
		onStop();
	}
	
	/**
	 * Called when the plugin is enabling
	 * 
	 * @since TacoAPI/Bukkit 2.0
	 */
	public abstract void onStart();
	
	/**
	 * Called when the plugin is disabling
	 * 
	 * @since TacoAPI/Bukkit 2.0
	 */
	public abstract void onStop();
	
	/**
	 * Start Metrics (c) Hidendra
	 * 
	 * @since TacoAPI/Bukkit 2.0
	 */
	public void startMetrics() {
		metrics.start();
	}
	
	/**
	 * Get the Metrics object
	 * 
	 * @return the Metrics object for this plugin
	 * @since TacoAPI/Bukkit 3.0
	 */
	public Metrics getMetrics() {
		return metrics;
	}
	
	/**
	 * Get how long it took for the plugin to enable
	 * 
	 * @return How long it took to enable (in seconds)
	 * @since TacoAPI/Bukkit 2.0
	 */
	public double timeTakenToEnable() {
		return (timeEnd - timeStart) / 1000D;
	}
	
	/**
	 * Convenience method to register events. Equivalent to
	 * 
	 * <pre>
	 * getServer().getPluginManager().registerEvents(l, this)
	 * </pre>
	 * 
	 * @param l
	 *            The listener to register
	 * @since TacoAPI/Bukkit 2.0
	 */
	public void registerEvents(Listener l) {
		getServer().getPluginManager().registerEvents(l, this);
	}
	
}
