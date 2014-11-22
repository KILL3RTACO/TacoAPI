package com.kill3rtaco.api.bukkit.menu;

import org.bukkit.inventory.ItemStack;

/**
 * Represents an InventoryButton that can display a changeable number.
 * 
 * @author KILL3RTACO
 *
 * @since TacoAPI/Bukkit 3.0
 * @see InventoryButton
 */
public class InventoryNumberedButton extends InventoryButton {
	
	protected int	_number;
	
	/**
	 * Create a new InventoryNumberedButton. Numbered button
	 * 
	 * @param menu
	 *            the menu this button belongs to
	 * @param item
	 *            the item representing this button
	 * @param number
	 *            the number
	 * @since TacoAPI/Bukkit 3.0
	 */
	public InventoryNumberedButton(InventoryMenu menu, ItemStack item,
			String title,
			int number) {
		super(menu, item, title);
		_number = number;
		_item.setAmount(number);
	}
	
	/**
	 * Set the number for this button
	 * 
	 * @param number
	 *            the new number
	 * @return the previous number
	 * @since TacoAPI/Bukkit 3.0
	 */
	public int setNumber(int number) {
		int oldNum = _number;
		_number = oldNum;
		_item.setAmount(_number);
		repaint();
		return oldNum;
	}
	
	/**
	 * Get the number of this button
	 * 
	 * @return the number
	 * @since TacoAPI/Bukkit 3.0
	 */
	public int getNumber() {
		return _number;
	}
	
	/**
	 * Increment this buttons number by 1
	 * 
	 * @return the old number
	 * @since TacoAPI/Bukkit 3.0
	 */
	public int incrementNumber() {
		return incrementNumber(1);
	}
	
	/**
	 * Increment this buttons number by the given amount
	 * 
	 * @return the old number
	 * @since TacoAPI/Bukkit 3.0
	 */
	public int incrementNumber(int amount) {
		return setNumber(_number + 1);
	}
	
	/**
	 * Decrement this buttons number by 1
	 * 
	 * @return the old number
	 * @since TacoAPI/Bukkit 3.0
	 */
	public int decrementNumber() {
		return decrementNumber(1);
	}
	
	/**
	 * Decrement this buttons number by the given amount
	 * 
	 * @return the old number
	 * @since TacoAPI/Bukkit 3.0
	 */
	public int decrementNumber(int amount) {
		return setNumber(_number - 1);
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
	
	public void setDescription(String desc) {
		super.setDescription(doReplace(desc));
	}
	
	private String doReplace(String str) {
		return str.replace("%num", _number + "");
	}
	
}
