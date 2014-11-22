package com.kill3rtaco.api.bukkit.menu;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

//used to be found in ItemMail
public class InventoryMenuClick {
	
	private boolean	_top	= false, _bottom = false, _neither = false;
	
	public InventoryMenuClick(InventoryClickEvent event) {
		int size = event.getView().getTopInventory().getSize();
		if (event.getRawSlot() == -1)
			_neither = true;
		else if (event.getRawSlot() < size)
			_top = true;
		else
			_bottom = true;
	}
	
	public InventoryMenuClick(InventoryDragEvent event) {
		int size = event.getView().getTopInventory().getSize();
		for (int i : event.getRawSlots()) {
			if (i == -1) {
				_neither = true;
				break;
			}
			if (i < size)
				_top = true;
			else
				_bottom = true;
		}
	}
	
	public boolean top() {
		return _top && !_bottom;
	}
	
	public boolean bottom() {
		return _bottom && !_top;
	}
	
	public boolean both() {
		return _top && _bottom;
	}
	
	public boolean neither() {
		return _neither;
	}
	
}
