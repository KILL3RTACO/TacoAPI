package com.kill3rtaco.api.bukkit;

import java.text.DecimalFormat;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

/**
 * A class designed to help manage the economy.<br/>
 * <br/>
 * Dependencies: Vault (using this class if the required dependencies aren't
 * installed will cause issues)
 * 
 * @author KILL3RTACO
 *
 * @since TacoAPI/Bukkit 3.0
 */
public class TEcon {
	
	private static Economy	_econ;
	
	public static void init() {
		RegisteredServiceProvider<Economy> economyProvider =
				Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
		if (economyProvider != null) {
			_econ = economyProvider.getProvider();
		}
	}
	
	/**
	 * Get the balance of a player
	 * 
	 * @param name
	 *            The name of the player to check
	 * @return The balance of a player
	 * @since TacoApi/Bukkit 3.0
	 */
	public static double getBalance(String name) {
		return _econ.getBalance(name);
	}
	
	/**
	 * Adds the currency name to the balance of a player.
	 * 
	 * @param name
	 *            The name of the player to check
	 * @return The balance of a player, including the currency name
	 * @since TacoApi/Bukkit 3.0
	 */
	public static String getFormattedBalance(String name) {
		DecimalFormat formatter = new DecimalFormat("#,###.##");
		double bal = getBalance(name);
		if (bal == 1) {
			return formatter.format(bal) + " " + currencyName();
		} else {
			return formatter.format(bal) + " " + currencyNamePlural();
		}
	}
	
	/**
	 * Get the name of the Economy plugin in use
	 * 
	 * @return The name of the economy method
	 * @since TacoApi/Bukkit 3.0
	 */
	public static String getEconomyName() {
		return _econ.getName();
	}
	
	/**
	 * Get the name of the currency in use
	 * 
	 * @return Name of the currency in use
	 * @since TacoApi/Bukkit 3.0
	 */
	public static String currencyName() {
		return _econ.currencyNameSingular();
	}
	
	/**
	 * Get the name of the currency in use in plural form
	 * 
	 * @return Plural name of the currency in use
	 * @since TacoApi/Bukkit 3.0
	 */
	public static String currencyNamePlural() {
		return _econ.currencyNamePlural();
	}
	
	/**
	 * Give money to a player
	 * 
	 * @param name
	 *            The player to give to
	 * @param amount
	 *            The amount to give
	 * @since TacoApi/Bukkit 3.0
	 */
	public static void deposit(String name, double amount) {
		_econ.depositPlayer(name, amount);
	}
	
	/**
	 * Take money from a player
	 * 
	 * @param name
	 *            The player to take from
	 * @param amount
	 *            The amount to take
	 */
	public static void withdraw(String name, double amount) {
		_econ.withdrawPlayer(name, amount);
	}
	
	/**
	 * Make one player pay another
	 * 
	 * @param sender
	 *            The player paying
	 * @param receiver
	 *            the player to pay
	 * @param amount
	 *            The amount to pay
	 * @return Whether the sender can pay the amount specified
	 * @since TacoApi/Bukkit 3.0
	 */
	public static boolean pay(String sender, String receiver, double amount) {
		if (_econ.has(sender, amount)) {
			withdraw(sender, amount);
			deposit(receiver, amount);
			return true;
		} else {
			return false;
		}
		
	}
	
	/**
	 * Test if a player has the amount given
	 * 
	 * @param player
	 *            The player to test
	 * @param amount
	 *            The amount to test
	 * @return Whether the player can pay the amount given
	 * @since TacoApi/Bukkit 3.0
	 */
	public static boolean canPay(String player, double amount) {
		return _econ.has(player, amount);
	}
	
}
