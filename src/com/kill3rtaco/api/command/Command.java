package com.kill3rtaco.api.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Apply this annotation to a method that will be run when a user (or the
 * program itself) runs a command. Note that this should never be 'extended'
 * (i.e. creating an annotation with the same method signatures and adding your
 * own). Instead, a new annotation should be made that will be processed by the
 * command manager, as well as this annotation.
 * 
 * @author KILL3RTCACO
 * @see ParentCommand
 * @see CommandHelp
 * @since TacoAPI/Command 1.0
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {
	
	public String name();
	
	public String[] aliases() default {};
	
	public String args() default "";
	
	public String desc();
	
}
