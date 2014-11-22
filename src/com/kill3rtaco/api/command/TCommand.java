package com.kill3rtaco.api.command;

import java.lang.reflect.Method;

/**
 * Simplest form of a command. Represents a command that can be run at any time.
 * 
 * @author KILL3RTACO
 * @see TCommandManager
 * @see CommandContext
 * @since TacoAPI/Command 1.0
 *
 */
public class TCommand implements Comparable<TCommand> {
	
	private String		_label	= null, _name, _args, _desc, _help = null;
	private String[]	_aliases;
	private Method		_toInvoke;
	
	public TCommand(Method method) {
		_toInvoke = method;
		if (method.isAnnotationPresent(ParentCommand.class))
			_label = method.getAnnotation(ParentCommand.class).value();
		Command cmdAnnotation = method.getAnnotation(Command.class);
		_name = cmdAnnotation.name();
		_args = cmdAnnotation.args();
		_aliases = cmdAnnotation.aliases();
		_desc = cmdAnnotation.desc();
		if (method.isAnnotationPresent(CommandHelp.class))
			_help = method.getAnnotation(CommandHelp.class).value();
	}
	
	public String getLabel() {
		return _label;
	}
	
	public String getName() {
		return _name;
	}
	
	public String[] getAliases() {
		return _aliases;
	}
	
	public String getArgs() {
		return _args;
	}
	
	public String getDescription() {
		return _desc;
	}
	
	public String getHelp() {
		return _help;
	}
	
	public boolean isSimple() {
		return _label == null;
	}
	
	public boolean hasAlias(String alias) {
		if (_name.equalsIgnoreCase(alias))
			return true;
		for (String s : _aliases) {
			if (s.equalsIgnoreCase(alias))
				return true;
		}
		return false;
	}
	
	public <T extends CommandContext> void run(T context) throws Exception {
		_toInvoke.invoke((Object) _toInvoke.getDeclaringClass().newInstance(), context);
	}
	
	public static boolean testValidity(Method method) {
		return method.isAnnotationPresent(Command.class);
	}
	
	public int compareTo(TCommand command) {
		String str = (_label != null ? _label : _name);
		String str2 = (!command.isSimple() ? command.getLabel() : command.getName());
		return str.compareTo(str2);
	}
	
}
