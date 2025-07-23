package it.AleCreeply.ChatGate.commands;

import it.AleCreeply.ChatGate.ChatGate;
import it.AleCreeply.ChatGate.managers.MessageManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChatGateCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0) {
            sender.sendMessage("§cUnknown command. Use /chatgate help");
            return true;
        }

        String sub = args[0].toLowerCase();

        switch (sub) {
            case "reload" -> {
                if (!sender.hasPermission("chatgate.reload")) {
                    sender.sendMessage(MessageManager.getMessage("no-permission"));
                    return true;
                }

                ChatGate.getInstance().reloadConfig();
                ChatGate.getInstance().loadChats();
                sender.sendMessage(MessageManager.getMessage("config-reloaded"));
                return true;
            }

            case "create" -> {
                if (!sender.hasPermission("chatgate.create")) {
                    sender.sendMessage(MessageManager.getMessage("no-permission"));
                    return true;
                }

                if (!(sender instanceof Player player)) {
                    sender.sendMessage("§cOnly players can use this command");
                    return true;
                }

                if (args.length < 4) {
                    sender.sendMessage(MessageManager.getUsage(sender, label));
                    return true;
                }

                String id = args[1].toLowerCase();

                if (ChatGate.getInstance().getChats().containsKey(id)) {
                    sender.sendMessage("§cThere is already a chat called " + id);
                    return true;
                }

                String displayName = args[2];
                StringBuilder formatBuilder = new StringBuilder();
                for (int i = 3; i < args.length; i++) {
                    formatBuilder.append(args[i]).append(" ");
                }
                String format = formatBuilder.toString().trim();

                FileConfiguration config = ChatGate.getInstance().getConfig();
                String path = "chats." + id;

                config.set(path + ".display-name", displayName);
                config.set(path + ".format", format);

                ChatGate.getInstance().saveConfig();
                ChatGate.getInstance().loadChats();

                sender.sendMessage("§aYou have successfully created the chat " + id);
                return true;
            }

            case "delete" -> {
                if (!sender.hasPermission("chatgate.delete")) {
                    sender.sendMessage(MessageManager.getMessage("no-permission"));
                    return true;
                }

                if (args.length < 2) {
                    sender.sendMessage(MessageManager.getUsage(sender, label));
                    return true;
                }

                String id = args[1].toLowerCase();

                FileConfiguration config = ChatGate.getInstance().getConfig();

                if (!config.contains("chats." + id)) {
                    sender.sendMessage("§cThere is no chat called " + id);
                    return true;
                }

                config.set("chats." + id, null);
                ChatGate.getInstance().saveConfig();
                ChatGate.getInstance().loadChats();

                sender.sendMessage("§aYou have successfully deleted the chat " + id);
                return true;
            }

            case "help" -> {
                sender.sendMessage(" ");
                sender.sendMessage(" §d§lCHATGATE §r§8| §7By AleCreeply");
                sender.sendMessage(" ");
                sender.sendMessage(" §8» §e/chatgate reload");
                sender.sendMessage(" §8» §e/chatgate create <id> <display-name> <format>");
                sender.sendMessage(" §8» §e/chatgate delete <id>");
                sender.sendMessage(" §8» §e/chatgate help");
                sender.sendMessage(" ");
                return true;
            }

            default -> {
                sender.sendMessage("§cUnknown command. Use /chatgate help");
                return true;
            }
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.addAll(Arrays.asList("create", "delete", "reload", "help"));
            return completions;
        }

        if (args[0].equalsIgnoreCase("create")) {
            switch (args.length) {
                case 2 -> completions.add("<id>");
                case 3 -> completions.add("<displayName>");
                case 4 -> completions.add("<format>");
            }
        }

        if (args[0].equalsIgnoreCase("delete") && args.length == 2) {
            completions.addAll(ChatGate.getInstance().getChats().keySet());
        }

        return completions;
    }
}
