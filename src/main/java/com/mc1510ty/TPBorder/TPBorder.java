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

    // 四角形のボーダーの幅（中心からの距離）
    private double borderRangeX = 10000;
    private double borderRangeZ = 10000;

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        saveDefaultConfig();
        borderRangeX = getConfig().getDouble("border-range-x", 100);
        borderRangeZ = getConfig().getDouble("border-range-z", 100);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("setborder")) {
            if (args.length == 0) {
                // 引数なし → 現在の範囲を表示
                sender.sendMessage(String.format("§a現在のボーダー範囲: X方向 ±%.1f, Z方向 ±%.1f", borderRangeX, borderRangeZ));
                return true;
            }
            if (args.length != 2) {
                sender.sendMessage("§c使い方: /setborder <X方向の距離> <Z方向の距離>");
                return true;
            }
            try {
                borderRangeX = Double.parseDouble(args[0]);
                borderRangeZ = Double.parseDouble(args[1]);
                getConfig().set("border-range-x", borderRangeX);
                getConfig().set("border-range-z", borderRangeZ);
                saveConfig();
                sender.sendMessage("§aボーダー範囲を X: " + borderRangeX + " , Z: " + borderRangeZ + " に設定しました。");
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

        double x = to.getX();
        double z = to.getZ();

        double minX = spawn.getX() - borderRangeX;
        double maxX = spawn.getX() + borderRangeX;
        double minZ = spawn.getZ() - borderRangeZ;
        double maxZ = spawn.getZ() + borderRangeZ;

        if (x < minX || x > maxX || z < minZ || z > maxZ) {
            player.teleport(spawn);
            player.sendMessage("§cワールドボーダーの外には行けません！");
        }
    }
}
