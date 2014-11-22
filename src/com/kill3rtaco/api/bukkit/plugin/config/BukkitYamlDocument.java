package com.kill3rtaco.api.bukkit.plugin.config;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.kill3rtaco.api.bukkit.util.ItemUtils;
import com.kill3rtaco.api.config.yml.YamlDocument;
import com.kill3rtaco.api.config.yml.YamlNode;

/**
 * @author KILL3RTACO
 *
 * @since
 */
public class BukkitYamlDocument extends YamlDocument {
	
	public BukkitYamlDocument() {
		
	}
	
	public BukkitYamlDocument(File file) {
		super(file);
	}
	
	public void set(String path, Object value) {
		set(path, value, SaveType.EXPANDED);
	}
	
	@SuppressWarnings("deprecation")
	public void set(String path, Object value, SaveType saveType) {
		if (value instanceof Color) {
			Color color = (Color) value;
			if (saveType == SaveType.COMPACT) {
				set(path, toHex(color.asRGB()));
			} else {
				YamlNode node = getNode(path, true);
				node.set("red", color.getRed());
				node.set("green", color.getGreen());
				node.set("blue", color.getBlue());
			}
		} else if (value instanceof ItemStack) {
			ItemStack items = (ItemStack) value;
			if (saveType == SaveType.COMPACT) {
				set(path, items.getTypeId() + ":" + items.getDurability() + " " + items.getAmount());
			} else {
				YamlNode node = getNode(path, true);
				node.set("id", items.getTypeId());
				node.set("damage", items.getDurability());
				node.set("amount", items.getAmount());
			}
		} else if (value instanceof Location || value instanceof Vector) {
			String world = null;
			double x, y, z, pitch, yaw;
			x = y = z = pitch = yaw = 0;
			if (value instanceof Location) {
				Location loc = (Location) value;
				x = loc.getX();
				y = loc.getY();
				z = loc.getZ();
				pitch = loc.getPitch();
				yaw = loc.getYaw();
				world = (loc.getWorld() != null ? loc.getWorld().getName() : null);
			} else {
				Vector vec = (Vector) value;
				x = vec.getX();
				y = vec.getY();
				z = vec.getZ();
			}
			if (saveType == SaveType.COMPACT) {
				String str = (world != null && !world.isEmpty() ? world : "") + " " + x + " " + y + " " + z + " " + yaw + " " + pitch;
				set(path, str.trim());
			} else {
				YamlNode node = getNode(path, true);
				if (world != null && !world.isEmpty())
					set("world", world);
				node.set("x", x);
				node.set("y", y);
				node.set("z", z);
				node.set("pitch", pitch);
				node.set("yaw", yaw);
			}
		} else {
			super.set(path, value);
			return;
		}
		
		//only here if bukkit object
		if (options().saveOnSet)
			save();
	}
	
	public Color getColor(String path) {
		if (!isSet(path))
			return null;
		
		YamlNode node = getNode(path);
		int r, g, b;
		r = g = b = 0;
		if (node.isSection()) {
			// get rgb values from red|r green|g blue|b nodes
			
			if (node.isSet("red") && node.getNode("red").isNumber())
				r = node.getNode("red").asInt();
			else if (node.isSet("r") && node.getNode("r").isNumber())
				r = node.getNode("r").asInt();
			
			if (node.isSet("green") && node.getNode("green").isNumber())
				g = node.getNode("green").asInt();
			else if (node.isSet("g") && node.getNode("g").isNumber())
				g = node.getNode("g").asInt();
			
			if (node.isSet("blue") && node.getNode("blue").isNumber())
				b = node.getNode("blue").asInt();
			else if (node.isSet("b") && node.getNode("b").isNumber())
				b = node.getNode("b").asInt();
			
		} else if (node.isNumber()) {
			//colors are integers. ex: 0xFFFFFF is white, but holds the integer
			//value 256 * 256 * 256, which is 16777215
			return Color.fromRGB(node.asInt());
		} else {
			//assume value is a string, parse as "r g b"
			//if any cannot be parsed as int, or does not exist, the value is 0
			String str = node.asString();
			if (str.isEmpty())
				return Color.BLACK;
			String[] split = str.split("\\s+");
			
			r = makeInt(split[0]);
			g = (split.length > 1 ? makeInt(split[1]) : 0);
			b = (split.length > 2 ? makeInt(split[2]) : 0);
		}
		return Color.fromRGB(r, g, b);
	}
	
	public Color getColor(String path, Color def) {
		if (isSet(path))
			return getColor(path);
		
		if (options().saveDefaults)
			set(path, def);
		
		return def;
	}
	
	//simple, no meta involved
	public ItemStack getSimpleItemStack(String path) {
		if (!isSet(path))
			return null;
		
		YamlNode node = getNode(path);
		if (node.isSection()) {
			//get values from nodes
			
			//requied value
			if (!node.isSet("id"))
				return null;
			
			ItemStack items = ItemUtils.createItemStack(node.getString("id"), 1);
			items.setDurability((short) (node.isNumber("damage") ? node.getInt("damage") : 0));
			items.setAmount((node.isNumber("amount") ? node.getInt("amount") : 1));
			return items;
		} else {
			//assume string
			String str = node.asString();
			if (str == null || str.isEmpty())
				return null;
			
			String[] split = str.split("\\s+");
			int amount = 1;
			if (split.length > 1) {
				Integer i = makeInt(split[1]);
				if (i != null)
					amount = i;
			}
			return ItemUtils.createItemStack(split[0], amount);
		}
	}
	
	//meta not saved
	public ItemStack getSimpleItemStack(String path, ItemStack def) {
		if (isSet(path))
			return getSimpleItemStack(path);
		
		if (options().saveDefaults)
			set(path, def);
		
		return def;
	}
	
	public Location getLocation(String path) {
		if (!isSet(path))
			return null;
		
		YamlNode node = getNode(path);
		if (node.isSection()) {
			World world = (node.isSet("world") ? Bukkit.getWorld(node.getString("world")) : null);
			double x = (node.isSet("x") ? node.getDouble("x") : 0);
			double y = (node.isSet("y") ? node.getDouble("y") : 0);
			double z = (node.isSet("z") ? node.getDouble("z") : 0);
			double pitch = (node.isSet("pitch") ? node.getDouble("pitch") : 0);
			double yaw = (node.isSet("yaw") ? node.getDouble("yaw") : 0);
			return new Location(world, x, y, z, (float) yaw, (float) pitch);
		} else {
			//assume string
			
			String str = node.asString();
			if (str == null)
				return null;
			
			if (str.isEmpty())
				return new Location(null, 0, 0, 0, 0, 0);
			
			String[] split = str.split("\\s+");
			int index = 0;
			World world = Bukkit.getWorld(split[index]);
			if (world != null)
				index++;
			
			//I honestly did not expect each expression to be the same
			//once one fails, the rest do as well
			double x = (split.length > index ? makeDbl(index++) : 0);
			double y = (split.length > index ? makeDbl(index++) : 0);
			double z = (split.length > index ? makeDbl(index++) : 0);
			double yaw = (split.length > index ? makeDbl(index++) : 0);
			double pitch = (split.length > index ? makeDbl(index++) : 0);
			
			return new Location(world, x, y, z, (float) yaw, (float) pitch);
		}
	}
	
	public Location getLocation(String path, Location def) {
		if (isSet(path))
			return getLocation(path);
		
		if (options().saveDefaults)
			set(path, def);
		
		return def;
	}
	
	public enum SaveType {
		
		/**
		 * When saving a Bukkit object, save it as a single String. For
		 * instance, if this is used for an org.bukkit.Color object, then it
		 * will be saved as its hexadecimal representation. Example: Color.WHITE
		 * will be saved as the String "0xFFFFFF"
		 */
		COMPACT,
		
		/**
		 * When saving a Bukkit object, save each constructor parameter as their
		 * own node. For instance, if this is used for an org.bukkit.Color
		 * object, then the "red" value will be saved in the "red" node, "green"
		 * in the "green" node, and so on.
		 */
		EXPANDED;
	}
	
}
