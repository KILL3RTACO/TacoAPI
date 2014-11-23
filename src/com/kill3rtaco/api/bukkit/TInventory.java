package com.kill3rtaco.api.bukkit;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * A class to help with inventory operations
 * 
 * @author KILL3RTACO
 *
 * @since TacoAPI/Bukkit 3.0
 */
public class TInventory {
	/**
	 * Test if an inventory has room to hold the given items
	 * 
	 * @param inv
	 *            the player to test
	 * @param items
	 *            the items to test
	 * @return true if the items will fit in the inventory
	 * @since TacoApi/Bukkit 3.0
	 */
	public static boolean canHold(Inventory inv, ItemStack items) {
		int space = 0, needed = items.getAmount();
		for (ItemStack i : inv) {
			if (i == null) {
				space += items.getMaxStackSize();
//			} else if (i.getType() == items.getType() && i.getDurability() == items.getDurability() &&
//					(i.getItemMeta().hasDisplayName() ? i.getItemMeta().getDisplayName()
//							.equalsIgnoreCase(items.getItemMeta().getDisplayName()) : true)) {
//				space += items.getMaxStackSize() - i.getAmount();
//			}
			} else if (i.isSimilar(items)) {
				space += items.getMaxStackSize() - i.getAmount();
			}
		}
		if (space >= needed) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Tests whether the given Inventory's content is empty
	 * 
	 * @param inv
	 * @return Whether or not the inventory is empty
	 * @since TacoApi/Bukkit 3.0
	 */
	public static boolean isEmpty(Inventory inv) {
		for (ItemStack i : inv) {
			if (i != null)
				return false;
		}
		return true;
	}
	
	/**
	 * Gives the player items
	 * 
	 * @param inv
	 *            the inventory to add the items to
	 * @param items
	 *            the items to give
	 * @return false if the player doesn't have enough room to hold the items in
	 *         their inventory
	 * @since TacoApi/Bukkit 3.0
	 */
	public static boolean addItems(Inventory inv, ItemStack items) {
		if (canHold(inv, items)) {
			inv.addItem(items);
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Test if the inventory has the given items. This is different than
	 * Bukkit's Inventory.contains() method, this will check any and all
	 * ItemStack that equal the given ItemStack (ignoring the amount). The
	 * specified ItemStack can have an amount greater than 64, the usual maximum
	 * stack size.
	 * 
	 * @param inv
	 *            the inventory to test
	 * @param items
	 *            the items being looked for
	 * @return true if the inventory contains the given items
	 * @since TacoAPI/Bukkit 3.0
	 */
	public static boolean hasItems(Inventory inv, ItemStack items) {
		int needed = items.getAmount();
		int amount = 0;
		for (ItemStack i : inv) {
			if (i == null && items == null)
				return true;
			
			if (i.isSimilar(items)) {
				amount += i.getAmount();
			}
			
			if (amount >= needed)
				return true;
		}
		return false;
	}
	
	/**
	 * Takes the specified items out of the given player's invetory. This will
	 * return false if the player does not have the items
	 * 
	 * @param inv
	 *            the invnetory to test
	 * @param items
	 *            the items to test
	 * @return false if the player does not have the specified items, true
	 *         otherwise.
	 * @since TacoApi/Bukkit 3.0
	 */
	public static boolean takeItems(Inventory inv, ItemStack items) {
		if (inv.contains(items)) {
			inv.removeItem(items);
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Removes any ItemStacks whose .getType() == material.
	 * 
	 * @param player
	 *            the player to remove the items
	 * @param material
	 *            then material to remove
	 * @return the amount removed
	 * @since TacoApi/Bukkit 3.0
	 */
	public static int removeAll(Player player, Material material) {
		int removed = getAmountOfMaterial(player, material);
		if (removed > 0)
			player.getInventory().remove(material);
		return removed;
	}
	
	/**
	 * Gets the amount of the given material that the player has in their
	 * inventory
	 * 
	 * @param player
	 *            the player whose inventory is to be searched
	 * @param material
	 *            the material to look for
	 * @return the amount found, or 0 if material == null
	 * @since TacoApi/Bukkit 3.0
	 */
	@SuppressWarnings("deprecation")
	public static int getAmountOfMaterial(Player player, Material material) {
		if (material == null)
			return 0;
		else
			return getAmountOfMaterial(player, material.getId(), 0);
	}
	
	/**
	 * Gets the amount of the material with the given id that the player has in
	 * their inventory
	 * 
	 * @param player
	 *            the player whose inventory is to be searched
	 * @param id
	 *            the id of the material to look for
	 * @return the amount found, or 0 if Material.getMaterial(id) == null
	 * @since TacoApi/Bukkit 3.0
	 */
	@SuppressWarnings("deprecation")
	public static int getAmountOfMaterial(Player player, int id) {
		return getAmountOfMaterial(player, Material.getMaterial(id));
	}
	
	/**
	 * Gets the amount of the material with the given id and damage that the
	 * player has in their inventory
	 * 
	 * @param player
	 *            the player whose inventory is to be searched
	 * @param id
	 *            the id of the material to look for
	 * @param damage
	 *            the damage of the material to look for
	 * @return the amount found, or 0 if Material.getMaterial(id) == null
	 * @since TacoApi/Bukkit 3.0
	 */
	@SuppressWarnings("deprecation")
	public static int getAmountOfMaterial(Player player, int id, int damage) {
		int amount = 0;
		if (id == 0 || Material.getMaterial(id) == null)
			return 0;
		for (ItemStack i : player.getInventory()) {
			if (i.getTypeId() == id && i.getDurability() == damage) {
				amount += i.getAmount();
			}
		}
		return amount;
	}
}
