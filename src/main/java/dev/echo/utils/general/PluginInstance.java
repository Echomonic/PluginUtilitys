package dev.echo.utils.general;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import net.md_5.bungee.api.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class PluginInstance {

    @Getter
    @Setter
    private static Plugin bungeeClass;
    @Getter
    @Setter
    private static JavaPlugin spigotClass;

    @SneakyThrows
    public static <T> T getSpigotClass(T base) {
        T plugin = (T) base.getClass().getDeclaredConstructors()[0].newInstance();
        return plugin;
    }
}
