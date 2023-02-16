package dev.echo.utils.general.nms;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import dev.echo.utils.general.Color;
import dev.echo.utils.general.logger.LogLevel;
import dev.echo.utils.general.logger.Logger;
import dev.echo.utils.spigot.player.SkinSetter;
import dev.echo.utils.spigot.reflection.MinecraftVersion;
import dev.echo.utils.spigot.reflection.PacketUtil;
import io.netty.handler.codec.DecoderException;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutNamedEntitySpawn;
import net.minecraft.network.protocol.game.PacketPlayOutPlayerInfo;
import net.minecraft.network.protocol.game.PacketPlayOutRespawn;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.level.EnumGamemode;
import net.minecraft.world.level.World;
import net.minecraft.world.level.dimension.DimensionManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static dev.echo.utils.spigot.reflection.PacketUtil.sendPacket;
import static dev.echo.utils.spigot.reflection.PacketUtil.sendPacketNotFor;

public class Skin {

    @Getter
    private final String data;

    @Getter
    private final String signature;

    @Getter
    @Setter
    private String name = "";

    public Skin(String data,String signature){
        this.data = data;
        this.signature = signature;
    }

    public Skin(String[] sigData){
        this.data = sigData[0];
        this.signature = sigData[1];
    }
    public Skin(String name){
        this.name = name;
        this.data = grabSkinFromName(name)[0];
        this.signature = grabSkinFromName(name)[1];
    }
    public void assign(Player player) {
        if (player == null || !player.isOnline()) {
            Logger.log(LogLevel.ERROR, "Couldn't find player");
            return;
        }
        if(name != null || !name.isEmpty()){
            setSkin(player);
        }
    }
    public void assign(Player player,String name) {
        if (player == null || !player.isOnline()) {
            Logger.log(LogLevel.ERROR, "Couldn't find player");
            return;
        }
        if(name != null || !name.isEmpty()){
            setSkin(player);
        }
    }
    private void setSkin(Player player){
        if(MinecraftVersion.getSubVersion() != 19){
            System.out.println("This function currently only supports 1.19.");
            return;
        }
        try {

            CraftPlayer cp = ((CraftPlayer) player);
            EntityPlayer entityPlayer = (EntityPlayer) player.getClass().getMethod("getHandle").invoke(player);
            GameProfile profile = cp.getProfile();

            sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.e, entityPlayer));
            sendPacketNotFor(player, new PacketPlayOutEntityDestroy(cp.getEntityId()));

            if(name != null && !name.isEmpty()){
                SkinSetter setter = new SkinSetter();
                String uuidData = setter.get("https://api.mojang.com/users/profiles/minecraft/%s", name);
                String uuid = setter.getUUID(uuidData);
                String skinData = setter.get("https://sessionserver.mojang.com/session/minecraft/profile/%s?unsigned=false", uuid);
                String skin = setter.getSkin(skinData);
                String signature = setter.getSig(skinData);
                try {
                    profile.getProperties().removeAll("textures");
                    profile.getProperties().put("textures", new Property("textures", skin, signature));
                } catch (DecoderException | IndexOutOfBoundsException exception) {
                    player.sendMessage(ChatColor.RED + "There was a problem getting the skin!");
                    return;
                }
            }else{
                try {
                    profile.getProperties().removeAll("textures");
                    profile.getProperties().put("textures", new Property("textures", data, signature));
                } catch (DecoderException | IndexOutOfBoundsException exception) {
                    player.sendMessage(ChatColor.RED + "There was a problem getting the skin!");
                    return;
                }
            }

            DimensionManager manager = cp.getHandle().getWorld().getDimensionManager();
            ResourceKey<World> resourceKey = cp.getHandle().getSpawnDimension();
            long hashSeed = cp.getWorld().getSeed();
            EnumGamemode gameMode = cp.getHandle().d.getGameMode();

            sendPacket(player,new PacketPlayOutRespawn(manager, resourceKey, hashSeed, gameMode, gameMode, false, false, false));
            sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.a, cp.getHandle()));
            sendPacketNotFor(player, new PacketPlayOutNamedEntitySpawn(cp.getHandle()));
            handlePlayer(player);
            player.sendMessage(Color.c("&7Your skin has been updated to:&a " + name + "&7."));
        }catch(Exception e){
            player.sendMessage(ChatColor.RED + "There was a problem loading the skin.");
        }


    }
    private void handlePlayer(Player player){
        Location location = player.getLocation();
        boolean flying = player.isFlying();
        boolean swimming = player.isSwimming();
        boolean sneaking = player.isSneaking();
        boolean sprinting = player.isSprinting();
        boolean gliding = player.isGliding();
        boolean glowing = player.isGlowing();
        int slot = player.getInventory().getHeldItemSlot();

        if(player.getInventory().getItemInOffHand() != null){
            ItemStack offHand = player.getInventory().getItemInOffHand();
            player.getInventory().setItemInOffHand(offHand);
        }

        player.teleport(location);
        player.setFlying(flying);
        player.setSwimming(swimming);
        player.setGliding(gliding);
        player.setGlowing(glowing);
        player.setSneaking(sneaking);
        player.setSprinting(sprinting);
        player.updateInventory();
        player.getInventory().setHeldItemSlot(slot);
    }
    String[] grabSkinFromName(String name){
        SkinSetter setter = new SkinSetter();
        String uuidData = setter.get("https://api.mojang.com/users/profiles/minecraft/%s", name);
        String uuid = setter.getUUID(uuidData);
        String skinData = setter.get("https://sessionserver.mojang.com/session/minecraft/profile/%s?unsigned=false", uuid);
        String skin = setter.getSkin(skinData);
        String signature = setter.getSig(skinData);

        return new String[]{skin,signature};
    }
}
