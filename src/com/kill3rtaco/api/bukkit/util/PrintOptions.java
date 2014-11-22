package com.kill3rtaco.api.bukkit.util;

import org.bukkit.craftbukkit.libs.jline.TerminalFactory;

/**
 * Helper class for CommandManager and CommandContext classes.
 * 
 * @author KILL3RTACO
 *
 * @since TacoAPI/Bukkit 3.0
 */
public class PrintOptions implements Cloneable {
	
	/**
	 * Set to true if color codes should be removed when printing output to the
	 * console. Default: true if the console does not support ANSI colors
	 * (Typically only if Bukkit is being run on Windows, unless a console like
	 * cmder is used)
	 * 
	 * @since TacoAPI/Bukkit 3.0
	 */
	public boolean	stripIfConsole		= !TerminalFactory.get().isAnsiSupported();
	
	/**
	 * Set to true if color codes should be removed when printing output to a
	 * player. Default: false
	 * 
	 * @since TacoAPI/Bukkit 3.0
	 */
	public boolean	stripIfPlayer		= false;
	
	/**
	 * Set to true if color codes should be removed when printing output to a
	 * CommandBlock. Default: false
	 * 
	 * @since TacoAPI/Bukkit 3.0
	 */
	public boolean	stripIfCommandBlock	= false;
	
	/**
	 * Set to true if color codes should be removed when printing output to a
	 * CommandMinecart. Default: false
	 * 
	 * @since TacoAPI/Bukkit 3.0
	 */
	public boolean	stripIfMinecart		= false;
	
	public PrintOptions clone() {
		PrintOptions clone = new PrintOptions();
		clone.stripIfConsole = stripIfConsole;
		clone.stripIfPlayer = stripIfPlayer;
		clone.stripIfCommandBlock = stripIfCommandBlock;
		clone.stripIfMinecart = stripIfMinecart;
		return clone;
	}
	
}
