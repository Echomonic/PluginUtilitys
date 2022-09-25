package dev.echo.utils.spigot.listeners;

import dev.echo.utils.general.Color;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

public class EventUtil {

    @SneakyThrows
    public static void registerEvents(JavaPlugin plugin) {
        for (Class<? extends Listener> listenerClass : new Reflections().getSubTypesOf(Listener.class))
            Bukkit.getPluginManager().registerEvents(listenerClass.getConstructor().newInstance(), plugin);

        Bukkit.getConsoleSender().sendMessage(Color.c("%%green%%Registered Listeners!"));

    }

}
