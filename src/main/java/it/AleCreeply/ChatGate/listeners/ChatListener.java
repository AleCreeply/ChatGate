package it.AleCreeply.ChatGate.listeners;

import it.AleCreeply.ChatGate.managers.ChatManager;
import it.AleCreeply.ChatGate.models.CustomChat;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ChatListener implements Listener {

    private final ChatManager ChatManager = it.AleCreeply.ChatGate.managers.ChatManager.getInstance();

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        if (!ChatManager.hasToggledChat(player)) return;

        CustomChat chat = ChatManager.getToggledChat(player);

        if (chat == null) return;

        event.setCancelled(true);

        String message = event.getMessage();

        it.AleCreeply.ChatGate.managers.ChatManager.getInstance().sendChatMessage(chat, player, message);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        it.AleCreeply.ChatGate.managers.ChatManager.getInstance().removeToggled(event.getPlayer());
    }
}
