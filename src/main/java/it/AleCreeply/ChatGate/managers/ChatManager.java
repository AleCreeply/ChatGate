package it.AleCreeply.ChatGate.managers;

import it.AleCreeply.ChatGate.models.CustomChat;
import it.AleCreeply.ChatGate.ChatGate;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChatManager {

    private static final ChatManager instance = new ChatManager();

    public static ChatManager getInstance() {
        return instance;
    }

    private final Map<UUID, String> toggledChats = new HashMap<>();

    public boolean toggleChat(Player player, CustomChat chat) {
        UUID uuid = player.getUniqueId();
        String current = toggledChats.get(uuid);

        if (chat.getId().equalsIgnoreCase(current)) {
            toggledChats.remove(uuid);
            return false;
        } else {
            toggledChats.put(uuid, chat.getId());
            return true;
        }
    }

    public boolean hasToggledChat(Player player) {
        return toggledChats.containsKey(player.getUniqueId());
    }

    public CustomChat getToggledChat(Player player) {
        String chatId = toggledChats.get(player.getUniqueId());
        if (chatId == null) return null;

        return ChatGate.getInstance().getChats().get(chatId);
    }

    public void sendChatMessage(CustomChat chat, Player sender, String message) {
        String format = chat.getFormat();

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            format = PlaceholderAPI.setPlaceholders(sender, format);
        }

        format = format.replace("%player%", sender.getName())
                .replace("%message%", message);

        format = ColorManager.color(format);

        for (Player target : Bukkit.getOnlinePlayers()) {
            if (target.hasPermission("chatgate.chats." + chat.getId())) {
                target.sendMessage(format);
            }
        }

        if (ChatGate.getInstance().getConfig().getBoolean("settings.log-chat")) {
            String cleanLog = "[" + chat.getDisplayName().replaceAll("§.", "") + "] "
                    + sender.getName() + ": " + message;

            Bukkit.getLogger().info(cleanLog);
        }
    }

    public void removeToggled(Player player) {
        toggledChats.remove(player.getUniqueId());
    }
}
