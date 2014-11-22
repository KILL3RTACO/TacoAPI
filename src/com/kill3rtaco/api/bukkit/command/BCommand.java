package com.kill3rtaco.api.bukkit.command;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.scheduler.BukkitRunnable;

import com.kill3rtaco.api.bukkit.command.tab.TabCompletionLists;
import com.kill3rtaco.api.bukkit.command.tab.TabCompletionListsLiteral;
import com.kill3rtaco.api.command.TCommand;

/**
 * Represents a Bukkit command.
 * 
 * @author KILL3RTACO
 * @since TacoAPI/Bukkit 3.0
 * @see BukkitCommandManager
 *
 */
public class BCommand extends TCommand {
	
	protected boolean						_async, _commandBlock, _console,
											_invisible, _minecart, _player;
	protected String						_perm;
	protected Map<Integer, String>			_tcls			= new HashMap<Integer, String>();		//<index, id>
	protected Map<Integer, List<String>>	_tclLiterals	= new HashMap<Integer, List<String>>();
	
	public BCommand(Method method) {
		super(method);
		BukkitCommand bcmd = method.getAnnotation(BukkitCommand.class);
		_async = bcmd.async();
		_commandBlock = bcmd.commandBlock();
		_console = bcmd.console();
		_invisible = bcmd.invisible();
		_minecart = bcmd.minecart();
		_player = bcmd.player();
		_perm = bcmd.perm();
		if (method.isAnnotationPresent(TabCompletionLists.class)) {
			TabCompletionLists lists = method.getAnnotation(TabCompletionLists.class);
			String[] arr = lists.value();
			for (int i = 0; i < arr.length; i++) {
				String s = arr[i];
				if (s == null || s.isEmpty())
					continue;
				_tcls.put(i, s);
			}
		}
		if (method.isAnnotationPresent(TabCompletionListsLiteral.class)) {
			TabCompletionListsLiteral literals = method.getAnnotation(TabCompletionListsLiteral.class);
			String[] arr = literals.value();
			for (int i = 0; i < arr.length; i++) {
				String s = arr[i];
				if (s == null)
					continue;
				String[] split = s.split(",");
				if (arr.length == 0)
					continue;
				_tclLiterals.put(i, Arrays.asList(split));
			}
		}
	}
	
	/**
	 * Get the permission required to run this command
	 * 
	 * @return The permission
	 * @see BukkitCommand#perm()
	 * @since TacoAPI/Bukkit 3.0
	 */
	public String getPermission() {
		return _perm;
	}
	
	/**
	 * Should this command be run asynchronously? If so, the command will be run
	 * in an async BukkitRunnable
	 * 
	 * @return true if this command should be run asynchronously?
	 * @see BukkitCommand#async()
	 * @since TacoAPI/Bukkit 3.0
	 */
	public boolean isAsync() {
		return _async;
	}
	
	/**
	 * Can a Player run this command?
	 * 
	 * @return true if a Player can run this command
	 * @see BukkitCommand#player()
	 * @since TacoAPI/Bukkit 3.0
	 */
	public boolean playerCanRun() {
		return _player;
	}
	
	/**
	 * Is this command invisible? Invisible commands can still be run, but will
	 * not be shown in help listings or tab completion
	 * 
	 * @return true if the command is invisible
	 * @since TacoAPI/Bukkit 3.0
	 * @see BukkitCommand#invisible()
	 */
	public boolean isInvisible() {
		return _invisible;
	}
	
	/**
	 * Can this command be run from the console?
	 * 
	 * @return true if this command can be run from the console
	 * @see BukkitCommand#console()
	 * @since TacoAPI/Bukkit 3.0
	 */
	public boolean consoleCanRun() {
		return _console;
	}
	
	/**
	 * Can this command be run from a CommandBlock?
	 * 
	 * @return true if this command can be run from a CommandBlock
	 * @see BukkitCommand#commandBlock()
	 * @since TacoAPI/Bukkit 3.0
	 */
	public boolean commandBlockCanRun() {
		return _commandBlock;
	}
	
	/**
	 * Can this command be run from a minecart with a command block?
	 * 
	 * @return true if this command can be run from a CommandMinecart
	 * @see BukkitCommand#minecart()
	 * @since TacoAPI/Bukkit 3.0
	 */
	public boolean minecartCanRun() {
		return _minecart;
	}
	
	/**
	 * Run the command
	 * 
	 * @param context
	 *            The context of the command, including who ran it, the
	 *            arguments, etc.
	 * @since TacoAPI/Bukkit 3.0
	 */
	public void run(final BukkitCommandContext context) {
		if (!_async) {
			superRun(context);
			return;
		}
		new BukkitRunnable() {
			
			@Override
			public void run() {
				superRun(context);
			}
			
		}.runTaskAsynchronously(context._plugin);
	}
	
	private void superRun(BukkitCommandContext context) {
		try {
			super.run(context);
		} catch (Exception e) {
			e.printStackTrace();
			context.printError("An exception occurred while running this command");
		}
	}
	
	/**
	 * Test whether a BCommand can be constructed from the given method
	 * 
	 * @param method
	 *            The method test
	 * @return true if a BCommand can be constructed safely from the given
	 *         method
	 * @since TacoAPI/Bukkit 3.0
	 */
	public static boolean testValidity(Method method) {
		return TCommand.testValidity(method) &&
				method.isAnnotationPresent(BukkitCommand.class);
	}
	
	String getTabCompletionListId(int index) {
		return _tcls.get(index);
	}
	
	List<String> getLiteralList(int index, String alias) {
		if (!_tclLiterals.containsKey(index))
			return new ArrayList<String>();
		List<String> valid = new ArrayList<String>();
		for (String s : _tclLiterals.get(index)) {
			if (s.toLowerCase().startsWith(alias.toLowerCase()))
				valid.add(s);
		}
		return valid;
	}
	
}
