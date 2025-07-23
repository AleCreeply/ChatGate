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

    // Messaggio base senza contesto player
    public static String getMessage(String key) {
        String msg = plugin.getConfig().getString("messages." + key);
        if (msg == null) return "§cMessaggio mancante: " + key;
        msg = msg.replace("%prefix%", getPrefix());
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    // Messaggio con placeholder statici personalizzati (es. %arg%)
    public static String getMessage(String key, Map<String, String> placeholders) {
        String msg = getMessage(key);
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            msg = msg.replace(entry.getKey(), entry.getValue());
        }
        return msg;
    }

    // Nuovo metodo: messaggio con supporto PlaceholderAPI e contesto sender
    public static String getMessage(CommandSender sender, String key) {
        String msg = plugin.getConfig().getString("messages." + key);
        if (msg == null) return "§cMessage missing: " + key;

        msg = msg.replace("%prefix%", getPrefix());
        // se è un Player, applica PlaceholderAPI, altrimenti no
        if (sender instanceof Player && Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            msg = PlaceholderAPI.setPlaceholders((Player) sender, msg);
        }
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    // Overload con sender e placeholder statici extra (tipo %arg%)
    public static String getMessage(CommandSender sender, String key, Map<String, String> placeholders) {
        String msg = getMessage(sender, key);
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