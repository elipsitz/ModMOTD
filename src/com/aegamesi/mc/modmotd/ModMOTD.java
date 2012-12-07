package com.aegamesi.mc.modmotd;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class ModMOTD extends JavaPlugin implements Listener {
	public String motd = "";
	public String info = "";

	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
		saveDefaultConfig();
		motd = getConfig().getString("motd");
		info = getConfig().getString("info");
	}

	public void onDisable() {
		getConfig().set("motd", motd);
		getConfig().set("info", info);
		saveConfig();
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent evt) {
		if (evt.getPlayer().hasPermission("modmotd.*")) {
			if (motd.trim().length() > 0) {
				evt.getPlayer().sendMessage(processColours("&l&a-- MOD MESSAGE -- " + info + " --"));
				evt.getPlayer().sendMessage(processColours("&c" + motd));
			}
		}
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!sender.hasPermission("modmotd.*")) {
			sender.sendMessage("You do not have permission.");
			return true;
		}
		if (cmd.getName().equalsIgnoreCase("modset")) {
			if (args.length < 1) {
				if (motd.trim().length() > 0) {
					sender.sendMessage(processColours("&l&a-- MOD MESSAGE -- " + info + " --"));
					sender.sendMessage(processColours("&c" + motd));
				} else {
					sender.sendMessage("No motd");
				}
				return true;
			}
			motd = combine(args, 0, 0);
			info = "set by " + sender.getName() + " on " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			sender.sendMessage("Set motd to: " + motd);
			return true;
		}
		if (cmd.getName().equalsIgnoreCase("modappend")) {
			if (args.length < 1) {
				sender.sendMessage("Invalid usage of /modappend");
				return true;
			}
			motd += " | " + combine(args, 0, 0);
			info = "appended by " + sender.getName() + " on " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			sender.sendMessage("Set motd to: " + motd);
			return true;
		}
		if (cmd.getName().equalsIgnoreCase("modclear")) {
			motd = "";
			info = "";
			sender.sendMessage("Cleared.");
			return true;
		}
		return false;
	}

	public static String processColours(String str) {
		return str.replaceAll("(&([a-f0-9klmnor]))", "\u00A7$2");
	}

	public static String combine(String args[], int start, int num) {
		if (num == 0)
			num = args.length - start;
		String result = "";
		for (int i = start; i < start + num; i++)
			result += args[i] + ((i == start + num - 1) ? "" : " ");
		return result;
	}
}