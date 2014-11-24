package com.kill3rtaco.api.config.yml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Represents a Collection. A Collection translates to List&lt;Map&lt;String, Object&gt;&gt; in Java, but since that is
 * hard to manage, this class was created to represent it.
 * 
 * @author KILL3RTACO
 *
 * @since TacoAPI/Config 1.0
 */
public class YamlCollection implements Iterable<YamlCollectionEntry> {
	
	private YamlNode					_parent;
	private List<YamlCollectionEntry>	_collection;
	
	public YamlCollection() {
		_collection = new ArrayList<YamlCollectionEntry>();
	}
	
	public YamlCollection(List<Map<String, Object>> mapList) {
		this();
		for (Map<String, Object> m : mapList) {
			YamlNodeContainer c = newEntry();
			c.addNodes(m);
		}
	}
	
	void setParent(YamlNode parent) {
		_parent = parent;
	}
	
	public YamlNode getParent() {
		return _parent;
	}
	
	/**
	 * Add a new entry to this collection
	 * 
	 * @return the new entry
	 * @since TacoAPI/Config 1.0
	 */
	public YamlCollectionEntry newEntry() {
		YamlCollectionEntry d = new YamlCollectionEntry(_collection.size(), this);
		_collection.add(d);
		return d;
	}
	
	/**
	 * Get a specific entry in this collection
	 * 
	 * @param index
	 *            the index of the entry
	 * @return the entry
	 * @since TacoAPI/Config 1.0
	 */
	public YamlNodeContainer get(int index) {
		return _collection.get(index);
	}
	
	/**
	 * Get the size of this collection
	 * 
	 * @return the size of this collection
	 * @since TacoAPI/Config 1.0
	 */
	public int size() {
		return _collection.size();
	}
	
	/**
	 * Returns true if this collection is empty
	 * 
	 * @return true if this collection is empty
	 * @since TacoAPI/Config 1.0
	 */
	public boolean isEmpty() {
		return _collection.isEmpty();
	}
	
	/**
	 * Converts this collection into a list of maps
	 * 
	 * @return a map list
	 * @since TacoAPI/Config 1.0
	 */
	public List<Map<String, Object>> toMapList() {
		List<Map<String, Object>> friendly = new ArrayList<Map<String, Object>>();
		for (YamlNodeContainer c : _collection) {
			Map<String, Object> map = new HashMap<String, Object>();
			for (YamlNode n : c) {
				map.putAll(n.toMap());
			}
			friendly.add(map);
		}
		return friendly;
	}
	
	/**
	 * Converts this collection into a general list
	 * 
	 * @return a list
	 * @since TacoAPI/Config 1.0
	 */
	public List<Object> toList() {
		List<Object> list = new ArrayList<Object>();
		for (Map<String, Object> m : toMapList()) {
			list.add(m);
		}
		return list;
	}
	
	@Override
	public String toString() {
		return toMapList().toString();
	}
	
	@Override
	public Iterator<YamlCollectionEntry> iterator() {
		return _collection.iterator();
	}
	
}
