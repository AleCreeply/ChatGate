package it.AleCreeply.ChatGate.managers;

import it.AleCreeply.ChatGate.ChatGate;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class MessageManager {

    private static final ChatGate plugin = ChatGate.getInstance();

    public static String getPrefix() {
        String prefix = plugin.getConfig().getString("messages.prefix");
        return prefix != null ? ColorManager.color(prefix) : "";
    }

    public static String getMessage(String key) {
        // Cerca prima come chiave assoluta
        String msg = plugin.getConfig().getString(key);

        // Se non esiste, prova in messages.<key>
        if (msg == null) {
            msg = plugin.getConfig().getString("messages." + key);
            if (msg == null) return "§cMissing message: " + key;
        }

        msg = msg.replace("%prefix%", getPrefix());
        return ColorManager.color(msg);
    }

    public static String getMessage(CommandSender sender, String key) {
        String msg = getMessage(key);

        if (sender instanceof Player && Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            msg = PlaceholderAPI.setPlaceholders((Player) sender, msg);
        }

        return msg;
    }

    public static String getMessage(String key, Map<String, String> placeholders) {
        String msg = getMessage(key);

        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            msg = msg.replace(entry.getKey(), entry.getValue());
        }

        return msg;
    }

    public static String getMessage(CommandSender sender, String key, Map<String, String> placeholders) {
        String msg = getMessage(key, placeholders);

        if (sender instanceof Player && Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            msg = PlaceholderAPI.setPlaceholders((Player) sender, msg);
        }

        return msg;
    }
}
