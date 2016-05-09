package taupegun.listeners;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import taupegun.start.TaupeGunPlugin;
import taupegun.structures.Team;

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
		Player player = ev.getEntity();
		
		for(Player p : plugin.getServer().getOnlinePlayers())
		{
			p.playSound(p.getLocation(), Sound.AMBIENCE_THUNDER, 1.0F, 1.0F);
		}
		
		Team team = plugin.getContext().getTeamOfPlayer(player);
		
		ev.setDeathMessage(team.getColor()+player.getName()+ChatColor.GRAY+" has been killed");
		
		player.setGameMode(GameMode.SPECTATOR);
		plugin.getContext().removePlayerFromATeam(player);
		plugin.getContext().getAllPlayers().remove(player);
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent ev)
	{
		
		if (plugin.getContext().isInvincible()){
			ev.setCancelled(true);
		}
		
	}
	
}
