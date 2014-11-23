package com.kill3rtaco.api.bukkit.dev;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLConnection;

import org.bukkit.configuration.file.YamlConfiguration;

/**
 * The main controlling class containing information of TBukkit, such as the
 * version
 * 
 * @author KILL3RTACO
 * @since TBukkit 1.0
 *
 */
public class TBukkit {
	
	public static final String				AUTHOR					= "KILL3RTACO";
	public static final String				NAME					= "TBukkit";
	public static final String				VERSION					= "1.0";
	private static boolean					ENABLED					= true;
	
	public static final File				CONFIG_FILE;
	public static final YamlConfiguration	CONFIG;
	
	public static final String				API_KEY_HEADER			= "X-API-Key";
	public static final String				API_KEY;
	
	public static final String				USER_AGENT_HEADER		= "User-Agent";
	public static final String				USER_AGENT				= NAME + "/" + VERSION + " (by " + AUTHOR + ")";
	
	/**
	 * The regex used to test version strings. Value is "[0-9]+((\\.[0-9]+)+)?"
	 */
	public static final String				VERSION_REGEX			= "[0-9]+((\\.[0-9]+)+)?";
	public static final String				VERSION_CONVERT_REGEX	= "(.*v)(?=" + VERSION_REGEX + ")";
	
	private TBukkit() {}
	
	static {
		//there will always be at least one plugin, because a plugin will call the class
		File jar = new File(TBukkit.class.getProtectionDomain().getCodeSource().getLocation().getPath());
		CONFIG_FILE = new File(jar.getParentFile(), "TBukkit/config.yml");
//		System.out.println(CONFIG_FILE);
//		CONFIG_FILE = new File(Bukkit.getPluginManager().getPlugins()[0].getDataFolder().getParentFile(), "TBukkit/config.yml");
		CONFIG_FILE.getParentFile().mkdirs();
		
		CONFIG = YamlConfiguration.loadConfiguration(CONFIG_FILE);
		CONFIG.addDefault("api-key", null);
		CONFIG.addDefault("enable", true);
		
		API_KEY = CONFIG.getString("api-key");
		ENABLED = CONFIG.getBoolean("enabled");
		
		saveConfig();
	}
	
	/**
	 * Disable TBukkit. This method should only be used if it is assumed the
	 * server owner <i>wants</i> it disabled. For instance, a command that was
	 * run to disable it.<br/>
	 * <br/>
	 * It is important to note that TBukkit uses a global opt-out system, if it
	 * is disabled no plugin will be able to use it. It is the job of the plugin
	 * to implement their own opt-out system, which can be done easily
	 * considering the following psuedo-code:
	 * 
	 * <pre>
	 * if (config.useTBukkit()) {
	 * 	//TBukkit operations
	 * }
	 * </pre>
	 * 
	 * The above if statement above can be further defined by adding
	 * <code>&& TBukkit.isEnabled()</code>, ensuring that the if statement will
	 * only run if TBukkit is enabled in the first place.
	 * 
	 * @since TBukkit 1.0
	 */
	public static void disable() {
		ENABLED = false;
		saveConfig();
	}
	
	/**
	 * Enable TBukkit. This method should only be used if it is assumed the
	 * server owner <i>wants</i> it enabled. For instance, a command that was
	 * run to enable it.<br/>
	 * <br/>
	 * It is important to note that TBukkit uses a global opt-out system, if it
	 * is disabled no plugin will be able to use it. It is the job of the plugin
	 * to implement their own opt-out system, which can be done easily
	 * considering the following psuedo-code:
	 * 
	 * <pre>
	 * if (config.useTBukkit()) {
	 * 	//TBukkit operations
	 * }
	 * </pre>
	 * 
	 * The above if statement above can be further defined by adding
	 * <code>&& TBukkit.isEnabled()</code>, ensuring that the if statement will
	 * only run if TBukkit is enabled in the first place.
	 * 
	 * @since TBukkit 1.0
	 */
	public static void enable() {
		ENABLED = true;
		saveConfig();
	}
	
	/**
	 * Is TBukkit enabled?<br/>
	 * If it is not, <i>all</i> features are enabled
	 * 
	 * @return whether TBukkit is enabled or not
	 * @see #enable()
	 * @see #disable()
	 * @since TBukkit 1.0
	 */
	public static boolean isEnabled() {
		return ENABLED;
	}
	
	private static void saveConfig() {
		try {
			CONFIG.save(CONFIG_FILE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Preps a connection by setting the X-API-Key and User-Agent headers.
	 * 
	 * @param connection
	 *            The connection to prep
	 * @param doOutput
	 *            Are you writing data?
	 * @since TBukkit 1.0
	 */
	public static void prepConnection(URLConnection connection, boolean doOutput) {
		connection.setConnectTimeout(5000); //5 seconds
		if (TBukkit.API_KEY != null)
			connection.addRequestProperty(TBukkit.API_KEY_HEADER, TBukkit.API_KEY);
		connection.addRequestProperty(TBukkit.USER_AGENT_HEADER, TBukkit.USER_AGENT);
//		connection.setDoOutput(doOutput);
	}
	
	/**
	 * Gets the output.
	 * 
	 * @param connection
	 *            The connection to get the output from
	 * @return The output of the given connection
	 * @since TBukkit 1.0
	 */
	public static String getOutput(URLConnection connection) {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line = reader.readLine();
//			System.out.println(line);
			return line;
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	/**
	 * Test if the given String matches {@link TBukkit#VERSION_REGEX}
	 * 
	 * @param version
	 *            The string to test
	 * @return if the given String match the regex
	 * @see TBukkit#VERSION_REGEX
	 * @since TBukkit 1.0
	 */
	public static boolean validVersionString(String version) {
		return version.matches(VERSION_REGEX);
	}
	
	/**
	 * Assuming that str matches {NAME}v{VERSION}, and that {NAME} is any number
	 * of character, and that {VERSION} matches {@link TBukkit#VERSION_REGEX},
	 * this method returns only the {VERSION} portion of the given string.
	 * 
	 * @param str
	 * @return a version string if found, null otherwise
	 * @since TBukkit 1.0
	 */
	public static String extractVersion(String str) {
		String version = str.replaceAll(VERSION_CONVERT_REGEX, "");
		if (!version.matches(VERSION_REGEX))
			return null;
		return version;
	}
	
	/**
	 * Test two Strings and return the latest version, much like how Math.max()
	 * works. This is the same as calling compareTo() on the strings<br/>
	 * <br/>
	 * Both strings must match {@link #VERSION_REGEX}, otherwise an
	 * {@link IllegalArgumentException} is thrown<br/>
	 * Examples:
	 * <ul>
	 * <li>5</li>
	 * <li>1.0</li>
	 * <li>1.7.0</li>
	 * <li>38.0.2125.104</li>
	 * </ul>
	 * 
	 * @param v1
	 *            The version string to match against v2
	 * @param v2
	 *            The version string to match against v1
	 * @return v1 if both Strings match, otherwise whichever String is
	 *         determined to be the 'latest' version
	 * @throws IllegalArgumentException
	 *             if one of the Strings does not match {@link #VERSION_REGEX}
	 * @see TBukkit#VERSION_REGEX
	 * @since TBukkit 1.0
	 */
	public static String latestVersion(String v1, String v2) {
		if (validVersionString(v1))
			throw new IllegalArgumentException("v1 (" + v1 + ") does not match the version regex " + VERSION_REGEX);
		if (validVersionString(v2))
			throw new IllegalArgumentException("v2 (" + v2 + ") does not match the version regex " + VERSION_REGEX);
		int result = v1.compareTo(v2);
		if (result <= 0)
			return v1;
		else
			return v2;
	}
	
	static void say(String message) {
		System.out.println(NAME + ": " + message);
	}
	
}
