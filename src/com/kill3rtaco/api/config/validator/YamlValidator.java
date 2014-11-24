package com.kill3rtaco.api.config.validator;

import java.io.File;
import java.util.List;

import com.kill3rtaco.api.config.yml.YamlCollection;
import com.kill3rtaco.api.config.yml.YamlCollectionEntry;
import com.kill3rtaco.api.config.yml.YamlDocument;
import com.kill3rtaco.api.config.yml.YamlNode;
import com.kill3rtaco.api.config.yml.YamlNodeContainer;

/**
 * @author KILL3RTACO
 *
 * @since
 */
public class YamlValidator {
	
	private YamlNodeContainer	_schema, _document;
	
	public YamlValidator(YamlNodeContainer schema, YamlNodeContainer document) {
		_schema = schema;
		_document = document;
	}
	
	public YamlNodeContainer getSchema() {
		return _schema;
	}
	
	public YamlNodeContainer getDocument() {
		return _document;
	}
	
	public boolean validate() throws ValidatorException {
		return validateContainer(_schema, _document);
	}
	
	private boolean validateContainer(YamlNodeContainer schema, YamlNodeContainer container) throws ValidatorException {
		for (YamlNode n : schema) {
			if (!validateNode(container, n, container.getNode(n.getName()), container.getPath() + "." + n.getName()))
				return false;
		}
		return true;
	}
	
	private boolean validateNode(YamlNodeContainer container, YamlNode schema, YamlNode node, String path) throws ValidatorException {
		boolean required = schema.getBoolean("required", false);
		if (node == null) {
			if (required)
				throw new ValidatorException("Node '" + path + "' is required but not found", path, schema.getPath());
		}
		
		String type = schema.getString("type");
		if (type == null)
			type = "str";
		
		if (type.equalsIgnoreCase("list")) {
			if (node == null && schema.isSet("default")) {
				YamlNode def = schema.getNode("default");
				if (!def.isList())
					throw new ValidatorException("Type mismatch: default for node '" + path + "' is not a list", path, schema.getPath());
				
				container.getNode(path, true).set(def.asList());
			}
			
			if (!node.isList())
				throw new ValidatorException("Type mismatch: node '" + node.getPath() + "' is not a list", path, schema.getPath());
		} else if (type.equalsIgnoreCase("list/str")) {
			if (node == null && schema.isSet("default")) {
				YamlNode def = schema.getNode("default");
				if (!def.isList())
					throw new ValidatorException("Type mismatch: default value for node '" + path + "' is not a list", path, schema.getPath());
				
				container.getNode(path, true).set(def.asStringList());
				return true;
			}
			
			boolean whitelist;
			List<String> list;
			if (schema.isList("whitelist")) {
				whitelist = true;
				list = schema.getStringList("whitelist");
			} else if (schema.isList("blacklist")) {
				whitelist = false;
				list = schema.getStringList("blacklist");
			} else {
				return true;
			}
			
			List<String> strList = node.asStringList();
			for (String str : strList) {
				if (whitelist && !containsIgnoreCase(list, str))
					throw new ValidatorException("List at '" + node.getPath() + "' contains a non-whitelisted string: '" + str + "'", path, schema.getPath());
				else if (!whitelist && containsIgnoreCase(list, str))
					throw new ValidatorException("List at '" + node.getPath() + "' contains a blacklisted string: '" + str + "'", path, schema.getPath());
			}
		} else if (type.equalsIgnoreCase("list/num")) {
			if (node == null && schema.isSet("default")) {
				YamlNode def = schema.getNode("default");
				if (!def.isList())
					throw new ValidatorException("Type mismatch: default value for node '" + path + "' is not a list", path, schema.getPath());
				
				container.getNode(path, true).set(def.asDoubleList());
				return true;
			}
			
			List<Double> nums = node.asDoubleList();
			
			if (!node.isCollection("ranges"))
				return true;
			
			YamlCollection ranges = node.getCollection("ranges");
			
			for (double n : nums) {
				if (!matchesRanges(n, ranges))
					throw new ValidatorException("Value in list at '" + node.getPath() + "' (" + n + ") does not match any of the given ranges", path, schema.getPath());
			}
			
		} else if (type.equalsIgnoreCase("collection")) {
			if (node == null && schema.isSet("default")) {
				YamlNode def = schema.getNode("default");
				if (!def.isCollection())
					throw new ValidatorException("Type mismtach: default value for node '" + path + "' is not a collection", path, schema.getPath());
				return true;
			}
			if (!node.isCollection())
				throw new ValidatorException("Type mismatch: node '" + node.getPath() + "' is not a collection", path, schema.getPath());
			
			return validateCollection(schema.getNode("collection"), node.asCollection());
		} else if (type.equalsIgnoreCase("str")) {
			boolean whitelist;
			List<String> list;
			if (schema.isList("whitelist")) {
				whitelist = true;
				list = schema.getStringList("whitelist");
			} else if (schema.isList("blacklist")) {
				whitelist = false;
				list = schema.getStringList("blacklist");
			} else {
				return true;
			}
			
			String str = node.asString();
			if (whitelist && !containsIgnoreCase(list, str))
				throw new ValidatorException("String at '" + node.getPath() + "' is not in whitelist", path, schema.getPath());
			else if (!whitelist && containsIgnoreCase(list, str))
				throw new ValidatorException("String at '" + node.getPath() + "' is in blacklist", path, schema.getPath());
		} else if (type.equalsIgnoreCase("num")) {
			if (!node.isNumber())
				throw new ValidatorException("Type mismatch: node '" + node.getPath() + "' is not a number", path, schema.getPath());
			
			if (!schema.isCollection("ranges"))
				return true;
			
			//must match one range
			YamlCollection ranges = schema.getCollection("ranges");
			double num = node.asDouble();
			if (!matchesRanges(num, ranges))
				throw new ValidatorException("Integer at '" + node.getPath() + "' did not match any of the given ranges", path, schema.getPath());
		} else if (type.equalsIgnoreCase("map")) {
			if (!node.isSection())
				throw new ValidatorException("Node '" + node.getPath() + "' is not a map/section", path, schema.getPath());
			
			if (!schema.isSet("map"))
				return true;
			
			return validateContainer(schema.getNode("map"), node);
		} else {
			throw new ValidatorException("Unknown type '" + type + "'", path, schema.getPath());
		}
		
		return true;
	}
	
	private boolean validateCollection(YamlNode schema, YamlCollection collection) throws ValidatorException {
		for (YamlCollectionEntry e : collection) {
			if (!validateContainer(schema, e))
				return false;
		}
		return true;
	}
	
	private boolean containsIgnoreCase(List<String> list, String str) {
		for (String s : list) {
			if (s.matches("^/.*/$")) {
				String regex = s.substring(1, s.length() - 1);
				if (str.matches(regex))
					return true;
			}
			
			if (s.equalsIgnoreCase(str))
				return true;
		}
		return false;
	}
	
	private boolean matchesRanges(double num, YamlCollection ranges) {
		for (YamlCollectionEntry e : ranges) {
			if (e.isNumber("min") && num < e.getDouble("min"))
				continue;
			
			if (e.isNumber("max") && num > e.getDouble("max"))
				continue;
			
			return true; //matched a range
		}
		return false;
	}
	
	public static void main(String[] args) {
		YamlDocument doc = new YamlDocument(new File("/home/taco/Documents/Programming/yaml_test.yml"));
		YamlDocument schema = new YamlDocument(new File("/home/taco/Documents/Programming/yaml_test_validate.yml"));
		
		try {
			YamlValidator validator = new YamlValidator(schema, doc);
			validator.validate();
			System.out.println("Document is valid");
		} catch (ValidatorException e) {
			e.printStackTrace();
		}
	}
}
