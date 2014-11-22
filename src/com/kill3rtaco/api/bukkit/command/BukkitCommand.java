package com.kill3rtaco.api.bukkit.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Applied to a method to a method that should be considered as a Bukkit
 * command. <br/>
 * <br/>
 * Values:
 * <ul>
 * <li><b>async</b> - Set to true if the command should be wrapped in a
 * BukkitRunnable and run asynchronously (Default: false). The plugin variable
 * of the call will be set correctly</li>
 * <li><b>console</b> - Set to true if this command should be allowed to run
 * from the console (Default: false)</li>
 * <li><b>commandBlock</b> - Set to true if this command should be allowed to
 * run from a CommandBlock (Default: false)</li>
 * <li><b>invisible</b> - Set to true if this command should not be shown in
 * help listing or in tab completion</li>
 * <li><b>minecart</b> - Set to true if this command should be allowed to run
 * from a CommandMinecart (Default: false)</li>
 * <li><b>perm</b> - Set this to set the permission thats required for this
 * command to run (Default: empty string)</li>
 * <li><b>player</b> - Set to true if this command should be allowed to be run
 * by a Player (Default: true)</li>
 * </ul>
 * 
 * @author KILL3RTACO
 * @since TacoAPI/Bukkit 3.0
 * @see BCommand
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BukkitCommand {
	
	public boolean async() default false;
	
	public boolean console() default false;
	
	public boolean commandBlock() default false;
	
	public boolean invisible() default false;
	
	public boolean minecart() default false;
	
	public String perm() default "";
	
	public boolean player() default true;
	
}
