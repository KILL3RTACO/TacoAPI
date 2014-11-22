package com.kill3rtaco.api.bukkit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class TServer {
	
	/**
	 * Get the plugin that handles the specified command
	 * 
	 * @param name
	 *            the command name
	 * @return the plugin that handles the command, or null if none exists
	 * @since TacoAPI/Bukkit 3.0
	 */
	public static Plugin getPluginFromCommand(String name) {
		String alias = name.toLowerCase();
		PluginCommand cmd = Bukkit.getServer().getPluginCommand(alias);
		if (cmd == null)
			return null;
		return cmd.getPlugin();
	}
	
	/**
	 * Get the shortest alias of a command. This method checks all commands
	 * aliases belonging to a plugin and returns the shortest one that belongs
	 * to the given plugin. For example, 'give' is a common comamnd. Suppose the
	 * following plugins have a command with the name 'give':
	 * 
	 * <pre>
	 * ---plugin.yml of Awesomeness---
	 * name: Awesomeness
	 * ...
	 * commands:
	 *   give:
	 *   
	 * --- plugin.yml of MyPlugin---
	 * name: MyPlugin
	 * ...
	 * commands:
	 *   give:
	 *     aliases: [g]
	 * </pre>
	 * 
	 * If <code>getShortestAlias(myPlugin, "give")</code> was called, the
	 * shortest alias that would be returned is 'g', not only because 'give' is
	 * being used by Awesomness, but it also has the lowest amount characters
	 * 
	 * @param plugin
	 *            the plugin
	 * @param name
	 *            the command name
	 * @return the shortest alias
	 * @since TacoAPI/Bukkit 3.0
	 */
	public static String getShortestAlias(JavaPlugin plugin, String name) {
		ArrayList<String> aliases = new ArrayList<String>();
		if (pluginOwnsCommand(plugin, name)) {
			aliases.add(name);
		}
		//test if the aliases, when used, belong to the given JavaPlugin
		for (String s : plugin.getCommand(name).getAliases()) {
			if (pluginOwnsCommand(plugin, s)) {
				aliases.add(s);
			}
		}
		if (aliases.size() == 0) {
			return null;
		} else if (aliases.size() > 1) {
			Collections.sort(aliases, new Comparator<String>() {
				
				@Override
				public int compare(String s1, String s2) {
					Integer l1 = s1.length();
					Integer l2 = s2.length();
					return l1.compareTo(l2);
				}
				
			});
		}
		return aliases.get(0);
	}
	
	/**
	 * Test if the plugin owns the given command. This is a useful check to see
	 * if a certain command will be run by your plugin or a different plugin
	 * 
	 * @param plugin
	 *            the plugin to test
	 * @param alias
	 *            the name (or an alias) of a command
	 * @return true if the given plugin owns the execution rights to the given
	 *         command
	 * @since TacoAPI/Bukkit 3.0
	 */
	public static boolean pluginOwnsCommand(Plugin plugin, String alias) {
		return getPluginFromCommand(alias).getName().equalsIgnoreCase(plugin.getName());
	}
	
	/**
	 * Dispatch a command as console
	 * 
	 * @param command
	 *            the command to run
	 * @since TacoAPI/Bukkit 3.0
	 */
	public static void dispatchCommand(String command) {
		Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), command);
	}
	
}
