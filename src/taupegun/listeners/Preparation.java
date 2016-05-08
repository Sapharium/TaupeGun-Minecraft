package taupegun.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import taupegun.start.TaupeGunPlugin;
import taupegun.structures.Team;

public class Preparation implements Listener{

	private TaupeGunPlugin plugin = null;
	
	public Preparation(TaupeGunPlugin newPlugin){
		this.plugin = newPlugin;
	}
	
	@EventHandler
	public void onPlace(BlockPlaceEvent ev)
	{
		if(!plugin.getContext().hasStarted())
		{
			ev.setCancelled(true);
		}
	}

	@EventHandler
	public void onBreak(BlockBreakEvent ev)
	{
		if(!plugin.getContext().hasStarted())
		{
			ev.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent ev)
	{
		if(!plugin.getContext().hasStarted())
		{
			ev.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onMoveItemInventory(InventoryMoveItemEvent ev){
		
		if(!plugin.getContext().hasStarted())
		{
			ev.setCancelled(true);
		}
		
	}
	
	@EventHandler
	public void OnPlayerMove(PlayerMoveEvent ev){
		
		if(!plugin.getContext().hasStarted())
		{
			Player player = ev.getPlayer();
			
			if(plugin.getContext().isAlreadyInATeam(player)){
				
				Team team = plugin.getContext().getTeamOfPlayer(player);
				
				
				
			}
		}
		
	}
}
