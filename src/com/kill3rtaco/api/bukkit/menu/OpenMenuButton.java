package com.kill3rtaco.api.bukkit.menu;

import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

/**
 * An InventoryButton that opens a menu when it is left-clicked
 * 
 * @author KILL3RTACO
 *
 * @since TacoAPI/Bukkit 3.0
 */
public class OpenMenuButton extends InventoryButton {
	
	/**
	 * @param menu
	 *            the menu this button belongs to
	 * @param item
	 *            the item that should represent this button
	 * @param title
	 *            the title of this button
	 * @param toOpen
	 *            the menu to open when this button is clicked
	 * @param taskHandler
	 *            the plugin to pass to the BukkitRunnable when calling
	 *            openMenu()
	 * @since TacoAPI/Bukkit 3.0
	 */
	public OpenMenuButton(InventoryMenu menu, ItemStack item, String title,
			final InventoryMenu toOpen, final Plugin taskHandler) {
		super(menu, item, title);
		onClick(LEFT_CLICK, new InventoryButtonRunnable() {
			
			@Override
			public void run() {
				openMenu(toOpen, getPlayer(), taskHandler);
			}
			
		});
	}
	
}
