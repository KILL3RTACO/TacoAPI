package com.kill3rtaco.api.util;

import java.util.ArrayList;

/**
 * Essentially a HashMap, but ordered by the elements at the time they were
 * added
 * 
 * @author KILL3RTACO
 *
 * @since TacoAPI 3
 */
public class DoubleArrayList<K, V> {
	
	private ArrayList<K>	_keys;
	private ArrayList<V>	_values;
	
	/**
	 * Create a new, exmpty, DoubleArrayList
	 * 
	 * @since
	 */
	public DoubleArrayList() {
		this(new ArrayList<K>(), new ArrayList<V>());
	}
	
	/**
	 * Create a new DoubleArrayList and add the values. Any key without a
	 * corresponding value will be assigned null as its value
	 * 
	 * @param keys
	 *            the keys to add
	 * @param values
	 *            the values, corresponding to the keys.
	 * @since
	 */
	public DoubleArrayList(ArrayList<K> keys, ArrayList<V> values) {
		_keys = keys;
		_values = values;
	}
	
	/**
	 * Convenience method, shortcut for {@link #getKeys()}.indexOf(key)
	 * 
	 * @param key
	 *            the key
	 * @return the index of the key, or -1 if there is no such key
	 * @since TacoAPI 3
	 */
	public int indexOf(K key) {
		return _keys.indexOf(key);
	}
	
	public V put(K key, V value) {
		int index = indexOf(key);
		if (index < 0) {
			_keys.add(key);
			_values.add(value);
			return null;
		} else {
			V prevValue = get(key);
			_keys.set(index, key);
			_values.set(index, value);
			return prevValue;
		}
	}
	
	public void putAll(DoubleArrayList<K, V> list) {
		putAll(list.getKeys(), list.getValues());
	}
	
	public void putAll(ArrayList<K> keys, ArrayList<V> values) {
//		if(keys.size() != values.size())
//			throw new IllegalArgumentException("Both lists do not have the same size");
		for (int i = 0; i < keys.size(); i++) {
			K key = keys.get(i);
			V value;
			if (values.isEmpty() || i == values.size())
				value = null;
			else
				value = values.get(i);
			
			put(key, value);
		}
	}
	
	public V get(K key) {
		int index = indexOf(key);
		if (index < 0)
			return null;
		return _values.get(index);
	}
	
	public ArrayList<K> getKeys() {
		return _keys;
	}
	
	public ArrayList<V> getValues() {
		return _values;
	}
	
	public int size() {
		return _keys.size();
	}
	
	public boolean isEmpty() {
		return _keys.isEmpty();
	}
	
	public boolean containsKey(K key) {
		return indexOf(key) > -1;
	}
	
	public boolean containsValue(V value) {
		for (V v : _values) {
			if ((v == null && value == null) || v.equals(v))
				return true;
		}
		return false;
	}
	
	public V remove(K key) {
		int index = indexOf(key);
		if (index < 0)
			return null;
		_keys.remove(index);
		return _values.remove(index);
	}
	
	public void clear() {
		_keys.clear();
		_values.clear();
	}
	
}
