package taupegun.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

import taupegun.start.TaupeGunPlugin;

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
	
}
