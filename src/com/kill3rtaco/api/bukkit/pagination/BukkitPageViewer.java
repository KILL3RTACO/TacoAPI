package com.kill3rtaco.api.bukkit.pagination;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import com.kill3rtaco.api.bukkit.util.PrintOptions;
import com.kill3rtaco.api.pagination.PageViewer;
import com.kill3rtaco.api.pagination.Paginator;
import com.kill3rtaco.tacoapi.plugin.TacoAPIPlugin;

/**
 * An implementation of PageViewer suitable for Bukkit plugins
 * 
 * @author KILL3RTACO
 *
 * @since TacoAPI/Bukkit 3.0
 */
public class BukkitPageViewer extends PageViewer {
	
	/**
	 * Similar to PageViewer.DEF_HEADER_FORMAT, but with color added
	 */
	public static final String	DEF_HEADER_FORMAT	= "&b=====[&3%title &9%page&b]=====";
	
	public BukkitPageViewer(Paginator<?> paginator) {
		super(paginator);
	}
	
	public BukkitPageViewer(String title) {
		this(title, DEF_HEADER_FORMAT);
	}
	
	public BukkitPageViewer(String title, String headerFormat) {
		super(title, headerFormat);
	}
	
	public BukkitPageViewer(String title, String headerFormat, String subtitle) {
		super(title, headerFormat, subtitle);
	}
	
	@Override
	public void showPage(int page) {
		showPage(page, Bukkit.getServer().getConsoleSender());
	}
	
	public void showPage(int page, CommandSender to) {
		showPage(page, to, new PrintOptions());
	}
	
	public void showPage(int page, CommandSender to, PrintOptions options) {
		//print header
		print(to, makeHeader(page), options);
		
		//print subtitle if it exists
		if (getSubtitle() != null && !getSubtitle().isEmpty())
			print(to, getSubtitle(), options);
		
		//print page contents
		for (String s : getPage(page))
			print(to, s, options);
		
	}
	
	@Override
	protected void print(String message) {
		print(Bukkit.getServer().getConsoleSender(), message);
	}
	
	protected void print(String message, PrintOptions options) {
		print(Bukkit.getServer().getConsoleSender(), message, options);
	}
	
	protected void print(CommandSender to, String message) {
		print(to, message, new PrintOptions());
	}
	
	protected void print(CommandSender to, String message, PrintOptions options) {
		TacoAPIPlugin.chat.sendMessageTo(to, message, false, options);
	}
	
}
