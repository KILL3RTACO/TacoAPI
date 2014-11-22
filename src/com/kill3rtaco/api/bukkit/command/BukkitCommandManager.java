package com.kill3rtaco.api.bukkit.command;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.CommandMinecart;
import org.bukkit.plugin.java.JavaPlugin;

import com.kill3rtaco.api.bukkit.TChat;
import com.kill3rtaco.api.bukkit.TPerm;
import com.kill3rtaco.api.bukkit.command.tab.TabCompletionList;
import com.kill3rtaco.api.bukkit.pagination.BukkitPageViewer;
import com.kill3rtaco.api.bukkit.util.ChatUtils;
import com.kill3rtaco.api.bukkit.util.PrintOptions;
import com.kill3rtaco.api.command.TCommandManager;

/**
 * Bukkit CommandManager implementation. Manages TabCompletion and execution of
 * commands
 * 
 * @author KILL3RTACO
 * @since TacoAPI/Bukkit 3.0
 * @see BCommand
 *
 */
public class BukkitCommandManager implements TCommandManager, CommandExecutor, TabCompleter {
	
	protected List<BCommand>		_commands	= new ArrayList<BCommand>();
	protected Map<String, Method>	_tcls		= new HashMap<String, Method>();
	protected JavaPlugin			_plugin;
	protected PrintOptions			_options;
	protected TChat					_chat;
	
	public BukkitCommandManager(JavaPlugin plugin) {
		this(plugin, new PrintOptions());
	}
	
	public BukkitCommandManager(JavaPlugin plugin, PrintOptions options) {
		_plugin = plugin;
		_options = options;
		_chat = new TChat(_plugin.getName());
//		regTabCompletionLists(TabCompletionListDefaults.class); //not needed for this plugin
	}
	
	@Override
	public int commandsRegistered() {
		return _commands.size();
	}
	
	@Override
	public void reg(Class<?> clazz) {
		for (Method m : clazz.getDeclaredMethods()) {
			if (BCommand.testValidity(m)) {
				BCommand cmd = new BCommand(m);
				PluginCommand bukkitCmd;
				if (cmd.isSimple())
					bukkitCmd = _plugin.getCommand(cmd.getName());
				else
					bukkitCmd = _plugin.getCommand(cmd.getLabel());
				if (bukkitCmd == null)
					continue;
				bukkitCmd.setExecutor(this);
				if (bukkitCmd.getTabCompleter() == null)
					bukkitCmd.setTabCompleter(this);
				_commands.add(cmd);
			}
		}
	}
	
	//better do call this before commands
	public void regTabCompletionLists(Class<?> clazz) {
		for (Method m : clazz.getDeclaredMethods()) {
			if (!m.isAnnotationPresent(TabCompletionList.class))
				continue;
			if (m.getReturnType() != List.class)
				return;
			Class<?>[] params = m.getParameterTypes();
			if (params.length != 1 || params[0] != String.class)
				continue;
			String id = m.getAnnotation(TabCompletionList.class).value();
			_tcls.put(id, m);
//			System.out.println("ID: " + id + ", Method: " + m.getName());
		}
	}
	
	@Override
	public List<BCommand> getRegisteredCommands() {
		return _commands;
	}
	
	public BCommand getCommand(String bukkitCmdName, String arg0) {
		for (BCommand c : _commands) {
			if (!c.isSimple() && c.getLabel().equalsIgnoreCase(bukkitCmdName) && c.hasAlias(arg0))
				return c;
			if (c.isSimple() && c.getName().equalsIgnoreCase(bukkitCmdName))
				return c;
		}
		return null;
	}
	
	/**
	 * Get whether the command sender has the given permission
	 * 
	 * @param context
	 *            the context that will be given to the command this method
	 *            returns true
	 * @param command
	 *            the command that will be run if this method returns true
	 * @return true if the command sender has permission
	 * @since TacoAPI/Bukkit 3.0
	 */
	protected boolean handlePerm(BukkitCommandContext context, BCommand command) {
		return context.senderHasPermission(command.getPermission());
	}
	
	private List<String> commandNames(String bukkitCmdName, String start) {
		List<String> valid = new ArrayList<String>();
		for (BCommand c : _commands) {
			if (c.isInvisible())
				continue;
			if (!c.isSimple() && c.getLabel().equalsIgnoreCase(bukkitCmdName) && c.getName().toLowerCase().startsWith(start.toLowerCase())) {
				valid.add(c.getName());
			}
		}
		Collections.sort(valid);
		return valid;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		String bukkitCmdName = cmd.getName();
		//arg0 will be # or #.# depending on number format (instead of the actual number itself)
		String arg0 = (args.length > 0 ? (ChatUtils.isInt(args[0]) ? "#" : ChatUtils.isDouble(args[0]) ? "#.#" : args[0]) : "");
		BCommand command = getCommand(bukkitCmdName, arg0);
		
		if (command == null) {
			/*
			 * for a simple command that exists and the arg '?' was given, it will not even get this far
			 * simple commands must have they're own help, but the @CommandHelp can still be attached and
			 * getHelp() will still work
			 */
			if (arg0.equals("?") || arg0.equalsIgnoreCase("help")) {
				if (args.length >= 2) { // /label ? <arg>
					int page = 1;
					String arg = args[1];
					try {
						page = Integer.parseInt(arg);
						showHelp(sender, bukkitCmdName, label, page);
					} catch (NumberFormatException e) {
						BCommand needHelp = getCommand(bukkitCmdName, arg);
						if (needHelp == null) {
							showHelp(sender, bukkitCmdName, label, 1);
							return true;
						}
						if (!handlePerm(new BukkitCommandContext(_plugin, sender, label, arg, new String[]{}), needHelp)) {
							printError(sender, "/" + label + " " + arg + ": you don't have permisson");
							return true;
						}
						for (String s : needHelp.getHelp().split("\n")) {
							String msg = "&e" + s;
							msg = msg.replace("&r", "&r&e")
									.replace("%label", label)
									.replace("%name", arg);
							new TChat("").sendMessageTo(sender, "&e" + s, false);
						}
						return true;
					}
				}
				showHelp(sender, bukkitCmdName, label, 1);
			} else {
				printError(sender, "Command &e/" + (label + " " + arg0).trim() + " &rnot found");
			}
			return true;
		}
		
		boolean console = (sender instanceof ConsoleCommandSender);
		boolean commandBlock = (sender instanceof BlockCommandSender);
		boolean minecart = (sender instanceof CommandMinecart);
		boolean player = (sender instanceof Player);
		
		if (console && !command.consoleCanRun()) {
			printError(sender, "This command cannot be run by console");
			return true;
		} else if (commandBlock && !command.commandBlockCanRun()) {
			printError(sender, "This command cannot be run by a CommandBlock");
			return true;
		} else if (minecart && !command.minecartCanRun()) {
			printError(sender, "This command cannot be run by a CommandMinecart");
			return true;
		} else if (player && !command.playerCanRun()) {
			printError(sender, "This command cannot be run by a player");
			return true;
		}
		
		BukkitCommandContext context;
		
		if (command.isSimple()) {
			context = new BukkitCommandContext(_plugin, sender, bukkitCmdName, args);
		} else {
			context = new BukkitCommandContext(_plugin, sender, bukkitCmdName, arg0, (arg0.equals("#") || arg0.equals("#.#") ? args : ChatUtils.removeFirstArg(args)));
		}
		
		String labelAndName = "/";
		if (!command.isSimple())
			labelAndName += command.getLabel();
		labelAndName += " " + command.getName();
		
		if (!handlePerm(context, command)) {
			context.printError(labelAndName + ": you don't have permission");
			return true;
		}
		
		command.run(context);
		
		return true;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
		String bukkitCmdName = cmd.getName();
		String start = args[args.length - 1];
		List<String> nothing = new ArrayList<String>();
		if (args.length == 1) {
			//tabcomplete the command
			return commandNames(bukkitCmdName, start);
		}
		//arg0 will be # or #.# depending on number format (instead of the actual number itself)
		String arg0 = (args.length > 0 ? (ChatUtils.isInt(args[0]) ? "#" : ChatUtils.isDouble(args[0]) ? "#.#" : args[0]) : "");
		if (arg0.equals("?") && args.length > 1) {
			return commandNames(bukkitCmdName, args[1]);
		}
		BCommand command = getCommand(bukkitCmdName, arg0);
		
		if (command == null)
			return nothing;
		
		int index = args.length;
		if (command.isSimple())
			index--; // /name incom <- this is an index of 1 in args, but should be 0
		else
			index -= 2; // /label name incom <- this is an index of 2 in args, but should be 0
			
		String listId = command.getTabCompletionListId(index);
		if (listId == null) {
			return command.getLiteralList(index, start);
		}
		
		Method method = _tcls.get(listId);
		if (method == null)
			return nothing;
		try {
			//throws exception only if the programmer failed to comply with the guidelines of @TabCompletionList
			return (List<String>) method.invoke(method.getDeclaringClass().newInstance(), start);
		} catch (Exception e) {
			e.printStackTrace();
			return nothing;
		}
	}
	
	@Override
	public boolean dispatchCommand(String cmd, String[] args) {
		//send as if by console, unused otherwise
		Command command = _plugin.getCommand(cmd);
		if (command == null)
			return false;
		onCommand(Bukkit.getConsoleSender(), command, cmd, args);
		return true;
	}
	
	@Override
	public void showGeneralHelp() {
		showGeneralHelp(Bukkit.getServer().getConsoleSender());
	}
	
	public void showGeneralHelp(CommandSender toWho) {
		//get all (Bukkit) commands registered by this command handler
		//print them as
		// /label - {description}
		List<Command> commands = new ArrayList<Command>();
		for (BCommand c : _commands) {
			Command cmd = _plugin.getCommand(c.getLabel());
			if (commands.contains(cmd))
				continue;
			commands.add(cmd);
		}
		for (Command c : commands) {
			String desc = c.getDescription();
			if (desc == null || desc.isEmpty())
				desc = "No description available";
			_chat.sendMessageTo(toWho, "&e/" + c.getLabel() + "&7 - &e" + desc, false, _options);
		}
	}
	
	/**
	 * This method is overridden but does not do anything
	 */
	@Override
	public void showHelp(String labelOrName) {
		//unused
	}
	
	//only works for non-simple commands, simple commands must supply there own help
	private void showHelp(CommandSender sender, String label, String alias, int page) {
		BukkitPageViewer pages = new BukkitPageViewer("&b/" + alias + " &3Command Help", "&3=====[%title &9%page&3]=====");
		pages.setSubtitle("&aKEY: &2<> &7- &eRequired &2[] &7- &eOptional &2/ &7- &eOr");
		List<BCommand> commands = new ArrayList<BCommand>();
		for (BCommand c : _commands) {
			if (!c.isSimple() && c.getLabel().equalsIgnoreCase(label) && !c.isInvisible() && TPerm.hasPermission(sender, c.getPermission()))
				commands.add(c);
		}
		pages.append("&3/&b" + alias + " &3?&b/&3help &b[page] &7- &bShow general help");
		for (BCommand c : commands) {
			String delim = "&b/&3";
			String subAliases = c.getName() + (c.getAliases().length > 0 ? delim + ChatUtils.join(c.getAliases(), delim) : "");
			String args = c.getArgs();
			String line = "&b/" + alias + (!subAliases.isEmpty() ? " &3" + subAliases : "") + (!args.isEmpty() ? " &3" + args : "") + " &7- &b" + c.getDescription();
			pages.append(line);
		}
		Collections.sort(pages.getElements());
		if (pages.hasNoPages()) {
			_chat.sendMessageTo(sender, "&cNo help found. Perhaps you don't have some of the permissions required for the commands?", false, _options);
			return;
		}
		if (!pages.hasPage(page))
			page = 1;
		pages.showPage(page, sender);
	}
	
	public void printMessage(CommandSender sender, String message) {
		_chat.sendMessageTo(sender, message, false, _options);
	}
	
	public void printError(CommandSender sender, String message) {
		printMessage(sender, "&c" + message.replace("&r", "&r&c"));
	}
	
}
