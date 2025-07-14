package it.AleCreeply.chatGate.placeholders;

import it.AleCreeply.chatGate.managers.chatManager;
import it.AleCreeply.chatGate.models.customChat;
import it.AleCreeply.chatGate.managers.colorManager;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class placeholderapiExpansion extends PlaceholderExpansion {

    @Override
    public @NotNull String getIdentifier() {
        return "chatgate";
    }

    @Override
    public @NotNull String getAuthor() {
        return "AleCreeply";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public boolean persist () {
        return true;
    }

    @Override
    public boolean canRegister () {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        if (player == null) return "";

        if (identifier.equalsIgnoreCase("chat_displayname")) {
            customChat chat = chatManager.getInstance().getToggledChat(player);

            if (chat == null) return "Nessuna";

            return colorManager.color(chat.getDisplayName());
        }
        return null;
    }
}
