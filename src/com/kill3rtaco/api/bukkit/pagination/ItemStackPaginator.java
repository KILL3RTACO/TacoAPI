package com.kill3rtaco.api.bukkit.pagination;

import java.util.Collection;

import org.bukkit.inventory.ItemStack;

import com.kill3rtaco.api.pagination.Paginator;

/**
 * A Paginator for ItemStacks. Splits ItemStacks if their amount exceeds their
 * maxStackSize.
 * 
 * @author KILL3RTACO
 *
 * @since TacoAPI/Bukkit 3.0
 */
public class ItemStackPaginator extends Paginator<ItemStack> {
	
	public ItemStackPaginator(int elementsPerPage) {
		super(elementsPerPage);
	}
	
	public ItemStackPaginator(int elementsPerPage,
			Collection<ItemStack> elements) {
		super(elementsPerPage, elements);
	}
	
	public void append(ItemStack items) {
		//add items to current elements
		int maxStack = items.getType().getMaxStackSize();
		for (ItemStack i : getElements()) {
			if (i == null) {
				int amount = items.getAmount();
				i = items.clone();
				int add = Math.min(amount, maxStack);
				i.setAmount(add);
				items.setAmount(items.getAmount() - add);
				if (items.getAmount() == 0)
					return;
				
				continue;
			}
			
			int amount = i.getAmount();
			if (i.isSimilar(items) && amount < maxStack) {
				int add = Math.min(maxStack - amount, items.getAmount());
				i.setAmount(i.getAmount() + add);
				items.setAmount(items.getAmount() - add);
				if (items.getAmount() == 0)
					return;
			}
		}
		
		//split if necessary
		if (items.getAmount() > maxStack) {
			
			ItemStack max = items.clone();
			max.setAmount(items.getType().getMaxStackSize());
			super.append(max);
			
			ItemStack lower = items.clone();
			lower.setAmount(items.getAmount() - max.getAmount());
			append(lower); //might be split even further
			return;
		}
		super.append(items);
	}
	
}
