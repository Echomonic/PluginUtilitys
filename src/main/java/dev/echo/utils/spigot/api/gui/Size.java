package dev.echo.utils.spigot.api.gui;

public enum Size {

    SIZE_54(54),
    SIZE_27(27),
    SIZE_18(18),
    SIZE_45(45),
    SIZE_9(9),
    SIZE_36(36),


   // 45 54 27 18

    ;
    private final int guiSize;

    Size(int guiSize){

        this.guiSize = guiSize;
    }

    public int getGuiSize() {
        return guiSize;
    }
}
