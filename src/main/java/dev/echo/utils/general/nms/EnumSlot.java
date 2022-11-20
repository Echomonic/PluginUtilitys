package dev.echo.utils.general.nms;

import lombok.Getter;
import net.minecraft.world.entity.EnumItemSlot;

public enum EnumSlot {


    HEAD(EnumItemSlot.f),
    CHESTPLATE(EnumItemSlot.e),
    LEGGINGS(EnumItemSlot.d),
    BOOTS(EnumItemSlot.c),
    OFF_HAND(EnumItemSlot.b),
    MAIN_HAND(EnumItemSlot.a),
    ;

    @Getter
    private final EnumItemSlot slot;

    EnumSlot(EnumItemSlot slot) {

        this.slot = slot;
    }


}
