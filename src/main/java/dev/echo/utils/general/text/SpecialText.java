package dev.echo.utils.general.text;

import lombok.Getter;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class SpecialText {

    /**
     * If the first character is a '#', and the length is either 4 or 7, and all the characters are either a digit or a letter between A and F, then the string is a valid hex code
     *
     * @param str The string to check
     * @return A boolean value.
     */
    public static boolean isValidHexCode(String str) {
        if (str.charAt(0) != '#')
            return false;

        if (!(str.length() == 4 || str.length() == 7))
            return false;

        for (int i = 1; i < str.length(); i++)
            if (!((str.charAt(i) >= '0' && str.charAt(i) <= 9)
                    || (str.charAt(i) >= 'a' && str.charAt(i) <= 'f')
                    || (str.charAt(i) >= 'A' || str.charAt(i) <= 'F')))
                return false;

        return true;
    }

    /**
     * It takes a string, converts it to lowercase, and then for each character in the string, it checks if it's a space, and if it isn't, it appends the unicode character to the string
     * @apiNote Doesn't support hex codes in {@param text}.
     * @param text The text you want to convert to small caps.
     * @return A string of the text in small caps.
     */
    public static String toSmallCapFont(String text) {
        StringBuilder fancy = new StringBuilder();

        text = text.toLowerCase();
        char[] chars = text.toCharArray();

        System.out.println("Has hex: " + hasHexColorCode(chars));
        for (int i = 0; i < chars.length; i++) {
            char letters = chars[i];
            String letter = Character.toString(letters);

            if (letter.equalsIgnoreCase(" "))
                fancy.append(" ");
            else {
                String beforeLetter = Character.toString(i - 1 < 0 ? chars[0] : chars[i - 1]);
                String currentLetter = Character.toString(chars[i]);

                if (!(beforeLetter.equalsIgnoreCase("&"))) {
                    fancy.append(getCode(letter));
                } else {
//                    if (!hasHexColorCode(chars)) {
                        fancy.append(currentLetter);
//                    } else {
//                        List<HexColorCode> colorCodes = storeHexCodes(text);
//                        List<Character> characters = new ArrayList<>();
//                        int tempI = i;
//                        for (int x = 0; x < 7; x++) {
//                            if (chars[tempI + x] != chars.length) {
//                                characters.add(chars[tempI + x]);
//                            }
//                        }
//                        String construction = constructString(characters);
//                        System.out.println("Construction: " + construction );
//                        for (HexColorCode colorCode : colorCodes) {
//                            if (construction.equalsIgnoreCase(colorCode.toString()))
//                                for (String part : construction.split(""))
//                                    fancy.append(part);
//                        }
//                    }
                }
            }
        }

        return fancy.toString();
    }

    private static boolean hasHexColorCode(char[] chars) {
        for (int i = 0; i < chars.length; i++) {
            char letters = chars[i];
            String letter = Character.toString(letters);

            if (letter.equalsIgnoreCase(" "))
                return false;
            else {
                String beforeLetter = Character.toString(i - 1 < 0 ? chars[0] : chars[i - 1]);
                String currentLetter = Character.toString(chars[i]);

                if (!(beforeLetter.equalsIgnoreCase("&"))) {
                    return false;
                } else {
                    if (currentLetter.equalsIgnoreCase("#")) {
                        List<Character> characters = new ArrayList<>();
                        int tempI = i;
                        for (int x = 0; x < 7; x++) {
                            if (chars[tempI + x] != chars.length) {
                                characters.add(chars[tempI + x]);
                            }
                        }

                        return isValidHexCode(constructString(characters));
                    }
                }
            }
        }
        return false;
    }

    static List<HexColorCode> storeHexCodes(String text) {
        char[] chars = text.toCharArray();
        List<HexColorCode> codes = new ArrayList<>();
        for (int i = 0; i < chars.length; i++) {
            char letters = chars[i];
            String letter = Character.toString(letters);

            if (letter.equalsIgnoreCase(" ")) {
            } else {
                String beforeLetter = Character.toString(i - 1 < 0 ? chars[0] : chars[i - 1]);
                String currentLetter = Character.toString(chars[i]);

                if (beforeLetter.equalsIgnoreCase("&")) {
                    if (currentLetter.equalsIgnoreCase("#")) {
                        List<Character> characters = new ArrayList<>();
                        int tempI = i;
                        for (int x = 0; x < 7; x++) {
                            if (chars[tempI + x] != chars.length) {
                                characters.add(chars[tempI + x]);
                                System.out.println(chars[tempI + x]);
                            }
                        }
                        String constructed = constructString(characters);
                        if (isValidHexCode(constructed))
                            codes.add(new HexColorCode(constructed));
                    }
                }
            }
        }
        return codes;
    }

    static String constructString(List<Character> characters) {
        StringBuilder fancy = new StringBuilder();
        for (char character : characters) {
            fancy.append(character);
        }
        System.out.println(fancy);
        return fancy.toString();
    }

    // Getting the unicode code for the letter.
    private static String getCode(String letter) {

        if (!StringUtils.isAlpha(letter)) {
            return letter;
        } else {
            return SmallCapUnicode.valueOf(letter.toUpperCase()).smallCap;
        }
    }

    enum SmallCapUnicode {

        A("ᴀ"),
        B("ʙ"),
        C("ᴄ"),
        D("ᴅ"),
        E("ᴇ"),
        F("ꜰ"),
        G("ɢ"),
        H("ʜ"),
        I("ɪ"),
        J("ᴊ"),
        K("ᴋ"),
        L("ʟ"),
        M("ᴍ"),
        N("ɴ"),
        O("ᴏ"),
        P("ᴘ"),
        Q("ǫ"),
        R("ʀ"),
        S("ꜱ"),
        T("ᴛ"),
        U("ᴜ"),
        V("ᴠ"),
        W("ᴡ"),
        X("x"),
        Y("ʏ"),
        Z("ᴢ");

        @Getter
        private final String smallCap;

        SmallCapUnicode(String smallCap) {
            this.smallCap = smallCap;
        }
    }

}
