package dev.echo.utils.spigot.listeners;

import dev.echo.utils.spigot.gui.Gui;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public class BasicListeners implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        Player player = (Player) event.getWhoClicked();

        if (event.getClickedInventory() == null) return;

        Inventory inventory = event.getClickedInventory();

        if(inventory.getHolder() instanceof Gui){
            Gui gui = (Gui) inventory.getHolder();
            gui.click(event);
        }
    }
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event){
        Player player = (Player) event.getPlayer();

        if(event.getInventory().getHolder() instanceof Gui){
            Gui gui = (Gui) event.getInventory().getHolder();

            gui.close(event);
        }


    }
}
