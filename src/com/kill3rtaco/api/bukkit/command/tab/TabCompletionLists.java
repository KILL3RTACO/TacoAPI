package com.kill3rtaco.api.bukkit.command.tab;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.kill3rtaco.api.bukkit.command.BCommand;
import com.kill3rtaco.api.bukkit.command.BukkitCommand;

/**
 * Notifies the command handler which tab completion lists to use for the
 * command method this annotation is applied to.<br/>
 * <br/>
 * When specifying the values, each value should take the format {INDEX}:{ID};
 * where {INDEX} is the index of the argument when a command is going to be run,
 * and {ID} is the id of the TabCompletionList. An example of valid values are
 * as follows:
 * 
 * <pre>
 * {@literal @TabCompletionList({"1:" + TacoAPI.TCL_PLAYER_NAMES})}
 * </pre>
 * 
 * When the {@link BCommand} is being constructed, invalid values from this
 * annotation are silently ignored. Invalid values are Strings that meet any of
 * these conditions:
 * <ul>
 * <li>The string is empty</li>
 * <li>The string does not contain a colon (":")</li>
 * <li>The substring before the colon cannot be parsed by Integer.parseInt()</li>
 * <li>The String does not have anything after the colon</li>
 * </ul>
 * 
 * @author KILL3RTACO
 * @since TacoAPI/Bukkit 3.0
 * @see BukkitCommand
 * @see TabCompletionList
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TabCompletionLists {
	
	//index:id
	public String[] value();
	
}
