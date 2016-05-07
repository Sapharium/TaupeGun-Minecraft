package taupegun.listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

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
	public void onInventoryClick(InventoryClickEvent ev)
	{
		if(!plugin.getContext().hasStarted()){
		
			Player player = (Player) ev.getWhoClicked();
			
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
					
					int slot = 0;
					for (Player pn : team.getPlayers())
					{
						ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short)SkullType.PLAYER.ordinal());
						SkullMeta meta = (SkullMeta) skull.getItemMeta();
						meta.setDisplayName(ChatColor.RESET + pn.getDisplayName());
						
						List<String> lore = new ArrayList<String>();
						lore.add(ChatColor.GRAY + "Remove the player from the team");
						meta.setLore(lore);
						meta.setOwner(pn.getDisplayName());
						skull.setItemMeta(meta);
						iv.setItem(slot, skull);
						slot++;
					}
					
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

			
			if (ev.getCurrentItem() != null && ev.getInventory().getName().contains("- Kits -"))
			{
				ev.setCancelled(true);
				
				if(ev.getCurrentItem() == null)
				{return;}

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
