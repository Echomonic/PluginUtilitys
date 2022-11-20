package dev.echo.utils.spigot.reflection;

import net.minecraft.network.protocol.Packet;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PacketUtil {

    public static void sendPacket(Player player, Object packet) {
        try {
            Object handle  = player.getClass().getMethod("getHandle").invoke(player);
            Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
            playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void sendPacket(Object packet){
        for(Player player : Bukkit.getOnlinePlayers()) sendPacket(player,packet);
    }
    public static void sendPacketNotFor(Player player, Object packet){
        for (Player targets : Bukkit.getOnlinePlayers()) {
            if (targets.getUniqueId().toString().equals(player.getUniqueId().toString())) continue;
            sendPacket(targets,packet);
        }
    }
    public static Class<?> getNMSClass(String name) {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        try {
            return Class.forName("net.minecraft.server." + version + "." + name);
        }

        catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }

    }
    public static Class<?> getCraftBukkitClass(String name) {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        try {
            return Class.forName("org.bukkit.craftbukkit." + version + "." + name);
        }

        catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }

    }
}
