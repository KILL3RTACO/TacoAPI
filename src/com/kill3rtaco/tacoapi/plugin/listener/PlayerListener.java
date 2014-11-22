package com.kill3rtaco.tacoapi.plugin.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.kill3rtaco.api.bukkit.TPlayer;

public class PlayerListener implements Listener {
	
	@EventHandler(ignoreCancelled = true)
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		TPlayer.saveLocation(event.getPlayer().getName(), event.getFrom());
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onGameModeChange(PlayerGameModeChangeEvent event) {
		Player player = event.getPlayer();
		TPlayer.saveGameMode(player.getName(), player.getGameMode());
	}
	
//	@EventHandler
//	public void onPlayerInteract(PlayerInteractEvent event){
//		if(event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock().getType() == Material.SIGN_POST){
//			TacoAPI.getPlayerAPI().savePlayerInventory(event.getPlayer());
//		}else{
//			TacoAPI.getPlayerAPI().restoreInventory(event.getPlayer());
//		}
//	}
	
}
