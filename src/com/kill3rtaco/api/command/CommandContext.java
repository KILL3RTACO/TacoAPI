package com.kill3rtaco.api.command;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents command information when it is run. This is passed to a method
 * with the proper {@literal @Command} annotation.
 * 
 * @author KILL3RTACO
 * @since TacoAPI/Command 1.0
 *
 */
public abstract class CommandContext {
	
	private String						_label, _name;
	private String[]					_args;
	private static Map<String, String>	_messages	= new HashMap<String, String>();
	
	/**
	 * Construct a "simple command" context. A simple command is a command who
	 * has no parent, therefore the arguments are treated as a whole, instead of
	 * being parsed and passed to a separate CommandContext
	 * 
	 * @param name
	 *            The name of the command
	 * @param args
	 *            The arguments that were supplied by the command sender
	 */
	public CommandContext(String name, String[] args) {
		this(null, name, args);
	}
	
	/**
	 * Construct a command context.
	 * 
	 * @param label
	 *            The name of the parent command, or null for this to be a
	 *            "simple" command context.
	 * @param name
	 *            The name of the command
	 * @param args
	 *            The arguments supplied by the command sender
	 */
	public CommandContext(String label, String name, String[] args) {
		_label = label;
		_name = name;
		_args = args;
		addMessages();
	}
	
	/**
	 * Test the argument length with an "equals" operation. This is equivalent
	 * to<br/>
	 * 
	 * <pre>
	 * getArgs().length == length
	 * </pre>
	 * 
	 * @param length
	 *            The lest to test
	 * @return true if the argument length equals the given length
	 */
	public boolean eq(int length) {
		return _args.length == length;
	}
	
	/**
	 * Test the argument length with a "less than" operation. This is equivalent
	 * to<br/>
	 * 
	 * <pre>
	 * getArgs().length &lt; length
	 * </pre>
	 * 
	 * @param length
	 *            The lest to test
	 * @return true if the argument length is less than the given length
	 */
	public boolean lt(int length) {
		return _args.length < length;
	}
	
	/**
	 * Test the argument length with a "less than or equal to" operation. This
	 * is equivalent to<br/>
	 * 
	 * <pre>
	 * getArgs().length &lt;= length
	 * </pre>
	 * 
	 * @param length
	 *            The lest to test
	 * @return true if the argument length is less than or equal to the given
	 *         length
	 */
	public boolean lteq(int length) {
		return _args.length <= length;
	}
	
	/**
	 * Test the argument length with a "greater than" operation. This is
	 * equivalent to<br/>
	 * 
	 * <pre>
	 * getArgs().length &gt; length
	 * </pre>
	 * 
	 * @param length
	 *            The lest to test
	 * @return true if the argument length is greater than the given length
	 */
	public boolean gt(int length) {
		return _args.length > length;
	}
	
	/**
	 * Test the argument length with a "greater than or equal to" operation.
	 * This is equivalent to<br/>
	 * 
	 * <pre>
	 * getArgs().length &gt;= length
	 * </pre>
	 * 
	 * @param length
	 *            The lest to test
	 * @return true if the argument length is greater than or equal to the given
	 *         length
	 */
	public boolean gteq(int length) {
		return _args.length >= length;
	}
	
	/**
	 * Get the name of the parent command. This is the value of the
	 * {@literal @ParentCommand} annotation, if present
	 * 
	 * @return null if this is a simple command, otherwise the name of the
	 *         parent command
	 */
	public String getLabel() {
		return _label;
	}
	
	/**
	 * Get the name of the command being run.
	 * 
	 * @return The name
	 */
	public String getName() {
		return _name;
	}
	
	/**
	 * Get the arguments supplied by the command sender
	 * 
	 * @return The arguments
	 */
	public String[] getArgs() {
		return _args;
	}
	
	/**
	 * Join all the arguments with a space. As an example, consider the
	 * arguments:<br/>
	 * 
	 * <pre>
	 * {&quot;oreo&quot;, &quot;infused&quot;, &quot;chocolate&quot;, &quot;chip&quot;, &quot;cookies&quot;}
	 * </pre>
	 * 
	 * <code>getJoinedArgs()</code> would produce:
	 * 
	 * <pre>
	 * &quot;oreo infused chocolate chip cookies&quot;
	 * </pre>
	 * 
	 * @return A string of all arguments separated by a space
	 */
	public String getJoinedArgs() {
		return getJoinedArgs(0);
	}
	
	/**
	 * Join all arguments from <code>startIndex</code> to the end and separate
	 * them with a space. For example, given the arguments:<br/>
	 * 
	 * <pre>
	 * {&quot;oreo&quot;, &quot;infused&quot;, &quot;chocolate&quot;, &quot;chip&quot;, &quot;cookies&quot;}
	 * </pre>
	 * 
	 * In this case, <code>getJoinedArgs(2)</code> would produce:
	 * 
	 * <pre>
	 * &quot;chocolate chip cookies&quot;
	 * </pre>
	 * 
	 * @param startIndex
	 *            Where to start
	 * @return A string of joined arguments
	 */
	public String getJoinedArgs(int startIndex) {
		return getJoinedArgs(startIndex, " ");
	}
	
	/**
	 * Join all arguments from <code>startIndex</code> to the end and separate
	 * them with the specified delimiter. For example, given the arguments:<br/>
	 * 
	 * <pre>
	 * {&quot;oreo&quot;, &quot;infused&quot;, &quot;chocolate&quot;, &quot;chip&quot;, &quot;cookies&quot;}
	 * </pre>
	 * 
	 * In this case, <code>getJoinedArgs(2, "-")</code> would produce:
	 * 
	 * <pre>
	 * &quot;chocolate-chip-cookies&quot;
	 * </pre>
	 * 
	 * @param startIndex
	 *            Where to start
	 * @param delim
	 *            The delimieter to use
	 * @return A string of joined arguments starting at <code>startIndex</code>,
	 *         separated by <code>delim</code>
	 */
	public String getJoinedArgs(int startIndex, String delim) {
		return getJoinedArgs(startIndex, _args.length - 1, delim);
	}
	
	/**
	 * Join all arguments from <code>startIndex</code> to <code>endIndex</code>
	 * and separate them with the specified delimiter. For example, given the
	 * arguments:<br/>
	 * 
	 * <pre>
	 * {&quot;oreo&quot;, &quot;infused&quot;, &quot;chocolate&quot;, &quot;chip&quot;, &quot;cookies&quot;, &quot;are&quot;, &quot;amazing&quot;}
	 * </pre>
	 * 
	 * In this case, <code>getJoinedArgs(2, 4, "-")</code> would produce:
	 * 
	 * <pre>
	 * &quot;chocolate-chip-cookies&quot;
	 * </pre>
	 * 
	 * @param startIndex
	 *            Where to start
	 * @param delim
	 *            The delimieter to use
	 * @return A string of joined arguments starting at <code>startIndex</code>,
	 *         separated by <code>delim</code>
	 */
	public String getJoinedArgs(int startIndex, int endIndex, String delim) {
		String joined = "";
		for (int i = startIndex; i <= endIndex; i++) {
			if (_args.length <= i)
				break;
			joined += getString(i, "");
			if (i < endIndex)
				joined += delim;
		}
		return joined.trim();
	}
	
	/**
	 * Get all arguments, starting at the given index.
	 * 
	 * @param startIndex
	 *            Where to start
	 * @return All arguments from <code>startIndex</code> to
	 *         <code>getArgs().length</code>
	 */
	public String[] getSlice(int startIndex) {
		return getSlice(startIndex, _args.length - 1);
	}
	
	/**
	 * Get all arguments from <code>startIndex</code> to <code>endIndex</code>
	 * 
	 * @param startIndex
	 *            Where to start
	 * @param endIndex
	 *            Where to end
	 * @return
	 */
	public String[] getSlice(int startIndex, int endIndex) {
		if (startIndex < 0 || startIndex >= _args.length)
			throw new IllegalArgumentException("startIndex is out of bounds");
		else if (endIndex > startIndex)
			throw new IllegalArgumentException("endIndex cannot be greater than startIndex");
		
		String[] args = new String[endIndex - startIndex];
		for (int i = startIndex; i <= endIndex; i++) {
			args[startIndex - i] = _args[i];
		}
		return args;
	}
	
	/**
	 * Test if the argument at <code>index</code> passes an equalsIgnoreCase()
	 * call with any of the given test Strings
	 * 
	 * @param index
	 * @param tests
	 * 
	 * @return false if no Strings where given, otherwise if
	 *         tests[n].equalsIgnoreCase(getString(index)) is true
	 */
	public boolean eic(int index, String... tests) {
		if (tests.length == 0)
			return false;
		for (String s : tests) {
			if (getString(index, "").equalsIgnoreCase(s))
				return true;
		}
		return false;
	}
	
	/**
	 * Print a message to the command sender.
	 * 
	 * @param message
	 *            The message to send
	 */
	public abstract void printMessage(String message);
	
	/**
	 * Optionally add messages to the CommandContext class.
	 * 
	 * @see CommandContext#addMessage(String, String)
	 */
	protected abstract void addMessages();
	
	/**
	 * Print a previously add message to the command sender. If
	 * 
	 * <pre>
	 * CommandContext.addMessage(&quot;Taco.cookies&quot;, &quot;I love %s cookies&quot;);
	 * </pre>
	 * 
	 * was called previously, then the call
	 * 
	 * <pre>
	 * printMessage(&quot;Taco.cookies&quot;, &quot;oreo&quot;);
	 * </pre>
	 * 
	 * will result in
	 * 
	 * <pre>
	 * I love oreo cookies
	 * </pre>
	 * 
	 * being sent to the command sender
	 * 
	 * @param id
	 *            The id of the message
	 * @param args
	 *            The arguments to pass to String.format(), or
	 *            <code>(Object[]) null</code> if no formatting should be done
	 */
	public void printMessage(String id, Object... args) {
		String message;
		if (args == null)
			message = getMessage(id);
		else
			message = String.format(getMessage(id), args);
		printMessage(message);
	}
	
	/**
	 * Print an error to the command sender. This may have the same effect as
	 * {@link #printMessage(String)}, depending on the implementation
	 * 
	 * @param message
	 *            The message to send
	 */
	public abstract void printError(String message);
	
	/**
	 * Print an error to the command sender. This may have the same effect as
	 * {@link #printMessage(String, Object...)}, depending on the implementation
	 * 
	 * @param message
	 *            The message to send
	 * @param id
	 *            The id of the message
	 * @param args
	 *            The arguments to pass to String.format(), or
	 *            <code>(Object[]) null</code> if no formatting should be done
	 */
	public void printError(String id, Object... args) {
		printError(String.format(getMessage(id), args));
	}
	
	/**
	 * Get the argument at <code>index</code>.
	 * 
	 * @param index
	 *            The index of the argument
	 * @return argument at <code>index</code>, or an empty String if
	 *         <code>index</code> is out of bounds.
	 */
	public String getString(int index) {
		return getString(index, "");
	}
	
	/**
	 * Get the argument at <code>index</code>. If <code>index</code> is out of
	 * bounds, then <code>def</code> is returned
	 * 
	 * @param index
	 *            The index of the argument
	 * @param def
	 *            The default
	 * @return The argument at <code>index</code>, or <code>def</code> if
	 *         <code>index</code> is out of bounds
	 */
	public String getString(int index, String def) {
		return (index >= _args.length || index < 0 ? def : _args[index]);
	}
	
	/**
	 * Get whether or not the argument at <code>index</code> can be parsed as an
	 * Integer
	 * 
	 * @param index
	 *            The index of the argument
	 * @return true if the argument at <code>index</code> can be parse as an
	 *         Integer
	 */
	public boolean isInt(int index) {
		try {
			Integer.parseInt(getString(index));
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	/**
	 * Get whether or not the argument at <code>index</code> can be parsed as an
	 * Double
	 * 
	 * @param index
	 *            The index of the argument
	 * @return true if the argument at <code>index</code> can be parse as an
	 *         Double
	 */
	public boolean isDouble(int index) {
		try {
			Double.parseDouble(getString(index));
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	/**
	 * Get the argument at <code>index</code> as an int
	 * 
	 * @param index
	 *            The index of the argument
	 * @return The argument at <code>index</code> as an int, or 0 if
	 *         <code>index</code> is out of bounds or the argument could not be
	 *         parsed an an Integer
	 */
	public int getInteger(int index) {
		return getInteger(index, 0);
	}
	
	/**
	 * Get the argument at <code>index</code> as an int
	 * 
	 * @param index
	 *            The index of the argument
	 * @param def
	 *            The default
	 * @return The argument at <code>index</code> as an int, or <code>def</code>
	 *         if <code>index</code> is out of bounds or the argument could not
	 *         be parsed an an Integer
	 */
	public int getInteger(int index, int def) {
		try {
			return Integer.parseInt(getString(index));
		} catch (NumberFormatException e) {
			return def;
		}
	}
	
	/**
	 * Get the argument at <code>index</code> as a double
	 * 
	 * @param index
	 *            The index of the argument
	 * @return The argument at <code>index</code> as an double, or 0 if
	 *         <code>index</code> is out of bounds or the argument could not be
	 *         parsed an an Double
	 */
	public double getDouble(int index) {
		return getDouble(index, 0);
	}
	
	/**
	 * Get the argument at <code>index</code> as a double
	 * 
	 * @param index
	 *            The index of the argument
	 * @param def
	 *            The default
	 * @return The argument at <code>index</code> as an int, or <code>def</code>
	 *         if <code>index</code> is out of bounds or the argument could not
	 *         be parsed an an Double
	 */
	public double getDouble(int index, int def) {
		try {
			return Double.parseDouble(getString(index));
		} catch (NumberFormatException e) {
			return def;
		}
	}
	
	/**
	 * Add a message to the CommandContext class with the given id and format.
	 * The format is used by String.format() in the methods below (unless the
	 * method was overwritten by a subclass).<br/>
	 * <br/>
	 * These messages can be retrieved later with any of the following methods:
	 * <ul>
	 * <li>{@link CommandContext#getMessage(String)}</li>
	 * <li>context.{@link #printMessage(String, Object...)}</li>
	 * <li>context.{@link #printError(String, Object...)}</li>
	 * </ul>
	 * 
	 * @param id
	 *            The
	 * @param format
	 */
	public static void addMessage(String id, String format) {
		_messages.put(id, format);
	}
	
	/**
	 * Remove a message.
	 * 
	 * @param id
	 *            The id of the message
	 */
	public static void removeMessage(String id) {
		addMessage(id, null);
	}
	
	/**
	 * Get a message previously add with
	 * {@link CommandContext#addMessage(String, String)}
	 * 
	 * @param id
	 *            The id of the message
	 * @return The message (or message format), or null if a message with the
	 *         given id could not be found
	 */
	public static String getMessage(String id) {
		String msg = _messages.get(id);
		if (msg == null)
			msg = "";
		return msg;
	}
	
}
