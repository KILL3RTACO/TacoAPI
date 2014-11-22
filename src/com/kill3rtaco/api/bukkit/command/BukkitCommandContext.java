package com.kill3rtaco.api.bukkit.command;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.CommandMinecart;
import org.bukkit.plugin.java.JavaPlugin;

import com.kill3rtaco.api.bukkit.TChat;
import com.kill3rtaco.api.bukkit.TPerm;
import com.kill3rtaco.api.bukkit.util.PrintOptions;
import com.kill3rtaco.api.command.CommandContext;

/**
 * Represents a Bukkit CommandContext used by {@link BukkitCommandManager}
 * 
 * @author KILL3RTACO
 * @since TacoAPI/Bukkit 3.0
 *
 */
public class BukkitCommandContext extends CommandContext {
	
	/**
	 * MessageID for when a player doesn't have enough money. Takes a
	 * float/double argument (<code>%f</code>).
	 * 
	 * @since TacoAPI/Bukkit 3.0
	 */
	public static final String	MID_INSUFFUCIENT_FUNDS			= "TacoAPI.err.insufficientFunds";
	
	/**
	 * MessageID for when a CommandSender does not have permission to do
	 * something. Takes no arguments.
	 * 
	 * @since TacoAPI/Bukkit 3.0
	 */
	public static final String	MID_INSUFFICIENT_PERM			= "TacoAPI.err.insufficientPermission";
	
	/**
	 * Same as {@link #MID_INSUFFICIENT_PERM}, but will print the permission as
	 * well. Takes a String argument (<code>%s</code>).
	 * 
	 * @since TacoAPI/Bukkit 3.0
	 */
	public static final String	MID_INSUFFICIENT_PERM_DEFINE	= "TacoAPI.err.insufficientPermission.define";
	
	/**
	 * MessageID for when a String cannot be parsed as a double/float. Takes a
	 * String argument (<code>%s</code>).
	 * 
	 * @since TacoAPI/Bukkit 3.0
	 */
	public static final String	MID_NOT_DOUBLE					= "TacoAPI.err.notDouble";
	
	/**
	 * MessageID for when a String cannot be parsed as an integer. Takes a
	 * String argument (<code>%s</code>).
	 * 
	 * @since TacoAPI/Bukkit 3.0
	 */
	public static final String	MID_NOT_INT						= "TacoAPI.err.notInt";
	
	/**
	 * MessageID for when a String cannot be parsed as any type of number. Takes
	 * a String argument (<code>%s</code>).
	 * 
	 * @since TacoAPI/Bukkit 3.0
	 */
	public static final String	MID_NOT_NUM						= "TacoAPI.err.notNum";
	
	/**
	 * MessageID for when a Player is not online. Takes a String argument (
	 * <code>%s</code>).
	 * 
	 * @since TacoAPI/Bukkit 3.0
	 */
	public static final String	MID_NOT_ONLINE					= "TacoAPI.err.notOnline";
	
	protected CommandSender		_sender;
	protected PrintOptions		_options						= new PrintOptions();
	protected JavaPlugin		_plugin;
	
	public BukkitCommandContext(JavaPlugin plugin, CommandSender sender,
			String name, String[] args) {
		this(plugin, sender, null, name, args);
	}
	
	public BukkitCommandContext(JavaPlugin plugin, CommandSender sender,
			String label,
			String name, String[] args) {
		super(label, name, args);
		_sender = sender;
		_plugin = plugin;
	}
	
	protected void addMessages() {
		CommandContext.addMessage(MID_INSUFFUCIENT_FUNDS, "&cYou don't have enough money (&e%f&c)");
		CommandContext.addMessage(MID_INSUFFICIENT_PERM, "&cYou don't have permission");
		CommandContext.addMessage(MID_INSUFFICIENT_PERM_DEFINE, "&cYou don't have permission (&e%s&c)");
		CommandContext.addMessage(MID_NOT_DOUBLE, "&e%s &cis not a decimal");
		CommandContext.addMessage(MID_NOT_INT, "&e%s &cis not an integer");
		CommandContext.addMessage(MID_NOT_NUM, "&e%s &cis not a number");
		CommandContext.addMessage(MID_NOT_ONLINE, "&cThe player &e%s &cis not online");
	}
	
	/**
	 * Set the PrintOptions for this context
	 * 
	 * @param options
	 *            The PrintOptions to set
	 * @see PrintOptions
	 * @since TacoAPI/Bukkit 3.0
	 */
	public void setPrintOptions(PrintOptions options) {
		_options = options;
	}
	
	/**
	 * Get the PrintOptions for this context.
	 * 
	 * @return The PrintOptions for this context
	 * @see PrintOptions
	 * @since TacoAPI/Bukkit 3.0
	 */
	public PrintOptions getPrintOptions() {
		return _options;
	}
	
	/**
	 * Get the sender of this command
	 * 
	 * @return the sender
	 * @since TacoAPI/Bukkit 3.0
	 */
	public CommandSender getSender() {
		return _sender;
	}
	
	/**
	 * Is the sender of this command a Player?
	 * 
	 * @return true if the sender of this command is a Player
	 * @since TacoAPI/Bukkit 3.0
	 */
	public boolean senderIsPlayer() {
		return _sender instanceof Player;
	}
	
	/**
	 * Get the Player who sent this command
	 * 
	 * @return The Player who sent the command, or null if the sender is not a
	 *         Player
	 * @since TacoAPI/Bukkit 3.0
	 */
	public Player getPlayer() {
		if (senderIsPlayer())
			return (Player) _sender;
		return null;
	}
	
	/**
	 * Get the Player who has the name similar to the String at the specified
	 * index
	 * 
	 * @param index
	 *            The index to lookup
	 * @return The Player, or null if the index was out of bounds or no player
	 *         was found
	 * @since TacoAPI/Bukkit 3.0
	 */
	@SuppressWarnings("deprecation")
	public Player getPlayer(int index) {
		return Bukkit.getPlayer(getString(index, ""));
	}
	
	/**
	 * Get the Material whose name matches the String found at the specified
	 * index. (For the programmer's convenience, .toUppercase() is called on the
	 * String)
	 * 
	 * @param index
	 *            The index to lookup
	 * @return The Material, or null if the index was out of bounds or the
	 *         Material could not be found
	 * @since TacoAPI/Bukkit 3.0
	 */
	public Material getMaterial(int index) {
		return Material.getMaterial(getString(index, "").toUpperCase());
	}
	
	/**
	 * Get the name of the Player who ran this command
	 * 
	 * @return The Player's name, or an empty String if this command was not run
	 *         by a Player
	 * @since TacoAPI/Bukkit 3.0
	 */
	public String getPlayerName() {
		if (senderIsPlayer())
			return getPlayer().getName();
		return "";
	}
	
	/**
	 * Did the console run this command?
	 * 
	 * @return true if this command was run from the console
	 * @since TacoAPI/Bukkit 3.0
	 */
	public boolean senderIsConsole() {
		return _sender instanceof ConsoleCommandSender;
	}
	
	/**
	 * Convenience method, eqivalent to <code>Bukkit.getConsoleSender()</code>
	 * 
	 * @return The ConsoleCommandSender found in the <code>Bukkit</code> class
	 * @see ConsoleCommandSender
	 * @since TacoAPI/Bukkit 3.0
	 */
	public ConsoleCommandSender getConsole() {
		return Bukkit.getConsoleSender();
	}
	
	/**
	 * Was this command run from a CommandBlock?
	 * 
	 * @return true if this command was run from a CommandBlock
	 * @see BlockCommandSender
	 * @since TacoAPI/Bukkit 3.0
	 */
	public boolean senderIsCommandBlock() {
		return _sender instanceof BlockCommandSender;
	}
	
	/**
	 * Get the CommandBlock ({@link BlockCommandSender}) that ran this command
	 * 
	 * @return the CommandBlock that ran this command, or null if this command
	 *         was not run by a CommandBlock
	 * @see BlockCommandSender
	 * @since TacoAPI/Bukkit 3.0
	 */
	public BlockCommandSender getCommandBlock() {
		if (senderIsCommandBlock())
			return (BlockCommandSender) _sender;
		return null;
	}
	
	/**
	 * Was this command run from a CommandMinecart?
	 * 
	 * @return true if this command was run from a CommandMinecart
	 * @see CommandMinecart
	 * @since TacoAPI/Bukkit 3.0
	 */
	public boolean senderIsCommandMinecart() {
		return _sender instanceof CommandMinecart;
	}
	
	/**
	 * Get the CommandMinecart that ran this command
	 * 
	 * @return The minecart, or null if this command was not run by a
	 *         CommandMinecart
	 * @since TacoAPI/Bukkit 3.0
	 * @see CommandMinecart
	 */
	public CommandMinecart getCommandMinecart() {
		if (senderIsCommandMinecart())
			return (CommandMinecart) _sender;
		return null;
	}
	
	/**
	 * Does the sender have the specified permission?
	 * 
	 * @param permission
	 *            The permission to test
	 * @return true if the sender of this command has the specified
	 * @since TacoAPI/Bukkit 3.0
	 */
	public boolean senderHasPermission(String permission) {
		if (senderIsPlayer())
			return TPerm.hasPermission(getPlayer(), permission);
		return _sender.hasPermission(permission);
	}
	
	@Override
	public void printMessage(String message) {
		printMessage(message, false);
	}
	
	public void printMessage(String message, boolean header) {
		printMessage(message, header, new PrintOptions());
	}
	
	public void printMessage(String message, boolean header, PrintOptions options) {
		new TChat(_plugin.getName()).sendMessageTo(_sender, message, header, options);
	}
	
	@Override
	public void printError(String message) {
		printError(message, false);
	}
	
	public void printError(String message, boolean header) {
		printError(message, header, new PrintOptions());
	}
	
	public void printError(String message, boolean header, PrintOptions options) {
		printMessage(message, header, options);
	}
	
}
