package com.kill3rtaco.api.config.yml;

/**
 * @author KILL3RTACO
 *
 * @since
 */
public class YamlCollectionEntry extends YamlNodeContainer {
	
	private int				_index;
	private YamlCollection	_parent;
	
	protected YamlCollectionEntry(int index, YamlCollection parent) {
		_index = index;
		_parent = parent;
	}
	
	public int getIndex() {
		return _index;
	}
	
	public YamlCollection getParent() {
		return _parent;
	}
	
	public String getPath() {
		String index = "$" + _index;
		YamlNode _collParent = _parent.getParent();
		if (_collParent == null)
			return "$collection" + index;
		return _collParent.getPath() + index;
	}
}
