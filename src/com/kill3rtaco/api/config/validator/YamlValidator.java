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
			if (!validateNode(n, container.getNode(n.getName())))
				return false;
		}
		return true;
	}
	
	private boolean validateNode(YamlNode schema, YamlNode node) throws ValidatorException {
		if (node == null) {
			if (schema.getBoolean("required", false))
				throw new ValidatorException("Node '" + schema.getName() + "' is required but not found (schema: '" + schema.getPath() + "')");
			return true;
		}
		
		String type = schema.getString("type");
		if (type == null)
			type = "str";
		
		if (type.equalsIgnoreCase("list")) {
			if (!node.isList())
				throw new ValidatorException("Type mismatch: node '" + node.getPath() + "' is not a list");
		} else if (type.equalsIgnoreCase("list/str")) {
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
					throw new ValidatorException("List at '" + node.getPath() + "' contains a non-whitelisted string: '" + str + "'");
				else if (!whitelist && containsIgnoreCase(list, str))
					throw new ValidatorException("List at '" + node.getPath() + "' contains a blacklisted string: '" + str + "'");
			}
		} else if (type.equalsIgnoreCase("list/num")) {
			List<Double> nums = node.asDoubleList();
			
			if (!node.isCollection("ranges"))
				return true;
			
			YamlCollection ranges = node.getCollection("ranges");
			
			for (double n : nums) {
				if (!matchesRanges(n, ranges))
					throw new ValidatorException("Value in list at '" + node.getPath() + "' (" + n + ") does not match any of the given ranges");
			}
			
		} else if (type.equalsIgnoreCase("collection")) {
			if (!node.isCollection())
				throw new ValidatorException("Type mismatch: node '" + node.getPath() + "' is not a collection");
			if (!schema.isSet("collection"))
				return true;
			
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
				throw new ValidatorException("String at '" + node.getPath() + "' is not in whitelist");
			else if (!whitelist && containsIgnoreCase(list, str))
				throw new ValidatorException("String at '" + node.getPath() + "' is in blacklist");
		} else if (type.equalsIgnoreCase("num")) {
			if (!node.isNumber())
				throw new ValidatorException("Type mismatch: node '" + node.getPath() + "' is not a number");
			
			if (!schema.isCollection("ranges"))
				return true;
			
			//must match one range
			YamlCollection ranges = schema.getCollection("ranges");
			double num = node.asDouble();
			if (!matchesRanges(num, ranges))
				throw new ValidatorException("Integer at '" + node.getPath() + "' did not match any of the given ranges");
		} else if (type.equalsIgnoreCase("map")) {
			if (!node.isSection())
				throw new ValidatorException("Node '" + node.getPath() + "' is not a map/section");
			
			if (!schema.isSet("map"))
				return true;
			
			return validateContainer(schema.getNode("map"), node);
		} else {
			throw new ValidatorException("Unknown type '" + type + "' (schema: '" + schema.getPath() + "')");
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
