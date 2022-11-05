package dev.echo.utils.general;

import dev.echo.utils.spigot.reflection.MinecraftVersion;
import lombok.SneakyThrows;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static dev.echo.utils.spigot.reflection.ReflectionUtils.getNMSClass;
import static dev.echo.utils.spigot.reflection.ReflectionUtils.sendPacket;

public class Color {
    public static String c(String msg) {
        if(MinecraftVersion.getSubVersion() >= 16){
            return translateColorStrings(translateColorCodes(msg));
        }
        return ChatColor.translateAlternateColorCodes('&',translateColorStrings(msg));
    }

    public static String c(String msg, Object... objs) {

        return c(String.format(msg, objs));
    }

    private static String translateColorStrings(String text) {
        String formatted = text;
        String symbol = "%%";
        for (ChatColor color : ChatColor.values()) {
            String name = symbol + color.name().toLowerCase() + symbol;
            formatted = formatted.replace(name, color.toString());
        }
        return formatted;
    }

    public static List<String> cL(List<String> list) {

        return list.stream().map(Color::c).collect(Collectors.toList());
    }

    @SneakyThrows
    public static void title(Player player, String... texts) {

        Object enumTitle = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE").get(null);
        Object titleChat = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + texts[0] + "\"}");


        Constructor<?> titleConstructor = getNMSClass("PacketPlayOutTitle").getConstructor(getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"), int.class, int.class, int.class);
        Object titlePacket = titleConstructor.newInstance(enumTitle, titleChat, 30, 50, 30);

        sendPacket(player, titlePacket);

        if (texts[1] != null) {
            Object enumSubtitle = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("SUBTITLE").get(null);
            Object subtitleChat = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + texts[1] + "\"}");
            Object subtitlePacket = titleConstructor.newInstance(enumSubtitle, subtitleChat, 30, 40, 30);
            sendPacket(player, subtitlePacket);
        }
    }

    public static org.bukkit.Color hexToRGB(String hex) {
        return org.bukkit.Color.fromRGB(Integer.valueOf(hex.substring(1, 3), 16),
                Integer.valueOf(hex.substring(3, 5), 16),
                Integer.valueOf(hex.substring(5, 7), 16));
    }

    @SneakyThrows
    public static void actionbar(Player player, String text) {
        text = c(text);
        if (MinecraftVersion.getSubVersion() >= 9) {
            Object enumTitle = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("ACTIONBAR").get(null);
            Object titleChat = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + text + "\"}");

            Constructor<?> titleConstructor = getNMSClass("PacketPlayOutTitle").getConstructor(getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"),
                    int.class, int.class, int.class);
            Object titlePacket = titleConstructor.newInstance(enumTitle, titleChat, 30, 50, 30);

            sendPacket(player, titlePacket);
        } else {
            Object chatComponent = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class)
                    .invoke(null, "{\"text\":\"" + text + "\"}");

            Constructor<?> chat = getNMSClass("PacketPlayOutChat").getConstructor(getNMSClass("IChatBaseComponent"), byte.class);
            Object packetSerial = chat.newInstance(chatComponent, (byte) 2);
            sendPacket(player, packetSerial);
        }
    }

    static public final String WITH_DELIMITER = "((?<=%1$s)|(?=%1$s))";
    private static String translateColorCodes(String text) {

        String[] texts = text.split(String.format(WITH_DELIMITER, "&"));

        StringBuilder finalText = new StringBuilder();

        for (int i = 0; i < texts.length; i++) {
            if (texts[i].equalsIgnoreCase("&")) {
                //get the next string
                i++;
                if (texts[i].charAt(0) == '#') {
                    finalText.append(net.md_5.bungee.api.ChatColor.of(texts[i].substring(0, 7)) + texts[i].substring(7));
                } else {
                    finalText.append(ChatColor.translateAlternateColorCodes('&', "&" + texts[i]));
                }
            } else {
                finalText.append(texts[i]);
            }
        }

        return finalText.toString();
    }

    private static final List<String> rainbowArray = Arrays.asList("§4", "§c", "§6", "§e", "§a", "§2", "§b", "§3", "§9", "§1", "§5", "§d");

    public static String rainbow(String msg) {
        String fancyText = "";

        int i = 0;

        for (char letters : msg.toCharArray()) {
            String letter = Character.toString(letters);
            String space = fancyText;

            if (!letter.equalsIgnoreCase(" ")) {
                if (i >= rainbowArray.size() - 1) {
                    i = 0;
                } else {
                    i++;
                }
                fancyText += rainbowArray.get(i) + letter;
            } else {
                fancyText = space + letter;
            }
        }
        return fancyText;
    }

    public static String rainbow(String msg, List<String> rainbowArray) {
        String fancyText = "";

        int i = 0;

        for (char letters : msg.toCharArray()) {
            String letter = Character.toString(letters);
            String space = fancyText;

            if (!letter.equalsIgnoreCase(" ")) {
                if (i >= rainbowArray.size() - 1) {
                    i = 0;
                } else {
                    i++;
                }
                fancyText += Color.c(rainbowArray.get(i)) + letter;
            } else {
                fancyText = space + letter;
            }
        }
        return fancyText;
    }

    public static class Bungee {
        public static void actionbar(ProxiedPlayer player, String text) {
            player.sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(c(text)));
        }

        public static void fActionBar(ProxiedPlayer player, String text, Object... formats) {
            player.sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(c(text, formats)));
        }
    }
}
