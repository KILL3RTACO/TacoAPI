package com.kill3rtaco.api.config.validator;

/**
 * An exception that is thrown when the validator encounters a problem
 * 
 * @author KILL3RTACO
 *
 * @since TacoAPI/YamlValidate 1.0
 */
public class ValidatorException extends Exception {
	
	private static final long	serialVersionUID	= -3630507321352364205L;
	private String				_message, _nodePath, _schemaPath;
	
	public ValidatorException(String message, String nodePath, String schemaPath) {
		_message = message;
		_nodePath = nodePath;
		_schemaPath = schemaPath;
	}
	
	public String getMessage() {
		return _message;
	}
	
	/**
	 * Get the path of the node being checked (or the node that is missing)
	 * 
	 * @return the path of the missing or checked node
	 * @since TacoAPI/YamlValidate 1.0
	 */
	public String getNodePath() {
		return _nodePath;
	}
	
	/**
	 * Get the matching schema path for the node being checked
	 * 
	 * @return
	 * @since
	 */
	public String getSchemaPath() {
		return _schemaPath;
	}
}
