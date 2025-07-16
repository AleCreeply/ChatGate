package it.AleCreeply.ChatGate.commands;

import it.AleCreeply.ChatGate.ChatGate;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ChatGateCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0) {
            sender.sendMessage(ChatColor.YELLOW + "ChatGate Plugin by Alessandro");
            sender.sendMessage(ChatColor.GRAY + "Usa /chatgate reload per ricaricare la config.");
            return true;
        }

        String sub = args[0].toLowerCase();

        switch (sub) {
            case "reload" -> {
                if (!sender.hasPermission("chatgate.reload")) {
                    sender.sendMessage(ChatColor.RED + "❌ Non hai il permesso per usare questo comando.");
                    return true;
                }

                ChatGate.getInstance().reloadConfig();
                ChatGate.getInstance().loadChats();
                sender.sendMessage(ChatColor.GREEN + "✔️ Config ricaricato con successo!");
                return true;
            }

            case "create" -> {
                if (!sender.hasPermission("chatgate.create")) {
                    sender.sendMessage(ChatColor.RED + "Non hai il permesso per creare una chat.");
                    return true;
                }

                if (!(sender instanceof Player player)) {
                    sender.sendMessage(ChatColor.RED + "Solo i player possono eseguire questo comando.");
                    return true;
                }

                if (args.length < 4) {
                    sender.sendMessage(ChatColor.RED + "Utilizzo corretto:");
                    sender.sendMessage(ChatColor.YELLOW + "/chatgate create <id> <display-name> <format>");
                    return true;
                }

                String id = args[1].toLowerCase();

                if (ChatGate.getInstance().getChats().containsKey(id)) {
                    sender.sendMessage(ChatColor.RED + "Esiste già una chat con ID '" + id + "'.");
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

                sender.sendMessage(ChatColor.GREEN + "Chat '" + id + "' creata e caricata con successo!");
                return true;
            }

            case "delete" -> {
                if (!sender.hasPermission("chatgate.delete")) {
                    sender.sendMessage(ChatColor.RED + "Non hai il permesso per eliminare una chat.");
                    return true;
                }

                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "Uso corretto: /chatgate delete <id>");
                    return true;
                }

                String id = args[1].toLowerCase();

                FileConfiguration config = ChatGate.getInstance().getConfig();

                if (!config.contains("chats." + id)) {
                    sender.sendMessage(ChatColor.RED + "Non esiste nessuna chat con ID '" + id + "'.");
                    return true;
                }

                config.set("chats." + id, null); // elimina dal config
                ChatGate.getInstance().saveConfig();
                ChatGate.getInstance().loadChats();

                sender.sendMessage(ChatColor.GREEN + "Chat '" + id + "' eliminata con successo!");
                return true;
            }

            case "help" -> {
                sender.sendMessage(ChatColor.YELLOW + "Comandi disponibili:");
                sender.sendMessage(ChatColor.GRAY + " - /chatgate reload");
                sender.sendMessage(ChatColor.GRAY + " - /chatgate create <id> <display-name> <format>");
                sender.sendMessage(ChatColor.GRAY + " - /chatgate delete <id>");
                sender.sendMessage(ChatColor.GRAY + " - /chatgate help");
                return true;
            }

            default -> {
                sender.sendMessage(ChatColor.RED + "Comando sconosciuto. Usa /chatgate help");
                return true;
            }
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        if (args.length == 1) {
            List<String> subs = List.of("reload", "help", "create", "delete");
            return subs.stream()
                    .filter(s -> s.startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (args.length == 2) {
            String sub = args[0].toLowerCase();

            if (sub.equals("delete")) {
                List<String> chats = new ArrayList<>(ChatGate.getInstance().getChats().keySet());
                return chats.stream()
                        .filter(c -> c.startsWith(args[1].toLowerCase()))
                        .collect(Collectors.toList());
            }

            if (sub.equals("create")) {
                return Collections.emptyList();
            }
        }

        return Collections.emptyList();
    }
}
