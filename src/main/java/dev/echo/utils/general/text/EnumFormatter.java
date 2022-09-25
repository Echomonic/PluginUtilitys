package dev.echo.utils.general.text;

import java.util.StringJoiner;

public class EnumFormatter {

    public static String formatEnumName(Enum<?> val) {
        StringJoiner joiner = new StringJoiner(" ");
        for (String s : val.name().split("_"))
            joiner.add(s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase());
        return joiner.toString();
    }

}
