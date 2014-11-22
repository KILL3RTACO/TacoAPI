package com.kill3rtaco.api.bukkit.command.tab;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.kill3rtaco.api.bukkit.command.BukkitCommand;

/**
 * Apply this annotation to a method to indicate that the method should be
 * registered as a method that returns a list to be used in tab completion<br/>
 * <br/>
 * The method that this annotation is applied to must have a return type of
 * List&lt;String&gt; and only take one String parameter. A valid example is as
 * follows:
 * 
 * <pre>
 * {@literal @TabCompletionList("id")}
 * public List&lt;String&gt; list(String alias){
 *     //....
 * }
 * </pre>
 * 
 * @author KILL3RTACO
 * @since TacoAPI/Bukkit 3.0
 * @see BukkitCommand
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TabCompletionList {
	
	//the id of the list
	public String value();
	
}
