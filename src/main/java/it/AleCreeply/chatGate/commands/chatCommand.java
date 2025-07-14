package it.AleCreeply.chatGate.commands;

import it.AleCreeply.chatGate.ChatGate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import it.AleCreeply.chatGate.models.customChat;
import it.AleCreeply.chatGate.managers.messageManager;
import it.AleCreeply.chatGate.managers.chatManager;
import org.bukkit.entity.Player;

import java.util.*;

public class chatCommand implements CommandExecutor, TabCompleter {

    private final chatManager ChatManager = chatManager.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cOnly players can use this command");
            return true;
        }

        if (args.length < 1) {
            player.sendMessage(messageManager.getMessage("usage"));
            return true;
        }

        String chatId = args[0].toLowerCase();

        customChat chat = ChatGate.getInstance().getChats().get(chatId);
        if (chat == null) {
            Map<String, String> placeholders = new HashMap<>();
            placeholders.put("%chat%", args[0]);
            String msg = messageManager.getMessage("chat-not-found", placeholders);
            player.sendMessage(msg);
            return true;
        }

        String permission = "chatgate.chats." + chatId;
        if (!player.hasPermission(permission)) {
            player.sendMessage(messageManager.getMessage("no-permission"));
            return true;
        }

        if (args.length > 1) {
            String message = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
            chatManager.getInstance().sendChatMessage(chat, player, message);
            return true;
        }

        boolean toggled = chatManager.getInstance().toggleChat(player, chat);
        if (toggled) {
            Map<String, String> placeholders = new HashMap<>();
            placeholders.put("%chat%", chat.getDisplayName());
            String msg = messageManager.getMessage("chat-enabled", placeholders);
            player.sendMessage(msg);
        } else {
            Map<String, String> placeholders = new HashMap<>();
            placeholders.put("%chat%", chat.getDisplayName());
            String msg = messageManager.getMessage("chat-disabled", placeholders);
            player.sendMessage(msg);
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) return Collections.<String>emptyList();

        if (args.length == 1) {
            List<String> suggestions = new ArrayList<>();

            for (String chatId : ChatGate.getInstance().getChats().keySet()) {
                if (player.hasPermission("chatgate.chats." + chatId)) {
                    suggestions.add(chatId);
                }
            }

            return suggestions;
        }

        return Collections.<String>emptyList();
    }
}
