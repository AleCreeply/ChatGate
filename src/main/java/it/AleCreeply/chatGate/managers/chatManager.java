package it.AleCreeply.chatGate.managers;

import it.AleCreeply.chatGate.models.customChat;
import it.AleCreeply.chatGate.ChatGate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class chatManager {

    private static final chatManager instance = new chatManager();

    public static chatManager getInstance() {
        return instance;
    }

    private final Map<UUID, String> toggledChats = new HashMap<>();

    public boolean toggleChat(Player player, customChat chat) {
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

    public customChat getToggledChat(Player player) {
        String chatId = toggledChats.get(player.getUniqueId());
        if (chatId == null) return null;

        return ChatGate.getInstance().getChats().get(chatId);
    }

    public void sendChatMessage(customChat chat, Player sender, String message) {
        String formatted = colorManager.color(chat.getFormat())
                .replace("%player%", sender.getName())
                .replace("%message%", message);

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
