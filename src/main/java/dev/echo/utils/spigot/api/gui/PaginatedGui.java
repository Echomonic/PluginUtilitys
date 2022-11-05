package dev.echo.utils.spigot.api.gui;

import dev.echo.utils.general.Color;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public abstract class PaginatedGui extends Gui {

    private int index = 0;
    private int page = 0;

    @Setter(AccessLevel.PROTECTED)
    @Getter(AccessLevel.PROTECTED)
    private List<ItemStack> items = new ArrayList<>();

    public PaginatedGui(String title, Size slots) {
        super(title, slots);
        this.setArrows(slots,true);
    }


    @Override
    public void open(Player player) {
        Inventory inventory = this.getInventory();
        inventory.clear();
        this.a();
        super.open(player);
    }

    protected int[] arrows = new int[]{48, 50};
    protected Material[] arrowMaterials = new Material[]{Material.ARROW,Material.ARROW};

    private int closeButton = 49;

    @Setter(AccessLevel.PROTECTED)
    private boolean includeCloseButton = this.getSize().getGuiSize() != 9;

    protected void setArrows(Size size, boolean includeCloseButton) {

        //DON'T MOVE
        this.includeCloseButton = includeCloseButton;


        switch (size) {

            case SIZE_9:
                this.arrows = new int[]{0, 8};
                this.includeCloseButton = false;
                return;

            case SIZE_18:
                this.arrows = new int[]{12, 14};
                if(includeCloseButton)
                    closeButton = 13;
                break;

            case SIZE_27:
                this.arrows = new int[]{20,22};
                if(includeCloseButton)
                    closeButton = 21;
                break;

            case SIZE_36:
                this.arrows = new int[]{30,32};
                if(includeCloseButton)
                    closeButton = 31;
                break;

            case SIZE_45:
                this.arrows = new int[]{39,41};
                if(includeCloseButton)
                    closeButton = 40;
                break;

            case SIZE_54:
                this.arrows = new int[]{48, 50};
                if(includeCloseButton)
                    closeButton = 49;
                break;

        }
    }

    @Override
    public void click(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        int slot = event.getSlot();
        if (slot == arrows[0]) {
            if (page == 0) {
                player.sendMessage(Color.c("&cYou're already on the first page."));
            } else {
                page = page - 1;
                open(player);
            }
        } else if (slot == arrows[1]) {
            if (((index + 1) >= items.size())) {
                player.sendMessage(Color.c("&cYou're already on the last page."));
            } else {
                page = page + 1;
                open(player);
            }
        } else if (slot == closeButton && includeCloseButton) {
            player.closeInventory();
        }
    }

    protected boolean isUtilitySlot(int slot) {

        return slot == arrows[0] || slot == arrows[1] || slot == closeButton;
    }

    protected void a() {
        int maxItems = this.getSlots();
        for (int i = 0; i < maxItems; i++) {
            index = maxItems * page + i;
            if (index >= items.size()) break;
            if (items.get(index) != null) {
                getInventory().addItem(items.get(index));
            }
        }
        setItem(arrows[0], ItemBuilder.build(arrowMaterials[0], itemBuilder -> {
            if (page == 0) itemBuilder.setDisplayName("&cFirst page");
            else itemBuilder.setDisplayName("&a<-- Back a Page");

            itemBuilder.addItemFlags(ItemFlag.values());
        }));
        if (includeCloseButton)
            setItem(closeButton, ItemBuilder.build(Material.BARRIER, itemBuilder -> {
                itemBuilder.setDisplayName("&cClose");
                itemBuilder.addItemFlags(ItemFlag.values());
            }));
        setItem(arrows[1], ItemBuilder.build(arrowMaterials[1], itemBuilder -> {
            if (((index + 1) >= items.size())) itemBuilder.setDisplayName("&cLast page");
            else itemBuilder.setDisplayName("&aNext Page -->");
            itemBuilder.addItemFlags(ItemFlag.values());
        }));
    }
}
