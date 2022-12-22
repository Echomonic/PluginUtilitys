package dev.echo.utils.spigot.player;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class DataRestoration {

    private final Player player;


    private static final HashMap<UUID, Object[]> rollbackData = new HashMap<>();
    private static final HashMap<UUID, HashMap<String, Object>> tempMap = new HashMap<>();

    public DataRestoration(Player player) {
        this.player = player;
        tempMap.put(player.getUniqueId(), new HashMap<>());
    }

    public void store() {

        rollbackData.put(player.getUniqueId(), new Object[]{
                player.getLocation(),
                player.getInventory().getContents(),
                player.getInventory().getArmorContents(),
                player.getInventory().getHeldItemSlot(),
                player.getGameMode(),
                player.getActivePotionEffects(),
                player.getHealth(),
        });
    }
    public void restore() {
        Object[] data = rollbackData.get(player.getUniqueId());
        player.teleport((Location) data[0]);
        player.getInventory().setContents((ItemStack[]) data[1]);
        player.getInventory().setArmorContents((ItemStack[]) data[2]);
        player.getInventory().setHeldItemSlot((int) data[3]);
        player.setGameMode((GameMode) data[4]);
        player.addPotionEffects((Collection<PotionEffect>) data[5]);
        player.setHealth((double) data[6]);
        rollbackData.remove(player.getUniqueId());
    }

    public boolean dataTypeStored(DataType type) {

        return tempMap.getOrDefault(player.getUniqueId(), new HashMap<>()).containsKey(type.name());
    }
    public void rollDataType(DataType type) {
        UUID uuid = player.getUniqueId();
        HashMap<String, Object> data = tempMap.getOrDefault(uuid, new HashMap<>());
        data.remove(type.name());
        tempMap.put(uuid, data);
    }
    public void storeDataType(DataType type) {
        UUID uuid = player.getUniqueId();
        HashMap<String, Object> data = tempMap.getOrDefault(uuid, new HashMap<>());
        switch (type) {
            case HEALTH:
                data.put(type.name(), player.getHealth());
                break;
            case GAMEMODE:
                data.put(type.name(), player.getGameMode());
                break;
            case LOCATION:
                data.put(type.name(), player.getLocation());
                break;
            case ACTIVE_EFFECTS:
                data.put(type.name(), player.getActivePotionEffects());
                break;
            case HELD_ITEM_SLOT:
                data.put(type.name(), player.getInventory().getHeldItemSlot());
                break;
            case ARMOR_CONTENTS:
                data.put(type.name(), player.getInventory().getArmorContents());
                break;
            case CONTENTS:
                data.put(type.name(), player.getInventory().getContents());
                break;
        }
        tempMap.put(uuid, data);
    }

    public <T> T getDataType(DataType type) {

        return (T) tempMap.getOrDefault(player.getUniqueId(), new HashMap<>()).get(type.name());
    }

    public boolean isDataStored() {
        return rollbackData.containsKey(player.getUniqueId());
    }


    public enum DataType {
        LOCATION,
        CONTENTS,
        ARMOR_CONTENTS,
        HELD_ITEM_SLOT,
        GAMEMODE,
        ACTIVE_EFFECTS,
        HEALTH
    }


}
