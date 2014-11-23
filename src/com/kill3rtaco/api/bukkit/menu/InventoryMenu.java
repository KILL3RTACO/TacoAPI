package com.kill3rtaco.api.bukkit.menu;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import com.kill3rtaco.api.bukkit.util.ChatUtils;

/**
 * Represents an inventory with "buttons" (ItemStacks) that can trigger an
 * action when clicked. Note that InventoryClickEvents from an InventoryMenu are
 * always cancelled. Instances of InventoryMenu should not be used to work like
 * an actual inventory.<br/>
 * <br/>
 * It should also be noted that showing two different players the same instance
 * of an InventoryMenu may produce unexpected results
 * 
 * @author KILL3RTACO
 *
 * @since TacoAPI/Bukkit 3.0
 */
public class InventoryMenu implements InventoryHolder {
	
	protected Inventory						_inv;
	protected String						_title;
	private Map<Integer, InventoryButton>	_buttons;
	private int								_maxRowIndex	= 0;
	
	/**
	 * Create a new InventoryMenu
	 * 
	 * @since TacoAPI/Bukkit 3.0
	 */
	public InventoryMenu() {
		this("");
	}
	
	/**
	 * Create a new (titled) InventoryMenu. The title cannot be more than 32
	 * characters long (including color codes)
	 * 
	 * @param title
	 *            the title of the menu
	 * @since TacoAPI/Bukkit 3.0
	 */
	public InventoryMenu(String title) {
		_title = title;
		_buttons = new HashMap<Integer, InventoryButton>();
	}
	
	public String getTitle() {
		return _title;
	}
	
	@Override
	public Inventory getInventory() {
		if (_inv == null) {
			_inv = Bukkit.createInventory(this, (_maxRowIndex + 1) * 9, ChatUtils.formatMessage(_title));
			for (int i : _buttons.keySet()) {
				_inv.setItem(i, _buttons.get(i).getItem());
			}
		}
		return _inv;
	}
	
	/**
	 * Add and set the position of a button
	 * 
	 * @param x
	 *            the x position of the button (0 - 8)
	 * @param y
	 *            the y position of the button
	 * @param button
	 *            the button
	 * @since TacoAPI/Bukkit 3.0
	 */
	public void setButton(int x, int y, InventoryButton button) {
		if (x < 0)
			x = 0;
		else if (x > 8)
			x = 8;
		if (y < 0)
			y = 0;
		_maxRowIndex = Math.max(_maxRowIndex, y);
		int slot = posToSlot(x, y);
		if (_inv != null)
			_inv.setItem(slot, button.getItem());
		_buttons.put(slot, button);
		button.setPosition(x, y);
	}
	
	/**
	 * Get the button at the specified position
	 * 
	 * @param x
	 *            the x position
	 * @param y
	 *            the y position
	 * @return the button, or null if no button is there
	 * @since TacoAPI/Bukkit 3.0
	 */
	public InventoryButton getButtonAt(int x, int y) {
		return _buttons.get(posToSlot(x, y));
	}
	
	public void onClick(int slot, ClickType clickType, Player player) {
		onClick(slot, InventoryButton.getClickType(clickType), player);
	}
	
	public void onClick(int slot, int clickType, Player player) {
		int[] pos = slotToPos(slot);
		onClick(pos[0], pos[1], clickType, player);
	}
	
	public void onClick(int x, int y, int clickType, Player player) {
		InventoryButton button = getButtonAt(x, y);
		if (button == null)
			return;
		button.onClick(clickType, player);
		if (button.shouldCloseMenu()) {
			player.closeInventory();
		}
	}
	
	public void showTo(Player player) {
		player.closeInventory();
		player.openInventory(getInventory());
	}
	
	/**
	 * Converts a x,y position to a slot number
	 * 
	 * @param x
	 *            the x position
	 * @param y
	 *            the y position
	 * @return the slot
	 * @since TacoAPI/Bukkit 3.0
	 */
	public static int posToSlot(int x, int y) {
		return (y * 9) + x;
	}
	
	/**
	 * Converts a slot to an x,y position
	 * 
	 * @param slot
	 *            the slot number
	 * @return an int array representing the position, with index 0 being the x
	 *         value and index 1 being the y value
	 * @since TacoAPI/Bukkit 3.0
	 */
	public static int[] slotToPos(int slot) {
		int x = slot % 9;
		int y = slot / 9;
		return new int[]{x, y};
	}
	
}
