package it.AleCreeply.ChatGate.managers;

import it.AleCreeply.ChatGate.models.CustomChat;
import it.AleCreeply.ChatGate.ChatGate;
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
        String formatPath = "chats." + chat.getId() + ".format";

        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("%player%", sender.getName());
        placeholders.put("%message%", message);

        String formatted = MessageManager.getMessage(sender, formatPath, placeholders);

        for (Player target : Bukkit.getOnlinePlayers()) {
            if (target.hasPermission("chatgate.chats." + chat.getId())) {
                target.sendMessage(formatted);
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
