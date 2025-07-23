package it.AleCreeply.ChatGate.managers;

import it.AleCreeply.ChatGate.ChatGate;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class MessageManager {

    private static ChatGate plugin = ChatGate.getInstance();

    public static String getMessage(String key) {
        String msg = plugin.getConfig().getString("messages." + key);
        if (msg == null) return "§cMessaggio mancante: " + key;
        msg = msg.replace("%prefix%", getPrefix());
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public static String getMessage(String key, Map<String, String> placeholders) {
        String msg = getMessage(key);
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            msg = msg.replace(entry.getKey(), entry.getValue());
        }
        return msg;
    }

    public static String getPrefix() {
        String prefix = plugin.getConfig().getString("messages.prefix");
        return prefix != null ? ChatColor.translateAlternateColorCodes('&', prefix) : "";
    }
}
