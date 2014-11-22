package com.kill3rtaco.api.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.TNTPrimed;

/**
 * A class to help with various entity operations
 * 
 * @author KILL3RTACO
 *
 * @since TacoAPI/Bukkit 3.0
 */
public class TEntity {
	
	/**
	 * Spawn a primed TNT block at a location
	 * 
	 * @param loc
	 *            the location to spawn the TNT
	 * @param fuseTicks
	 *            the fuse length of the TNT, in ticks
	 * @since TacoApi/Bukkit 3.0
	 */
	public static void spawnPrimedTNT(Location loc, int fuseTicks) {
		loc.getWorld().spawn(loc, TNTPrimed.class).setFuseTicks(fuseTicks);
	}
	
	/**
	 * Get an entity by its UUID
	 * 
	 * @param uuid
	 *            the UUID
	 * @return the entity, or null if not found
	 * @since TacoApi/Bukkit 3.0
	 */
	public static Entity getEntityByUUID(String uuid) {
		for (World w : Bukkit.getWorlds()) {
			for (Entity e : w.getEntities()) {
				if (e.getUniqueId().equals(uuid)) {
					return e;
				}
			}
		}
		return null;
	}
}
