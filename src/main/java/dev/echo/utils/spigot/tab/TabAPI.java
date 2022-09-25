package dev.echo.utils.spigot.tab;




import dev.echo.utils.spigot.api.CommandContext;

import java.util.List;

public class TabAPI {

    public static void addToTab(List<String> tabList, String[] args, int argument, String string) {
        if (args.length == (argument + 1)) {
            tabList.add(string);
        } else {
            tabList.remove(string);
        }
    }

    public static void addToTab(List<String> tabList, String[] args, int argument, String string, boolean addOn) {
        if (args.length == (addOn ? argument + 1 : argument)) {
            tabList.add(string);
        } else {
            tabList.remove(string);
        }
    }

    public static void addToTabIfPermission(List<String> tabList, String[] args, int argument, String string,
                                      CommandContext context,
                                      String permission) {
        if (!context.isPermissible(permission)) return;
        if (args.length == argument + 1) {
            tabList.add(string);
        } else {
            tabList.remove(string);
        }
    }
    public static boolean isCurrentArg(String[] args, int argument, String string) {
        return args[argument <= 0 ? 0 : argument - 1].equalsIgnoreCase(string);
    }

}
