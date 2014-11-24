package com.kill3rtaco.api.config.yml;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a node in YAML (the thing with the ':')
 * 
 * @author KILL3RTACO
 *
 * @since TacoAPI/Config 1.0
 */
public class YamlNode extends YamlNodeContainer {
	
	private Object				_value	= null;
	private String				_name, _comment;
	private YamlNodeContainer	_parent;
	
	protected YamlNode(YamlNodeContainer parent, String name) {
		_parent = parent;
		_name = name;
	}
	
	/**
	 * Get the name of this node
	 * 
	 * @return the name
	 * @since TacoAPI/Config 1.0
	 */
	public String getName() {
		return _name;
	}
	
	/**
	 * Get the parent of this node. Note that if this node is a root node, this will return the same Object as
	 * {@code getRoot()}.
	 * 
	 * @return the parent, which can be another YamlNode or a YamlDocument
	 * @since TacoAPI/Config 1.0
	 */
	public YamlNodeContainer getParent() {
		return _parent;
	}
	
	/**
	 * Get the YamlDocument this node belongs to
	 * 
	 * @return the YamlDocument
	 * @since TacoAPI/Config 1.0
	 */
	public YamlDocument getRoot() {
		if (isRootNode())
			return (YamlDocument) getParent();
		
		return ((YamlNode) getParent()).getRoot();
	}
	
	/**
	 * Returns true if this node is a top-level node
	 * 
	 * @return true if this node is a top-level node
	 * @since TacoAPI/Config 1.0
	 */
	public boolean isRootNode() {
		return getParent() instanceof YamlDocument;
	}
	
	/**
	 * Get the path to this node, starting from the root of the document. Note that if this node is a root node, this
	 * method will return the same result as {@code getName()}.
	 * 
	 * @return the absolute path to the node
	 * @since
	 */
	public String getPath() {
		return getParent().getPath() + (getParent() instanceof YamlDocument ? "" : ".") + getName();
	}
	
	@Override
	public YamlNode getNode(String path, boolean create) {
		if (path == null || path.isEmpty())
			return this;
		return super.getNode(path, create);
	}
	
	/**
	 * Set the comment for this node. The comment will be displayed above the node when the document is saved. Comments
	 * can have multiple lines by using {@code \n}
	 * 
	 * @param comment
	 *            the comment
	 * @since TacoAPI/Config 1.0
	 */
	public void setComment(String comment) {
		_comment = comment;
	}
	
	/**
	 * Get the comment for this node
	 * 
	 * @return the comment
	 * @since TacoAPI/Config 1.0
	 */
	public String getComment() {
		return _comment;
	}
	
	/**
	 * Returns true if a value has been set for this node.
	 * 
	 * @return true if a value has been set for this node
	 * @since TacoAPI/Config 1.0
	 */
	public boolean isSet() {
		return _value != null || !getNodes().isEmpty();
	}
	
	/**
	 * Returns true if this node is a container node, meaning that it stores other nodes
	 * 
	 * @return true if this node contains other nodes
	 * @since TacoAPI/Config 1.0
	 */
	public boolean isSection() {
		return !getNodes().isEmpty();
	}
	
	/**
	 * Returns true if the value of this node can be cast to a number (i.e. int/double)
	 * 
	 * @return true if the value of this node can be converted to a number
	 * @since TacoAPI/Config 1.0
	 */
	public boolean isNumber() {
		return makeInt(asObject()) != null;
	}
	
	/**
	 * Returns true if the value of this node is a YamlCollection
	 * 
	 * @return true if the value of this node is a collection
	 * @since TacoAPI/Config 1.0
	 */
	public boolean isCollection() {
		return asObject() instanceof YamlCollection;
	}
	
	/**
	 * Returns true if the value of this node is a list. It is important to note that any node can be converted to a
	 * list, this method is used to determine whether this node's value is strictly a list.
	 * 
	 * @return true if the value if the value of this node is a list
	 * @since TacoAPI/Config 1.0
	 */
	public boolean isList() {
		return asObject() instanceof List;
	}
	
	/**
	 * Set the value of this node. Note that, in the event that a map is given as a value, the node assumes that the map
	 * is an instance of Map<String, Object>, and adds nodes to itself accordingly
	 * 
	 * @param value
	 *            the new value of this node
	 * @since TacoAPI/Config 1.0
	 */
	@SuppressWarnings("unchecked")
	public void set(Object value) {
		if (value instanceof Map) {
			addNodes((Map<String, Object>) value);
			return;
		} else if (value instanceof YamlCollection) {
			((YamlCollection) value).setParent(this);
		}
		_value = value;
	}
	
	/**
	 * Get the value of this node as an object.
	 * 
	 * @return the value of this node as an Object
	 * @since TacoAPI/Config 1.0
	 */
	public Object asObject() {
		return _value;
	}
	
	/**
	 * Get the value of this node as a list.
	 * 
	 * @return the value of this node as a List
	 * @since TacoAPI/Config 1.0
	 */
	public List<Object> asList() {
		return makeList(asObject());
	}
	
	/**
	 * Get the value of this node as an boolean.
	 * 
	 * @return the value of this node as a boolean
	 * @since TacoAPI/Config 1.0
	 */
	public boolean asBoolean() {
		return makeBool(asObject());
	}
	
	/**
	 * Get the value of this node as a boolean list. Note that even if this node fails {@code isSection()}, its value
	 * will be added to the returned list
	 * 
	 * @return the value of this node as a boolean list
	 * @since TacoAPI/Config 1.0
	 */
	public List<Boolean> asBooleanList() {
		return makeBoolList(asObject());
	}
	
	/**
	 * Get the value of this node as a collection
	 * 
	 * @return this node's value as a collection, or null if the value is not a collection
	 * @since TacoAPI/Config 1.0
	 */
	public YamlCollection asCollection() {
		if (!isCollection())
			return null;
		return (YamlCollection) asObject();
	}
	
	/**
	 * Get the value of this node as an integer.
	 * 
	 * @return The value of this node as an integer, or 0 if the value is not an integer
	 * @since TacoAPI/Config 1.0
	 */
	public int asInt() {
		return makeInt(asObject());
	}
	
	/**
	 * Get the value of this node as an integer list. Note that even if this node fails {@code isSection()}, its value
	 * will be added to the returned list. If the value of this node is already a list, then only those values that pass
	 * the leniency test for an integer are added to the list. Note that the returned list will never be null.
	 * 
	 * @return the value of this node as an integer list
	 * @since TacoAPI
	 */
	public List<Integer> asIntList() {
		return makeIntList(asObject());
	}
	
	/**
	 * Get the value of this node as a double
	 * 
	 * @return the value of this node as a double
	 * @since TacoAPI/Config 1.0
	 */
	public double asDouble() {
		return makeDbl(asObject());
	}
	
	/**
	 * Get the value of this node as a double list. Note that even if this node fails {@code isSection()}, its value
	 * will be added to the returned list. If the value of this node is already a list, then only those values that pass
	 * the leniency test for an double are added to the list. Note that the returned list will never be null.
	 * 
	 * @return the value of theis node as a double list
	 * @since TacoAPI/Config 1.0
	 */
	public List<Double> asDoubleList() {
		return makeDblList(asObject());
	}
	
	/**
	 * Get the value of this node as a String. Note that this may not always return the same result as
	 * {@code asObject.toString()}, but it better to use this method instead.
	 * 
	 * @return the value of this node as a String
	 * @since TacoAPI/Config 1.0
	 */
	public String asString() {
		return makeStr(asObject());
	}
	
	/**
	 * Get the value of this node as a string list. Note that even if this node fails {@code isSection()}, its value
	 * will be added to the returned list. If the value of this node is already a list, then the values are converted to
	 * Strings and then added to the list. Note that the returned list will never be null.
	 * 
	 * @return the value of this node as a string list
	 * @since TacoAPI/Config 1.0
	 */
	public List<String> asStringList() {
		return makeStrList(asObject());
	}
	
	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		if (isSection())
			map.put(getName(), super.toMap());
		else if (isCollection())
			map.put(getName(), asCollection().toMapList());
		else
			map.put(getName(), asObject());
		return map;
	}
	
}
