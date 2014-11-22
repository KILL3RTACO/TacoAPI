package com.kill3rtaco.api.command;

import java.util.List;

/**
 * An interface for creating a command manager.
 * 
 * @author KILL3RTACO
 * @see TCommand
 * @since TacoAPI/Command 1.0
 *
 */
public interface TCommandManager {
	
	/**
	 * Get the amount of commands registered
	 * 
	 * @return the amount of commands that have been registered by this command
	 *         manager
	 */
	public int commandsRegistered();
	
	/**
	 * Register a class containing commands. By convention, this method should
	 * check <code>clazz</code> for all valid command methods. The validity of
	 * methods depends on the implementation. The simplest form should be a
	 * method with the {@literal @Command} annotation, and optionally
	 * {@literal @ParentCommand} and {@literal @CommandHelp}. By convention, the
	 * class <i>must</i> have a nullary constructor (a constructor with no
	 * arguments, or no constructors at all). This may vary depending on the
	 * implementation.
	 * 
	 * @param clazz
	 *            The class containing methods to register
	 */
	public void reg(Class<?> clazz);
	
	/**
	 * Get a list of all registered commands. Whether this list is mutable or
	 * just a clone depends on the implementation
	 * 
	 * @return A list of registered commands
	 */
	public List<? extends TCommand> getRegisteredCommands();
	
	/**
	 * Run a command. Depending on the implementation, this method may not do
	 * anything. Traditionally, however, a command string should be taken and
	 * split by whitespace, passing the first item as <code>cmd</code> and the
	 * rest passed as <code>args</code>. A code example of the aforementioned
	 * explanation can be seen below:
	 * 
	 * <pre>
	 * String command = &quot;iris color pink&quot;;
	 * String[] split = command.split(&quot;\\s+&quot;);
	 * String cmd = split[0];
	 * args = removeFirst(args); //programmer's job to implement
	 * dispatchCommand(cmd, args);
	 * </pre>
	 * 
	 * @param cmd
	 *            The command to run
	 * @param args
	 *            The command's arguments
	 * @return true if the command was run
	 */
	public boolean dispatchCommand(String cmd, String[] args);
	
	/**
	 * Show generalized help about all available commands
	 */
	public void showGeneralHelp();
	
	/**
	 * Show generalized help on a command. The required string parameter's
	 * meaning depends on both the context as well as the implementation.
	 * Conventionally, the given parameter should mean "label or name". <br/>
	 * </br> "simple" commands do not have a label, therefore the
	 * <code>name</code> value is checked instead.
	 * 
	 * @param labelOrName
	 */
	public void showHelp(String labelOrName);
	
}
