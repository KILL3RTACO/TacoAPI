package com.kill3rtaco.api.command;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class SimpleCommandManager implements TCommandManager {
	
	private List<TCommand>	_commands	= new ArrayList<TCommand>();
	
	@Override
	public int commandsRegistered() {
		return _commands.size();
	}
	
	@Override
	public void reg(Class<?> clazz) {
		for (Method m : clazz.getDeclaredMethods()) {
			if (TCommand.testValidity(m))
				_commands.add(new TCommand(m));
		}
	}
	
	@Override
	public List<TCommand> getRegisteredCommands() {
		return _commands;
	}
	
	private TCommand getCommand(String cmd, String arg0) {
		for (TCommand c : _commands) {
			if (c.isSimple() && c.getName().equalsIgnoreCase(cmd))
				return c;
			else if (!c.isSimple() && c.getLabel().equalsIgnoreCase(cmd) && c.getName().equalsIgnoreCase(arg0))
				return c;
		}
		return null;
	}
	
	@Override
	public boolean dispatchCommand(String cmd, String[] args) {
		TCommand command = getCommand(cmd, (args.length == 0 ? "" : args[0]));
		if (command == null) {
			return false;
		} else {
			SimpleCommandContext context = new SimpleCommandContext((command.isSimple() ? null : command.getLabel()), cmd, args);
			try {
				command.run(context);
			} catch (Exception e) {
				e.printStackTrace();
				context.printError("An error occurred while running this command");
			}
			return true;
		}
	}
	
	@Override
	public void showGeneralHelp() {
		
	}
	
	@Override
	public void showHelp(String labelOrName) {
		
	}
	
}
