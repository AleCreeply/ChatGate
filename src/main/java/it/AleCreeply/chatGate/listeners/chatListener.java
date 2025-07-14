package it.AleCreeply.chatGate.listeners;

import it.AleCreeply.chatGate.managers.chatManager;
import it.AleCreeply.chatGate.models.customChat;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class chatListener implements Listener {

    private final chatManager ChatManager = chatManager.getInstance();

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        if (!ChatManager.hasToggledChat(player)) return;

        customChat chat = ChatManager.getToggledChat(player);

        if (chat == null) return;

        event.setCancelled(true);

        String message = event.getMessage();

        chatManager.getInstance().sendChatMessage(chat, player, message);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        chatManager.getInstance().removeToggled(event.getPlayer());
    }
}
