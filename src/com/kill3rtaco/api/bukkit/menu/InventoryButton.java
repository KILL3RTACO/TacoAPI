package com.kill3rtaco.api.bukkit.menu;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.kill3rtaco.api.bukkit.util.ChatUtils;
import com.kill3rtaco.api.util.DoubleArrayList;

/**
 * Represents a button in an InventoryMenu
 * 
 * @author KILL3RTACO
 *
 * @since TacoAPI/Bukkit 3.0
 */
public class InventoryButton {
	
	/**
	 * Flag indicating that the runnable should be run when the player left
	 * clicks a button
	 */
	public static final int										LEFT_CLICK			= 1;
	
	/**
	 * Flag indicating that the runnable should be run when the player middle
	 * clicks a button
	 */
	public static final int										MIDDLE_CLICK		= 1 << 1;
	
	/**
	 * Flag indicating that the runnable should be run when the player right
	 * clicks a button
	 */
	public static final int										RIGHT_CLICK			= 1 << 2;
	
	/**
	 * Flag indicating that the runnable should be run when the player left
	 * clicks a button while holding the shift key
	 */
	public static final int										SHIFT_LEFT_CLICK	= 1 << 3;
	
	/**
	 * Flag indicating that the runnable should be run when the player right
	 * clicks a button while holding the shift key
	 */
	public static final int										SHIFT_RIGHT_CLICK	= 1 << 4;
	
	/**
	 * Flag indicating that the runnable should be run when the player double
	 * (left) clicks a button
	 */
	public static final int										DOUBLE_CLICK		= 1 << 5;
	
	/**
	 * Flag combining all click flags.
	 */
	public static final int										ALL					= LEFT_CLICK | MIDDLE_CLICK | RIGHT_CLICK | SHIFT_LEFT_CLICK | SHIFT_RIGHT_CLICK | DOUBLE_CLICK;
	
	private DoubleArrayList<Integer, InventoryButtonRunnable>	_onClick;
	private InventoryMenu										_menu;
	private int													_posX, _posY;
	private boolean												_firstRunnable		= true,
			_shouldCloseMenu = true;
	
	protected ItemStack											_item;
	protected String											_title, _desc;
	
	/**
	 * Create a new InventoryButton.
	 * 
	 * @param menu
	 *            the menu this button belongs to
	 * @param item
	 *            the item to use as its display. Note that for aesthetic
	 *            reasons and to avoid confusion the amount of the given
	 *            ItemStack is immediately set to 1.
	 * @throws IllegalArgumentException
	 *             if menu or item are null
	 * @since TacoAPI/Bukkit 3.0
	 * @see InventoryNumberedButton
	 */
	public InventoryButton(InventoryMenu menu, ItemStack item, String title) {
		if (menu == null)
			throw new IllegalArgumentException("menu cannot be null");
		if (item == null)
			throw new IllegalArgumentException("item cannot be null");
		_menu = menu;
		_item = item;
		_item.setAmount(1);
		if (title != null)
			setTitle(title);
		_onClick = new DoubleArrayList<Integer, InventoryButtonRunnable>();
	}
	
	/**
	 * Set whether this button should close the menu it belongs to when it is
	 * clicked
	 * 
	 * @param shouldClose
	 *            true if this button should close the menu when clicked
	 * @since TacoAPI/Bukkit 3.0
	 */
	public void setShouldCloseMenu(boolean shouldClose) {
		_shouldCloseMenu = shouldClose;
	}
	
	/**
	 * Get whether this button should close its menu when clicked
	 * 
	 * @return true if this button should close its menu when clicked
	 * @since TacoAPI/Bukkit 3.0
	 */
	public boolean shouldCloseMenu() {
		return _shouldCloseMenu;
	}
	
	/**
	 * If set to true (its default value), this button will only the first
	 * applicable runnable found (according to the click type) when clicked.
	 * Otherwise, all applicable runnables will be run.
	 * 
	 * @param firstRunnable
	 *            true if only the first runnable found should be run
	 * @since TacoAPI/Bukkit 3.0
	 */
	public void firstRunnable(boolean firstRunnable) {
		_firstRunnable = firstRunnable;
	}
	
	/**
	 * Get whether this button should only run the first applicable runnable
	 * found (according to the click type) when clicked
	 * 
	 * @return true if this button should only run the first applicable runnable
	 *         when clicked
	 * @since TacoAPI/Bukkit 3.0
	 */
	public boolean firstRunnable() {
		return _firstRunnable;
	}
	
	/**
	 * Get the menu this button belongs to.
	 * 
	 * @return the menu this button belongs to
	 * @since TacoAPI/Bukkit 3.0
	 */
	public InventoryMenu getMenu() {
		return _menu;
	}
	
	/**
	 * Get the item representing this button
	 * 
	 * @return the item
	 * @since TacoAPI/Bukkit 3.0
	 */
	public ItemStack getItem() {
		return _item;
	}
	
	/**
	 * Set the title of this button (the item's display name).
	 * 
	 * @param title
	 *            the title of this button
	 * @since TacoAPI/Bukkit 3.0
	 */
	public void setTitle(String title) {
		ItemMeta meta = _item.getItemMeta();
		meta.setDisplayName(ChatUtils.formatMessage(title));
		_item.setItemMeta(meta);
		_title = title;
	}
	
	/**
	 * Get this button's title
	 * 
	 * @return the title of this button
	 * @since TacoAPI/Bukkit 3.0
	 */
	public String getTitle() {
		return _title;
	}
	
	/**
	 * Set the description of the button. The description is automatically
	 * line-wrapped.<br/>
	 * <br/>
	 * <ul>
	 * <li>For InventoryNumberedButtons, <code>%num</code> can be included in
	 * the description to be replaced with the button's number (via getNumber())
	 * </li>
	 * <li>For InventoryToggleButtons, <code>%state</code> can be included in
	 * the description to be replaced with the button's state, either "on" or
	 * "off"</li>
	 * </ul>
	 * 
	 * @param desc
	 *            the description
	 * @since TacoAPI/Bukkit 3.0
	 */
	public void setDescription(String desc) {
		setDescription(desc, true);
	}
	
	/**
	 * Set the description of the button, (color formatting allowed) and whether
	 * to wrap lines. If line wrapping is set to false, <code>\n</code> can
	 * always be used for new lines.<br/>
	 * <br/>
	 * <ul>
	 * <li>For InventoryNumberedButtons, <code>%num</code> can be included in
	 * the description to be replaced with the button's number (via getNumber())
	 * </li>
	 * <li>For InventoryToggleButtons, <code>%state</code> can be included in
	 * the description to be replaced with the button's state, either "on" or
	 * "off"</li>
	 * </ul>
	 * 
	 * @param desc
	 *            the description
	 * @param wrapLines
	 *            true if lines should be wrapped
	 * @since TacoAPI/Bukkit 3.0
	 */
	public void setDescription(String desc, boolean wrapLines) {
		_desc = ChatUtils.formatMessage(desc);
		String tt;
		if (wrapLines) {
			tt = "";
			int count = 0;
			int lettersInLine = 25;
			for (String s : _desc.split("\\s+")) {
				count += s.length();
				tt += s;
				if (count >= lettersInLine) {
					tt += "\n";
					count = 0;
				} else {
					tt += " ";
					count++;
				}
			}
		} else {
			tt = desc;
		}
		ArrayList<String> lore = new ArrayList<String>();
		for (String s : tt.split("\n")) {
			lore.add(s);
		}
		ItemMeta meta = _item.getItemMeta();
		meta.setLore(lore);
		_item.setItemMeta(meta);
	}
	
	/**
	 * Get this button's description. This does not include newline characters
	 * if line wrapping was enabled when setting the description.
	 * 
	 * @return this button's description
	 * @since
	 */
	public String getDescription() {
		return _desc;
	}
	
	/**
	 * Register a click listener. The <code>onClick</code> runnable will be run
	 * when any of the given click types are fired. Multiple click types can be
	 * added by bitwise OR'ing them.<br/>
	 * <br/>
	 * Example:
	 * 
	 * <pre>
	 * <code>//button is an instance of InventoryButton</code>
	 * button.onClick(InventoryButton.LEFT_CLICK | InventoryButton.DOUBLE_CLICK, new InventoryButtonRunnable(){
	 *     
	 *     <code>@Override</code>
	 *     public void run(){
	 *         //do stuff here
	 *     }
	 *     
	 * });
	 * </pre>
	 * 
	 * @param clickTypes
	 *            the click types
	 * @param onClick
	 *            the runnable to run when this button is clicked
	 * @since TacoAPI/Bukkit 3.0
	 */
	public void onClick(int clickTypes, InventoryButtonRunnable onClick) {
		onClick.setButton(this);
		_onClick.put(clickTypes, onClick);
	}
	
	/**
	 * Convience method for calling onClick(int) using the ClickType enum from
	 * bukkit
	 * 
	 * @param type
	 *            the ClickType
	 * @param player
	 *            the player that clicked the button
	 * @since TacoAPI/Bukkit 3.0
	 */
	public void onClick(ClickType type, Player player) {
		int clickType = getClickType(type);
		if (clickType == 0)
			return;
		onClick(clickType, player);
	}
	
	/**
	 * Called when the button has been click
	 * 
	 * @param clickType
	 *            the type of click
	 * @param player
	 *            the player that clicked the button
	 * @return true if the runnable(s) for the click was/were run
	 * @since TacoAPI/Bukkit
	 */
	public boolean onClick(int clickType, Player player) {
		boolean found = false;
		for (int i : _onClick.getKeys()) {
			if ((i & clickType) > 0) {
				InventoryButtonRunnable runnable = _onClick.get(i);
				if (runnable == null)
					continue;
				runnable.setPlayer(player);
				runnable.run();
				runnable.setPlayer(null);
				found = true;
				if (_firstRunnable)
					return true;
			}
		}
		return found;
	}
	
	void setPosition(int x, int y) {
		_posX = x;
		_posY = y;
	}
	
	/**
	 * Get the x position of this button
	 * 
	 * @return the x position
	 * @since TacoAPI/Bukkit 3.0
	 */
	public int getX() {
		return _posX;
	}
	
	/**
	 * Get the y position of this button
	 * 
	 * @return the y position
	 * @since TacoAPI/Bukkit 3.0
	 */
	public int getY() {
		return _posY;
	}
	
	/**
	 * Use this method to repaint the button, meaning to update its item in the
	 * inventory
	 * 
	 * @since TacoAPI/Bukkit 3.0
	 */
	protected void repaint() {
		getMenu().setButton(getX(), getY(), this);
	}
	
	/**
	 * Converts a Bukkit ClickType to a InventoryButton click type
	 * 
	 * @param click
	 *            the bukkit click type
	 * @return the InventoryButton click type, or 0 if not found
	 * @since TacoAPI/Bukkit 3.0
	 */
	public static int getClickType(ClickType click) {
		if (click == ClickType.LEFT)
			return LEFT_CLICK;
		else if (click == ClickType.SHIFT_LEFT)
			return SHIFT_LEFT_CLICK;
		else if (click == ClickType.MIDDLE)
			return MIDDLE_CLICK;
		else if (click == ClickType.RIGHT)
			return RIGHT_CLICK;
		else if (click == ClickType.SHIFT_RIGHT)
			return SHIFT_RIGHT_CLICK;
		else if (click == ClickType.DOUBLE_CLICK)
			return DOUBLE_CLICK;
		else
			return 0;
	}
	
}
