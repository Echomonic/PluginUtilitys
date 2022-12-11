package dev.echo.utils.spigot.gui;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;


public class ItemBuilder {


    private ItemStack stack;

    public ItemBuilder(Material mat) {
        stack = new ItemStack(mat);
    }

    public ItemBuilder(ItemStack stack) {
        this.stack = stack;
    }

    public ItemBuilder(Material mat, int sh) {
        stack = new ItemStack(mat, 1, (byte) sh);
    }

    public static ItemStack build(Material material,Consumer<ItemBuilder> con){
        ItemBuilder itemBuilder = new ItemBuilder(material);
        con.accept(itemBuilder);

        return itemBuilder.build();
    }

    public static ItemStack build(ItemStack stack,Consumer<ItemBuilder> con){
        ItemBuilder itemBuilder = new ItemBuilder(stack);
        con.accept(itemBuilder);

        return itemBuilder.build();
    }
    /**
     *
     * @return items
     *
     * @implNote getting the the meta of the item
     */

    public ItemMeta getItemMeta() {
        return stack.getItemMeta();
    }

    /**
     *
     * @param color, used for setting the color of leather armor
     * @return this
     *
     */
    public ItemBuilder setColor(Color color) {
        LeatherArmorMeta meta = (LeatherArmorMeta) stack.getItemMeta();
        meta.setColor(color);
        setItemMeta(meta);
        return this;
    }
    public ItemBuilder setOwner(Player owner){
        SkullMeta meta = (SkullMeta) getItemMeta();
        meta.setOwningPlayer(owner);
        setItemMeta(meta);
        return this;
    }
    public ItemBuilder setType(Material material) {
        stack.setType(material);
        return this;
    }



    public ItemBuilder setGlow (boolean glow) {
        if (glow) {
            addEnchant(Enchantment.KNOCKBACK, 1);
            addItemFlag(ItemFlag.HIDE_ENCHANTS);
        } else {
            ItemMeta meta = getItemMeta();
            for (Enchantment enchantment : meta.getEnchants().keySet()) {
                meta.removeEnchant(enchantment);
            }
        }
        return this;
    }

    /**
     *
     * @param unbreakable, Just allows the item to not break/have durability.
     * @return this
     */
    public ItemBuilder setUnbreakable(boolean unbreakable) {
        ItemMeta meta = stack.getItemMeta();
        meta.setUnbreakable(unbreakable);
        stack.setItemMeta(meta);
        return this;
    }
    public ItemBuilder setDurability(int durability) {
        stack.setDurability((short) durability);
        return this;
    }
    /**
     *
     * @param color, To set the the banner color
     * @return this
     *
     */
    public ItemBuilder setBannerColor (DyeColor color) {
        BannerMeta meta = (BannerMeta) stack.getItemMeta();
        meta.setBaseColor(color);
        setItemMeta(meta);
        return this;
    }

    /**
     *
     * @param amount, sets the amount given to the player or how
     *                much is put in a gui
     * @return this
     *
     */
    public ItemBuilder setAmount(int amount) {
        stack.setAmount(amount);
        return this;
    }

    public ItemBuilder setItemMeta(ItemMeta meta) {
        stack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setHead(String owner) {
        SkullMeta meta = (SkullMeta) stack.getItemMeta();
        meta.setOwner(owner);
        setItemMeta(meta);
        return this;
    }

    public ItemBuilder setDisplayName(String display_name) {
        ItemMeta meta = getItemMeta();
        meta.setDisplayName(dev.echo.utils.general.Color.c("&r"+display_name));
        setItemMeta(meta);
        return this;
    }

    public ItemBuilder setItemStack (ItemStack stack) {
        this.stack = stack;
        return this;
    }

    public ItemBuilder setLore(ArrayList<String> lore) {
        ItemMeta meta = getItemMeta();
        meta.setLore(dev.echo.utils.general.Color.cL(lore));
        setItemMeta(meta);
        return this;
    }
    public ItemBuilder setLore(List<String> lore) {
        ItemMeta meta = getItemMeta();
        meta.setLore(dev.echo.utils.general.Color.cL(lore));
        setItemMeta(meta);
        return this;
    }
    
    public ItemBuilder setLore(List<String> lore, Object... objs) {
        ItemMeta meta = getItemMeta();
        meta.setLore(lore.stream().map(s -> dev.echo.utils.general.Color.c(s,objs)).collect(Collectors.toList()));
        setItemMeta(meta);
        return this;
    }
    public ItemBuilder setLore (String lore) {
        ArrayList<String> loreList = new ArrayList<>();
        loreList.add(lore);
        ItemMeta meta = getItemMeta();
        meta.setLore(dev.echo.utils.general.Color.cL(loreList));
        setItemMeta(meta);
        return this;
    }

    public ItemBuilder addEnchant(Enchantment enchantment, int level) {
        ItemMeta meta = getItemMeta();
        meta.addEnchant(enchantment, level, true);
        setItemMeta(meta);
        return this;
    }
    public ItemBuilder setLocalizedName(String name){
        ItemMeta meta = getItemMeta();
        meta.setLocalizedName(dev.echo.utils.general.Color.c(name));
        setItemMeta(meta);
        return this;
    }
    public ItemBuilder addUnsafeEnchant(Enchantment enchantment, int level) {
        this.stack.addUnsafeEnchantment(enchantment,level);
        return this;
    }
    public ItemBuilder setLore(String lore, String split) {
        String[] parts = lore.split(split);
        ArrayList<String> loreList = new ArrayList<>();

        for(String part : parts){
            loreList.add(dev.echo.utils.general.Color.c(part));
        }
        setLore(loreList);
        return this;
    }
    public ItemBuilder addItemFlag(ItemFlag flag) {
        ItemMeta meta = getItemMeta();
        meta.addItemFlags(flag);
        setItemMeta(meta);
        return this;
    }

    public ItemBuilder addItemFlags(ItemFlag... flag) {
        ItemMeta meta = getItemMeta();
        meta.addItemFlags(flag);
        setItemMeta(meta);
        return this;
    }
    public ItemBuilder setCustomModelData(int modelData){
        ItemMeta meta = getItemMeta();
        meta.setCustomModelData(modelData);
        setItemMeta(meta);
        return this;
    }
    public ItemBuilder setHeadTexture(String url){
        ItemMeta headMeta = getItemMeta();

        if(url.isEmpty()){
            return this;
        }
        GameProfile profile = new GameProfile(UUID.randomUUID(), "");
        byte[] encodedData = Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
        Field profileField = null;
        try {
            profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, profile);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e1) {
            e1.printStackTrace();
        }

        setItemMeta(headMeta);
        return this;
    }
    public ItemStack build() {
        return stack;
    }

}
