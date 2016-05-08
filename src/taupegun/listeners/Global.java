package taupegun.listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import taupegun.start.TaupeGunPlugin;
import taupegun.structures.Kit;
import taupegun.structures.Team;

/**
 * Global listeners
 * @author LetMeR00t
 *
 */
public class Global implements Listener{

	/**
	 * Reference to the plugin Object
	 */
	private TaupeGunPlugin plugin = null;
	
	/**
	 * Reference to the ChatInventory object
	 */
	private ChatInventory ci = null;
	
	/**
	 * Default constructor
	 * @param newPlugin	plugin Object
	 */
	public Global(TaupeGunPlugin newPlugin, ChatInventory ci){
		this.plugin = newPlugin;
		this.ci = ci;
	}
	
	@EventHandler
	public void onPlayerInteraction(PlayerInteractEvent ev){
		
		if (!plugin.getContext().hasStarted() && (ev.getAction().equals(Action.RIGHT_CLICK_BLOCK) || ev.getAction().equals(Action.RIGHT_CLICK_AIR))){
			
			Player player = ev.getPlayer();

			// Choose a team
			if (ev.getItem().getType() == Material.SKULL_ITEM && ChatColor.stripColor(ev.getItem().getItemMeta().getDisplayName()).equalsIgnoreCase("Choose a team"))
			{
				ev.setCancelled(true);
				
				Inventory iv = plugin.getServer().createInventory(null, 54, ChatColor.AQUA + "Choose a team");
				
				int slot = 0;
				for (Entry <String,Team> entry : plugin.getContext().getTeams().entrySet())
				{
					Team team = entry.getValue();
					
					ItemStack i = new ItemStack(Material.BEACON);
					ItemMeta im = i.getItemMeta();
					im.setDisplayName(team.getColor() + team.getName());
					List<String> lore = new ArrayList<String>();
					
					for(Player play : team.getPlayers())
					{
						lore.add(team.getColor() + "- " + play.getDisplayName());
					}
					
					im.setLore(lore);
					i.setItemMeta(im);
					
					iv.setItem(slot, i);
					slot++;
				}
				
				player.openInventory(iv);
				return;

			}
			
		}
		
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent ev)
	{
		if(!plugin.getContext().hasStarted()){
		
			Player player = (Player) ev.getWhoClicked();
			
			// Choose a team
			if (ChatColor.stripColor(ev.getInventory().getName()).equalsIgnoreCase("Choose a team"))
			{
				ev.setCancelled(true);
								
				if(ev.getCurrentItem() != null && ev.getCurrentItem().getType() == Material.BEACON)
				{
					player.closeInventory();
					
					Team team = plugin.getContext().getTeam(ev.getCurrentItem().getItemMeta().getDisplayName());
					
					// Check if the player is already in a team
					if (plugin.getContext().isAlreadyInATeam(player)){
						
						// Check if it's the same team
						if (team.getName().equalsIgnoreCase(ChatColor.stripColor(ev.getCurrentItem().getItemMeta().getDisplayName()))){
							return;
						}
						
						// We change the team of the player
						plugin.getContext().changeTeamPlayer(player, team);
						
					}
					else{
						
						// We add the player
						plugin.getContext().addPlayerToATeam(player, team);
						
					}
					
					plugin.getServer().broadcastMessage(player.getName()+" has joined the team "+team.getColor()+team.getName());
					
					return;
				}
			}
			
			// Manage teams
			if (ev.getInventory().getName().contains("- Teams -"))
			{
				ev.setCancelled(true);
				
				if(ev.getCurrentItem() != null && ev.getCurrentItem().getType() == Material.DIAMOND)
				{					
					ci.changeLastState(player, StateChat.CREATE_TEAM_NAME);
					player.closeInventory();
					player.sendMessage(ChatColor.GRAY + "Write the name of the new team");
					
					return;
				}
				
				if(ev.getCurrentItem() != null && ev.getCurrentItem().getType() == Material.BEACON)
				{
					player.closeInventory();
					
					Team team = plugin.getContext().getTeam(ev.getCurrentItem().getItemMeta().getDisplayName());
					
					Inventory iv = plugin.getServer().createInventory(null, 54, team.getColor() + team.getName());
					
					ItemStack cc = new ItemStack(Material.FEATHER);
					ItemMeta ccm = cc.getItemMeta();
					ccm.setDisplayName(ChatColor.AQUA + "Change the team color");
					cc.setItemMeta(ccm);
					iv.setItem(51, cc);
					
					ItemStack re = new ItemStack(Material.ANVIL);
					ItemMeta are = re.getItemMeta();
					are.setDisplayName(ChatColor.AQUA + "Rename the team");
					re.setItemMeta(are);
					iv.setItem(52, re);
					
					ItemStack de = new ItemStack(Material.TNT);
					ItemMeta ade = de.getItemMeta();
					ade.setDisplayName(ChatColor.AQUA + "Remove the team");
					de.setItemMeta(ade);
					iv.setItem(53, de);
					
					player.openInventory(iv);
					return;
				}
			}

			// Manage kits
			if (ev.getCurrentItem() != null && ev.getInventory().getName().contains("- Kits -"))
			{
				ev.setCancelled(true);

				if (ev.getCurrentItem() != null){
					if(ev.getCurrentItem().getType() == Material.DIAMOND)
					{					
						ci.changeLastState(player, StateChat.CREATE_KIT_NAME);
						player.closeInventory();
						player.sendMessage(ChatColor.GRAY + "Write the name of the new kit.");
						
						return;
					}
	
					if(ev.getCurrentItem().getType() == Material.CHEST)
					{
						player.closeInventory();
						
						Kit kit = plugin.getContext().getKit(ev.getCurrentItem().getItemMeta().getDisplayName());
						
						if (kit != null){
							Inventory iv = plugin.getServer().createInventory(null, InventoryType.HOPPER, ChatColor.GOLD + kit.getName() + ChatColor.GRAY + " Option");
							
							ItemStack cc = new ItemStack(Material.CHEST);
							ItemMeta ccm = cc.getItemMeta();
							ccm.setDisplayName(ChatColor.AQUA + "Set Kit");
							cc.setItemMeta(ccm);
							iv.setItem(0, cc);
							
							ItemStack re = new ItemStack(Material.ANVIL);
							ItemMeta are = re.getItemMeta();
							are.setDisplayName(ChatColor.AQUA + "Rename kit");
							re.setItemMeta(are);
							iv.setItem(2, re);
							
							ItemStack de = new ItemStack(Material.TNT);
							ItemMeta ade = de.getItemMeta();
							ade.setDisplayName(ChatColor.AQUA + "Remove kit");
							de.setItemMeta(ade);
							iv.setItem(4, de);
							
							player.openInventory(iv);
						}
						
						return;
					}
				}
			}
			
			// In the case we manage a team (name of the team as inventory name)
			for (Entry<String,Team> entry : plugin.getContext().getTeams().entrySet())
			{
				Team team = entry.getValue();
				
				if (ChatColor.stripColor(ev.getInventory().getName()).equalsIgnoreCase(team.getName()))
				{
					ev.setCancelled(true);
					
					if (ev.getCurrentItem() != null){
						
						if (ev.getCurrentItem().getType() == Material.FEATHER)
						{
						
							plugin.getContext().changeTeamColor(team);
							
							plugin.getServer().broadcastMessage(ChatColor.GRAY + "Team "+team.getColor() + team.getName() + ChatColor.GRAY + " has changed his color");
							
							player.closeInventory();
							
							return;
						}
						
						if (ev.getCurrentItem().getType() == Material.ANVIL)
						{
							ci.changeLastState(player,  StateChat.RENAME_TEAM);
							ci.addValueState(player, StateChat.TEAM_NAME, team.getName());;
							
							player.sendMessage(ChatColor.GRAY + "Write the name of the new team " + team.getColor() +team.getName() + ChatColor.GRAY + ".");
							player.closeInventory();
							return;
						}
						
						if (ev.getCurrentItem().getType() == Material.TNT)
						{
							player.sendMessage(ChatColor.GRAY + "Team " + team.getColor() + team.getName() + ChatColor.GRAY + " has been removed.");
							
							plugin.getContext().removeTeam(team);
							
							player.closeInventory();
							return;
						}
					}
				}
			}
		}
		
	}
	
	@EventHandler
	public void onClose(InventoryCloseEvent ev)
	{

	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerChat(AsyncPlayerChatEvent ev)
	{
		
	}

	@EventHandler
	public void onPlayerCommand(PlayerCommandPreprocessEvent ev)
	{
		
	}
	
}
