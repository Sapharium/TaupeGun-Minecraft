package taupegun.listeners;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Wool;

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
	
}
