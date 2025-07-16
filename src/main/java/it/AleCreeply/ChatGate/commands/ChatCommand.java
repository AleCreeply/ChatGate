package it.AleCreeply.ChatGate.commands;

import it.AleCreeply.ChatGate.ChatGate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import it.AleCreeply.ChatGate.models.CustomChat;
import it.AleCreeply.ChatGate.managers.MessageManager;
import it.AleCreeply.ChatGate.managers.ChatManager;
import org.bukkit.entity.Player;

import java.util.*;

public class ChatCommand implements CommandExecutor, TabCompleter {

    private final ChatManager ChatManager = it.AleCreeply.ChatGate.managers.ChatManager.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cOnly players can use this command");
            return true;
        }

        if (args.length < 1) {
            player.sendMessage(MessageManager.getMessage("usage"));
            return true;
        }

        String chatId = args[0].toLowerCase();

        CustomChat chat = ChatGate.getInstance().getChats().get(chatId);
        if (chat == null) {
            Map<String, String> placeholders = new HashMap<>();
            placeholders.put("%chat%", args[0]);
            String msg = MessageManager.getMessage("chat-not-found", placeholders);
            player.sendMessage(msg);
            return true;
        }

        String permission = "chatgate.chats." + chatId;
        if (!player.hasPermission(permission)) {
            player.sendMessage(MessageManager.getMessage("no-permission"));
            return true;
        }

        if (args.length > 1) {
            String message = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
            it.AleCreeply.ChatGate.managers.ChatManager.getInstance().sendChatMessage(chat, player, message);
            return true;
        }

        boolean toggled = it.AleCreeply.ChatGate.managers.ChatManager.getInstance().toggleChat(player, chat);
        if (toggled) {
            Map<String, String> placeholders = new HashMap<>();
            placeholders.put("%chat%", chat.getDisplayName());
            String msg = MessageManager.getMessage("chat-enabled", placeholders);
            player.sendMessage(msg);
        } else {
            Map<String, String> placeholders = new HashMap<>();
            placeholders.put("%chat%", chat.getDisplayName());
            String msg = MessageManager.getMessage("chat-disabled", placeholders);
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
