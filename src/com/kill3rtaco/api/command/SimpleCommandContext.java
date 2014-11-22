package com.kill3rtaco.api.command;

public class SimpleCommandContext extends CommandContext {
	
	public SimpleCommandContext(String label, String name, String[] args) {
		super(label, name, args);
	}
	
	@Override
	public void printMessage(String message) {
		System.out.println(message);
	}
	
	@Override
	protected void addMessages() {
		
	}
	
	@Override
	public void printError(String message) {
		System.err.println(message);
	}
	
}
