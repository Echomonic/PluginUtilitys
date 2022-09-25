package dev.echo.utils.general;

import dev.echo.utils.spigot.player.SkinSetter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PersonalCape {

    public static String getCape(Player player){
        SkinSetter setter = new SkinSetter();


        String uuidData = setter.get("https://api.mojang.com/users/profiles/minecraft/%s" , player.getName());
        String uuid = setter.getUUID(uuidData);
        String skinData = setter.get("https://sessionserver.mojang.com/session/minecraft/profile/%s?unsigned=false" , uuid);
        String skin = setter.getSkin(skinData);
        byte[] bytes = Base64.getDecoder().decode(skin);
        String decoded = new String(bytes);

        List<String> extracted = extractUrls(decoded);
        if(extracted.size() <= 1){
            return "";
        }
        return extractUrls(decoded).get(1);
    }
    public static String getCape(String name){
        SkinSetter setter = new SkinSetter();


        String uuidData = setter.get("https://api.mojang.com/users/profiles/minecraft/%s", name);
        String uuid = setter.getUUID(uuidData);
        String skinData = setter.get("https://sessionserver.mojang.com/session/minecraft/profile/%s?unsigned=false" , uuid);
        String skin = setter.getSkin(skinData);
        byte[] bytes = Base64.getDecoder().decode(skin);
        String decoded = new String(bytes);

        List<String> extracted = extractUrls(decoded);
        if(extracted.size() <= 1){
            return "Doesn't have an official minecraft cape";
        }
        return extractUrls(decoded).get(1);
    }

    public static List<String> extractUrls(String text)
    {
        List<String> containedUrls = new ArrayList<>();
        String urlRegex = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
        Matcher urlMatcher = pattern.matcher(text);

        while (urlMatcher.find())
        {
            containedUrls.add(text.substring(urlMatcher.start(0),
                    urlMatcher.end(0)));
        }

        return containedUrls;
    }
}
