package dev.echo.utils.spigot.reflection;

import lombok.Getter;
import org.bukkit.Bukkit;

public class MinecraftVersion {


    @Getter
    private final static String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];

    public static int getSubVersion(){
        return Integer.parseInt(version.replace("1_", "").replace("v","").replaceAll("_R\\d", ""));
    }

}
