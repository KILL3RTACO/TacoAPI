package com.kill3rtaco.api.bukkit.dev;

import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.Date;

import com.kill3rtaco.api.util.json.JSONArray;
import com.kill3rtaco.api.util.json.JSONException;
import com.kill3rtaco.api.util.json.JSONObject;

/**
 * Represents a BukkitDev game-version found at
 * http://dev.bukkit.org/game-versions.json<br/>
 * <br/>
 * Also holds all GameVersions, available via {@link #versions()} (copy)
 * 
 * @author KILL3RTACO
 * @since TBukkit 1.0
 *
 */
public class GameVersion implements Cloneable {
	
	private static GameVersion[]	VERSIONS;
	
	private int						_id;
	private String					_name, _internalId, _releaseDateString;
	private Calendar				_releaseDate;
	private boolean					_isDevelopment, _breaksCompatibility;
	private JSONObject				_meta;
	
	static {
		try {
			URLConnection conn = new URL("http://dev.bukkit.org/game-versions.json").openConnection();
			
			//An API key is not needed for this
//			TBukkit.prepConnection(conn, false); 
			
			JSONObject versions = new JSONObject(TBukkit.getOutput(conn));
			VERSIONS = new GameVersion[versions.length()];
			JSONArray keys = versions.names();
			for (int i = 0; i < keys.length(); i++) {
				String id = keys.getString(i);
				VERSIONS[i] = new GameVersion(Integer.parseInt(id), versions.getJSONObject(id));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Construct a new GameVersion object with the given id and information
	 * 
	 * @param id
	 *            The ID of the game version
	 * @param dto
	 *            The information to extract
	 * @since TBukkit 1.0
	 */
	public GameVersion(int id, JSONObject dto) {
		try {
			_id = id;
			_meta = dto;
			_isDevelopment = dto.getBoolean("is_development");
			_breaksCompatibility = dto.getBoolean("breaks_compatibility");
			_name = dto.getString("name");
			_internalId = dto.getString("internal_id");
			_releaseDateString = dto.getString("release_date");
			_releaseDate = Calendar.getInstance();
			String[] calParts = _releaseDateString.split("-");
			_releaseDate.set(Calendar.YEAR, Integer.parseInt(calParts[0]));
			_releaseDate.set(Calendar.MONTH, Integer.parseInt(calParts[1]));
			_releaseDate.set(Calendar.DAY_OF_MONTH, Integer.parseInt(calParts[2]));
			_releaseDate.set(Calendar.HOUR_OF_DAY, 0);
			_releaseDate.set(Calendar.MINUTE, 0);
			_releaseDate.set(Calendar.SECOND, 0);
			_releaseDate.set(Calendar.MILLISECOND, 0);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Get the ID of this game version
	 * 
	 * @return The ID
	 * @see #getInternalId()
	 * @since TBukkit 1.0
	 */
	public int getId() {
		return _id;
	}
	
	/**
	 * Does this game version break compatibility? (usually false)
	 * 
	 * @return true if this breaks compatibility
	 * @since TBukkit 1.0
	 */
	public boolean breaksCompatibility() {
		return _breaksCompatibility;
	}
	
	/**
	 * Is this a development build? (usually false, as no dev builds seem to be
	 * put in the game-versions.json file)
	 * 
	 * @return true if this is a development build
	 * @since TBukkit 1.0
	 */
	public boolean isDevelopment() {
		return _isDevelopment;
	}
	
	/**
	 * Get the name of the game version. This usually follows a few different
	 * formats:
	 * <ul>
	 * <li>Beta {VERSION}</li>
	 * <li>{VERSION}</li>
	 * <li>CB {BUILD_NUMBER}</li>
	 * <li>CB {VERSION}</li>
	 * </ul>
	 * 
	 * @return the name
	 * @since TBukkit 1.0
	 * @see #getInternalId()
	 */
	public String getName() {
		return _name;
	}
	
	/**
	 * Get the internal ID of this game version. This is usually the same as
	 * <code>{@link #getName()}.toLowerCase()</code>
	 * 
	 * @return the internal id
	 * @see #getName()
	 * @since TBukkit 1.0
	 */
	public String getInternalId() {
		return _internalId;
	}
	
	/**
	 * Get the release date of this game version as a Calendar object.
	 * 
	 * @return the release date
	 * @see #getReleaseDateString()
	 * @since TBukkit 1.0
	 */
	public Calendar getReleaseDate() {
		return (Calendar) _releaseDate.clone();
	}
	
	/**
	 * Get a String representation of the release date of this game version. The
	 * returned String is in the format YYYY-MM-DD
	 * 
	 * @return A string representation of the release date
	 * @see #getReleaseDate()
	 * @since TBukkit 1.0
	 */
	public String getReleaseDateString() {
		return _releaseDateString;
	}
	
	/**
	 * Get whether this game version was released after the given date
	 * 
	 * @param date
	 *            the date to test
	 * @return true if this was released after the given date
	 * @since TBukkit 1.0
	 */
	public boolean releasedAfter(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return _releaseDate.after(cal);
	}
	
	/**
	 * Get whether this game version was released before the given date
	 * 
	 * @param date
	 *            the date to test
	 * @return true if this was released before the given date
	 * @since TBukkit 1.0
	 */
	public boolean releasedBefore(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return _releaseDate.before(cal);
	}
	
	public GameVersion clone() {
		return new GameVersion(_id, _meta);
	}
	
	public String toString() {
		return "GamVersion{name='" + _name + "'}";
	}
	
	/**
	 * Get a game version from its id
	 * 
	 * @param id
	 *            The id of the game version to get
	 * @return the game version, or null if not found
	 * @since TBukkit 1.0
	 */
	public static GameVersion getFromID(int id) {
		for (GameVersion v : VERSIONS) {
			if (v.getId() == id)
				return v;
		}
		return null;
	}
	
	/**
	 * Get a game version from its name
	 * 
	 * @param name
	 *            The name of the game version to get
	 * @return the game version, or null if not found
	 * @since TBukkit 1.0
	 */
	public static GameVersion getFromName(String name) {
		for (GameVersion v : VERSIONS) {
			if (v.getName().equals(name))
				return v;
		}
		return null;
	}
	
	/**
	 * Get a copy of all available game versions
	 * 
	 * @return an array of game versions
	 * @since TBukkit 1.0
	 */
	public static GameVersion[] versions() {
		GameVersion[] versions = new GameVersion[VERSIONS.length];
		for (int i = 0; i < VERSIONS.length; i++) {
			versions[i] = VERSIONS[i].clone();
		}
		return versions;
	}
	
}
