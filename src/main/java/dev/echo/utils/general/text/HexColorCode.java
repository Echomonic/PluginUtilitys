package dev.echo.utils.general.text;

import lombok.Getter;
import org.bukkit.Color;


public class HexColorCode{

    @Getter
    private final String hexString;

    public HexColorCode(String hexString){

        this.hexString = hexString;
    }

    public boolean isValid(){
        return SpecialText.isValidHexCode(this.hexString);
    }
    public int length(){
        return hexString.length();
    }
    public String toString(){
        return hexString.toString();
    }

    private static Color hexToRGB(String hex) {
        return Color.fromRGB(Integer.valueOf(hex.substring(1, 3), 16),
                Integer.valueOf(hex.substring(3, 5), 16),
                Integer.valueOf(hex.substring(5, 7), 16));
    }
    public Color toRGB(){

        return hexToRGB(hexString);
    }

}
