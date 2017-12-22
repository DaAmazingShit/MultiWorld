package pl.DaAmazingShit.MultiWorld;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

import pl.DaAmazingShit.MultiWorld.listeners.MWListener;
import pl.DaAmazingShit.MultiWorld.util.Help;
import pl.DaAmazingShit.MultiWorld.util.Lang;
import pl.DaAmazingShit.MultiWorld.util.Respond;
import pl.DaAmazingShit.MultiWorld.util.Help.Help2;
import pl.DaAmazingShit.MultiWorld.util.Info;
import pl.DaAmazingShit.MultiWorld.util.Respond.Error;

public class MultiWorldMain extends JavaPlugin {
	
	//public static Server staticServer;
	
	// Configuration & stuff
	public static File configFile = new File("plugins/MultiWorld", "configuration.yml");
	public static File permFile = new File("plugins/MultiWorld", "perms.yml");
	
	public static Configuration permissions = new Configuration(permFile);
	public static Configuration config = new Configuration(configFile);
	
	public Plugin that = this;
	public static List<String> loadedWorlds = new LinkedList<String>();
	
	public static String pluginVersion = "1.0-beta3";
	public static String version       = "git-Bukkit-0.0.0-428-g51dd641-rcmB9-b53jnks";
	public static String mcVersion     = "1.2_02";
	
	
	public MultiWorldMain() {
		
	}
	
	@Override
	public void onEnable() {
		System.out.println("Enabling MultiWorld...");
		try {
			registerEvents();
			if (!configFile.exists()) {
				this.firstStartup();
			}
			this.loadWorlds();
		}
		catch (Exception ex) {
			System.out.println("Could not enable MultiWorld! This is a bug!");
			ex.printStackTrace();
		}
	}
	
	@Override
	public void onDisable() {
		
	}
	
	private void registerEvents() {
		
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvent(Type.PLAYER_MOVE, new MWListener(), Priority.Monitor, this);
		pm.registerEvent(Type.PLAYER_COMMAND, new MWListener(), Priority.Monitor, this);
		pm.registerEvent(Type.PLAYER_TELEPORT, new MWListener(), Priority.Monitor, this);
	}
	
	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String str, String[] args) {
		if (cs instanceof Player) {
	        Player player = (Player) cs;
	        
	        
	        String helpCmd   = Lang.helpCommand;
	        String createCmd = Lang.createCommand;
	        String removeCmd = Lang.removeCommand;
	        String infoCmd   = Lang.infoCommand;
	        String whoCmd    = Lang.whoCommand;
	        String importCmd = Lang.importCommand;
	        String whereCmd  = Lang.whereCommand;
	        String clearCmd  = Lang.clearCommand;
	        
	        
	        /**
	         * XXX REMOVE COMMAND
	         * 
	         */
	        
	        if (cmd.getName().equalsIgnoreCase(removeCmd)) {
	        	World world = null;
	        	if (args.length == 0) {
	        		player.sendMessage(Lang.prefixWrong + Respond.notEnoughArguments);
	        		return true;
	        	}
	        	try {
	            	// Great checker: if world exists V
	        		world = this.getServer().getWorld(args[0]);
	        		
	        		File toRemove = new File(args[0]);
	        		toRemove.delete();
	        		
	        		player.sendMessage(Lang.prefix + Respond.removed(args[0]));
	        		
	        		this.eraseWorld(args[0]);
	        	}
	        	catch (Exception ex) {
	        		player.sendMessage(Lang.prefixWrong + Respond.Error.remove(args[0]));
	        		ex.printStackTrace();
	        		return true;
	        	}
	        }
	        
	        /**
	         * XXX CREATE COMMAND
	         * 
	         */
	        
	        if (cmd.getName().equalsIgnoreCase(createCmd)) {
	        	if (args.length == 0 || args.length == 1) {
	        		player.sendMessage(Lang.prefixWrong + Respond.notEnoughArguments);
	        		return true;
	        	}
	        	if (Respond.getEnv(args[1]) == null) {
	        		player.sendMessage(Lang.prefixWrong + Respond.wrongEnvironment());
	        		return true;
	        	}
	        	try {
	        		if (args[1].equalsIgnoreCase("normal")) {
	        			this.getServer().createWorld(args[0], Environment.NORMAL);
	        		}
	        		
	        		if (args[1].equalsIgnoreCase("nether")) {
	        			this.getServer().createWorld(args[0], Environment.NETHER);
	        		}
	        		
	        		player.sendMessage(Lang.prefix + Respond.created(args[0]));
	        		
	        		this.addWorld(args[0], args[1]);
	        	}
	        	catch (Exception ex) {
	        		player.sendMessage(Lang.prefixWrong + Error.create(args[0]));
	        		ex.printStackTrace();
	        		return true;
	        	}
	        }
	        
	        /**
	         * XXX IMPORT COMMAND
	         * 
	         */
	        
	        if (cmd.getName().equalsIgnoreCase(importCmd)) {
	        	if (args.length == 0 || args.length == 1) {
	        		player.sendMessage(Lang.prefixWrong + Respond.notEnoughArguments);
	        		return true;
	        	}
	        	if (Respond.getEnv(args[1]) == null) {
	        		player.sendMessage(Lang.prefixWrong + Respond.wrongEnvironment());
	        		return true;
	        	}
	        	try {
	        		Environment kupa = Respond.getEnv(args[1]);
	        		this.getServer().createWorld(args[0], kupa);
	        		
	        		String im = Respond.importedSuccess;
	        		im.replaceAll("<WORLD>", args[0]);
	        		player.sendMessage(Lang.prefix + Respond.imported(args[0]));
	        		
	        		this.addWorld(args[0], args[1]);
	        	}
	        	catch (Exception ex) {
	        		player.sendMessage(Lang.prefixWrong + Respond.Error.imported(args[0]));
	        		ex.printStackTrace();
	        		return true;
	        	}
	        }
	        
	        /**
	         * XXX WHO COMMAND
	         * 
	         */
	        
	        if (cmd.getName().equalsIgnoreCase(whoCmd)) {
	        	if (args.length == 0) {
	        		player.sendMessage(Lang.prefixWrong + Respond.notEnoughArguments);
	        		return true;
	        	}
	        	try {
	        		if (MWListener.worldsAndPlayers.get(args[0]) == null) {
	        			player.sendMessage(Lang.prefix + "�e" + args[0] + "�f has no players in.");
	        			return true;
	        		}
	        		String pl  = MWListener.worldsAndPlayers.get(args[0]);
	            	player.sendMessage(Lang.prefix + Respond.who(pl, Integer.toString(MWListener.worldsAndPlayers.size()), args[0]));
	        	}
	        	catch (Exception ex) {
	        		player.sendMessage(Lang.prefixWrong + Error.unknown());
	        		ex.printStackTrace();
	        		return true;
	        	}
	        }
	        
	        /**
	         * XXX WHERE COMMAND
	         * 
	         */
	        if (cmd.getName().equalsIgnoreCase(whereCmd)) {
	        	if (args.length == 0) {
	        		player.sendMessage(Lang.prefixWrong + Respond.notEnoughArguments);
	        		return true;
	        	}
	        	try {
	        		Player p = this.getServer().getPlayer(args[0]);
	        		if (MWListener.whereIsPlayer.get(args[0]) == null) {
	        			player.sendMessage(Lang.prefix + "�e" + args[0] + "�f is not online.");
	        			return true;
	        		}
	            	player.sendMessage(Lang.prefix + Respond.where(p.getName(), p.getWorld().getName()));
	        	}
	        	catch (Exception ex) {
	        		player.sendMessage(Lang.prefixWrong + Error.unknown());
	        		ex.printStackTrace();
	        		return true;
	        	}
	        }
	        
	        /**
	         * XXX HELP COMMAND
	         * 
	         */
	        
	        if (cmd.getName().equalsIgnoreCase(helpCmd)) {
	        	
	        	if (args.length == 1) {
		        	if (args[0].equalsIgnoreCase("next")) {
		            	try {
			            	player.sendMessage(Help2.line1);
			            	player.sendMessage(Help2.line2);
			            	player.sendMessage(Help2.line3);
			            	player.sendMessage(Help2.line4);
			            	player.sendMessage(Help2.line5);
			            	player.sendMessage(Help2.line6);
			            	player.sendMessage(Help2.line7);
		            		return true;
		            	}
		            	catch (Exception ex) {
		            		player.sendMessage(Lang.prefixWrong + Respond.Error.unknown());
		            		ex.printStackTrace();
		            		return true;
		            	}
		        	}
	        	}
	        	
	            try {
            		player.sendMessage(Help.line1);
            		player.sendMessage(Help.line2);
            		player.sendMessage(Help.line3);
            		player.sendMessage(Help.line4);
            		player.sendMessage(Help.line5);
            		player.sendMessage(Help.line6);
            		player.sendMessage(Help.line7);
            		player.sendMessage(Help.line8);
            		player.sendMessage(Help.line9);
            		return true;
	            }
	            catch (Exception ex) {
	            	player.sendMessage(Lang.prefixWrong + Respond.Error.unknown());
	            	ex.printStackTrace();
	            	return true;
	            }
	        }
	        
	        /**
	         * XXX CLEAR COMMAND
	         * 
	         */
	        
	        if (cmd.getName().equalsIgnoreCase(clearCmd)) {

	        	if (args.length == 0) {
	            	World world = player.getWorld();
	        		List<Entity> entities = new LinkedList<Entity>();
	        		entities = this.getServer().getWorld(world.getName()).getEntities();
	        		
	        		for (Entity ent : entities) {
	        			if ((!(ent instanceof HumanEntity)) || (!(ent instanceof Player))) {
	        				ent.remove();
	        			}
	        		}
    				player.sendMessage(Lang.prefix + Respond.clear(world.getName()));
	        		return true;
	        	}
	        	try {
	        		List<Entity> entities = new LinkedList<Entity>();
	        		entities = this.getServer().getWorld(args[0]).getEntities();
	        		
    				String lol = args[0];
	        		for (Entity ent : entities) {
	        			if ((!(ent instanceof HumanEntity)) || (!(ent instanceof Player))) {
	        				ent.remove();
	        			}
	        		}
    				player.sendMessage(Lang.prefix + Respond.clear(lol));
	        	}
	        	catch (Exception ex) {
	        		player.sendMessage(Lang.prefixWrong + Respond.unknownError);
	        		ex.printStackTrace();
	        		return true;
	        	}
	        }
	        
	        /**
	         * XXX INFO COMMAND
	         * 
	         */
	        
	        if (cmd.getName().equalsIgnoreCase(infoCmd)) {
	        	if (args.length == 1) {
		        	if (loadedWorlds.contains(args[0])) {
			        	try {
			        		World w = this.getServer().getWorld(args[0]);
			        		player.sendMessage(Info.line1(w.getName()));
			        		if (w.getEnvironment() == Environment.NORMAL) {
			        			player.sendMessage(Info.line2(w.getName(), "normal"));
			        		}
			        		if (w.getEnvironment() == Environment.NETHER) {
			        			player.sendMessage(Info.line2(w.getName(), "nether"));
			        		}
			        		player.sendMessage(Info.line3(Integer.toString(w.getEntities().size())));
			        		player.sendMessage(Info.line4(MWListener.worldsAndPlayers.get(w.getName())));
			        		return true;
			        	}
			        	catch (Exception ex) {
			        		player.sendMessage(Lang.prefixWrong + Respond.unknownError);
			        		ex.printStackTrace();
			        		return true;
			        	}
		        	}
	        	}
	        	try {
	        		player.sendMessage(Info.line1(player.getWorld().getName()));
	        		if (player.getWorld().getEnvironment() == Environment.NORMAL) {
	        			player.sendMessage(Info.line2(player.getWorld().getName(), "normal"));
	        		}
	        		if (player.getWorld().getEnvironment() == Environment.NETHER) {
	        			player.sendMessage(Info.line2(player.getWorld().getName(), "nether"));
	        		}
	        		player.sendMessage(Info.line3(Integer.toString(player.getWorld().getEntities().size())));
	        		player.sendMessage(Info.line4(MWListener.worldsAndPlayers.get(player.getWorld().getName())));
	        	}
	        	catch (Exception ex) {
	        		player.sendMessage(Lang.prefixWrong + Respond.unknownError);
	        		ex.printStackTrace();
	        		return true;
	        	}
	        }
	        return true;
		}
		return false;
	}
	
	/**
	 * UTIL METHODS
	 * @throws Exception 
	 * 
	 * 
	 */
	
	public void createWorld(String str, String env) throws Exception {
		if (env.equalsIgnoreCase("normal")) {
			this.getServer().createWorld(str, Environment.NORMAL);
		}
		if (env.equalsIgnoreCase("nether")) {
			this.getServer().createWorld(str, Environment.NETHER);
		}
		else {
			throw new Exception("wow bark");
		}
		
	}
	
	public void loadWorlds() {
		MultiWorldMain.config.load();
		List<String> worlds = new LinkedList<String>();
		worlds = MultiWorldMain.config.getStringList("worldlist", worlds);
		
		for (String w : worlds) {
			if ((boolean)MultiWorldMain.config.getProperty("worlds." + w + ".load-on-startup")) {
				// do try{}
				String nulll = null;
				nulll = MultiWorldMain.config.getString("worlds." + w + ".environment", nulll);
				if (nulll.equalsIgnoreCase("NORMAL")) {
					this.getServer().createWorld(w, Environment.NORMAL);
				}
				if (nulll.equalsIgnoreCase("NETHER")) {
					this.getServer().createWorld(w, Environment.NETHER);
				}
				loadedWorlds.add(w);
			}
		}
	}
	
	public void addWorld(String worldname, String environment) {
		MultiWorldMain.config.load();
		MultiWorldMain.config.setProperty("worlds." + worldname + ".environment", environment);
		MultiWorldMain.config.setProperty("worlds." + worldname + ".load-on-startup", true);
		List<String> worlds = new LinkedList<String>();
		worlds = config.getStringList("worldlist", worlds);
		worlds.add(worldname);
		MultiWorldMain.config.setProperty("worldlist", worlds);
		MultiWorldMain.config.save();
	}
	
	public void eraseWorld(String worldname) {
		MultiWorldMain.config.load();
		MultiWorldMain.config.removeProperty("worlds." + worldname);
		List<String> worlds = new LinkedList<String>();
		worlds = config.getStringList("worldlist", worlds);
		worlds.remove(worldname);
		MultiWorldMain.config.setProperty("worldlist", worlds);
		MultiWorldMain.config.save();
		try {
			loadedWorlds.remove(worldname);
		}
		catch (Exception ex) {
			
		}
	}
	
	public void firstStartup() {
		MultiWorldMain.config.load();
		List<String> worlds = new LinkedList<String>();
		worlds.add("world");
		MultiWorldMain.config.setProperty("worldlist", worlds);
		
		MultiWorldMain.config.setProperty("worlds.world.environment", Environment.NORMAL.toString());
		MultiWorldMain.config.setProperty("worlds.world.load-on-startup", true);
		MultiWorldMain.config.save();
	}
}
