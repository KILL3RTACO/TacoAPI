package com.kill3rtaco.api.config.validator;

public class ValidatorException extends Exception {
	
	private static final long	serialVersionUID	= -3630507321352364205L;
	private String				_message;
	
	public ValidatorException(String message) {
		_message = message;
	}
	
	public String getMessage() {
		return _message;
	}
	
}
