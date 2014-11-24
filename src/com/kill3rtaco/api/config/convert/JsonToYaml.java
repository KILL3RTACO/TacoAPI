package com.kill3rtaco.api.config.convert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kill3rtaco.api.config.yml.YamlDocument;
import com.kill3rtaco.api.util.json.JSONArray;
import com.kill3rtaco.api.util.json.JSONException;
import com.kill3rtaco.api.util.json.JSONObject;

/**
 * Convert a (org.json) JSONObject to a YamlDocument. JSONObjects have the same restrictions as YamlDocuments when they
 * are parsed. i.e. If the first element of an array is an object, that array is considered a YamlCollection, and
 * non-object values are ignored. The reverse is also true: if the first element of an array is not an object, then the
 * array is considered a List&lt;Object&gt;, and object values are ignored
 * 
 * @author KILL3RTACO
 *
 * @since TacoAPI/ConfigConvert 1.0
 */
public class JsonToYaml {
	
	public static YamlDocument convert(JSONObject object) throws JSONException {
		return new YamlDocument(convertContainer(object));
	}
	
	private static Map<String, Object> convertContainer(JSONObject container) throws JSONException {
		Map<String, Object> map = new HashMap<String, Object>();
		
		JSONArray names = container.names();
		for (int i = 0; i < names.length(); i++) {
			String name = names.getString(i);
			Object value = container.get(name);
			
			if (value instanceof JSONObject) {
				map.put(name, convertContainer((JSONObject) value));
			} else if (value instanceof JSONArray) {
				map.put(name, convertList((JSONArray) value));
			} else {
				map.put(name, value);
			}
		}
		
		return map;
	}
	
	//return List<Object> or List<Map<String, Object>> (YamlCollection)
	private static Object convertList(JSONArray arr) throws JSONException {
		if (arr.length() == 0)
			return new ArrayList<Object>();
		
		if (arr.get(0) instanceof JSONObject) {
			List<Map<String, Object>> collection = new ArrayList<Map<String, Object>>();
			for (int i = 0; i < arr.length(); i++) {
				if (!(arr.get(i) instanceof JSONObject))
					continue;
				
				collection.add(convertContainer(arr.getJSONObject(i)));
			}
			return collection;
		} else {
			List<Object> list = new ArrayList<Object>();
			for (int i = 0; i < arr.length(); i++) {
				if (arr.get(i) instanceof JSONObject || arr.get(i) instanceof JSONArray)
					continue;
				
				list.add(arr.get(i));
			}
			return list;
		}
	}
}
