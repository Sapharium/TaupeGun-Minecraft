package taupegun.structures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import taupegun.start.TaupeGunPlugin;

/**
 * Singleton Class - Only one instance
 * Context class contains all elements needed for a Taupe Gun session.
 */
public class Context {

	/**
	 * Object referencing the plugin
	 */
	private TaupeGunPlugin plugin = null;
	
	/**
	 * HashMap of teams with teamName -> Team
	 */
	private HashMap<String,Team> teams = null;
	
	/**
	 * List of moles teams
	 */
	private ArrayList<Team> molesTeams = null;
	
	/**
	 * List of players (all), only modify when join or exit the server
	 */
	private ArrayList<Player> players = null;
	
	/**
	 * List of moles
	 */
	private ArrayList<Player> moles = null;
	
	/**
	 * HashMap to identify each Player to a team
	 */
	private HashMap<Player,Team> playersTeams = null;
	
	/**
	 * List of Kits
	 */
	private ArrayList<Kit> kits = null;
	
	/**
	 * List of moles that don't give their kit
	 */
	private ArrayList<Player> molesWaitingKit = null;
	
	/**
	 * List of available colors
	 */
	private ArrayList<ChatColor> availableColors = null;
	
	/**
	 * Time information
	 */
	private int minutesLeft = 0;
	private int secondsLeft = 0;
	private int minutesMolesLeft = 0;
	
	/**
	 * Episodes information
	 */
	private int episode = 0;
	
	/**
	 * Game status
	 */
	private boolean hasStarted = false;
	private boolean molesActivated = false;
	private boolean titleManagerPluginEnabled = false;
	private boolean invincible = true;
	
	/**
	 * Quantities information
	 */
	private int molesPerTeam = 0;
	private int numberTeamMoles = 0;
	private int molesPerMolesTeam = 0;
	
	/**
	 * Singleton object representing the context
	 */
	private static Context context = null;
	
	private Context(TaupeGunPlugin newPlugin){
		
		this.plugin = newPlugin;
		this.teams = new HashMap<String,Team>();
		this.molesTeams = new ArrayList<Team>();
		this.players = new ArrayList<Player>();
		this.playersTeams = new HashMap<Player,Team>();
		this.kits = new ArrayList<Kit>();
		this.molesWaitingKit = new ArrayList<Player>();
		
		// Initialize available colors
		availableColors = new ArrayList<ChatColor>();
		
		// Add manually available colors
		availableColors.add(ChatColor.BLACK);
		availableColors.add(ChatColor.WHITE);
		availableColors.add(ChatColor.BLUE);
		availableColors.add(ChatColor.GREEN);
		availableColors.add(ChatColor.YELLOW);
		availableColors.add(ChatColor.RED);
		availableColors.add(ChatColor.GOLD);
		availableColors.add(ChatColor.DARK_PURPLE);
		availableColors.add(ChatColor.DARK_RED);
		availableColors.add(ChatColor.DARK_AQUA);
		availableColors.add(ChatColor.DARK_BLUE);
		availableColors.add(ChatColor.AQUA);
		
	}
	
	/**
	 * Get the current Context, this function has to be called at least once to initialize the context
	 * @param plugin	TaupeGunPlugin object associated
	 * @return	the Context object that contains most of information
	 */
	public static Context getContext(TaupeGunPlugin plugin){
		
		if (context == null){
			context = new Context(plugin);
		}
		
		return context;
	}
	
	/**
	 * Get the current Context without specifying the plugin structure
	 * @return	the Context object that contains most of information
	 */
	public Context getContext(){
		
		return context;
		
	}
	
	/* TEAMS AND PLAYERS FUNCTIONS */
	
	
	/**
	 * Add a new team
	 * @param teamName	Name of the team
	 * @return	the new instance of Team object
	 */
	public Team addTeam(String teamName){
		
		// Add the score board team
		org.bukkit.scoreboard.Team t = plugin.getServer().getScoreboardManager().getMainScoreboard().registerNewTeam(teamName);
		
		Team team = new Team(teamName,randomColor(),randomLocation(),t);
		
		teams.put(teamName, team);
		
		return team;
		
	}
	
	/**
	 * Add a new team of moles
	 * @param teamName	Name of the team
	 */
	public void addMolesTeam(String molesTeamName){
		
		// Add a classic team
		Team team =  addTeam(molesTeamName);
		
		// But we notice that it's a team of moles
		molesTeams.add(team);
		
	}
	
	/**
	 * Add a new mole
	 * @param player	The player that will become a mole
	 */
	public void addMole(Player player){
		
		moles.add(player);
		molesWaitingKit.add(player);
		
	}
	
	/**
	 * Add a player to a team
	 * @param player	Player to add
	 * @param team	Team that will have the new player
	 */
	public void addPlayerToATeam(Player player, Team team){
	
		team.addPlayer(player);
		playersTeams.put(player, team);
		team.getScoreboardTeam().addEntry(player.getDisplayName());
		
	}
	
	public boolean isAlreadyInATeam(Player player){
		return playersTeams.containsKey(player);
	}
	
	/**
	 * Add a new kit
	 * @param kitName	Name of the kit
	 * @return	the new instance of Kit object
	 */
	public Kit addKit(String kitName){
				
		Inventory inventory = plugin.getServer().createInventory(null, 27, ChatColor.DARK_BLUE + "- Kit "+kitName+" -");
		
		Kit kit = new Kit(kitName, inventory);
		
		kits.add(kit);
		
		return kit;
		
	}
	
	/**
	 * Change the team of a player
	 * @param player	Player concerned by the change
	 * @param team	Team that will have the new player
	 */
	public void changeTeamPlayer(Player player, Team team){
		
		removePlayerFromATeam(player);
		
		addPlayerToATeam(player,team);
		
	}
	
	/**
	 * Reveal a mole and change his team
	 * @param player	the player concerned
	 * @param moleTeam	the new team of moles that the player will have
	 */
	public void changeRevealedMole(Player player){
		
		removePlayerFromATeam(player);
		
		// Now find the new mole team
		int rand = new Random().nextInt(molesTeams.size()-1);
		
		Team team = molesTeams.get(rand);
		
		addPlayerToATeam(player,team);
		
		if (molesTeams.get(rand).countPlayer() == molesPerMolesTeam){
			molesTeams.remove(rand);
		}
		
		// Now, mole belongs to a team
		moles.remove(player);
	}
	
	/**
	 * Get a team with his name
	 * @param teamName	name of the team
	 * @return	Team instance according to the given name
	 */
	public Team getTeam(String teamName){
		return teams.get(ChatColor.stripColor(teamName));
	}
	
	/**
	 * Get a git with his name
	 * @param kitName	name of the kit
	 * @return	Kit instance according to the given name
	 */
	public Kit getKit(String kitName){
		Kit kit = null;
		
		Iterator<Kit> it = kits.iterator();
		
		while (it.hasNext() && kit == null){
			Kit tmpKit = it.next();
			
			if (tmpKit.getName().equalsIgnoreCase(ChatColor.stripColor(kitName))){
				kit = tmpKit;
			}
		}
		
		return kit;
	}
	
	/**
	 * Change the color of the Team (random)
	 * @param teamName	Name of the team
	 */
	public void changeTeamColor(Team team){
		
		availableColors.add(team.getColor());
		
		ChatColor color = randomColor();
		
		team.setColor(color);
	
		availableColors.remove(team.getColor());
		
		team.getScoreboardTeam().setPrefix(team.getColor()+"");
		team.getScoreboardTeam().setSuffix(ChatColor.RESET+"");
		
		for (Player player: team.getPlayers()){
			player.setPlayerListName(team.getColor()+"["+team.getName()+"] "+player.getDisplayName());
		}
		
	}
	
	/**
	 * Recover the team of a player
	 * @param player	Player to search
	 * @return	the Team instance of the player
	 */
	public Team getTeamOfPlayer(Player player){
		return playersTeams.get(player);
	}
	
	/**
	 * Remove a player from a team
	 * @param player	Player to remove
	 */
	public void removePlayerFromATeam(Player player){

		Team team = playersTeams.get(player);
		team.removePlayer(player);
		
		playersTeams.remove(player);
		
	}
	
	/**
	 * Remove a team
	 * @param teamName	the name of the team to remove
	 */
	public void removeTeam(Team team){
		
		ArrayList<Player> playersToRemoveFromTeam = null;
		
		teams.remove(team.getName(), team);
		
		playersToRemoveFromTeam = team.getPlayers();
		
		for (Player player: playersToRemoveFromTeam){
			team.removePlayer(player);
		}
		
		availableColors.add(team.getColor());
		
		team.getScoreboardTeam().unregister();
		
	}
	
	/**
	 * Remove a kit
	 * @param kitName	The name of the kit to remove
	 */
	public void removeKit(Kit kit){
		
		kits.remove(kit);
		
	}
	
	/**
	 * Add a player who join the server
	 * @param player
	 */
	public void addJoinedPlayer(Player player){
		players.add(player);
		/* TODO: Check if first connection*/
	}
	
	/**
	 * Remove a player who quit the server
	 */
	public void removeQuitPlayer(Player player){
		
		/* TODO: Keep a history for the player in the case he returns*/
	}
	
	/**
	 * Generate a random position according to the map size
	 * @return	a Location Object
	 */
	private Location randomLocation()
	{
		int size = plugin.getConfig().getInt("map.startSize");
		
		double rx = new Random().nextInt(size*2);
		double rz = new Random().nextInt(size*2);
		
		rx -= size;
		rz -= size;
		
		Location loc = new Location(plugin.getServer().getWorlds().get(0), rx, 200, rz);
		
		if ((-20 < rx && rx < 20) || (-20 < rz && rz < 20)) loc = randomLocation();
		
		return loc;
	}
	
	/**
	 * Generate a random color according to the available colors
	 * @return	A random color
	 */
	private ChatColor randomColor()
	{
		ChatColor color = null;
		
		int rand = new Random().nextInt(availableColors.size()-1);
		
		color = availableColors.get(rand);
		
		// Remove color from available colors
		availableColors.remove(color);
		
		return color;
	}
	
	/**
	 * Get the HashMap of teams
	 * @return	HashMap of teams
	 */
	public HashMap<String,Team> getTeams(){
		return teams;
	}
	
	/**
	 * Get ArrayList of moles
	 * @return	ArrayList of moles
	 */
	public ArrayList<Player> getMoles(){
		return moles;
	}
	
	/**
	 * Get the list of kits
	 * @return	ArrayList of kits
	 */
	public ArrayList<Kit> getKits(){
		return kits;
	}
	
	/**
	 * Count the number of players (all)
	 * @return the number of players (total)
	 */
	public int countAllPlayers(){
		int count = 0;
		
		Iterator<Team> it = teams.values().iterator();
		
		while (it.hasNext()){
			
			count += it.next().countPlayer();
			
		}
		
		return count;
	}
	
	/**
	 * Count the number of teams (all)
	 * @return the number of teams (total)
	 */
	public int countAllTeams(){		
		return teams.size();
	}
	
	/**
	 * Check if a player is a mole
	 * @param player	Player to check
	 * @return	Yes if so, else false
	 */
	public boolean isMole(Player player){
		return context.getMoles().contains(player);
	}
	
	/**
	 * Get the list of moles who didn't claimed a kit
	 * @return	the list of moles
	 */
	public ArrayList<Player> getMolesWaitingKit(){
		return molesWaitingKit;
	}
	
	/**
	 * Give a kit to the player
	 * @param player	Player that will have the kit
	 */
	public void giveKit(Player player){

		int rand = new Random().nextInt(kits.size()-1);
		
		Kit kit = kits.get(rand);
		
		Iterator<ItemStack> itemStack = kit.getInventory().iterator();
		
		ItemStack items = null;
		
		while (itemStack.hasNext()){
			items = itemStack.next();
			if (items.getType() != Material.AIR){
				player.getWorld().dropItemNaturally(player.getLocation(), items);
			}
			
		}
		
		molesWaitingKit.remove(player);
		
	}
	
	/**
	 * Get the list of all players
	 * @return	the ArrayList with all players
	 */
	public ArrayList<Player> getAllPlayers(){
		return this.players;
	}
	
	/**
	 * Check if a given team name already exists
	 * @param teamName	Name of the team
	 * @return	True if so, else false
	 */
	public boolean isTeamAlreadyExists(String teamName){
		return teams.containsKey(teamName);
	}
	
	public boolean isKitAlreadyExists(String kitName){
		boolean check = false;
		
		Iterator<Kit> it = kits.iterator();
		
		while (it.hasNext() && check == false){
			if (it.next().getName().equals(kitName)){
				check = true;
			}
		}
		
		return check;
	}
	
	
	/* TIME FUNCTIONS */
	
	/**
	 * Get minutes left
	 * @return value of minutes left
	 */
	public int getMinutesLeft(){
		return this.minutesLeft;
	}
	
	/**
	 * Get seconds left
	 * @return value of seconds left
	 */
	public int getSecondsLeft(){
		return this.secondsLeft;
	}
	
	/**
	 * Get minutes moles left
	 * @return value of minutes moles left
	 */
	public int getMinutesMolesLeft(){
		return this.minutesMolesLeft;
	}
	
	/**
	 * Set minutes left
	 * @param minutes	minutes left to set
	 */
	public void setMinutesLeft(int minutes){
		
	}
	
	/**
	 * Set seconds left
	 * @param seconds	seconds left to set
	 */
	public void setSecondsLeft(int seconds){
			
	}
	
	/**
	 * Set minutes moles left
	 * @param minutes	minutes moles left to set
	 */
	public void setMinutesMolesLeft(int minutes){
		
	}
	
	/**
	 * Set seconds moles left
	 * @param minutes	seconds moles left to set
	 */
	public void setSecondsMolesLeft(int minutes){
		
	}
	
	/**
	 * Increments minutes left
	 * @return	the new value of minutes left
	 */
	public int incMinutesLeft(){
		return ++this.minutesLeft;
	}
	
	/**
	 * Decrements minutes left
	 * @return	the new value of minutes left
	 */
	public int decMinutesLeft(){
		return --this.minutesLeft;
	}
	
	/**
	 * Increments seconds left
	 * @return	the new value of seconds left
	 */
	public int incSecondsLeft(){
		return ++this.secondsLeft;
	}
	
	/**
	 * Decrements seconds left
	 * @return	the new value of seconds left
	 */
	public int decSecondsLeft(){
		return --this.secondsLeft;
	}
	
	/**
	 * Increments minutes moles left
	 * @return	the new value of minutes moles left
	 */
	public int incMinutesMolesLeft(){
		return ++this.minutesMolesLeft;
	}
	
	/**
	 * Decrements minutes moles left
	 * @return	the new value of minutes moles left
	 */
	public int decMinutesMolesLeft(){
		return --this.minutesMolesLeft;
	}
	
	
	/* EPISODES FUNCTIONS */
	
	/**
	 * Get the episode number
	 * @return value of episode
	 */
	public int getEpisode(){
		return this.episode;
	}
	
	/**
	 * Incrmeents the episode number
	 * @return	the new episode number
	 */
	public int incEpisode(){
		return ++this.episode;
	}
	
	/* GAME STATUS */
	
	/**
	 * Check if moles are activated
	 * @return	a boolean
	 */
	public boolean isMolesActivated(){
		return this.molesActivated;
	}
	
	/**
	 * Check if session has started
	 * @return	a boolean
	 */
	public boolean hasStarted(){
		return this.hasStarted;
	}
	
	/**
	 * Activates moles
	 */
	public void activateMoles(){
		this.molesActivated = true;
	}
	
	/**
	 * Activates game session
	 */
	public void startGame(){
		this.hasStarted = true;
	}

	/**
	 * Check if Title Manager plugin is enabled
	 * @return	True if it is, else false
	 */
	public boolean isTitleManagerEnabled(){
		return this.titleManagerPluginEnabled;
	}
	
	/**
	 * Enable the plugin Title Manager for this plugin
	 */
	public void titleManagerPluginEnabled(){
		this.titleManagerPluginEnabled = true;
	}
	
	/**
	 * Activate damages
	 */
	public void activateDamages(){
		this.invincible = false;
	}
	
	/**
	 * Check if players are invincible
	 * @return	True if it is, else false
	 */
	public boolean isInvincible(){
		return this.invincible;
	}

	/* QUANTITIES FUNCTIONS */
	
	
	/**
	 * Get the number of moles per team
	 * @return	number of moles per team
	 */
	public int getMolesPerTeam() {
		return molesPerTeam;
	}

	/**
	 * Set the number of moles per team
	 * @param molesPerTeam	the number of moles per team
	 */
	public void setMolesPerTeam(int molesPerTeam) {
		this.molesPerTeam = molesPerTeam;
	}

	/**
	 * Get the number of moles teams
	 * @return	the number of moles teams
	 */
	public int getNumberTeamMoles() {
		return numberTeamMoles;
	}

	/**
	 * Set the number of moles teams
	 * @param numberTeamMoles	the number of moles teams
	 */
	public void setNumberTeamMoles(int numberTeamMoles) {
		this.numberTeamMoles = numberTeamMoles;
	}

	/**
	 * Get the number of moles per moles teams
	 * @return	the number of moles per moles teams
	 */
	public int getMolesPerMolesTeam() {
		return molesPerMolesTeam;
	}

	/**
	 * Set the number of moles per moles teams
	 * @param molesPerMolesTeam	the number of moles per moles teams
	 */
	public void setMolesPerMolesTeam(int molesPerMolesTeam) {
		this.molesPerMolesTeam = molesPerMolesTeam;
	}
}
