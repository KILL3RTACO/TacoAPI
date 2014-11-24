package com.kill3rtaco.api.config.yml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public abstract class YamlNodeContainer implements Iterable<YamlNode> {
	
	private List<YamlNode>		_children	= new ArrayList<YamlNode>();
	private YamlDocumentOptions	_options	= new YamlDocumentOptions();
	
	/**
	 * Get the path of this container
	 * 
	 * @return the path
	 * @since TacoAPI/Config 1.0
	 */
	public abstract String getPath();
	
	/**
	 * Get all child nodes of this container
	 * 
	 * @return the child nodes
	 * @since TacoAPI/Config 1.0
	 */
	public List<YamlNode> getNodes() {
		return _children;
	}
	
	public Iterator<YamlNode> iterator() {
		return _children.iterator();
	}
	
	void setOptions(YamlDocumentOptions options) {
		_options = options;
		for (YamlNode n : _children) {
			n.setOptions(options);
		}
	}
	
	YamlDocumentOptions options() {
		return _options;
	}
	
	protected YamlNode addNode(String name) {
		if (name == null || name.isEmpty())
			throw new IllegalArgumentException("name cannot be null or empty");
		YamlNode node = new YamlNode(this, name);
		_children.add(node);
		return node;
	}
	
	/**
	 * Add all nodes within the given container to this container
	 * 
	 * @param container
	 *            the container whose nodes are to be added
	 * @since TacoAPi/Config 1.0
	 */
	public void addNodes(YamlNodeContainer container) {
		addNodes(container.toMap());
	}
	
	/**
	 * Add nodes to this container using the information from a raw map
	 * 
	 * @param map
	 *            the map to use
	 * @since TacoAPI/Config 1.0
	 */
	@SuppressWarnings("unchecked")
	public void addNodes(Map<String, Object> map) {
		for (String s : map.keySet()) {
			YamlNode node = addNode(s);
			Object obj = map.get(s);
			if (obj instanceof Map) {
				Map<String, Object> values = (Map<String, Object>) obj;
				node.addNodes(values);
			} else {
				//if any object is map, make collection
				if (obj instanceof List) {
					List<Object> list = (List<Object>) obj;
					for (Object o : list) {
						if (o instanceof Map) {
							obj = makeCollection(list);
							break;
						}
					}
				}
				node.set(obj);
			}
//			System.out.println(node);
		}
	}
	
	/**
	 * Get a node from this container
	 * 
	 * @param path
	 *            the path to the node
	 * @return the node, or null if the node doesn't exist
	 * @since TacoAPI/Config 1.0
	 */
	public YamlNode getNode(String path) {
		return getNode(path, false);
	}
	
	/**
	 * Get a node from this container, and optionally create it if it doesn't exist
	 * 
	 * @param path
	 *            the path to the node
	 * @param create
	 *            true if the node should be created if it doesn't exist
	 * @return the node
	 * @since TacoAPI/Config 1.0
	 * @throws IllegalArgumentException
	 *             if the path is empty, null, or ends in a '.' Note that if this container is a YamlNode, passing null
	 *             or and empty String as path will result in this node being returned
	 */
	public YamlNode getNode(String path, boolean create) {
		if (path == null || path.isEmpty())
			throw new IllegalArgumentException("path cannot be null or empty"); //overridden for YamlNode
		if (path.endsWith("."))
			throw new IllegalArgumentException("path cannot end in a '.'");
		
		String name;
		if (path.contains(".")) {
			name = path.substring(0, path.indexOf("."));
			path = path.substring(name.length() + 1);
		} else {
			name = path + ""; //clone path
			path = "";
		}
		
		for (YamlNode n : _children) {
			if (n.getName().equals(name)) {
				if (path.isEmpty())
					return n;
				return n.getNode(path, create);
			}
		}
		
		if (create) {
			YamlNode node = addNode(name);
			for (String s : path.split("\\.")) {
				if (s.isEmpty())
					return node;
				node = node.addNode(s);
			}
			return node;
		}
		return null;
	}
	
	protected Boolean makeBool(Object o) {
		if (o instanceof Boolean) {
			return (Boolean) o;
		} else if (o instanceof Number) {
			return ((Number) o).intValue() == 1;
		} else if (o instanceof String) {
			String str = o.toString();
			for (String s : _options.trueSynonyms) {
				if (str.equalsIgnoreCase(s))
					return true;
			}
			return Boolean.valueOf(str);
		} else {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	protected List<Boolean> makeBoolList(Object o) {
		List<Boolean> list = new ArrayList<Boolean>();
		if (o == null) {
			return list;
		} else if (o instanceof List) {
			List<Object> objs = (List<Object>) o;
			for (Object obj : objs) {
				Boolean bool = makeBool(obj);
				if (bool != null)
					list.add(bool);
			}
		} else {
			list.add(makeBool(o));
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	protected YamlCollection makeCollection(List<Object> list) {
		List<Map<String, Object>> collection = new ArrayList<Map<String, Object>>();
		for (Object o : list) {
			if (o instanceof Map) {
				
				Map<String, Object> map = (Map<String, Object>) o;
				collection.add(map);
			}
		}
		return new YamlCollection(collection);
	}
	
	protected Double makeDbl(Object o) {
		if (o instanceof Number) {
			return ((Number) o).doubleValue();
		} else if (o instanceof String) {
			Integer i = makeInt(o);
			if (i != null)
				return i.doubleValue();
			try {
				return Double.parseDouble(o.toString());
			} catch (NumberFormatException e) {
				return null;
			}
		} else {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	protected List<Double> makeDblList(Object o) {
		List<Double> list = new ArrayList<Double>();
		if (o == null) {
			return list;
		} else if (o instanceof List) {
			List<Object> objs = (List<Object>) o;
			for (Object obj : objs) {
				Double dbl = makeDbl(obj);
				if (dbl != null)
					list.add(dbl);
			}
		} else {
			list.add(makeDbl(o));
		}
		return list;
	}
	
	public String toHex(int i) {
		return "0x" + Integer.toString(i, 16).toUpperCase();
	}
	
	protected Integer makeInt(Object o) {
		if (o instanceof Number) {
			return ((Number) o).intValue();
		} else if (o instanceof String) {
			String str = makeStr(o);
			//hexadecimal integer
			if (str.matches("0x(\\+|\\-)?[0-9a-fA-F]+")) {
				return Integer.parseInt(str.toUpperCase().substring(2), 16);
			}
			try {
				return Integer.parseInt(str);
			} catch (NumberFormatException e) {
				return null;
			}
		} else {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	protected List<Integer> makeIntList(Object o) {
		List<Integer> list = new ArrayList<Integer>();
		if (o == null) {
			return list;
		} else if (o instanceof List) {
			List<Object> objs = (List<Object>) o;
			for (Object obj : objs) {
				Integer i = makeInt(obj);
				if (i != null)
					list.add(i);
			}
		} else {
			list.add(makeInt(o));
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	protected List<Object> makeList(Object o) {
		if (o instanceof List)
			return (List<Object>) o;
		else
			return new ArrayList<Object>();
	}
	
	protected String makeStr(Object o) {
		if (o == null) {
			return null;
		} else {
			return o.toString().replace("\\'", "'");
		}
	}
	
	@SuppressWarnings("unchecked")
	protected List<String> makeStrList(Object o) {
		List<String> list = new ArrayList<String>();
		if (o == null) {
			return list;
		} else if (o instanceof List) {
			List<Object> objs = (List<Object>) o;
			for (Object obj : objs) {
				String str = makeStr(obj);
				if (str != null)
					list.add(str);
			}
		} else {
			list.add(makeStr(o));
		}
		return list;
	}
	
	/**
	 * Remove a node (and all its children) from this container
	 * 
	 * @param path
	 *            the path to the node
	 * @since
	 */
	public void removeNode(String path) {
		if (!isSet(path))
			return;
		
		_children.remove(getNode(path));
	}
	
	public void set(String path, Object value) {
		getNode(path, true).set(value);
	}
	
	public void setComment(String path, String comment) {
		if (!isSet(path))
			return;
		
		getNode(path).setComment(comment);
	}
	
	public void setDefault(String path, Object value) {
		if (!isSet(path))
			set(path, value);
	}
	
	public boolean isSet(String path) {
		if (getNode(path) == null)
			return false;
		return getNode(path).isSet();
	}
	
	public boolean isNumber(String path) {
		if (!isSet(path))
			return false;
		return getNode(path).isNumber();
	}
	
	public boolean isSection(String path) {
		if (!isSet(path))
			return false;
		return getNode(path).isSection();
	}
	
	public boolean isList(String path) {
		if (!isSet(path))
			return false;
		return getNode(path).isList();
	}
	
	public boolean isCollection(String path) {
		if (!isSet(path))
			return false;
		return getNode(path).isCollection();
	}
	
	public Object get(String path) {
		if (!isSet(path))
			return null;
		return getNode(path).asObject();
	}
	
	public boolean getBoolean(String path) {
		if (!isSet(path))
			return false;
		return getNode(path).asBoolean();
	}
	
	public boolean getBoolean(String path, boolean def) {
		if (!isSet(path)) {
			if (_options.saveDefaults)
				set(path, def);
			return def;
		}
		return getNode(path).asBoolean();
	}
	
	public List<Boolean> getBooleanList(String path) {
		if (!isSet(path))
			return new ArrayList<Boolean>();
		return getNode(path).asBooleanList();
	}
	
	public List<Boolean> getBooleanList(String path, List<Boolean> def) {
		if (!isSet(path)) {
			if (_options.saveDefaults)
				set(path, def);
			return def;
		}
		return getNode(path).asBooleanList();
	}
	
	public YamlCollection getCollection(String path) {
		if (!isSet(path))
			return null;
		return getNode(path).asCollection();
	}
	
	public YamlCollection getCollection(String path, YamlCollection def) {
		if (!isSet(path)) {
			if (_options.saveDefaults)
				set(path, def);
			return def;
		}
		return getNode(path).asCollection();
	}
	
	public double getDouble(String path) {
		if (!isSet(path))
			return 0;
		return getNode(path).asDouble();
	}
	
	public double getDouble(String path, double def) {
		if (!isSet(path)) {
			if (_options.saveDefaults)
				set(path, def);
			return def;
		}
		return getNode(path).asDouble();
	}
	
	public List<Double> getDoubleList(String path) {
		if (!isSet(path))
			return new ArrayList<Double>();
		return getNode(path).asDoubleList();
	}
	
	public List<Double> getDoubleList(String path, List<Double> def) {
		if (!isSet(path)) {
			if (_options.saveDefaults)
				set(path, def);
			return def;
		}
		return getNode(path).asDoubleList();
	}
	
	public int getInt(String path) {
		if (!isSet(path))
			return 0;
		return getNode(path).asInt();
	}
	
	public int getInt(String path, int def) {
		if (!isSet(path)) {
			if (_options.saveDefaults)
				set(path, def);
			return def;
		}
		return getNode(path).asInt();
	}
	
	public List<Integer> getIntList(String path) {
		if (!isSet(path))
			return new ArrayList<Integer>();
		return getNode(path).asIntList();
	}
	
	public List<Integer> getIntList(String path, List<Integer> def) {
		if (!isSet(path)) {
			if (_options.saveDefaults)
				set(path, def);
			return def;
		}
		return getNode(path).asIntList();
	}
	
	public List<Object> getList(String path) {
		if (!isSet(path))
			return new ArrayList<Object>();
		return getNode(path).asList();
	}
	
	public List<Object> getList(String path, List<Object> def) {
		if (!isSet(path)) {
			if (_options.saveDefaults)
				set(path, def);
			return def;
		}
		return getNode(path).asList();
	}
	
	public String getString(String path) {
		if (!isSet(path))
			return null;
		return getNode(path).asString();
	}
	
	public String getString(String path, String def) {
		if (!isSet(path)) {
			if (_options.saveDefaults)
				set(path, def);
			return def;
		}
		return getNode(path).asString();
	}
	
	public List<String> getStringList(String path) {
		if (!isSet(path))
			return new ArrayList<String>();
		return getNode(path).asStringList();
	}
	
	public List<String> getStringList(String path, List<String> def) {
		if (!isSet(path)) {
			if (_options.saveDefaults)
				set(path, def);
			return def;
		}
		return getNode(path).asStringList();
	}
	
	/**
	 * Convert this container into a map.
	 * 
	 * @return the values of this container as a map
	 * @since TacoAPI/Config 1.0
	 */
	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		for (YamlNode n : this) {
			map.putAll(n.toMap());
		}
		return map;
	}
	
	public String toString() {
		return toMap().toString();
	}
}
