package com.kill3rtaco.api.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.CommandMinecart;

import com.kill3rtaco.api.bukkit.util.ChatUtils;
import com.kill3rtaco.api.bukkit.util.PrintOptions;

/**
 * A class designed to help manage chat.<br/>
 * <br/>
 * Dependencies: ChatUtils, PrintOptions
 * 
 * @author KILL3RTACO
 *
 * @since TacoAPI/Bukkit 3.0
 */
public class TChat {
	
	protected String		_name;
	protected PrintOptions	_options;
	
	/**
	 * Construct a new TChat object with a given name. This name will be used
	 * for the header, which is a string in the form of "[{NAME}]"
	 * 
	 * @param name
	 *            the name
	 * @since TacoAPI/Bukkit 3.0
	 */
	public TChat(String name) {
		this(name, new PrintOptions());
	}
	
	/**
	 * Construct a new TChat object with a given name and default PrintOptions.
	 * The name will be used for the header, which is a string in the form of
	 * "[{NAME}]"
	 * 
	 * @param name
	 *            the name
	 * @param options
	 *            the default print options
	 * @since TacoAPI/Bukkit 3.0
	 */
	public TChat(String name, PrintOptions options) {
		_name = name;
		_options = options;
	}
	
	private String makeMessage(String message, boolean header) {
		String msg = message;
		if (header)
			//color of name still changeable
			msg = "&7[&9" + _name + "&r&7]&f " + message;
		return msg;
	}
	
	/**
	 * Send a message to a CommandSender.
	 * 
	 * @param sender
	 *            who to send the message to
	 * @param message
	 *            the message to send
	 * @since TacoAPI/Bukkit 3.0
	 */
	public void sendMessageTo(CommandSender sender, String message) {
		sendMessageTo(sender, message, true);
	}
	
	/**
	 * Send a message to a CommandSender and optionally add the header.
	 * 
	 * @param sender
	 *            who to send the message to
	 * @param message
	 *            the message to send
	 * @param header
	 *            true if the header should be shown
	 * @since TacoAPI/Bukkit 3.0
	 */
	public void sendMessageTo(CommandSender sender, String message, boolean header) {
		sendMessageTo(sender, message, header, _options);
	}
	
	/**
	 * Send a message to a CommandSender with an optional header and specified
	 * PrintOptions.
	 * 
	 * @param sender
	 *            who to send the message to
	 * @param message
	 *            the message to send
	 * @param header
	 *            true if the header should be shown
	 * @param options
	 *            the PrintOptions to use when sending the message
	 * @since TacoAPI/Bukkit 3.0
	 */
	public void sendMessageTo(CommandSender sender, String message, boolean header, PrintOptions options) {
		if (sender == null)
			return;
		String msg = makeMessage(message, header);
		if (sender instanceof Player && options.stripIfPlayer)
			msg = ChatUtils.removeColorCodes(msg);
		
		else if (sender instanceof ConsoleCommandSender && options.stripIfConsole)
			msg = ChatUtils.removeColorCodes(msg);
		
		else if (sender instanceof BlockCommandSender && options.stripIfCommandBlock)
			msg = ChatUtils.removeColorCodes(msg);
		
		else if (sender instanceof CommandMinecart && options.stripIfMinecart)
			msg = ChatUtils.removeColorCodes(msg);
		
		else
			msg = ChatUtils.formatMessage(msg);
		
		sender.sendMessage(msg);
	}
	
	/**
	 * Send a message to the console
	 * 
	 * @param message
	 *            the message to send
	 * @since TacoAPI/Bukkit 3.0
	 */
	public void out(String message) {
		out(message, true);
	}
	
	/**
	 * Send a message to the console with an optional header
	 * 
	 * @param message
	 *            the message to send
	 * @param header
	 *            true if the header should be shown
	 * @since TacoAPI/Bukkit 3.0
	 */
	public void out(String message, boolean header) {
		out(message, header, _options);
	}
	
	/**
	 * Send a message to the console with an optional header and specified
	 * PrintOptions
	 * 
	 * @param message
	 *            the message to send
	 * @param header
	 *            true if the header should be shown
	 * @param options
	 *            the printing options
	 * @since TacoAPI/Bukkit 3.0
	 */
	public void out(String message, boolean header, PrintOptions options) {
		sendMessageTo(Bukkit.getServer().getConsoleSender(), message, header, options);
	}
	
	/**
	 * Provided as legacy support. Sends a message to the console with out a
	 * header
	 * 
	 * @param message
	 *            the message to send
	 * @since TacoAPI/Bukkit 3.0
	 */
	public void outNoHeader(String message) {
		out(message, false);
	}
	
	/**
	 * Provided as legacy support, equivalent to changing the method name to
	 * <code>sendMessageTo</code>. Sends a message to a player.
	 * 
	 * @param player
	 *            the player to send the message to
	 * @param message
	 *            the message to send
	 * @since TacoAPI/Bukkit 3.0
	 */
	public void sendPlayerMessage(Player player, String message) {
		sendPlayerMessage(player, message, true);
	}
	
	/**
	 * Provided as legacy support, equivalent to changing the method name to
	 * <code>sendMessageTo</code>. Sends a message to a player with an optional
	 * header.
	 * 
	 * @param player
	 *            the player to send the message to
	 * @param message
	 *            the message to send
	 * @param header
	 *            true if the header should be shown
	 * @since TacoAPI/Bukkit 3.0
	 */
	public void sendPlayerMessage(Player player, String message, boolean header) {
		sendPlayerMessage(player, message, header, _options);
	}
	
	/**
	 * Provided as legacy support, equivalent to changing the method name to
	 * <code>sendMessageTo</code>. Sends a message to a player with an optional
	 * header and specified PrintOptions
	 * 
	 * @param player
	 *            the player to send the message to
	 * @param message
	 *            the message to send
	 * @param header
	 *            true if the header should be shown
	 * @param options
	 *            the printing options
	 * @since TacoAPI/Bukkit 3.0
	 */
	public void sendPlayerMessage(Player player, String message, boolean header, PrintOptions options) {
		sendMessageTo(player, message, header, options);
	}
	
	/**
	 * Broadcast a server-wide message
	 * 
	 * @param message
	 *            the message to send
	 * @since TacoAPI/Bukkit 3.0
	 */
	public void broadcast(String message) {
		broadcast(message, true);
	}
	
	/**
	 * Send a server-wide message with an optional header
	 * 
	 * @param message
	 *            the message to send
	 * @param header
	 *            true if the header should be shown
	 * @since TacoAPI/Bukkit 3.0
	 */
	public void broadcast(String message, boolean header) {
		broadcast(message, header, _options);
	}
	
	/**
	 * Send a server-wide message with an optional header and specified
	 * PrintOptions
	 * 
	 * @param message
	 *            the message to send
	 * @param header
	 *            true if the header should be shown
	 * @param options
	 *            the printing options
	 * @since TacoAPI/Bukkit 3.0
	 */
	public void broadcast(String message, boolean header, PrintOptions options) {
		for (Player p : Bukkit.getOnlinePlayers()) {
			sendPlayerMessage(p, message, header, options);
		}
	}
	
	/**
	 * Send a message to all players in a world
	 * 
	 * @param world
	 *            the world to send the message to
	 * @param message
	 *            the message to send
	 * @since TacoAPI/Bukkit 3.0
	 */
	public void broadcastGlobal(World world, String message) {
		broadcastGlobal(world, message, true);
	}
	
	/**
	 * Send a message to all players in a world with an optional header
	 * 
	 * @param world
	 *            the world to send the message to
	 * @param message
	 *            the message to send
	 * @param header
	 *            true if the header should be shown
	 * @since TacoAPI/Bukkit 3.0
	 */
	public void broadcastGlobal(World world, String message, boolean header) {
		broadcastGlobal(world, message, header, _options);
	}
	
	/**
	 * Send a message to all players in a world with an optional header with
	 * specified PrintOptions
	 * 
	 * @param world
	 *            the world to send the message to
	 * @param message
	 *            the message to send
	 * @param header
	 *            true if the header should be shown
	 * @param options
	 *            the printing options
	 * @since TacoAPI/Bukkit 3.0
	 */
	public void broadcastGlobal(World world, String message, boolean header, PrintOptions options) {
		for (Player p : world.getPlayers()) {
			sendPlayerMessage(p, message, header, options);
		}
	}
	
	/**
	 * Send a player a raw message. This is done by running the /tellraw command
	 * from console
	 * 
	 * @param player
	 *            the player to send the message
	 * @param rawMessage
	 *            the raw JSON text
	 * @since TacoAPI/Bukkit 3.0
	 */
	public void sendPlayerMessageRaw(Player player, String rawMessage) {
		TServer.dispatchCommand("tellraw " + player.getName() + " " + rawMessage);
	}
	
}
