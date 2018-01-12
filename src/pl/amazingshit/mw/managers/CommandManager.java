package pl.amazingshit.mw.managers;

import org.bukkit.command.CommandSender;

import pl.amazingshit.mw.commands.MWCreateCommand;
import pl.amazingshit.mw.commands.MWListCommand;
import pl.amazingshit.mw.commands.MWRemoveCommand;
import pl.amazingshit.mw.commands.MWTeleportCommand;
import pl.amazingshit.mw.commands.WSAnimalsCommand;
import pl.amazingshit.mw.commands.WSMonstersCommand;
import pl.amazingshit.mw.commands.WSPvPCommand;
import pl.amazingshit.mw.util.Sender;

public class CommandManager {

	private CommandSender cmdsender;
	private Sender sender;
	private String alias;
	private String[] arguments;

	public String[] args;

	public CommandManager(CommandSender sender, String cmdalias, String[] args) {
		this.sender    = new Sender(sender);
		this.cmdsender = sender;
		this.alias     = cmdalias;
		this.arguments = args;
	}

	public void handleCommand() {
		if (alias.equalsIgnoreCase("mw") || alias.equalsIgnoreCase("multiworld")) {
			if (arguments.length == 0) {
				Help.showHelp(cmdsender);
				return;
			}
			if (arguments[0].equalsIgnoreCase("create")) {
				MWCreateCommand.handle(cmdsender, arguments, sender);
				return;
			}
			if (arguments[0].equalsIgnoreCase("remove")) {
				MWRemoveCommand.handle(cmdsender, arguments, sender);
				return;
			}
			if (arguments[0].equalsIgnoreCase("tp")) {
				MWTeleportCommand.handle(cmdsender, arguments, sender);
				return;
			}
			else {
				Help.showHelp(cmdsender);
			}
		}
		if (alias.equalsIgnoreCase("worldsettings") || alias.equalsIgnoreCase("ws")) {
			if (arguments.length == 0) {
				Help.showSettingsHelp(cmdsender);
				return;
			}
			if (arguments[0].equalsIgnoreCase("animals")) {
				WSAnimalsCommand.handle(cmdsender, arguments, sender);
				return;
			}
			if (arguments[0].equalsIgnoreCase("monsters")) {
				WSMonstersCommand.handle(cmdsender, arguments, sender);
				return;
			}
			if (arguments[0].equalsIgnoreCase("pvp")) {
				WSPvPCommand.handle(cmdsender, arguments, sender);
				return;
			}
			else {
				Help.showSettingsHelp(cmdsender);
			}
		}
		if (alias.equalsIgnoreCase("worldlist") || alias.equalsIgnoreCase("wl")) {
			MWListCommand.handle(cmdsender, alias, sender);
		}
	}
}