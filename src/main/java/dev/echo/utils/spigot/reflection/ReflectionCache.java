package dev.echo.utils.spigot.reflection;

import com.google.common.collect.Maps;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class ReflectionCache {

    private static final HashMap<String, Object> cache = Maps.newHashMap();

    @SneakyThrows
    public static void cacheMethods(JavaPlugin plugin) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin,() ->{
        },0);
    }

    public static void setAllAccessible(boolean accessible) {
    }

    public static <T> T getCachedObject(String s) {
        return (T) cache.get(s.toLowerCase().replace(" ", "-"));
    }
}
