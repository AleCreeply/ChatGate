package it.AleCreeply.chatGate.commands;

import it.AleCreeply.chatGate.ChatGate;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class chatGateCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // Se nessun argomento, mostra help base
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

            case "help" -> {
                sender.sendMessage(ChatColor.YELLOW + "Comandi disponibili:");
                sender.sendMessage(" - /chatgate reload");
                sender.sendMessage(" - /chatgate help");
                return true;
                // in futuro: version, info, ecc...
            }

            default -> {
                sender.sendMessage(ChatColor.RED + "❌ Comando sconosciuto. Usa /chatgate help");
                return true;
            }
        }
    }
}
