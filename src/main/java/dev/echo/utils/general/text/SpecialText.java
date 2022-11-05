package dev.echo.utils.general.text;

import lombok.Getter;
import org.apache.commons.lang.StringUtils;

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
     *
     * @param text The text you want to convert to small caps.
     * @return A string of the text in small caps.
     */
    public static String toSmallCapFont(String text) {
        StringBuilder fancy = new StringBuilder();

        text = text.toLowerCase();

        for (char letters : text.toCharArray()) {
            String letter = Character.toString(letters);

            if (letter.equalsIgnoreCase(" "))
                fancy.append(" ");
            else
                fancy.append(getCode(letter));
        }


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
