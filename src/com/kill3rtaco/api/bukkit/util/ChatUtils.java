package com.kill3rtaco.api.bukkit.util;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

import org.bukkit.ChatColor;

/**
 * A utility chat specialized for String formatting. The following codes can be
 * used to format text:<br/>
 * <br/>
 * 
 * <pre>
 * 			{@code &a     GREEN}
 * 			{@code &b     AQUA}
 * 			{@code &c     RED}
 * 			{@code &d     LIGHT_PURPLE}
 * 			{@code &e     YELLOW}
 * 			{@code &f     WHITE}
 * 			{@code &0     BLACK}
 * 			{@code &1     DARK_BLUE}
 * 			{@code &2     DARK_GREEN}
 * 			{@code &3     DARK_AQUA}
 * 			{@code &4     DARK_RED}
 * 			{@code &5     DARK_PURPLE}
 * 			{@code &6     GOLD}
 * 			{@code &7     GRAY}
 * 			{@code &8     DARK_GRAY}
 * 			{@code &9     BLUE}
 * 			{@code &k     MAGIC}
 * 			{@code &l     BOLD}
 * 			{@code &m     STRIKETHROUGH}
 * 			{@code &n     UNDERLINE}
 * 			{@code &o     ITALICS}
 * 			{@code &r     RESET}
 * </pre>
 * 
 * @author Taco
 * @since TacoAPI/Bukkit 2.0
 *
 */
public final class ChatUtils {
	
	/**
	 * Creates a header with a default border color of &6
	 * 
	 * @param title
	 *            - The title of the header
	 * @return A formatted header
	 * @since TacoAPI/Bukkit 2.0
	 */
	public static String createHeader(String title) {
		return createHeader('6', title);
	}
	
	/**
	 * Creates a header with a specified border color
	 * 
	 * @param title
	 *            - The title of the header
	 * @return A formatted header
	 * @since TacoAPI/Bukkit 2.0
	 */
	public static String createHeader(char borderColor, String title) {
		return createHeader(title, "&" + borderColor + "=====[&f%title&" + borderColor + "]=====");
	}
	
	/**
	 * Create a header with a specified title and format.
	 * <ul>
	 * <li>%title - repplaced with the title</li>
	 * </ul>
	 * 
	 * @param title
	 *            The title of the header
	 * @param format
	 *            The format of the header
	 * @return The heaer
	 * @since TacoAPI/Bukkit 2.0
	 */
	public static String createHeader(String title, String format) {
		return formatMessage(format.replaceAll("%title", title));
	}
	
	/**
	 * Uses format codes (preceding by {@code &}) to format the given message.
	 * 
	 * @param message
	 *            - The message to format
	 * @return The formatted message
	 * @since TacoAPI/Bukkit 2.0
	 */
	public static String formatMessage(String message) {
		return ChatColor.translateAlternateColorCodes('&', message);
	}
	
	/**
	 * Opposite effect if one where to use {@code formatMessage()}. Instead of
	 * replacing the color code with a {@code ChatColor} object, it removes the
	 * color codes as if by:
	 * 
	 * <pre>
	 * if (s.contains(colorcode))
	 * 	s = s.replaceAll(colorcode, &quot;&quot;);
	 * </pre>
	 * 
	 * @param s
	 *            - The String which to remove the color codes from
	 * @return The same string with text formattting removed
	 * @since TacoAPI/Bukkit 2.0
	 */
	public static String removeColorCodes(String s) {
		//shorter (runtime and typing) than ChatColor.stripColor(formatMessage(s))
		return s.replaceAll("&[0-9a-fk-or]", "");
	}
	
	/**
	 * Tests whether a String is an int.
	 * 
	 * @param s
	 * @return true if the given String can be parsed as an int
	 * @since TacoAPI/Bukkit 3.0
	 */
	public static boolean isInt(String s) {
		try {
			Integer.parseInt(s);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	/**
	 * Test whether a String is a double
	 * 
	 * @param s
	 *            The String to test
	 * @return true if the given String can be parsed as a double
	 * @since TacoAPI/Bukkit 3.0
	 */
	public static boolean isDouble(String s) {
		try {
			Integer.parseInt(s);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	/**
	 * Join all elements of an array with a space
	 * 
	 * @param array
	 *            The array to join
	 * @return A string with all elements of the array joined with a space
	 *         between them
	 * @since TacoAPI/Bukkit 2.0
	 */
	public static String join(String[] array) {
		return join(array, " ");
	}
	
	/**
	 * Join an array with the specified delimiter
	 * 
	 * @param array
	 *            The array to join
	 * @param delimiter
	 *            The delimiter
	 * @return A string with all elements of the array joined with the specified
	 *         delimiter
	 * @since TacoAPI/Bukkit 2.0
	 */
	public static String join(String[] array, String delimiter) {
		String result = "";
		for (int i = 0; i < array.length; i++) {
			result += array[i];
			if (i < array.length - 1)
				result += delimiter;
		}
		return result;
	}
	
	/**
	 * Join all elements of a list with a space
	 * 
	 * @param list
	 *            The list to join
	 * @return A string with all elements of the list joined with a space
	 *         between them
	 * @since TacoAPI/Bukkit 2.0
	 */
	public static String join(ArrayList<String> list) {
		return join(list, " ");
	}
	
	/**
	 * Join all elements of a list with a specified delimiter
	 * 
	 * @param list
	 *            The list to join
	 * @return A string with all elements of the list joined with the specified
	 *         delimiter
	 * @since TacoAPI/Bukkit 2.0
	 */
	public static String join(ArrayList<String> list, String delimiter) {
		String result = "";
		for (int i = 0; i < list.size(); i++) {
			result += list.get(i);
			if (i < list.size() - 1)
				result += delimiter;
		}
		return result;
	}
	
	/**
	 * Removes the specified indices from a String array (everything before
	 * {@code startIndex} is kept). Code is from DeityAPI
	 * 
	 * @param array
	 *            - Array in which the specified indices before
	 *            {@code startIndex} is removed
	 * @param startIndex
	 *            - Any index before this is removed
	 * @return The same array given with the specified indices removed
	 * @since TacoAPI/Bukkit 2.0
	 */
	public static String[] removeArgs(String[] array, int startIndex) {
		if (array.length == 0)
			return array;
		if (array.length < startIndex)
			return new String[0];
		
		String[] newSplit = new String[array.length - startIndex];
		System.arraycopy(array, startIndex, newSplit, 0, array.length - startIndex);
		return newSplit;
	}
	
	/**
	 * Removes the first index in a String array. Code is from DeityAPI
	 * 
	 * @param array
	 *            - The array in which the first index is removed
	 * @return The same array given with the first index removed
	 * @since TacoAPI/Bukkit 2.0
	 */
	public static String[] removeFirstArg(String[] array) {
		return removeArgs(array, 1);
	}
	
	/**
	 * Converts a String to ProperCase.
	 * 
	 * @param s
	 *            the String to be converted
	 * @return the converted String
	 * @since TacoAPI/Bukkit 2.0
	 */
	public static String toProperCase(String s) {
		if (s.isEmpty())
			return "";
		String[] unimportant = new String[]{"a", "an", "and", "but", "is",
				"are", "for", "nor", "of", "or", "so", "the", "to", "yet"};
		String[] split = s.split("\\s+");
		String result = "";
		for (int i = 0; i < split.length; i++) {
			String word = split[i];
			boolean capitalize = true;
			for (String str : unimportant) {
				if (str.equalsIgnoreCase(word)) {
					if (i > 0 && i < split.length - 1) { //middle unimportant word
						capitalize = false;
						break;
					}
				}
			}
			if (capitalize)
				result += capitalize(word) + " ";
			else
				result += word.toLowerCase() + " ";
		}
		return result.trim();
	}
	
	/**
	 * Capitalizae a string
	 * 
	 * @param s
	 *            The string to capitalize
	 * @return The capitalized string
	 * @since TacoAPI/Bukkit 2.0
	 */
	public static String capitalize(String s) {
		if (s.isEmpty())
			return "";
		if (s.length() == 1) {
			return s.toUpperCase();
		} else if (s.length() == 2) {
			String first = (s.charAt(0) + "").toUpperCase();
			String sec = (s.charAt(1) + "").toLowerCase();
			return first + sec;
		} else {
			s = s.toUpperCase();
			return s.charAt(0) + s.substring(1, s.length()).toLowerCase();
		}
	}
	
	/**
	 * Get a random element from a list
	 * 
	 * @param list
	 *            The list
	 * @return A random element from the list
	 * @since TacoAPI/Bukkit 2.0
	 */
	public static <E> E getRandomElement(ArrayList<E> list) {
		Random random = new Random();
		int index = random.nextInt(list.size());
		return list.get(index);
	}
	
	/**
	 * Get a friendly timestamp string, in the form of<br/>
	 * {SHORT_WEEKDAY}, {MONTH_NAME} {DAY_OF_MONTH}, {HOUR_12}:{MINUTE}{AM | PM}
	 * 
	 * @param time
	 *            The time to use
	 * @return A friendly timestamp string
	 * @since TacoAPI/Bukkit 2.0
	 */
	public static String getFriendlyTimestamp(Timestamp time) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time.getTime());
		String weekday = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.ENGLISH);
		String month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int min = calendar.get(Calendar.MINUTE);
		String ampm = (calendar.get(Calendar.AM_PM) == Calendar.AM ? "a" : "p");
		return weekday + ", " + month + " " + day + ", " + (hour > 12 ? hour - 12 : hour) + ":" + (min < 10 ? "0" + min : min) + ampm;
	}
	
	/**
	 * Shortens the string to fit in the specified size with an ellipse "..." at
	 * the end.
	 * 
	 * @param str
	 *            The string to shorten
	 * @param length
	 *            The maximum length
	 * @return the shortened string
	 * @since TacoAPI/Bukkit 2.0
	 */
	public static String maxLength(String str, int maxLenghth) {
		if (str.length() < maxLenghth) {
			return str;
		} else if (maxLenghth > 3) {
			return str.substring(0, maxLenghth - 3) + "...";
		} else {
			throw new IllegalArgumentException("Minimum length of 3 characters.");
		}
	}
	
}
