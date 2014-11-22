package com.kill3rtaco.api.bukkit;

import java.util.Random;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * A class to help with effects, like PotionEffects and smoke
 * 
 * @author KILL3RTACO
 *
 * @since TacoAPI/Bukkit 3.0
 */
public class TEffect {
	
	/**
	 * Applies a potion effect to a player
	 * 
	 * @param player
	 *            The player to apply the effect
	 * @param effectType
	 *            The effect to apply
	 * @param duration
	 *            How long the effect should last in seconds
	 * @param strength
	 *            The strength of the effect
	 * @since TacoApi/Bukkit 3.0
	 */
	public static void applyPotionEffect(Player player, PotionEffectType effectType, int duration, int strength) {
		player.addPotionEffect(new PotionEffect(effectType, duration * 20, strength));
	}
	
	/**
	 * Creates an explosion at a location
	 * 
	 * @param location
	 *            The location for explosion
	 * @param force
	 *            The force of the explosion
	 * @since TacoApi/Bukkit 3.0
	 */
	public static void createExplosion(Location location, float force) {
		location.getWorld().createExplosion(location, force);
	}
	
	/**
	 * Kills a LivingEntity
	 * 
	 * @param entity
	 *            The entity to kill
	 * @since TacoApi/Bukkit 3.0
	 */
	public static void killEntity(LivingEntity entity) {
		entity.setHealth(0);
	}
	
	/**
	 * Ignite an entity for a certain amount of time
	 * 
	 * @param entity
	 *            The entity to ignite
	 * @param duration
	 *            How long it should last
	 * @since TacoApi/Bukkit 3.0
	 */
	public static void igniteEntity(Entity entity, int duration) {
		entity.setFireTicks(duration);
	}
	
	/**
	 * Removes a potion effect on a player
	 * 
	 * @param player
	 *            The player to remove the potion effect from
	 * @param effectType
	 *            The effect to remove
	 * @since TacoApi/Bukkit 3.0
	 */
	public static void removePotionEffect(Player player, PotionEffectType effectType) {
		player.removePotionEffect(effectType);
	}
	
	/**
	 * Strike lightning at a location
	 * 
	 * @param location
	 *            The location to strike with lightning
	 * @since TacoApi/Bukkit 3.0
	 */
	public static void strike(Location location) {
		location.getWorld().strikeLightning(location);
	}
	
	/**
	 * Strike lightning at a location, but without the side effects (i.e.
	 * damage/fire)
	 * 
	 * @param location
	 *            The location to strike
	 * @since TacoApi/Bukkit 3.0
	 */
	public static void strikeEffect(Location location) {
		location.getWorld().strikeLightningEffect(location);
	}
	
	/**
	 * Show smoke at a location
	 * 
	 * @param location
	 *            Where to show the smoke
	 * @since TacoApi/Bukkit 3.0
	 */
	public static void showSmoke(Location location) {
		showSmoke(location, 20);
	}
	
	/**
	 * Show smoke at a location
	 * 
	 * @param location
	 *            Where to show the smoke
	 * @param iterations
	 *            Amount of iterations - use carefully
	 * @since TacoApi/Bukkit 3.0
	 */
	public static void showSmoke(Location location, int iterations) {
		Random random = new Random();
		for (int i = 0; i < iterations; i++) {
			location.getWorld().playEffect(location, Effect.SMOKE, random.nextInt(9));
		}
	}
	
}
