package com.kill3rtaco.api.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation holding a help string for a command
 * 
 * @author KILL3RTACO
 * @since TacoAPI/Command 1.0
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandHelp {
	
	public String value();
	
}
