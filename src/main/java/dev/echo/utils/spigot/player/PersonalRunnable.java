package dev.echo.utils.spigot.player;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public abstract class PersonalRunnable extends BukkitRunnable {

    private final HashMap<UUID, PersonalRunnable> taskMap = new HashMap<>();

    public void assign(Player player){
        taskMap.put(player.getUniqueId(),this);

    }
    public void remove(Player player){
        if(taskMap.containsKey(player.getUniqueId())) {
            taskMap.get(player.getUniqueId()).cancel();
            taskMap.remove(player.getUniqueId());
        }
    }
}
