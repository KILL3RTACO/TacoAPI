package com.kill3rtaco.api.bukkit;

import org.bukkit.command.CommandSender;

/**
 * A class to help with Permissions. The permission checker checks if the player
 * or command sender has overriding parent permissions, see the hasPermission()
 * method for more details.
 * 
 * @author KILL3RTACO
 *
 * @since TacoAPI/Bukkit 3.0
 * @see #hasPermission(CommandSender, String)
 */
public class TPerm {
	
	/**
	 * Checks if a CommandSender has the given permission. If the CommandSender
	 * does not have the permission verbatim, then override permissions are
	 * checked; i.e. when testing the permission
	 * 'ExamplePlugin.example.parent.child', the method checks to see if the
	 * player has any of the following permissions:
	 * <ul>
	 * <li>ExamplePlugin.example.parent.child</li>
	 * <li>ExamplePlugin.example.parent.*</li>
	 * <li>ExamplePlugin.example.*</li>
	 * <li>ExamplePlugin.*</li>
	 * <li>ExamplePlugin</li>
	 * </ul>
	 * If the CommandSender does not have any overriding permissions, then false
	 * is returned.
	 * 
	 * @param sender
	 *            the command sender to check
	 * @param perm
	 *            the permission
	 * @return true if the command sender has the given permission (verbatim) or
	 *         any of its overrides
	 * @since TacoAPI/Bukkit 3.0
	 */
	public static boolean hasPermission(CommandSender sender, String perm) {
		if (perm == null || perm.isEmpty() || sender.hasPermission(perm))
			return true;
		if (perm.contains(".")) {
			return hasOverridePerm(sender, perm);
		}
		return false;
	}
	
	private static String getOverridePerm(String perm) {
		if (perm.matches(".*\\.\\*")) { //Plugin.perm.*
			perm = perm.substring(0, perm.lastIndexOf('.')); //Plugin.perm -> Plugin.*
		}
		if (!perm.contains("."))
			return perm;
		return perm.substring(0, perm.lastIndexOf('.')) + ".*";
	}
	
	private static boolean hasOverridePerm(CommandSender sender, String perm) {
		String overridePerm = getOverridePerm(perm);
		if (sender.hasPermission(overridePerm))
			return true;
		if (overridePerm.contains("."))
			return hasOverridePerm(sender, overridePerm);
		return false;
	}
	
}
