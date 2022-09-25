package dev.echo.utils.spigot.api.gui;

import dev.echo.utils.general.Color;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.PrimitiveIterator;

@SuppressWarnings("unused")
public abstract class Gui implements InventoryHolder {

    private final Inventory inventory;


    private int sizeAsSlots;

    private String title;
    private Size slots;

    private boolean isRinged = false;
    private boolean isFilled = false;

    /**
     * This also creates the gui.
     *
     * @param title for the gui.
     * @param slots for the gui.
     */
    public Gui(String title, Size slots) {
        this.title = title;
        this.sizeAsSlots = slots.getGuiSize();
        this.slots = slots;
        inventory = Bukkit.createInventory(this, slots.getGuiSize(), Color.c(title));
        handle();
    }


    /**
     * This also creates the gui but with the designated type.
     *
     * @param title for the gui.
     * @param type  so that we can use the different types of dev.echo.skyblock.guis.
     */

    public Gui(String title, InventoryType type) {
        this.title = title;
        inventory = Bukkit.createInventory(this, type, Color.c(title));
        handle();
    }

    /**
     * This method is a part of InventoryHolder and just needs an inventory to not return null.
     *
     * @return inventory so that the method doesn't return null.
     */

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    /**
     * Just sets the slots of the inventory.
     *
     * @param sizeAsSlots If there is some animation in the gui you can set the slots then open in again etc.
     */

    protected void setSlots(int sizeAsSlots) {
        this.sizeAsSlots = sizeAsSlots;
    }

    /**
     * @param title just sets the title of the gui.
     */

    protected void setTitle(String title) {
        this.title = Color.c(title);
    }

    /**
     * Gets the title from the constructor.
     *
     * @return title
     */

    public String getTitle() {
        return title;
    }

    /**
     * Gets the slots of the form the constructor.
     *
     * @return slots
     */
    public int getSlots() {
        return sizeAsSlots;
    }

    /**
     * @param startingSlot where is starts filling the slots with the list of items/itemStacks
     * @param itemStacks   the items that are going to be put in the gui and go through the slots.
     */
    protected void setItems(int startingSlot, ArrayList<ItemStack> itemStacks, boolean checkItemIsNull) {
        java.util.Arrays.stream(itemStacks.toArray()).forEach(items -> {
            for (int i = startingSlot; i < inventory.getSize(); i++) {
                if (checkItemIsNull) {
                    if (!isSlotNull(i)) {
                        inventory.setItem(i, (ItemStack) items);
                    }
                } else {
                    if (isSlotNull(i)) {
                        inventory.setItem(i, (ItemStack) items);
                    }
                }
            }
        });
    }

    public abstract void click(InventoryClickEvent event);

    protected boolean isItemNull(ItemStack stack){


        return stack == null || stack.getType() == Material.AIR;
    }


    protected Size getSize() {
        return slots;
    }

    /**
     * This just checks to see if the item is null or not.
     *
     * @return false if the item is null otherwise we return true.
     */
    public boolean isSlotNull(int slots) {
        return inventory.getItem(slots) == null || inventory.getItem(slots).getType() == Material.AIR;
    }

    /**
     * This uses an array of integers instead of just an integer by itself.
     *
     * @return false if the item is null otherwise we return true.
     */
    protected boolean areSlotsNull(int... slots) {
        for (PrimitiveIterator.OfInt it = java.util.Arrays.stream(slots).iterator(); it.hasNext(); ) {
            int slot = it.next();
            if (inventory.getItem(slot) == null || inventory.getItem(slot).getType() == Material.AIR) {

                return true;
            }
        }
        return false;
    }

    public Gui handle() {
        this.run();
        return this;
    }

    public void run() {

    }

    protected int convertArray(int... slots) {
        int x = 0;

        for (int i : slots)
            x = i;


        return x;
    }
    public void close(InventoryCloseEvent event){
    }

    /**
     * Just checks over a list of integers to see if the item in the inventory is null or not.
     *
     * @return false if the item is null otherwise we return true.
     */
    protected boolean isSlotNull(List<Integer> slots) {
        for (int i = 0; i < slots.size(); i++) {
            if (inventory.getItem(i) == null || inventory.getItem(i).getType() == Material.AIR) {

                return true;
            }
        }
        return false;
    }

    /**
     * @param slot,  where the item is going to get placed in the gui.
     * @param stack, The item that is getting inserted into the gui
     */
    protected void addItem(int slot, ItemStack stack) {
        if (isSlotNull(slot)) {
            inventory.setItem(slot, stack);
        }
    }

    protected void addItem(ItemStack... stack) {
        inventory.addItem(stack);
    }

    protected void setItem(int slot, ItemStack stack) {
        inventory.setItem(slot, stack);
    }

    protected void setItems(int slot, List<ItemStack> stack) {
        for (ItemStack itemStack : stack) {
            inventory.setItem(slot, itemStack);
        }
    }

    protected void fillSlots(int slot, ItemStack stack) {
        for (int i = slot; i < getSlots(); i++) {
            setItem(i, stack);
        }
    }

    /**
     * @param slots, where the item is going to get placed in the gui.
     * @param stack, The item that is getting inserted into the gui
     */
    protected void addItemToSlots(ItemStack stack, int... slots) {
        for (PrimitiveIterator.OfInt it = java.util.Arrays.stream(slots).iterator(); it.hasNext(); ) {
            int slot = it.next();
            inventory.setItem(slot, stack);
        }
    }

    protected void fillInventory(GlassType type) {
        ItemStack stack = type.getGlassType();
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(" ");
        meta.setLore(null);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_POTION_EFFECTS);

        stack.setItemMeta(meta);

        for (int i = 0; i < inventory.getSize(); i++) {
            if (isSlotNull(i)) {
                inventory.setItem(i, stack);
            }
        }
        this.isFilled = true;
    }

    /**
     * @param type, used so that there is continuity between classes.
     * @param size, to allow compatibility for the ring
     * @implNote Not finished gotta finish 2 more sizes.
     */


    protected void ringInventory(GlassType type, Size size) {
        ItemStack stack = type.getGlassType();
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(" ");
        meta.setLore(null);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_POTION_EFFECTS);

        stack.setItemMeta(meta);
        switch (size) {

            case SIZE_54:

                for (int i = 0; i < inventory.getSize(); i++) {
                    if (isSlotNull(i)) {
                        inventory.setItem(i, stack);
                    }
                }
                for (int x = 10; x < 44; x++) {
                    if (!isSlotNull(x)) {
                        inventory.setItem(x, new ItemStack(Material.AIR));
                    }
                }
                addItemToSlots(stack, 8, 18, 27, 36, 17, 26, 35, 45);
                break;
            case SIZE_45:

                for (int i = 0; i < inventory.getSize(); i++) {
                    if (isSlotNull(i)) {
                        inventory.setItem(i, stack);
                    }
                }
                for (int x = 10; x < 35; x++) {
                    if (!isSlotNull(x)) {
                        inventory.setItem(x, new ItemStack(Material.AIR));
                    }
                }
                addItemToSlots(stack, 18, 27, 36, 17, 26, 35);
                break;
            case SIZE_27:

                for (int i = 0; i < inventory.getSize(); i++) {
                    if (isSlotNull(i)) {
                        inventory.setItem(i, stack);
                    }
                }
                for (int x = 10; x < 17; x++) {
                    if (!isSlotNull(x)) {
                        inventory.setItem(x, new ItemStack(Material.AIR));
                    }
                }
                break;
        }
        this.isRinged = true;
    }

    protected void ringInventory(GlassType type) {
        Size size = getSize();
        ItemStack stack = type.getGlassType();
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(" ");
        meta.setLore(null);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_POTION_EFFECTS);

        stack.setItemMeta(meta);
        switch (size) {

            case SIZE_54:

                for (int i = 0; i < inventory.getSize(); i++) {
                    if (isSlotNull(i)) {
                        inventory.setItem(i, stack);
                    }
                }
                for (int x = 10; x < 44; x++) {
                    if (!isSlotNull(x)) {
                        inventory.setItem(x, new ItemStack(Material.AIR));
                    }
                }
                addItemToSlots(stack, 8, 18, 27, 36, 17, 26, 35, 45);
                break;
            case SIZE_45:

                for (int i = 0; i < inventory.getSize(); i++) {
                    if (isSlotNull(i)) {
                        inventory.setItem(i, stack);
                    }
                }
                for (int x = 10; x < 35; x++) {
                    if (!isSlotNull(x)) {
                        inventory.setItem(x, new ItemStack(Material.AIR));
                    }
                }
                addItemToSlots(stack, 18, 27, 36, 17, 26, 35);
                break;
            case SIZE_27:

                for (int i = 0; i < inventory.getSize(); i++) {
                    if (isSlotNull(i)) {
                        inventory.setItem(i, stack);
                    }
                }
                for (int x = 10; x < 17; x++) {
                    if (!isSlotNull(x)) {
                        inventory.setItem(x, new ItemStack(Material.AIR));
                    }
                }
                break;
        }
    }

    protected void forceRingInventory(GlassType type) {
        Size size = getSize();
        ItemStack stack = type.getGlassType();
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(" ");
        meta.setLore(null);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_POTION_EFFECTS);

        stack.setItemMeta(meta);
        switch (size) {

            case SIZE_54:

                for (int i = 0; i < inventory.getSize(); i++) {
                    inventory.setItem(i, stack);
                }
                for (int x = 10; x < 44; x++) {
                    if (!isSlotNull(x)) {
                        inventory.setItem(x, new ItemStack(Material.AIR));
                    }
                }
                addItemToSlots(stack, 8, 18, 27, 36, 17, 26, 35, 45);
                break;
            case SIZE_45:

                for (int i = 0; i < inventory.getSize(); i++) {
                    inventory.setItem(i, stack);
                }
                for (int x = 10; x < 35; x++) {
                    if (!isSlotNull(x)) {
                        inventory.setItem(x, new ItemStack(Material.AIR));
                    }
                }
                addItemToSlots(stack, 18, 27, 36, 17, 26, 35);
                break;
            case SIZE_27:

                for (int i = 0; i < inventory.getSize(); i++) {
                    inventory.setItem(i, stack);
                }
                for (int x = 10; x < 17; x++) {
                    if (!isSlotNull(x)) {
                        inventory.setItem(x, new ItemStack(Material.AIR));
                    }
                }
                break;
        }
    }

    /**
     * @param player that we send the gui to.
     */
    public void open(Player player) {
        player.openInventory(inventory);
    }
}