package it.AleCreeply.chatGate.listeners;

import it.AleCreeply.chatGate.ChatGate;
import org.bukkit.ChatColor;

import java.util.Map;

public class messageManager {

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
