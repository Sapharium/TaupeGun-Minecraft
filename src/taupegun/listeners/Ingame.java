package taupegun.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import taupegun.start.TaupeGunPlugin;

public class Ingame implements Listener{

	private TaupeGunPlugin plugin = null;
	
	public Ingame(TaupeGunPlugin newPlugin){
		this.plugin = newPlugin;
	}
	
	/**
	 * Trigger when a player died
	 * @param ev
	 */
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent ev)
	{

	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent ev)
	{
			
	}
	
}
