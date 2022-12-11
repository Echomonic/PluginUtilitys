package dev.echo.utils.spigot.gui;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum GlassType {

    BLACK_STAINED_GLASS_PANE(new ItemBuilder(Material.LEGACY_STAINED_GLASS_PANE,15).build()),
    STAINED_GLASS_PANE(new ItemBuilder(Material.LEGACY_STAINED_GLASS_PANE).build()),
    BLUE_STAINED_GLASS_PANE(new ItemBuilder(Material.LEGACY_STAINED_GLASS_PANE,11).build()),
    LIME_STAINED_GLASS_PANE(new ItemBuilder(Material.LEGACY_STAINED_GLASS_PANE,5).build()),
    DARK_GRAY_STAINED_GLASS_PANE(new ItemBuilder(Material.LEGACY_STAINED_GLASS_PANE,7).build()),
    RED_STAINED_GLASS_PANE(new ItemBuilder(Material.LEGACY_STAINED_GLASS_PANE,14).build()),
    CYAN_STAINED_GLASS_PANE(new ItemBuilder(Material.LEGACY_STAINED_GLASS_PANE,9).build()),

    ;
    private final ItemStack stack;

    GlassType(ItemStack stack){
        this.stack = stack;
    }

    public ItemStack getGlassType() {
        return stack;
    }
}
