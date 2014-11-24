package com.kill3rtaco.api.config.convert;

import java.util.List;

import com.kill3rtaco.api.config.yml.YamlCollection;
import com.kill3rtaco.api.config.yml.YamlCollectionEntry;
import com.kill3rtaco.api.config.yml.YamlDocument;
import com.kill3rtaco.api.config.yml.YamlNode;
import com.kill3rtaco.api.config.yml.YamlNodeContainer;
import com.kill3rtaco.api.util.json.JSONArray;
import com.kill3rtaco.api.util.json.JSONException;
import com.kill3rtaco.api.util.json.JSONObject;

/**
 * Convert a <code>YamlDocument</code> to a (org.json) <code>JSONObject</code>
 * 
 * @author KILL3RTACO
 *
 * @since TacoAPI/ConfigConvert 1.0
 */
public class YamlToJson {
	
	public static JSONObject convert(YamlDocument document) throws JSONException {
		return convertContainer(document);
	}
	
	private static JSONObject convertContainer(YamlNodeContainer container) throws JSONException {
		JSONObject obj = new JSONObject();
		for (YamlNode n : container) {
			obj.put(n.getName(), convertNode(n));
		}
		return obj;
	}
	
	private static Object convertNode(YamlNode node) throws JSONException {
		if (node == null || !node.isSet())
			return null;
		
		if (node.isSection())
			return convertContainer(node);
		
		if (node.isCollection())
			return convertCollection(node.asCollection());
		
		if (node.isList())
			return convertList(node.asList());
		
		return node.asObject();
	}
	
	private static JSONArray convertCollection(YamlCollection collection) throws JSONException {
		JSONArray arr = new JSONArray();
		for (YamlCollectionEntry e : collection) {
			arr.put(convertContainer(e));
		}
		return arr;
	}
	
	private static JSONArray convertList(List<Object> list) {
		JSONArray arr = new JSONArray();
		for (Object o : list) {
			if (o == null)
				continue;
			
			arr.put(o);
		}
		return arr;
	}
	
}
