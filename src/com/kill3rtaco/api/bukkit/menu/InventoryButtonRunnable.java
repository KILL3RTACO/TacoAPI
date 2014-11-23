package com.kill3rtaco.api.bukkit.menu;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.kill3rtaco.api.bukkit.TChat;

/**
 * A class to be used for the event methods of InventoryButtons
 * 
 * @author KILL3RTACO
 *
 * @since TacoAPI/Bukkit 3.0
 */
public abstract class InventoryButtonRunnable implements Runnable {
	
	private InventoryButton	_button;
	private Player			_player;
	private TChat			chat	= new TChat("");
	
	void setButton(InventoryButton button) {
		_button = button;
	}
	
	void setPlayer(Player player) {
		_player = player;
	}
	
	/**
	 * Get the button this runnable belongs to
	 * 
	 * @return the button this runnable belongs to
	 * @since TacoAPI/Bukkit 3.0
	 */
	public InventoryButton getButton() {
		return _button;
	}
	
	/**
	 * Get the player who clicked the button
	 * 
	 * @return the player who clicked the button
	 * @since TacoAPI/Bukkit 3.0
	 */
	public Player getPlayer() {
		return _player;
	}
	
	/**
	 * Sends the player who click the button a message
	 * 
	 * @param message
	 *            the message to send
	 * @since TacoAPI/Bukkit 3.0
	 */
	public void sendClickerMessage(String message) {
		chat.sendMessageTo(getPlayer(), message, false);
	}
	
	/**
	 * Opens another InventoryMenu on the next server tick. This is done
	 * because, if it was open on the same tick, there would be noticeable
	 * issues for the player.
	 * 
	 * @param menu
	 *            the menu to show
	 * @param showTo
	 *            who to show the menu to
	 * @param plugin
	 *            the plugin opening the menu
	 * @since TacoAPI/Bukkit 3.0
	 */
	public void openMenu(final InventoryMenu menu, final Player showTo, Plugin plugin) {
		new BukkitRunnable() {
			
			@Override
			public void run() {
				menu.showTo(showTo);
			}
			
		}.runTaskLater(plugin, 1L);
	}
}
