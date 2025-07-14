package it.AleCreeply.chatGate.managers;

import org.bukkit.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class colorManager {

    private static final Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]{6})");

    public static String color(String input) {
        if (input == null) return "";

        // Sostituisce HEX: &#rrggbb → §x§r§r§g§g§b§b
        Matcher matcher = HEX_PATTERN.matcher(input);
        StringBuffer buffer = new StringBuffer();

        while (matcher.find()) {
            String hex = matcher.group(1);

            StringBuilder magic = new StringBuilder("§x");
            for (char c : hex.toCharArray()) {
                magic.append('§').append(c);
            }

            matcher.appendReplacement(buffer, magic.toString());
        }

        matcher.appendTail(buffer);

        // Poi traduce anche &a, &b ecc
        return org.bukkit.ChatColor.translateAlternateColorCodes('&', buffer.toString());
    }
}
