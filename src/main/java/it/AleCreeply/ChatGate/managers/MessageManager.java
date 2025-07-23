package it.AleCreeply.ChatGate.managers;

import it.AleCreeply.ChatGate.ChatGate;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class MessageManager {

    private static final ChatGate plugin = ChatGate.getInstance();

    public static String getMessage(String key) {
        String msg = plugin.getConfig().getString("messages." + key);

        if (msg == null) return ColorManager.color("&cMissing message: &f" + key);

        msg = msg.replace("%prefix%", getPrefix());

        return ColorManager.color(msg);
    }

    public static String getMessage(String key, Map<String, String> placeholders) {
        String msg = plugin.getConfig().getString("messages." + key);

        if (msg == null) return ColorManager.color("&cMissing message: &f" + key);

        msg = msg.replace("%prefix%", getPrefix());

        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            msg = msg.replace(entry.getKey(), entry.getValue());
        }

        return ColorManager.color(msg);
    }

    public static String getMessage(String key, CommandSender sender, Map<String, String> placeholders) {
        String msg = plugin.getConfig().getString("messages." + key);

        if (msg == null) return ColorManager.color("&cMissing message: &f" + key);

        msg = msg.replace("%prefix%", getPrefix());

        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            msg = msg.replace(entry.getKey(), entry.getValue());
        }

        if (sender instanceof Player && Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            msg = PlaceholderAPI.setPlaceholders((Player) sender, msg);
        }

        return ColorManager.color(msg);
    }

    public static String getPrefix() {
        String prefix = plugin.getConfig().getString("messages.prefix");
        return prefix != null ? ColorManager.color(prefix) : "";
    }
}
