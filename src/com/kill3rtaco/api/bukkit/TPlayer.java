package com.kill3rtaco.api.bukkit;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.Wolf;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import com.kill3rtaco.api.bukkit.util.serialization.InventorySerialization;
import com.kill3rtaco.tacoapi.plugin.TacoAPIPlugin;

/**
 * A class to help with various player operations<br/>
 * </br/> Dependencies: TacoAPI [Plugin] (for player data folder location),
 * TacoSerialization (saving inventories)
 * 
 * 
 * @author KILL3RTACO
 *
 * @since TacoAPI/Bukkit 3.0
 */
public class TPlayer {
	
	/**
	 * Get all the pets of a player
	 * 
	 * @param playername
	 *            the name of the player
	 * @return the player's pets
	 * @since TacoAPI/Bukkit 3.0
	 */
	public ArrayList<Tameable> getPets(String playername) {
		ArrayList<Tameable> pets = new ArrayList<Tameable>();
		pets.addAll(getTamedWolves(playername));
		pets.addAll(getTamedOcelots(playername));
		pets.addAll(getTamedHorses(playername));
		return pets;
	}
	
	/**
	 * Get all wolves owned by a player
	 * 
	 * @param playername
	 *            the name of the player
	 * @return the player's wolves
	 * @since TacoAPI/Bukkit 3.0
	 */
	public static ArrayList<Wolf> getTamedWolves(String playername) {
		return getTamed(playername, Wolf.class);
	}
	
	/**
	 * Get all ocelots owned by a player
	 * 
	 * @param playername
	 *            the name of the player
	 * @return the player's ocelots
	 * @since TacoAPI/Bukkit 3.0
	 */
	public static ArrayList<Ocelot> getTamedOcelots(String playername) {
		return getTamed(playername, Ocelot.class);
	}
	
	/**
	 * Get all horses owned by a player
	 * 
	 * @param playername
	 *            the name of the player
	 * @return the player's horses
	 * @since TacoAPI/Bukkit 3.0
	 */
	public static ArrayList<Horse> getTamedHorses(String playername) {
		return getTamed(playername, Horse.class);
	}
	
	/**
	 * Get a list of tamed pets for a player
	 * 
	 * @param playername
	 *            the name of the player
	 * @param clazz
	 *            the class of the pet type (i.e. Horse.class)
	 * @return a list of tamed pets
	 * @throws IllegalArgumentException
	 *             if the given class is not a subclass of Tameable
	 * @since TacoAPI/Bukkit 3.0
	 */
	public static <T extends Entity> ArrayList<T> getTamed(String playername, Class<T> clazz) {
		ArrayList<T> owned = new ArrayList<T>();
		for (World w : TacoAPIPlugin.plugin.getServer().getWorlds()) {
			for (T t : w.getEntitiesByClass(clazz)) {
				if (!(t instanceof Tameable))
					throw new IllegalArgumentException("Given class is not instanceof Tameable");
				Tameable tame = (Tameable) t;
				if (tame.isTamed() && tame.getOwner().getName().equalsIgnoreCase(playername))
					owned.add(t);
			}
		}
		return owned;
	}
	
	/**
	 * Teleport a player to an entity (and show smoke)
	 * 
	 * @param player
	 *            the player to teleport
	 * @param entity
	 *            the destination
	 * @since TacoAPI/Bukkit 3.0
	 */
	public static void teleport(Player player, Entity entity) {
		teleport(player, entity, true);
	}
	
	/**
	 * Teleport a player to an entity and optionally show smoke
	 * 
	 * @param player
	 *            the player to teleport
	 * @param entity
	 *            the destination
	 * @param smoke
	 *            true if smoke should be shown when the player teleports
	 * @since TacoAPI/Bukkit 3.0
	 */
	public static void teleport(Player player, Entity entity, boolean smoke) {
		teleport(player, entity.getLocation(), smoke);
	}
	
	/**
	 * Teleport a player to a location (and show smoke)
	 * 
	 * @param player
	 *            the player to teleport
	 * @param location
	 *            the destination
	 * @since TacoAPI/Bukkit 3.0
	 */
	public static void teleport(Player player, Location location) {
		teleport(player, location, true);
	}
	
	/**
	 * Teleport a player to a location and optionally show smoke
	 * 
	 * @param player
	 *            the player to teleport
	 * @param location
	 *            the destination
	 * @param smoke
	 *            true if smoke should be shown when the player teleports
	 * @since TacoAPI/Bukkit 3.0
	 */
	public static void teleport(Player player, Location location, boolean smoke) {
		if (smoke)
			TEffect.showSmoke(player.getLocation());
		player.teleport(location);
		if (smoke)
			TEffect.showSmoke(player.getLocation());
	}
	
	/**
	 * Teleport a player to their last saved location (and show smoke)
	 * 
	 * @param player
	 *            the player to teleport
	 * @since TacoAPI/Bukkit 3.0
	 * @see #saveLocation(String, Location)
	 */
	public static void teleportToLastLocation(Player player) {
		teleportToLastLocation(player, true);
	}
	
	/**
	 * 
	 * Teleport a player to their last saved location and optionally show smoke
	 * 
	 * @param player
	 *            the player to teleport
	 * @param smoke
	 *            true if smoke should be shown when the player teleports
	 * @since TacoAPI/Bukkit 3.0
	 * @see #saveLocation(String, Location)
	 */
	public static void teleportToLastLocation(Player player, boolean smoke) {
		teleport(player, getLastLocation(player.getName()), smoke);
	}
	
	/**
	 * Get the last saved location of a player
	 * 
	 * @param name
	 *            the name of the player
	 * @return the last saved location
	 * @since TacoAPI/Bukkit 3.0
	 * @see #saveLocation(String, Location)
	 */
	public static Location getLastLocation(String name) {
		File file = new File(TacoAPIPlugin.playerData + "/" + name + ".yml");
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		String world = config.getString("last-location.world");
		World w = TacoAPIPlugin.plugin.getServer().getWorld(world);
		if (w == null)
			return null;
		double x = config.getDouble("last-location.x");
		double y = config.getDouble("last-location.y");
		double z = config.getDouble("last-location.z");
//		TacoAPI.chat.out(world + " " + x + " " + y + " " + z);
		return new Location(w, x, y, z);
	}
	
	/**
	 * Save the player's current location
	 * 
	 * @param name
	 *            the name of the player
	 * @param location
	 *            the location to save
	 * @since TacoAPI/Bukkit 3.0
	 */
	public static void saveLocation(String name, Location location) {
		File file = new File(TacoAPIPlugin.playerData + "/" + name + ".yml");
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		config.set("last-location.world", location.getWorld().getName());
		config.set("last-location.x", location.getX());
		config.set("last-location.y", location.getY());
		config.set("last-location.z", location.getZ());
		save(file, config);
	}
	
	/**
	 * Revert the player's game mode to their last saved gamemode
	 * 
	 * @param player
	 *            the player
	 * @since TacoAPI/Bukkit 3.0
	 * @see #saveGameMode(String, GameMode)
	 */
	public static void setToLastGameMode(Player player) {
		GameMode gm = getLastGameMode(player.getName());
		player.setGameMode(gm);
	}
	
	/**
	 * Get the last gamemode of a player
	 * 
	 * @param name
	 *            the name of the player
	 * @return the last saved gamemode
	 * @since TacoAPI/Bukkit 3.0
	 */
	@SuppressWarnings("deprecation")
	public static GameMode getLastGameMode(String name) {
		File file = new File(TacoAPIPlugin.playerData + "/" + name + ".yml");
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		return GameMode.getByValue(config.getInt("last-gamemode"));
	}
	
	/**
	 * Save the players current gamemode
	 * 
	 * @param name
	 *            the name of the player
	 * @param gameMode
	 *            the gamemode to save
	 * @since TacoAPI/Bukkit 3.0
	 */
	@SuppressWarnings("deprecation")
	public static void saveGameMode(String name, GameMode gameMode) {
		File file = new File(TacoAPIPlugin.playerData + "/" + name + ".yml");
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		config.set("last-gamemode", gameMode.getValue());
		save(file, config);
	}
	
	/**
	 * Save a player's inventory
	 * 
	 * @param player
	 *            the player
	 * @since TacoAPI/Bukkit 3.0
	 */
	public static void savePlayerInventory(Player player) {
		savePlayerInventory(player.getInventory(), player.getName());
	}
	
	/**
	 * Save a PlayerInventory
	 * 
	 * @param inventory
	 *            the inventory to save
	 * @param name
	 *            the name of the owner of the inventory (that's a mouthful to
	 *            say)
	 * @since TacoAPI/Bukkit 3.0
	 */
	public static void savePlayerInventory(PlayerInventory inventory, String name) {
		try {
			File invFile = new File(TacoAPIPlugin.playerData + "/inventories/" + name + "/inventory.json");
			File armorFile = new File(TacoAPIPlugin.playerData + "/inventories/" + name + "/armor.json");
			if (invFile.exists())
				invFile.delete();
			if (armorFile.exists())
				armorFile.delete();
			invFile.getParentFile().mkdirs();
			invFile.createNewFile();
			armorFile.createNewFile();
			
			String inv = InventorySerialization.serializeInventoryAsString(inventory, true, 5);
			String armor = InventorySerialization.serializeInventoryAsString(inventory.getArmorContents(), true, 5);
			FileWriter invWriter = new FileWriter(invFile);
			FileWriter armorWriter = new FileWriter(armorFile);
			invWriter.append(inv);
			invWriter.close();
			armorWriter.append(armor);
			armorWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Get a player's saved inventory. Note that this does not include the
	 * armor.
	 * 
	 * @param player
	 *            the player
	 * @return the player's inventory's contents
	 * @since TacoAPI/Bukkit 3.0
	 * @see #getPlayerArmor(Player)
	 */
	public static ItemStack[] getPlayerInventory(Player player) {
		return getPlayerInventory(player.getName());
	}
	
	/**
	 * Get a player's saved inventory. Note that this does not include the
	 * armor.
	 * 
	 * @param name
	 *            the name of the player
	 * @return the player's inventory's contents
	 * @since TacoAPI/Bukkit 3.0
	 * @see #getPlayerArmor(String)
	 */
	public static ItemStack[] getPlayerInventory(String name) {
		File invFile = new File(TacoAPIPlugin.playerData + "/inventories/" + name + "/inventory.json");
		return getItemsFromFile(invFile, 36);
	}
	
	/**
	 * Get a player's saved armor
	 * 
	 * @param player
	 *            the player
	 * @return the player's armor
	 * @since TacoAPI/Bukkit 3.0
	 */
	public static ItemStack[] getPlayerArmor(Player player) {
		return getPlayerArmor(player.getName());
	}
	
	/**
	 * Get a player's saved armor
	 * 
	 * @param name
	 *            the name of the player
	 * @return the player's armor
	 * @since TacoAPI/Bukkit 3.0
	 */
	public static ItemStack[] getPlayerArmor(String name) {
		File armorFile = new File(TacoAPIPlugin.playerData + "/inventories/" + name + "/armor.json");
		return getItemsFromFile(armorFile, 4);
	}
	
	private static ItemStack[] getItemsFromFile(File file, int size) {
		return InventorySerialization.getInventory(file, size);
	}
	
	/**
	 * Restore a player's saved inventory
	 * 
	 * @param player
	 *            the player whose inventory is to be restored
	 * @since TacoAPI/Bukkit 3.0
	 */
	public static void restoreInventory(Player player) {
		String name = player.getName();
		ItemStack[] inv = getPlayerInventory(name);
		ItemStack[] armor = getPlayerArmor(name);
		PlayerInventory inventory = player.getInventory();
		inventory.clear();
		inventory.setArmorContents(armor);
		for (int i = 0; i < inv.length; i++) {
			ItemStack items = inv[i];
			if (items != null)
				inventory.setItem(i, items);
		}
	}
	
	private static void save(File file, YamlConfiguration config) {
		try {
			file.getParentFile().mkdir();
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Get the block a player is looking at. This uses a slightly modified
	 * version of a method suggested by Assist on the Bukkit Forums.
	 * 
	 * @param player
	 *            the player
	 * @param range
	 *            the maximum range for the trace
	 * @return the block the player is looking at
	 * @since TacoAPI/Bukkit 3.0
	 */
	public static Block getTargetBlock(Player player, int range) {
		return getTargetBlock(player, range, null);
	}
	
	/**
	 * Get the target block of a player. This uses a slightly modified version
	 * of a method suggested by Assist on the Bukkit Forums.
	 * 
	 * @param player
	 *            the player
	 * @param range
	 *            the maximum range for the trace
	 * @param transparant
	 *            block types to consider transparent (AIR is always considered
	 *            transparent)
	 * @return the block the player is looking at
	 * @since TacoAPI/Bukkit 3.0
	 */
	public static Block getTargetBlock(Player player, int range, Collection<Material> transparent) {
		//Taco start
		if (player == null)
			return null;
		Location loc = player.getEyeLocation();
		//Taco end
		Vector dir = loc.getDirection().normalize();
		
		Block b = null;
		
		for (int i = 0; i <= range; i++) {
			b = loc.add(dir).getBlock();
			//Taco start
			if (b.getType() != Material.AIR || (transparent != null && transparent.contains(b.getType()))) //line of sight
				return b;
			//Taco end
		}
		
		return b;
	}
	
	/**
	 * Get the entity a player is looking at. This uses a slightly modified
	 * version of a method provided by DirtyStarfish on the Bukkit Forums.
	 * 
	 * @param player
	 *            the player
	 * @param range
	 *            the maximum range for the trace
	 * @param transparant
	 *            block types to consider transparent (AIR is always considered
	 *            transparent)
	 * @return the entity the player is looking at, or null if the player is not
	 *         looking at an entity
	 * @since TacoAPI/Bukkit 3.0
	 */
	public static Entity getTargetEntity(Player player, int range) {
		return getTargetEntity(player, range, null);
	}
	
	/**
	 * Get the entity a player is looking at. This uses a slightly modified
	 * version of a method provided by DirtyStarfish on the Bukkit Forums.
	 * 
	 * @param player
	 *            the player
	 * @param range
	 *            the maximum range for the trace
	 * 
	 * @return the entity the player is looking at, or null if the player is not
	 *         looking at an entity
	 * @since TacoAPI/Bukkit 3.0
	 */
	public static Entity getTargetEntity(Player player, int range, Collection<Material> transparent) {
		List<Entity> near = player.getNearbyEntities(range, range, range);
		BlockIterator it = new BlockIterator(player, range);
		while (it.hasNext()) {
			Block b = it.next();
			if (!b.getChunk().isLoaded())
				return null;
			for (Entity e : near) {
				Location loc = e.getLocation();
				if (loc.getBlockX() == b.getX() &&
						loc.getBlockY() == b.getY() &&
						loc.getBlockZ() == b.getZ())
					return e;
			}
			//Taco start
			if (b.getType() != Material.AIR || (transparent != null && transparent.contains(b.getType()))) //line of sight end
				return null;
			//Taco end
		}
		return null;
	}
}
