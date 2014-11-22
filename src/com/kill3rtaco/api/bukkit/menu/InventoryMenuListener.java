package com.kill3rtaco.api.bukkit.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

/**
 * A new instance of this class can be pass to PluginManager.registerEvents().
 * Note that the TacoAPI plugin automatically creates a new instance of this
 * class and registers the listener.
 * 
 * @author KILL3RTACO
 *
 * @since TacoAPI/Bukkit 3.0
 */
public class InventoryMenuListener implements Listener {
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onInventoryClick(InventoryClickEvent event) {
		if (event.getInventory().getHolder() instanceof InventoryMenu) {
			if (event.getRawSlot() >= event.getInventory().getSize()) {
				
				//cancel if shift click (add items from top to bottom)
				if (event.isShiftClick())
					event.setCancelled(true);
				
				return;
			}
			
			InventoryMenu menu = (InventoryMenu) event.getInventory().getHolder();
			menu.onClick(event.getSlot(), event.getClick(), (Player) event.getWhoClicked());
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	//bug when opening a new menu from a button - drops the item
	//this handler fixes that issue
	public void onItemDropFromMenu(PlayerDropItemEvent event) {
		if (event.getPlayer().getOpenInventory().getTopInventory().getHolder() instanceof InventoryMenu) {
			event.setCancelled(true);
		}
	}
	
	//helps prevent players from moving items from their own inventory into the menu
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onItemDrag(InventoryDragEvent event) {
		if (event.getInventory().getHolder() instanceof InventoryMenu) {
			InventoryMenuClick click = new InventoryMenuClick(event);
			if (click.both())
				event.setCancelled(true);
		}
	}
}