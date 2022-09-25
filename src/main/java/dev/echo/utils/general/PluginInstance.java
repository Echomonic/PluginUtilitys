package dev.echo.utils.general;

import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.plugin.Plugin;

public class PluginInstance {

    @Getter
    @Setter
    private static Plugin bungeeClass;
    @Getter
    @Setter
    private static Plugin spigotClass;
}
