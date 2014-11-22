package com.kill3rtaco.api.database;

/**
 * An Exception that was thrown while trying to retrieve data from the results
 * of a query
 * 
 * @author KILL3RTACO
 *
 */
public class DatabaseException extends Exception {
	
	private static final long	serialVersionUID	= -4155286232672183170L;
	private String				message;
	
	public DatabaseException(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
	
}
