package it.AleCreeply.ChatGate.placeholders;

import it.AleCreeply.ChatGate.managers.ChatManager;
import it.AleCreeply.ChatGate.models.CustomChat;
import it.AleCreeply.ChatGate.managers.ColorManager;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlaceholderapiExpansion extends PlaceholderExpansion {

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
            CustomChat chat = ChatManager.getInstance().getToggledChat(player);

            if (chat == null) return "Nessuna";

            return ColorManager.color(chat.getDisplayName());
        }
        return null;
    }
}
