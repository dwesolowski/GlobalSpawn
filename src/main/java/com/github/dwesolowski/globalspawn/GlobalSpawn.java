package com.github.dwesolowski.globalspawn;

import org.bukkit.Location;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.command.Command;
import org.bukkit.event.EventHandler;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.player.PlayerJoinEvent;

public class GlobalSpawn extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        saveConfig();
        registerMetrics();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Location spawn = (Location) getConfig().get("serverSpawn");
        player.teleport(spawn);
    }

    private void registerMetrics() {
        final MetricsLite metrics = new MetricsLite(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Command not allowed in console, must be used in-game only!");
            return true;
        }
        Player player = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("setspawn")) {
            if (!player.hasPermission("globalspawn.setspawn")) {
                player.sendMessage(ChatColor.RED + "You do not have permission to execute this command!");
                return true;
            }
            Location playerLoc = player.getLocation();
            getConfig().set("serverSpawn", playerLoc);
            saveConfig();
            player.sendMessage(ChatColor.GREEN + "The server spawn has been set!");
        } else if (cmd.getName().equalsIgnoreCase("spawn")) {
            if (getConfig().get("serverSpawn") == null) {
                player.sendMessage(ChatColor.RED + "The server spawn has not been set!");
                return true;
            }
            Location spawn = (Location) getConfig().get("serverSpawn");
            player.teleport(spawn);
            player.sendMessage(ChatColor.GOLD + "Teleported to the server spawn.");
        } else if (cmd.getName().equalsIgnoreCase("spawnReload")) {
            if (!player.hasPermission("globalspawn.spawnreload")) {
                player.sendMessage(ChatColor.RED + "You do not have permission to execute this command!");
                return true;
            }
            if (getConfig().get("serverSpawn") == null) {
                player.sendMessage(ChatColor.RED + "The server spawn has not been set!");
                return true;
            }
            reloadConfig();
            player.sendMessage(ChatColor.GOLD + "Configuration Reloaded!");
        }
        return true;
    }
}
