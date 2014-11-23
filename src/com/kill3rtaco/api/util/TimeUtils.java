package com.kill3rtaco.api.util;

import java.util.Calendar;
import java.util.Locale;

public class TimeUtils {
	
	public enum Time {
		
		SECOND(1),
		MINUTE(SECOND.getSeconds() * 60),
		HOUR(MINUTE.getSeconds() * 60),
		DAY(HOUR.getSeconds() * 24),
		YEAR(DAY.getSeconds() * 365);
		
		private int					seconds;
		public static final long	MINECRAFT_SECOND_TICKS	= 20L;
		public static final long	MILLISECONDS_IN_SECOND	= 1000L;
		
		private Time(int seconds) {
			this.seconds = seconds;
		}
		
		public int getSeconds() {
			return seconds;
			
		}
		
	}
	
	/**
	 * Get future time.
	 * 
	 * For instance, to get the time it would be 3 hours from now:<br/>
	 * 
	 * <pre>
	 * futureTime(Time.HOUR, 3)
	 * </pre>
	 * 
	 * @param base
	 *            the base
	 * @param modifier
	 *            the modifier
	 * @return future time
	 */
	public static long futureTime(Time base, int modifier) {
		long now = System.currentTimeMillis();
		long add = Time.MILLISECONDS_IN_SECOND * base.getSeconds() * modifier;
		return now + add;
	}
	
	/**
	 * @deprecated use multiple calls of futureTime(Time, int)
	 */
	public static long futureTimeComplex(int[] modifiers, Time... bases) {
		long now = System.currentTimeMillis();
		if (modifiers.length != bases.length) {
			throw new IllegalArgumentException("Length of modifiers (" + modifiers.length + ") does not match length of bases (" + bases.length + ")");
		}
		if (modifiers.length == 0)
			return now;
		long add = 0L;
		for (int i = 0; i < modifiers.length; i++) {
			long t = modifiers[i] * bases[i].getSeconds() * Time.MILLISECONDS_IN_SECOND;
			add += t;
		}
		return now + add;
	}
	
	/**
	 * Get a future time string, in the form of {years}y {days}d {hours}h
	 * {minutes}m {seconds}s
	 * 
	 * @param base
	 *            The base unit of time
	 * @param modifier
	 *            how many of the bases to use
	 * @return the time string
	 * @since TacoAPI/Bukkit 2.0
	 */
	public static String futureTimeAsString(Time base, int modifier) {
		return futureTimeAsString(futureTime(base, modifier));
	}
	
	/**
	 * Get a future time string, in the form of {years}y {days}d {hours}h
	 * {minutes}m {seconds}s
	 * 
	 * @param time
	 *            the amount of time ahead of the current time
	 * @return the time string
	 * @since TacoAPI/Bukkit 2.0
	 */
	public static String futureTimeAsString(long time) {
		return timeAsString(time, true);
	}
	
	/**
	 * Get past time
	 * 
	 * @param base
	 *            the base amount of time to use
	 * @param modifier
	 *            how many bases to use
	 * @return the time
	 * @since TacoAPI/Bukkit 2.0
	 */
	public static long pastTime(Time base, int modifier) {
		long now = System.currentTimeMillis();
		long sub = base.getSeconds() * modifier * Time.MILLISECONDS_IN_SECOND;
		return now - sub;
	}
	
	/**
	 * Get a past time string, in the form of {years}y {days}d {hours}h
	 * {minutes}m {seconds}s
	 * 
	 * @param base
	 *            The time base to use
	 * @param modifier
	 *            how many of the bases to use
	 * @return the time string
	 * @since TacoAPI/Bukkit 2.0
	 */
	public static String pastTimeAsString(Time base, int modifier) {
		return pastTimeAsString(pastTime(base, modifier));
	}
	
	/**
	 * Get a past time string, in the form of {years}y {days}d {hours}h
	 * {minutes}m {seconds}s
	 * 
	 * @param time
	 *            the amount of time before the current time
	 * @return the time string
	 * @since TacoAPI/Bukkit 2.0
	 */
	public static String pastTimeAsString(long time) {
		return timeAsString(time, false);
	}
	
	/**
	 * Get a time string, in the form of {years}y {days}d {hours}h {minutes}m
	 * {seconds}s
	 * 
	 * @param time
	 *            the time ahead or before the current time (depends on the
	 *            value of future)
	 * @param future
	 *            true if the amount is ahead of the current time
	 * @return the time string
	 * @since TacoAPI/Bukkit 2.0
	 */
	private static String timeAsString(long time, boolean future) {
		long given = time;
		long now = System.currentTimeMillis();
		if (future)
			time -= now;
		else
			time = now - time;
		if (future && time < 0) {
			throw new IllegalArgumentException("Given time (" + given + ") must be after current time (" + now + ")");
		} else if (!future && time < 0) {
			throw new IllegalArgumentException("Given time (" + given + ") must be before current time (" + now + ")");
		}
		long second = Time.MILLISECONDS_IN_SECOND;
		long minute = second * Time.MINUTE.getSeconds();
		long hour = second * Time.HOUR.getSeconds();
		long day = second * Time.DAY.getSeconds();
		long year = second * Time.YEAR.getSeconds();
		
		long years = time / year;
		long days = time % year / day;
		long hours = time % year % day / hour;
		long minutes = time % year % day % hour / minute;
		long seconds = time % year % day % hour % minute / second;
		
		String t = "";
		if (years > 0)
			t += years + "y ";
		if (days > 0)
			t += days + "d ";
		if (hours > 0)
			t += hours + "h ";
		if (minutes > 0)
			t += minutes + "m ";
		if (seconds > 0)
			t += seconds + "s";
		return t.trim();
	}
	
	/**
	 * Get a friendly date, in the form of {WEEKDAY}, {MONTH_NAME}
	 * {DAY_OF_MONTH}, {YEAR} - {HOUR}:{MINUTE} {AM/PM}
	 * 
	 * @param time
	 *            the time
	 * @param hour12
	 *            whether 12 hour clock should be used. If true, '13:37' will be
	 *            displayed as '1:37'
	 * @param shortDisplay
	 *            whether short names for the weekday and month should be used
	 * @return the friendly date string
	 * @since TacoAPI/Bukkit 2.0
	 */
	public static String getFriendlyDate(long time, boolean hour12, boolean shortDisplay) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		String weekday, month, ap;
		int day, year, hour, min;
		int style = shortDisplay ? Calendar.SHORT : Calendar.LONG;
		Locale locale = Locale.getDefault();
		month = calendar.getDisplayName(Calendar.MONTH, style, locale);
		day = calendar.get(Calendar.DAY_OF_MONTH);
		weekday = calendar.getDisplayName(Calendar.DAY_OF_WEEK, style, locale);
		year = calendar.get(Calendar.YEAR);
		hour = calendar.get(Calendar.HOUR_OF_DAY);
		if (hour12 && hour > 12)
			hour -= 12;
		else if (hour12 && hour == 0)
			hour = 12;
		min = calendar.get(Calendar.MINUTE);
		ap = (hour12 ? calendar.getDisplayName(Calendar.AM_PM, style, locale) : "");
		
		return weekday + ", " + month + " " + day + ", " + year + " - " + hour + ":" + min + ap;
	}
	
}
