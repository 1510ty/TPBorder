package com.mc1510ty.TPBorder;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class TPBorder extends JavaPlugin implements Listener {

    private double borderRadius = 100; // デフォルト値

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        saveDefaultConfig();
        borderRadius = getConfig().getDouble("border-radius", 100);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("setborder")) {
            if (args.length != 1) {
                sender.sendMessage("§c使い方: /setborder <半径>");
                return true;
            }
            try {
                borderRadius = Double.parseDouble(args[0]);
                getConfig().set("border-radius", borderRadius);
                saveConfig();
                sender.sendMessage("§aボーダー半径を " + borderRadius + " に設定しました。");
            } catch (NumberFormatException e) {
                sender.sendMessage("§c数字を入力してください。");
            }
            return true;
        }
        return false;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Location from = event.getFrom();
        Location to = event.getTo();
        if (to == null || (from.getBlockX() == to.getBlockX() && from.getBlockZ() == to.getBlockZ())) {
            return; // 移動していない
        }

        Player player = event.getPlayer();
        World world = player.getWorld();
        Location spawn = world.getSpawnLocation();

        double dx = to.getX() - spawn.getX();
        double dz = to.getZ() - spawn.getZ();
        double distance = Math.sqrt(dx * dx + dz * dz);

        if (distance > borderRadius) {
            player.teleport(spawn);
            player.sendMessage("§cワールドボーダーの外には行けません！");
        }
    }
}