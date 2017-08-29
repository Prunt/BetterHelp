package me.Prunt.betterhelp;

import java.lang.reflect.Field;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
	saveDefaultConfig();
    }

    // Kind of hacky way to do it
    // Source:
    // https://bukkit.org/threads/how-to-get-commandmap-with-default-commands.122746/
    private Collection<Command> commands() {
	SimplePluginManager spm = (SimplePluginManager) getServer().getPluginManager();

	try {
	    Field f = SimplePluginManager.class.getDeclaredField("commandMap");

	    f.setAccessible(true);
	    SimpleCommandMap scm = (SimpleCommandMap) f.get(spm);
	    f.setAccessible(false);

	    return scm.getCommands();
	} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
	    return null;
	}
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
	if (cmd.getName().equalsIgnoreCase("help")) {
	    ArrayList<String> list = new ArrayList<>();

	    for (Command command : commands()) {
		if (command.getPermission() != null) {
		    if (sender.hasPermission(command.getPermission())) {
			if (!command.getDescription().isEmpty()) {
			    String desc = ChatColor.GRAY + "/" + command.getName() + ChatColor.WHITE + " - "
				    + command.getDescription();

			    if (!list.contains(desc)) {
				list.add(desc);
			    }
			} else {
			    String nodesc = ChatColor.GRAY + "/" + command.getName() + ChatColor.WHITE + " - "
				    + getConfig().getString("no-description");

			    if (!list.contains(nodesc)) {
				list.add(nodesc);
			    }
			}
		    }
		} else if (!command.getDescription().isEmpty()) {
		    String desc = ChatColor.GRAY + "/" + command.getName() + ChatColor.WHITE + " - "
			    + command.getDescription();

		    if (!list.contains(desc)) {
			list.add(desc);
		    }
		} else {
		    String nodesc = ChatColor.GRAY + "/" + command.getName() + ChatColor.WHITE + " - "
			    + getConfig().getString("no-description");

		    if (!list.contains(nodesc)) {
			list.add(nodesc);
		    }
		}
	    }

	    Collections.sort(list, Collator.getInstance());

	    sender.sendMessage(ChatColor.DARK_GRAY + getConfig().getString("borders"));
	    for (String s : list) {
		sender.sendMessage(s);
	    }
	    sender.sendMessage(ChatColor.DARK_GRAY + getConfig().getString("borders"));
	}
	return true;
    }
}
