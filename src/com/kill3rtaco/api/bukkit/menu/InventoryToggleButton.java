package com.kill3rtaco.api.bukkit.menu;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Represents a button that changes material to represent on/off states.
 * 
 * @author KILL3RTACO
 *
 * @since TacoAPI/Bukkit 3.0
 */
public class InventoryToggleButton extends InventoryButton {
	
	protected ItemStack	_on, _off;
	protected boolean	_state;
	
	/**
	 * Create a new InventoryToggleButton, with its default state set to on.
	 * Note that for aesthetic reasons and to avoid confusion the amount of the
	 * given ItemStacks are immediately set to 1.
	 * 
	 * @param menu
	 *            the menu this button belongs to
	 * @param on
	 *            the item representing this button's on 'on' state
	 * @param off
	 *            the item representing this button's on 'off' state
	 * @param title
	 *            the title of this button
	 * @since TacoAPI/Bukkit 3.0
	 */
	public InventoryToggleButton(InventoryMenu menu, ItemStack on,
			ItemStack off,
			String title) {
		this(menu, on, off, title, true);
	}
	
	/**
	 * Create a new InventoryToggleButton, with a specified default state. Note
	 * that for aesthetic reasons and to avoid confusion the amount of the given
	 * ItemStacks are immediately set to 1.
	 * 
	 * @param menu
	 *            the menu this button belongs to
	 * @param on
	 *            the item representing this button's on 'on' state
	 * @param off
	 *            the item representing this button's on 'off' state
	 * @param title
	 *            the title of this button
	 * @param defaultState
	 *            true if this
	 * @since TacoAPI/Bukkit 3.0
	 */
	public InventoryToggleButton(InventoryMenu menu, ItemStack on,
			ItemStack off, String title, boolean defaultState) {
		super(menu, (defaultState ? on : off), title);
		_state = defaultState;
		_on = on;
		_off = off;
		_on.setAmount(1);
		_off.setAmount(1);
	}
	
	/**
	 * Toggle the state of this button. The button is immediately repainted
	 * afterwards
	 * 
	 * @since TacoAPI/Bukkit 3.0
	 */
	public void toggleState() {
		setState(!_state);
	}
	
	/**
	 * Set the state of this button. The button is immediately repainted
	 * aftwerwards
	 * 
	 * @param state
	 *            the new state
	 * @since TacoAPI/Bukkit 3.0
	 */
	public void setState(boolean state) {
		_state = state;
		if (state)
			_item = _on;
		else
			_item = _off;
		repaint();
	}
	
	/**
	 * Get the current state of the button
	 * 
	 * @return the state
	 * @since TacoAPI/Bukkit 3.0
	 */
	public boolean getState() {
		return _state;
	}
	
	public void setTitle(String title) {
		super.setTitle(doReplace(title));
	}
	
	public String getTitle() {
		return doReplace(super.getTitle());
	}
	
	public String getDescription() {
		return doReplace(super.getDescription());
	}
	
	private String doReplace(String str) {
		return str.replace("%state", _state ? "on" : "off");
	}
	
	/**
	 * Acts the same as InventoryButton.onClick(), but toggles this button's
	 * state as well. Note that the state toggle happens <i>after</i> the
	 * InventoryButtonRunnable(s) have been run.
	 * 
	 * @since TacoAPI/Bukkit 3.0
	 * @return true if the state was toggled
	 * @see InventoryButton#onClick()
	 */
	public boolean onClick(int clickType, Player player) {
		if (super.onClick(clickType, player)) {
			toggleState();
			setTitle(getTitle()); //not redundant, i promise
			return true;
		}
		return false;
	}
	
}
